/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Bui Minh Nhat s3878174
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

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import model.database.ArticleFilter;
import model.scrapping_engine.InitScraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GetZingNews extends GetArticleBehavior implements Runnable{

    private String url;

    public GetZingNews(String url) {
        this.url = url;
    }

    @Override
    public void scrapeArticle(String url, ArrayList<Article> articles) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).get().build();
            Document doc = Jsoup.parse(okHttpClient.newCall(request).execute().body().string());
            //Get all the article in the element
            Elements elements = doc.getElementsByTag("article");
            //Looping through each element
            for (Element element : elements) {
                // get title and article url
                String title = element.child(1).getElementsByClass("article-title").select("a").text();
                String linkPage = "https://zingnews.vn/" + element.getElementsByClass("article-title").select("a").attr("href");
                // get and format date
                String tempDate = element.getElementsByClass("date").text() + " " + element.getElementsByClass("time").text();
                Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(tempDate);
                // get image url
                String image = getImage(element.getElementsByClass("article-thumbnail").select("img").toString());
                // get article meta category
                String category = element.getElementsByClass("article-meta").select("a").attr("title");
                // construct article
                Article article = new Article(title, linkPage, date, image, getSource("ZingNews"), category);
                // thread-safe handling article
                synchronized(this) {
                    if (ArticleFilter.filterArticle(article)) {
                        articles.add(article);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("URL error in GetZingNews");
        }
    }


    @Override
    public void run() {
        scrapeArticle(this.url, InitScraper.articles);
    }
}
