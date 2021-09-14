/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Bui Minh Nhat s3878174
        Last modified date: 10/09/2021
        Contributor: Nguyen Dich Long s3879052
        Acknowledgement:
        https://mkyong.com/java/jsoup-basic-web-crawler-example/
        https://www.youtube.com/watch?v=KZK5rnxBWcU

 */
package model.scrapping_engine;

import model.database.Article;
import util.filter.ArticleFilter;
import util.get_article_behavior.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class URLCrawler implements Runnable {

    // Workflow:
    // 1. Scrape all categories links in the main homepages
    // 2. Filter categories
    // 3. Call scraper for each link

    private final String URL;
    private final List<Article> articleList;

    public URLCrawler(String URL, List<Article> articleList) {
        this.URL = URL;
        this.articleList = articleList;
    }

    public void getURLList() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            Document doc = Jsoup.connect(this.URL).get();
            Elements elements = doc.select(".list-rss li a[href], .rss-list li a[href], .category-menu li a[href], .uk-nav li a[href], .menu-ul li a[href]");
            for (Element element : elements) {
                String folder = element.attr("href");
                if (ArticleFilter.filterArticle(folder)) {
                    if (this.URL.contains("vnexpress")) {
                        executorService.execute(new GetWithRSS("https://vnexpress.net" + folder, articleList));
                    } else if (this.URL.contains("tuoitre")) {
                        if (folder.contains("https")) {
                            executorService.execute(new GetTuoiTre(folder, articleList));
                        } else {
                            executorService.execute(new GetTuoiTre("https://tuoitre.vn" + folder, articleList));
                        }
                    } else if (this.URL.contains("nhandan")) {
                        executorService.execute(new GetNhanDan("https://nhandan.vn" + folder, articleList));
                    } else if (this.URL.contains("zingnews")) {
                        executorService.execute(new GetZingNews("https://zingnews.vn" + folder, articleList));
                    } else if (this.URL.contains("thanhnien")) {
                        executorService.execute(new GetWithRSS(folder, articleList));
                    }
                }
            }
            executorService.shutdown();
            executorService.awaitTermination(15, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("URLCrawler Exception");
        }
    }

    @Override
    public void run() {
        getURLList();
    }

}
