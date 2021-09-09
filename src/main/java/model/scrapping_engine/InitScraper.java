/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author:
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat s3878174
        Acknowledgement:
        https://www.baeldung.com/java-executor-service-tutorial
        https://stackoverflow.com/questions/7351073/java-how-to-synchronize-array-accesses-and-what-are-the-limitations-on-what-goe
 */
package model.scrapping_engine;

import model.get_article_behavior.Article;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InitScraper {

    //    Order in catCounter are as following
    //    1 numCovid
    //    2 numPolitics
    //    3 numBusiness
    //    4 numTechnology
    //    5 numHealth
    //    6 numSport
    //    7 numEntertainment
    //    8 numWorld
    //    9 numOthers

    public static ArrayList<Integer> catCounter = new ArrayList<>(Collections.nCopies(9, 0)); // For controlling the quantity of each categories
    public static ArrayList<Article> articles = new ArrayList<>();
    public static ExecutorService executorService = Executors.newCachedThreadPool();

    public void scrapeLinks() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        // Call URLCrawler for each article center
        executorService.execute(new URLCrawler("https://vnexpress.net/rss"));
        executorService.execute(new URLCrawler("https://tuoitre.vn/"));
        executorService.execute(new URLCrawler("https://thanhnien.vn/rss.html"));
        executorService.execute(new URLCrawler("https://nhandan.vn/"));
        executorService.execute(new URLCrawler("https://zingnews.vn/"));
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);


        articles.sort(Comparator.comparing(Article::getDuration).reversed()); // Sort the data by release time

        long endTime = System.currentTimeMillis();
        long elap = endTime - startTime;
        System.out.println("Scraping done in: " + elap); // Check for how long does it take to scrape


    }

    public void stopThread() {
        executorService.shutdownNow();
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
