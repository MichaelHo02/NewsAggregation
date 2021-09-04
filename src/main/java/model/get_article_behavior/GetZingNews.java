package model.get_article_behavior;
import com.github.sisyphsu.dateparser.DateParserUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import io.github.cdimascio.essence.Essence;
import io.github.cdimascio.essence.EssenceResult;
import model.scrapping_engine.InitScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
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
//                String tempDate = element.getElementsByClass("date").text() + " " + element.getElementsByClass("time").text();
                OkHttpClient okHttpClientForArticle = new OkHttpClient();
                Request requestForArticle = new Request.Builder().url(linkPage).get().build();
                Document docForArticle = Jsoup.parse(okHttpClientForArticle.newCall(requestForArticle).execute().body().string());
                EssenceResult data = Essence.extract(docForArticle.html());
                System.out.println("Zing " + data.getDate());
                Article tmp = new Article(title,
                        linkPage,
                        DateParserUtils.parseDate(data.getDate()),
                        getImage(element.getElementsByClass("article-thumbnail").select("img").toString()),
                        getSource("ZingNews"),
                        null);
                articles.add(tmp);

            }
        } catch (IOException e) {
            System.out.println("URL error");
        }
    }

    @Override
    public void run() {
        scrapeArticle(this.url, InitScraper.articles);
    }
}
