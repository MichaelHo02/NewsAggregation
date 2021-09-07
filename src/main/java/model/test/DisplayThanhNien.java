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
            Content cont= new Content(doc.select("div.left h4").text(),"p");
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
        for (Element e : div.select("> *")) {
            try {
                // Recursion call if child is div
                if (e.is("div") && !e.attr("class").contains("image")) {
                    checkDivTN(e);
                }
                else if (e.is("p")) {
                    Content ptmp = new Content(e.text(),"p");
                } else if (e.attr("class").contains("image")) {
                    Content tmpimg = new Content(e.select("img").attr("data-src"), "img");
                    Content labimg = new Content(e.select("p").text(),"p");
                    CONTENT.add(tmpimg);
                    CONTENT.add(labimg);
                } else if (e.is("figure") && e.attr("class").equals("picture")) {
                    if (e.select("img").size() > 0) {
                      Content cont = new Content(e.select("img").attr("data-src"), "img");
                      Content lab = new Content(e.select("figcaption").text(),"p");
                      CONTENT.add(cont);
                      CONTENT.add(lab);
                    }
                    else if (e.hasText()) {
                        Content txt =new Content(e.text(),"p");
                    }
                }
                // Add header if child is header
                else if (e.is("h2") || e.is("h3")) {
                    Content cont = new Content(e.text(), "h");
                }
                // Add text label if child is neither image nor video and has text
                else if (e.hasText()) {
                   Content cont = new Content(div.text(),"div");
                    break;
                }
            }
            catch (IllegalArgumentException ex) { continue; }
        }
    }

}
