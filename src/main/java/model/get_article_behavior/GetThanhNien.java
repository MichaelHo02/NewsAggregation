package model.get_article_behavior;

import com.github.sisyphsu.dateparser.DateParserUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import model.database.ArticleFilter;
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
    public void scrapeArticle(String url, ArrayList<Article> articles) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).get().build();
            Document doc = Jsoup.parse(okHttpClient.newCall(request).execute().body().string());
//            Document doc = Jsoup.connect(url).get();
            for (Element element : doc.select("h2 > a[href]")) { // Fetch all links
                String tempLink = "https://thanhnien.vn/" + element.attr("href"); // Join links
//                System.out.println(tempLink);
//                Document tempDoc = Jsoup.connect(tempLink).get(); // Request to the destination link and extract contents
                OkHttpClient okHttpClientForArticle = new OkHttpClient();
                Request requestForArticle = new Request.Builder().url(tempLink).get().build();
                Document tempDoc = Jsoup.parse(okHttpClientForArticle.newCall(requestForArticle).execute().body().string());
                String title = tempDoc.select(".details__headline").text();
                String date = tempDoc.select(".details__meta").select("time").text();
                //TODO: image not display
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
//                if (!(imageURL.contains("jpg") || imageURL.contains("JPG") || imageURL.contains("jpeg") || imageURL.contains("png"))) {
//                    imageURL = ""; // Prevent bugs with ImageView
//                }
                Article article = new Article(title, tempLink, DateParserUtils.parseDate(date.substring(9)), imageURL, WebsiteURL.THANHNIEN, category);
                // check if this article belongs to any category, if none then all categories are full => terminate
                synchronized(this) {
                    if (ArticleFilter.filterArticle(article)) {
                        articles.add(article);
                    }
                    System.out.println("This is the list for article category" + article.getCategories());
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