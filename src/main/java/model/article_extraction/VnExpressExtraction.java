package model.article_extraction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public class VnExpressExtraction extends ArticleExtractor {
    @Override
    public List<ArticleFactory> getContent(String linkPage) {
        try {
            ARTICLE_FACTORY.clear();
            Document doc = Jsoup.connect(linkPage).get();
            Element article;
            if (doc.select("article.fck_detail").size() > 0) {
                article = doc.select("article.fck_detail").first();
            } else {
                article = doc.select("div[class*=fck_detail]").first();
            }
            // Add description to article view
            ARTICLE_FACTORY.add(new ArticleFactory(doc.select("p.description").text(), "p"));
            // ChEck the div tag of VNEXPRESS
            checkBody(article);
            // Add author label to article view
            String tmp = article.select("p[style*=text-align:right]").text();
            if (tmp.equals("")) {
                tmp = article.select("p[class*=author]").text();
            }
            ARTICLE_FACTORY.add(new ArticleFactory(tmp, "author"));

            return ARTICLE_FACTORY;
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
                ARTICLE_FACTORY.add(new ArticleFactory(ele.text(), type));

            } else if (ele.is("h2")) {
                ARTICLE_FACTORY.add(new ArticleFactory(ele.text(), "h"));
            }
            // If element is image
            else if (ele.is("figure") && ele.select("img").size() > 0) {
                String imgTmp = ele.select("img").attr("data-src");
                if (imgTmp.equals("")) {
                    imgTmp = ele.select("img").attr("src");
                }
                ARTICLE_FACTORY.add(new ArticleFactory(imgTmp, "img"));
                //Image Caption
                ARTICLE_FACTORY.add(new ArticleFactory(ele.select("figcaption").text(), "caption"));
            }
            // If element is gallery
            else if (ele.attr("class").contains("clearfix")) {
                if (ele.select("img").size() > 0) {
                    String imagTmp = ele.select("img").attr("data-src");
                    if (imagTmp.equals("")) {
                        imagTmp = ele.select("img").attr("src");
                    }
                    ARTICLE_FACTORY.add(new ArticleFactory(imagTmp, "img"));
                    //Image Caption
                    ARTICLE_FACTORY.add(new ArticleFactory(ele.select("p").text(), "caption"));
                }
            } else if (ele.is("div") && ele.attr("class").equals("box_brief_info")) {
                for (Element innerEle : ele.select("> *")) {
                    if (innerEle.is("p")) {
                        ARTICLE_FACTORY.add(new ArticleFactory(innerEle.text(), "p"));
                    }
                }
            } else if (ele.is("div")) {
                checkBody(ele);
            }
        }
    }
}
