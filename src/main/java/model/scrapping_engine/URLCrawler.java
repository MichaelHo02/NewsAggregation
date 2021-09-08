package model.scrapping_engine;

import model.database.ArticleFilter;
import model.get_article_behavior.GetNhanDan;
import model.get_article_behavior.GetTuoiTre;
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
            Elements elements = doc.select(".list-rss li a[href], .rss-list li a[href], .category-menu li a[href], .uk-nav li a[href], .menu-ul li a[href]");
            for (Element element : elements) {
                if (Thread.interrupted()) {
                    System.out.println("Is Interrupt");
                    if (!executorService.isTerminated()) {
                        executorService.shutdownNow();
                    }
                    return;
                }
                String folder = element.attr("href");
                if (ArticleFilter.filterArticle(folder)) {
                    System.out.println("This is the folder: " + folder);
                    if (url.contains("vnexpress")) {
                        executorService.execute(new GetWithRSS("https://vnexpress.net" + folder));
                    } else if (url.contains("tuoitre")) {
                        if (folder.contains("https")) {
                            executorService.execute(new GetTuoiTre(folder));
                        } else {
                            executorService.execute(new GetTuoiTre("https://tuoitre.vn" + folder));
                        }
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
