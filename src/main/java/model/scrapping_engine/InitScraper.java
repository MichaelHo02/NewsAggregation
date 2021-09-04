package model.scrapping_engine;

import model.get_article_behavior.Article;
import model.get_article_behavior.GetNhanDan;
import model.get_article_behavior.GetWithRSS;
import model.get_article_behavior.GetZingNews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static primary_page.controller.PrimaryController.*;

public class InitScraper {
    // Loop qua tat ca cac links, neu dung tag thi goi thread de tim
    //Order in array are as following
//    1 numCovid
//    2 numPolitics
//    3 numBusiness
//    4 numTechnology
//    5 numHealth
//    6 numSport
//    7 numEntertainment
//    8 numWorld
//    9 numOthers


    public static ArrayList<Integer> catCounter = new ArrayList<Integer>(Collections.nCopies(9, 0));
    public static ArrayList<String> urlList = new ArrayList<String>();

    public static CopyOnWriteArrayList<Article> articles = new CopyOnWriteArrayList<>();

    public void scrapeLinks() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
//        executorService.submit(new URLCrawler("https://vnexpress.net/rss"));
//        executorService.submit(new URLCrawler("https://tuoitre.vn/rss.htm"));
//        executorService.submit(new URLCrawler("https://thanhnien.vn/rss.html"));
        executorService.submit(new URLCrawler("https://nhandan.vn/"));
//        executorService.submit(new URLCrawler("https://zingnews.vn/"));
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            System.out.println("Scraping in InitScraper...");
        }
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
//        in.scrapeLinks();
//        in.scrapeArticles();
//        URLCrawler uc = new URLCrawler("https://vnexpress.net/rss");
//        Thread t1 = new Thread(uc);
//        t1.start();
//        t1.join();
//        for (Article x : articles) {
//            System.out.println(x.getTitlePage());
//        }
        in.scrapeLinks();
        System.out.println(articles.size());
    }

    //Array accessor
    public static void setValue(int index, int value) {
        synchronized (catCounter) {
            catCounter.set(index, value);
        }
    }

    public static int getValue(int index) {
        synchronized (catCounter) {
            return catCounter.get(index);
        }
    }
}
