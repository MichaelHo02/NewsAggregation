package model.get_article_behavior;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import model.database.ArticleFilter;
import model.scrapping_engine.InitScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

public class GetNhanDan extends GetArticleBehavior implements Runnable {

    private String url;

    public GetNhanDan(String url) {
        this.url = url;
    }

    @Override
    public void scrapeArticle(String url, ArrayList<Article> articles) {
        try {
            Document doc = Jsoup.connect(url).timeout(10000).get();
            for (Element element : doc.select("article")) { // Fetch all links
                try {
                    // get article url and handle exception
                    String tempLink = element.select("a").attr("href"); // Join links
                    if (!tempLink.contains("https://")) {
                        tempLink = "https://nhandan.vn" + tempLink;
                    }
                    // get article title, if not exists, skip
                    String title = element.getElementsByClass("box-title").text();
                    if (title == null) { continue; }
                    // get article date and img
                    String date = element.select("div[class*=box-meta]").text();
                    Date tempDate = new SimpleDateFormat("HH:mm dd/MM/yyyy").parse(date);
                    String imageURL = element.select("img").attr("data-src");
                    String category = title + " " + scrapeCat(url, 3); // concatenate category + title

                    Article article = new Article(title, tempLink, tempDate, imageURL, WebsiteURL.NHANDAN, category);
                    synchronized(this) {
                        if (ArticleFilter.filterArticle(article)) {
                            articles.add(article);
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
        scrapeArticle(this.url, InitScraper.articles);
    }

}