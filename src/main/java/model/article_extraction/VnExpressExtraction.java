/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Bui Minh Nhat
        Last modified date: 10/09/2021
        Contributor: Student name, Student ID
        Acknowledgement:
        https://www.w3schools.com/cssref/css_selectors.asp
        https://openplanning.net/10399/jsoup-java-html-parser
        https://www.youtube.com/watch?v=l1mER1bV0N0&ab_channel=WebDevSimplified
        https://jsoup.org/cookbook/extracting-data/selector-syntax
        https://nira.com/chrome-developer-tools/#:~:text=From%20the%20Chrome%20menu%3A,web%20page%20you're%20on.
 */

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
        for (Element element : div.select("> *")) {
            // If element is text not author
            if (element.is("p") && !element.attr("style").contains("text-align:right;") && !element.attr("class").contains("author")) {
                String type = "p";
                if (element.select("strong").size() > 0)
                    type = "h";
                ARTICLE_FACTORY.add(new ArticleFactory(element.text(), type));

            } else if (element.is("h2")) {
                ARTICLE_FACTORY.add(new ArticleFactory(element.text(), "h"));
            }
            // If element is image
            else if (element.is("figure") && element.select("img").size() > 0) {
                String imgTmp = element.select("img").attr("data-src");
                if (imgTmp.equals("")) {
                    imgTmp = element.select("img").attr("src");
                }
                ARTICLE_FACTORY.add(new ArticleFactory(imgTmp, "img"));
                //Image Caption
                ARTICLE_FACTORY.add(new ArticleFactory(element.select("figcaption").text(), "caption"));
            }
            // If element is gallery
            else if (element.attr("class").contains("clearfix")) {
                if (element.select("img").size() > 0) {
                    String imagTmp = element.select("img").attr("data-src");
                    if (imagTmp.equals("")) {
                        imagTmp = element.select("img").attr("src");
                    }
                    ARTICLE_FACTORY.add(new ArticleFactory(imagTmp, "img"));
                    //Image Caption
                    ARTICLE_FACTORY.add(new ArticleFactory(element.select("p").text(), "caption"));
                }
            } else if (element.is("div") && element.attr("class").equals("box_brief_info")) {
                for (Element innerEle : element.select("> *")) {
                    if (innerEle.is("p")) {
                        ARTICLE_FACTORY.add(new ArticleFactory(innerEle.text(), "p"));
                    }
                }
            } else if (element.is("div")) {
                checkBody(element);
            }
        }
    }
}
