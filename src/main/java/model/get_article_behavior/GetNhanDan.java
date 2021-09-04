package model.get_article_behavior;

import com.github.sisyphsu.dateparser.DateParserUtils;
import model.scrapping_engine.InitScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GetNhanDan extends GetArticleBehavior implements Runnable {

    private String url;

    public GetNhanDan(String url) {
        this.url = url;
    }

    @Override
    public void scrapeArticle(String url, CopyOnWriteArrayList<Article> articles) {

        try {
            Document doc = Jsoup.connect(url).get();
            for (Element element : doc.select("div.box-title > a[href]")) { // Fetch all links
                String tempLink = "https://nhandan.vn" + element.attr("href"); // Join links
                System.out.println(tempLink);
                Document tempDoc = Jsoup.connect(tempLink).get(); // Request to the destination link and extract contents
                String title = tempDoc.select(".box-title-detail.entry-title").text();
                String date = tempDoc.select(".box-date.pull-left").text();
                String imageURL = tempDoc.select("img").attr("data-src");
                String category = tempDoc.select(".uk-breadcrumb a").text();
                if (title.equals("") || title.isBlank() || title.isEmpty()) { // Handle unpassable article
                    continue;
                }
                else if (date.equals("") || date.isBlank() || date.isEmpty()) { // Handle abnormal date format
                    continue;
                }
                if (!(imageURL.contains("jpg") || imageURL.contains("JPG") || imageURL.contains("jpeg") || imageURL.contains("png"))) {
                    imageURL = ""; // Prevent bugs with ImageView
                }
                Article article = new Article(title, tempLink, DateParserUtils.parseDate(date.substring(9)), imageURL, WebsiteURL.NHANDAN, category);
                synchronized(this) {
                    System.out.println(InitScraper.articles);
                    articles.add(article);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to connect");
        }


    }

    @Override
    public void run() {
        scrapeArticle(this.url, InitScraper.articles);
    }


}