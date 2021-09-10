/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author:
        Last modified date: 10/09/2021
        Contributor:
        Acknowledgement:

 */
package model.scrapping_engine;

import model.database.ArticleDatabase;
import model.database.Article;
import model.get_article_behavior.WebsiteURL;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.Comparator;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundScraper implements Runnable {

    private boolean stopThread;
    private PropertyChangeSupport propertyChangeSupport;

    // Constructor
    public BackgroundScraper() {
        stopThread = false;
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void backgroundScrape() {
        while (true) {
            try {
                // Perform scraping new articles
                Thread.sleep(30_000);
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(new URLCrawler(WebsiteURL.VNEXPRESS.getUrl() +"rss"));
                executorService.execute(new URLCrawler(WebsiteURL.TUOITRE.getUrl()));
                executorService.execute(new URLCrawler(WebsiteURL.THANHNIEN.getUrl() + "rss.html"));
                executorService.execute(new URLCrawler(WebsiteURL.NHANDAN.getUrl()));
                executorService.execute(new URLCrawler(WebsiteURL.ZINGNEWS.getUrl()));
                executorService.shutdown();
                if (stopThread) {
                    executorService.shutdownNow();
                    return;
                } else {
                    InitScraper.articles.sort(Comparator.comparing(Article::getDuration).reversed()); // Sort the array

                    // Remove duplicate articles
                    HashSet<String> articlesCheck = new HashSet<>();
                    for (int i = 0; i < InitScraper.articles.size(); i++) {
                        if (stopThread) {
                            executorService.shutdownNow();
                            return;
                        }
                        if (!articlesCheck.contains(InitScraper.articles.get(i).getTitlePage())) {
                            System.out.println("Background adding");
                            articlesCheck.add(InitScraper.articles.get(i).getTitlePage());
                            ArticleDatabase.articles.add(InitScraper.articles.get(i));
                        }
                    }
                }
                System.out.println("Success!");
                doNotify(true);
            } catch (Exception e) {
                System.out.println("Cannot perform background scraping");
            }
        }
    }

    @Override
    public void run() {
        backgroundScrape();
    }

    public void end() {
        stopThread = true;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }


    private void doNotify(Boolean boo) {
        propertyChangeSupport.firePropertyChange("updateScrapeDone", null, boo);
    }
}
