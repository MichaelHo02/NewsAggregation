package model.article_extraction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class ThanhNienExtraction extends ArticleExtractor {

    @Override
    public List<ArticleFactory> getContent(String linkPage) {
        try {
            ARTICLE_FACTORY.clear();
            Document doc = Jsoup.connect(linkPage).get();
//          // TODO: Scrap sapo
            String firstImage = doc.select("#contentAvatar img").attr("src");
            Elements body = doc.select("div[class~=.*content]");
            Elements elements = doc.select("div[id=abody] > *");
//            System.out.println(doc.select("div[id=abody] > *").toString());
            if (body.select("div.sapo").size() > 0) {
                ArticleFactory cont = new ArticleFactory(body.select("div.sapo").text(),"p");
                ARTICLE_FACTORY.add(cont);
            }
            else {
                ArticleFactory cont = new ArticleFactory(doc.select("div.summary").text(),"p");
                ARTICLE_FACTORY.add(cont);
            }

            for (Element element : elements) {
                if (element.tagName().equals("img")) {
                    ArticleFactory tempImg = new ArticleFactory(element.select("div[id=contentAvatar] img").attr("src"),"img");
                    ARTICLE_FACTORY.add(tempImg);
                } else if (element.tagName().equals("p")) {
                    ArticleFactory tempP = new ArticleFactory(element.text(), "p");
                    ARTICLE_FACTORY.add(tempP);
                } else if (element.tagName().matches("h\\d")) {
                    ArticleFactory tempH = new ArticleFactory(element.text(), "h");
                    ARTICLE_FACTORY.add(tempH);
                    System.out.println(tempH.getContext());
                } else  if (element.is("div") && !element.className().equals("details__morenews")){
                    checkDivTN(element);
                }
            }

            //Add author
            ArticleFactory cont= new ArticleFactory(doc.select("div.left h4").text(),"author");
            ARTICLE_FACTORY.add(cont);

        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayTuoiTre");
        }
        return ARTICLE_FACTORY;
    }

    private void checkDivTN(Element div) {
        // If element has 0 children and is not an ad div
        if (div.select("> *").size() == 0 && !div.className().contains("ads") && div.hasText()){
            ArticleFactory tmpdiv = new ArticleFactory(div.text(),"div");
            ARTICLE_FACTORY.add(tmpdiv);
            return;
        }

        // Loop through div elements
        for (Element ele : div.select("> *")) {
            try {
                if (ele.is("div") && !ele.attr("class").contains("image")) {
                    checkDivTN(ele);
                }
                else if (ele.is("p")) {
                    ArticleFactory ptmp = new ArticleFactory(ele.text(),"p");
                } else if (ele.attr("class").contains("image")) {
                    //Extract image and Caption
                    ArticleFactory tmpimg = new ArticleFactory(ele.select("img").attr("data-src"), "img");
                    ArticleFactory labimg = new ArticleFactory(ele.select("p").text(),"caption");
                    ARTICLE_FACTORY.add(tmpimg);
                    ARTICLE_FACTORY.add(labimg);
                } else if (ele.is("figure") && ele.attr("class").equals("picture")) {
                    if (ele.select("img").size() > 0) {
                      ArticleFactory cont = new ArticleFactory(ele.select("img").attr("data-src"), "img");
                      ArticleFactory lab = new ArticleFactory(ele.select("figcaption").text(),"caption");
                      ARTICLE_FACTORY.add(cont);
                      ARTICLE_FACTORY.add(lab);
                    }
                    else if (ele.hasText()) {
                        ArticleFactory txt =new ArticleFactory(ele.text(),"caption");
                    }
                }
                else if (ele.is("h2") || ele.is("h3")) {
                    ArticleFactory cont = new ArticleFactory(ele.text(), "h");
                }
                else if (ele.hasText()) {
                   ArticleFactory cont = new ArticleFactory(div.text(),"div");
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