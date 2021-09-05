package model.get_article_behavior;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import model.scrapping_engine.InitScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class GetZingNews extends GetArticleBehavior implements Runnable{

    private String url;

    public GetZingNews(String url) {
        this.url = url;
    }

    @Override
    public void scrapeArticle(String url, CopyOnWriteArrayList<Article> articles) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).get().build();
            Document doc = Jsoup.parse(okHttpClient.newCall(request).execute().body().string());
            //Get all the article in the element
            Elements elements = doc.getElementsByTag("article");
            //Looping through each element
            for (Element element : elements) {
                String title = element.child(1).getElementsByClass("article-title").select("a").text();
                String linkPage = "https://zingnews.vn/" + element.getElementsByClass("article-title").select("a").attr("href");
                String tempDate = element.getElementsByClass("date").text() + " " + element.getElementsByClass("time").text();
                Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(tempDate);
                Article tmp = new Article(title,
                        linkPage,
                        date,
                        getImage(element.getElementsByClass("article-thumbnail").select("img").toString()),
                        getSource("ZingNews"),
                        null);
                synchronized(this) {
                    articles.add(tmp);
                }

            }
        } catch (IOException | ParseException e) {
            System.out.println("URL error");
        }
    }

    @Override
    public void run() {
        scrapeArticle(this.url, InitScraper.articles);
    }
}
