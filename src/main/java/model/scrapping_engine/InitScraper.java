package model.scrapping_engine;

import model.get_article_behavior.Article;
import model.get_article_behavior.GetNhanDan;
import model.get_article_behavior.GetWithRSS;
import model.get_article_behavior.GetZingNews;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static primary_page.controller.PrimaryController.*;

public class InitScraper {
    // Loop qua tat ca cac links, neu dung tag thi goi thread de tim
    static int numCovid = 0;
    static int numPolitics = 0;
    static int numBusiness = 0;
    static int numTechnology = 0;
    static int numHealth = 0;
    static int numSport = 0;
    static int numEntertainment = 0;
    static int numWorld = 0;
    static int numOthers = 0;

    public static ArrayList<String> urlList = new ArrayList<String>();

    public static CopyOnWriteArrayList<Article> articles = new CopyOnWriteArrayList<>();

    public void scrapeLinks() throws InterruptedException {
        Thread t1 = new Thread(new URLCrawler("https://vnexpress.net/rss"));
//        Thread t2 = new Thread(new URLCrawler("https://tuoitre.vn/rss.htm"));
//        Thread t3 = new Thread(new URLCrawler("https://thanhnien.vn/rss.html"));
//        Thread t4 = new Thread(new URLCrawler("https://nhandan.vn/"));
//        Thread t5 = new Thread(new URLCrawler("https://zingnews.vn/"));
        t1.start();
//        t2.start();
//        t3.start();
//        t4.start();
//        t5.start();
        t1.join();
//        t2.join();
//        t2.join();
//        t3.join();
//        t4.join();
//        t5.join();
    }

    public void scrapeArticles() throws InterruptedException {

        for (String entry : urlList) {
            if (entry.contains("rss")) {
                GetWithRSS getWithRSS = new GetWithRSS(entry);
                Thread temp = new Thread(getWithRSS);
                temp.start();
                temp.join();
            } else if (entry.contains("nhandan")) {
                GetNhanDan getNhanDan = new GetNhanDan(entry);
                Thread temp = new Thread(getNhanDan);
                temp.start();
                temp.join();
            } else if (entry.contains("zingnews")) {
                GetZingNews getZingNews = new GetZingNews(entry);
                Thread temp = new Thread(getZingNews);
                temp.start();
                temp.join();
            } else if (entry.contains("tuoitre")) {
                GetZingNews getTuoiTre = new GetZingNews(entry);
                Thread temp = new Thread(getTuoiTre);
                temp.start();
                temp.join();
            }
        }
    }

    //        tempTable.put("vnexpress", getURLList("https://vnexpress.net/rss"));
//        tempTable.put("tuoitre", getURLList("https://tuoitre.vn/rss.htm"));
//        tempTable.put("thanhnien", getURLList("https://thanhnien.vn/rss.html"));
//        tempTable.put("nhandan", getURLList("https://nhandan.vn/"));
//        tempTable.put("zingnews", getURLList("https://zingnews.vn/"));

    public static void main(String[] args) throws InterruptedException {
        InitScraper in = new InitScraper();
        in.scrapeLinks();
        in.scrapeArticles();

        for (Article x : articles) {
            System.out.println(x.getTitlePage());
        }

    }

}
