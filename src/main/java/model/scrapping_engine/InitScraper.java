package model.scrapping_engine;

import model.get_article_behavior.Article;
import model.get_article_behavior.GetNhanDan;
import model.get_article_behavior.GetWithRSS;
import model.get_article_behavior.GetZingNews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    public static int tempSize = 0;
    public static ArrayList<Integer> catCounter = new ArrayList<Integer>(Collections.nCopies(9, 0));
    public static ArrayList<String> urlList = new ArrayList<String>();

    public static ArrayList<Article> articles = new ArrayList<>();
    public static ExecutorService executorService = Executors.newCachedThreadPool();

    public void scrapeLinks() {
        long startTime = System.currentTimeMillis();

//        executorService.execute(new URLCrawler("https://vnexpress.net/rss"));
        executorService.execute(new URLCrawler("https://tuoitre.vn/"));
        executorService.execute(new URLCrawler("https://thanhnien.vn/rss.html"));
//        executorService.execute(new URLCrawler("https://nhandan.vn/"));
        executorService.execute(new URLCrawler("https://zingnews.vn/"));
        executorService.shutdown();
//        executorService.awaitTermination(10, TimeUnit.SECONDS);
        while (!executorService.isTerminated()) {
//            System.out.println("Scraping in InitScraper...");
        }
        articles.sort(Comparator.comparing(Article::getDuration).reversed());
        long endTime = System.currentTimeMillis();
        long elap = endTime - startTime;

//        articles.remove(2);
//        for (int i = 0; i < 20; i++) {
//            System.out.println(articles.get(i).getTitlePage());
//        }
        System.out.println("Scraping done in: " + elap);

    }

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        InitScraper in = new InitScraper();

        in.scrapeLinks();
        System.out.println(articles.size());
        long endTime = System.currentTimeMillis();
        long elap = endTime - startTime;
        System.out.println("Program done in: " + elap);
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
