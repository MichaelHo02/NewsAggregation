package model.test;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class DisplayThanhNien extends JsoupArticleDisplay {

    @Override
    public List<Content> getContent(String linkPage) {
        try {
            CONTENT.clear();
            Document doc = Jsoup.connect(linkPage).get();
//          // TODO: Scrap sapo
            String firstImage = doc.select("#contentAvatar img").attr("src");
            Elements body = doc.select("div[class~=.*content]");
            Elements elements = doc.select("div[id=abody] > *");
//            System.out.println(doc.select("div[id=abody] > *").toString());
            if (body.select("div.sapo").size() > 0) {
                Content cont = new Content(body.select("div.sapo").text(),"p");
                CONTENT.add(cont);
            }
            else {
                Content cont = new Content(doc.select("div.summary").text(),"p");
                CONTENT.add(cont);
            }

            for (Element element : elements) {
                if (element.tagName().equals("img")) {
                    Content tempImg = new Content(element.select("div[id=contentAvatar] img").attr("src"),"img");
                    CONTENT.add(tempImg);
                } else if (element.tagName().equals("p")) {
                    Content tempP = new Content(element.text(), "p");
                    CONTENT.add(tempP);
                } else if (element.tagName().matches("h\\d")) {
                    Content tempH = new Content(element.text(), "h");
                    CONTENT.add(tempH);
                    System.out.println(tempH.getContext());
                } else  if (element.is("div") && !element.className().equals("details__morenews")){
                    checkDivTN(element);
                }
            }

            //Add author
            Content cont= new Content(doc.select("div.left h4").text(),"author");
            CONTENT.add(cont);

        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayTuoiTre");
        }
        return CONTENT;
    }

    private void checkDivTN(Element div) {
        // If element has 0 children and is not an ad div
        if (div.select("> *").size() == 0 && !div.className().contains("ads") && div.hasText()){
            Content tmpdiv = new Content(div.text(),"div");
            CONTENT.add(tmpdiv);
            return;
        }

        // Loop through div elements
        for (Element ele : div.select("> *")) {
            try {
                if (ele.is("div") && !ele.attr("class").contains("image")) {
                    checkDivTN(ele);
                }
                else if (ele.is("p")) {
                    Content ptmp = new Content(ele.text(),"p");
                } else if (ele.attr("class").contains("image")) {
                    //Extract image and Caption
                    Content tmpimg = new Content(ele.select("img").attr("data-src"), "img");
                    Content labimg = new Content(ele.select("p").text(),"caption");
                    CONTENT.add(tmpimg);
                    CONTENT.add(labimg);
                } else if (ele.is("figure") && ele.attr("class").equals("picture")) {
                    if (ele.select("img").size() > 0) {
                      Content cont = new Content(ele.select("img").attr("data-src"), "img");
                      Content lab = new Content(ele.select("figcaption").text(),"caption");
                      CONTENT.add(cont);
                      CONTENT.add(lab);
                    }
                    else if (ele.hasText()) {
                        Content txt =new Content(ele.text(),"caption");
                    }
                }
                else if (ele.is("h2") || ele.is("h3")) {
                    Content cont = new Content(ele.text(), "h");
                }
                else if (ele.hasText()) {
                   Content cont = new Content(div.text(),"div");
                    break;
                }
            }
            catch (Exception ignored) {
                System.out.println("Error display Thanh Nien");
                continue;
            }
        }
    }

}
