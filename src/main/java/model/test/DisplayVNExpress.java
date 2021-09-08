package model.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class DisplayVNExpress extends JsoupArticleDisplay {
    @Override
    public List<Content> getContent(String linkPage) {
        try {
            CONTENT.clear();
            Document doc = Jsoup.connect(linkPage).get();
            Element article;
            if (doc.select("article.fck_detail").size() > 0) {
                article = doc.select("article.fck_detail").first();
            } else {
                article = doc.select("div[class*=fck_detail]").first();
            }
            // Add description to article view
            CONTENT.add(new Content(doc.select("p.description").text(), "p"));
            // ChEck the div tag of VNEXPRESS
            checkBody(article);
            // Add author label to article view
            String tmp = article.select("p[style*=text-align:right]").text();
            if (tmp.equals("")) {
                tmp = article.select("p[class*=author]").text();
            }
            CONTENT.add(new Content(tmp, "author"));

            return CONTENT;
        } catch (Exception e) {
            System.out.println("Failed connection to the destination page");
            return null;
        }
    }

    private void checkBody(Element div) {
        for (Element ele : div.select("> *")) {
            // If element is text not author
            if (ele.is("p") && !ele.attr("style").contains("text-align:right;") && !ele.attr("class").contains("author")) {
                String type = "p";
                if (ele.select("strong").size() > 0)
                    type = "h";
                CONTENT.add(new Content(ele.text(), type));

            } else if (ele.is("h2")) {
                CONTENT.add(new Content(ele.text(), "h"));
            }
            // If element is image
            else if (ele.is("figure") && ele.select("img").size() > 0) {
                String imgTmp = ele.select("img").attr("data-src");
                if (imgTmp.equals("")) {
                    imgTmp = ele.select("img").attr("src");
                }
                CONTENT.add(new Content(imgTmp, "img"));
                //Image Caption
                CONTENT.add(new Content(ele.select("figcaption").text(), "caption"));
            }
            // If element is gallery
            else if (ele.attr("class").contains("clearfix")) {
                if (ele.select("img").size() > 0) {
                    String imagTmp = ele.select("img").attr("data-src");
                    if (imagTmp.equals("")) {
                        imagTmp = ele.select("img").attr("src");
                    }
                    CONTENT.add(new Content(imagTmp, "img"));
                    //Image Caption
                    CONTENT.add(new Content(ele.select("p").text(), "caption"));
                }
            } else if (ele.is("div") && ele.attr("class").equals("box_brief_info")) {
                for (Element innerEle : ele.select("> *")) {
                    if (innerEle.is("p")) {
                        CONTENT.add(new Content(innerEle.text(), "p"));
                    }
                }
            } else if (ele.is("div")) {
                checkBody(ele);
            }
        }
    }
}
