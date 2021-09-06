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

    public void getURLList(String url, ArrayList<String> urlList) {
//        ArrayList<String> urlList = new ArrayList<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            Document doc = Jsoup.connect(url).get();
            // thanh nien full link
            // vnexpress, tuoi tre /rss/link (needs to be joined)
            // nhan dan needs to be joined
            // zingnews needs to be joined
            int countFolder = 0;
            Elements elements = doc.select(".list-rss li a[href], .rss-list li a[href], .category-menu li a[href], .uk-nav li a[href]");
            for (Element element : elements) {
                String folder = element.attr("href");
                if (ArticleFilter.filterArticle(folder)) {
                    countFolder++;
                    System.out.println("This is the filter: " + url + folder);
                    if (url.contains("vnexpress")) {
//                    urlList.add("https://vnexpress.net" + element.attr("href"));
                        executorService.execute(new GetWithRSS("https://vnexpress.net" + folder));
                    } else if (url.contains("tuoitre")) {
//                    urlList.add("https://tuoitre.vn" + element.attr("href"));
                        executorService.execute(new GetWithRSS("https://tuoitre.vn" + folder));
                    } else if (url.contains("nhandan")) {
//                    urlList.add("https://nhandan.vn" + element.attr("href"));
                        executorService.execute(new GetNhanDan("https://nhandan.vn" + folder));
                    } else if (url.contains("zingnews")) {
//                    urlList.add("https://zingnews.vn" + element.attr("href"));
                        executorService.execute(new GetZingNews("https://zingnews.vn" + folder));
                    } else if (url.contains("thanhnien")) {
//                        String temp = element.attr("href");
//                        if (temp.contains("video") || temp.contains("viec-lam") || temp.contains("game")) { continue; }
//                    urlList.add(temp);
                        executorService.execute(new GetWithRSS(folder));
                    }
                }
            }
            System.out.println("Total folder: " + countFolder);
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("cannot connect to page");
        }
    }

    @Override
    public void run() {
        getURLList(this.url, InitScraper.urlList);
    }

}
