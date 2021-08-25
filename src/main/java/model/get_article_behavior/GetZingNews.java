package model.get_article_behavior;
import com.github.sisyphsu.dateparser.DateParserUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import io.github.cdimascio.essence.Essence;
import io.github.cdimascio.essence.EssenceResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class GetZingNews extends ScrappingEngine {
    @Override
    public List<Article> getArticle(String url, int qty) {
        List<Article> articles = new ArrayList<>();
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).get().build();
            Document doc = Jsoup.parse(okHttpClient.newCall(request).execute().body().string());
            //Get all the article in the element
            Elements elements = doc.getElementsByTag("article");
            //Looping through each element
            int count = 0;
            for (Element element : elements) {
                //Get a limit number of element
                if (count >= qty) {
                    break;
                }
                String title = element.child(1).getElementsByClass("article-title").select("a").text();
                String linkPage = "https://zingnews.vn/" + element.getElementsByClass("article-title").select("a").attr("href");
//                String tempDate = element.getElementsByClass("date").text() + " " + element.getElementsByClass("time").text();
                OkHttpClient okHttpClientForArticle = new OkHttpClient();
                Request requestForArticle = new Request.Builder().url(linkPage).get().build();
                Document docForArticle = Jsoup.parse(okHttpClientForArticle.newCall(requestForArticle).execute().body().string());
                EssenceResult data = Essence.extract(docForArticle.html());
                System.out.println("Zing " + data.getDate());
                try {
                    System.out.println("Zing Stock:" + data.getDate());
                    System.out.println("Zing: " + DateParserUtils.parseDate(data.getDate()).toString());
                } catch (Exception e) {
                    System.out.println("Failed");
                }

                Article tmp = new Article(title,
                        linkPage,
                        DateParserUtils.parseDate(data.getDate()),
                        getImage(element.getElementsByClass("article-thumbnail").select("img").toString()),
                        getSource("ZingNews"),
                        null);
                articles.add(tmp);
                ++count;
            }

        } catch (IOException e) {
            System.out.println("URL error");
        }
        return articles;
    }
}
