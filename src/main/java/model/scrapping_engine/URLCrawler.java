package model.scrapping_engine;

import model.database.ArticleFilter;
import model.get_article_behavior.GetNhanDan;
import model.get_article_behavior.GetWithRSS;
import model.get_article_behavior.GetZingNews;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class URLCrawler implements Runnable {

    private String url;

    public URLCrawler(String url) {
        this.url = url;
    }

    public void getURLList(String url) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            Document doc = Jsoup.connect(url).get();
            int countFolder = 0;
            Elements elements = doc.select(".list-rss li a[href], .rss-list li a[href], .category-menu li a[href], .uk-nav li a[href]");
            for (Element element : elements) {
                String folder = element.attr("href");
                if (ArticleFilter.filterArticle(folder)) {
                    countFolder++;
                    if (url.contains("vnexpress")) {
                        executorService.execute(new GetWithRSS("https://vnexpress.net" + folder));
                    } else if (url.contains("tuoitre")) {
                        executorService.execute(new GetWithRSS("https://tuoitre.vn" + folder));
                    } else if (url.contains("nhandan")) {
                        executorService.execute(new GetNhanDan("https://nhandan.vn" + folder));
                    } else if (url.contains("zingnews")) {
                        executorService.execute(new GetZingNews("https://zingnews.vn" + folder));
                    } else if (url.contains("thanhnien")) {
                        executorService.execute(new GetWithRSS(folder));
                    }
                }
            }
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("cannot connect to page");
        }
    }

    @Override
    public void run() {
        getURLList(this.url);
    }

}
