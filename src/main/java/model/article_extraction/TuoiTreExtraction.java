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
import org.jsoup.select.Elements;

import java.util.List;

public class TuoiTreExtraction extends ArticleExtractor {

    @Override
    public List<ArticleFactory> getContent(String linkPage) {
        try {
            ARTICLE_FACTORY.clear();
            Document document = Jsoup.connect(linkPage).get();
            Elements elements = document.select("div#mainContentDetail");
            //Add Description
            ArticleFactory des = new ArticleFactory(elements.select("h2").text(), "p");
            ARTICLE_FACTORY.add(des);
            //Get the main body of the article
            divChecker( document.select("div#main-detail-body > *"));
            //Get tauthor
            if (elements.select("div.author").size() > 0) {
                ArticleFactory author = new ArticleFactory(elements.select("div.author").text(), "author");
                ARTICLE_FACTORY.add(author);
            }

        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayTuoiTre");
        }
        return ARTICLE_FACTORY;
    }

    public void divChecker(Elements div) {
        for (Element element : div) {
            try {
                if (element.is("p") && element.hasText()) { //If element is a ordinary p element
                    ArticleFactory tmp = new ArticleFactory(element.text(), "p");
                    ARTICLE_FACTORY.add(tmp);
                } else if (element.is("div")) { //If element is a p tag
                    // Add image if element is image
                    if (element.attr("type").equals("Photo")) {
                        String address = element.select("img").attr("src");
                        try {
                            //Clean up image url
                            address = address.replace("thumb_w/586/", "");
                        } catch (Exception ignored) {}
                        ArticleFactory img = new ArticleFactory(address, "img");
                        ARTICLE_FACTORY.add(img);
                        ArticleFactory cap = new ArticleFactory(element.select("p").text(), "caption");
                        ARTICLE_FACTORY.add(cap);
                    } else if (element.attr("type").equals("wrapnote")) {
                        divChecker(element.select("> *"));
                    }
                }
            } catch (Exception e) {
                System.out.println("Error display tuoitre");
                continue;
            }
        }
    }
}
