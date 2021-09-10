/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Nguyen Dich Long s3879052
        Last modified date: 10/09/2021
        Contributor: Student name, Student ID
        Acknowledgement:
         https://www.w3schools.com/cssref/css_selectors.asp
        https://openplanning.net/10399/jsoup-java-html-parser
        https://www.youtube.com/watch?v=l1mER1bV0N0&ab_channel=WebDevSimplified
        https://jsoup.org/cookbook/extracting-data/selector-syntax
        https://nira.com/chrome-developer-tools/#:~:text=From%20the%20Chrome%20menu%3A,web%20page%20you're%20on.
 */
package model.get_article_behavior;

import model.database.ArticleFilter;
import model.database.Article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GetNhanDan extends GetArticleBehavior implements Runnable {

    private final String URL;
    private final List<Article> articleList;

    public GetNhanDan(String URL, List<Article> articleList) {
        this.URL = URL;
        this.articleList = articleList;
    }

    @Override
    public void scrapeArticle() {
        try {
            Document doc = Jsoup.connect(URL).timeout(10000).get();
            for (Element element : doc.select("article")) { // Fetch all links
                try {
                    // get article url and handle exception
                    String tempLink = element.select("a").attr("href"); // Join links
                    if (!tempLink.contains("https://")) {
                        tempLink = WebsiteURL.NHANDAN.getUrl() + tempLink;
                    }
                    // get article title, if not exists, skip
                    String title = element.getElementsByClass("box-title").text();
                    if (title == null) { continue; }
                    // get article date and img
                    String date = element.select("div[class*=box-meta]").text();
                    Date tempDate = new SimpleDateFormat("HH:mm dd/MM/yyyy").parse(date);
                    String imageURL = element.select("img").attr("data-src");
                    String category = title + " " + scrapeCategory(URL, 3); // concatenate category + title

                    Article article = new Article(title, tempLink, tempDate, imageURL, WebsiteURL.NHANDAN, category);
                    synchronized(this) {
                        if (ArticleFilter.filterArticle(article)) {
                            articleList.add(article);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Cannot parse date in GetNhanDan");
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to connect in GetNhanDan");
        }
    }

    @Override
    public void run() {
        scrapeArticle();
    }

}