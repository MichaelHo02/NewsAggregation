package model.get_article_behavior;

import com.github.sisyphsu.dateparser.DateParserUtils;
import model.scrapping_engine.InitScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GetThanhNien extends GetArticleBehavior implements Runnable {

    private String url;

    public GetThanhNien(String url) {
        this.url = url;
    }

    @Override
    public void scrapeArticle(String url, CopyOnWriteArrayList<Article> articles) {
        try {
            Document doc = Jsoup.connect(url).get();
            for (Element element : doc.select("h2 > a[href]")) { // Fetch all links
                String tempLink = "https://thanhnien.vn/" + element.attr("href"); // Join links
                System.out.println(tempLink);
                Document tempDoc = Jsoup.connect(tempLink).get(); // Request to the destination link and extract contents
                String title = tempDoc.select(".details__headline").text();
                String date = tempDoc.select(".details__meta").select("time").text();
                String imageURL = tempDoc.select(".pswp-content__image").select("img").attr("src");
                String category = tempDoc.select(".breadcrumbs a").text();

                // Uncomment these lines for testing purpose
//                System.out.println("Title: " + title);
//                System.out.println("Date: " + date);
//                System.out.println("Img: " + imageURL);
//                System.out.println("Category: " + category);
                if (title.equals("") || title.isBlank() || title.isEmpty()) { // Handle unpassable article
                    continue;
                }
                else if (date.equals("") || date.isBlank() || date.isEmpty()) { // Handle abnormal date format
                    continue;
                }
                if (!(imageURL.contains("jpg") || imageURL.contains("JPG") || imageURL.contains("jpeg") || imageURL.contains("png"))) {
                    imageURL = ""; // Prevent bugs with ImageView
                }
                Article article = new Article(title, tempLink, DateParserUtils.parseDate(date.substring(9)), imageURL, WebsiteURL.THANHNIEN, category);
                articles.add(article);
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