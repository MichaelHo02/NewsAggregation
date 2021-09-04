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


    public static ArrayList<Integer> catCounter = new ArrayList<Integer>(Collections.nCopies(9, 0));
    public static ArrayList<String> urlList = new ArrayList<String>();

    public static CopyOnWriteArrayList<Article> articles = new CopyOnWriteArrayList<>();
    public static ExecutorService executorService = Executors.newCachedThreadPool();
    public void scrapeLinks() throws InterruptedException {

        executorService.execute(new URLCrawler("https://vnexpress.net/rss"));
        executorService.execute(new URLCrawler("https://tuoitre.vn/rss.htm"));
        executorService.execute(new URLCrawler("https://thanhnien.vn/rss.html"));
//        executorService.execute(new URLCrawler("https://nhandan.vn/"));
//        executorService.submit(new URLCrawler("https://zingnews.vn/"));
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            System.out.println("Scraping in InitScraper...");
        }
//        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws InterruptedException {
        InitScraper in = new InitScraper();

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
