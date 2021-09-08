package model.scrapping_engine;

import model.database.ArticleDatabase;
import model.get_article_behavior.Article;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundScraper implements Runnable {

    private boolean stopThread;

    private PropertyChangeSupport propertyChangeSupport;

    public BackgroundScraper() {
        stopThread = false;
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void backgroundScrape() {
        while (true) {
            try {
                Thread.sleep(100_000);
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(new URLCrawler("https://vnexpress.net/rss"));
                executorService.execute(new URLCrawler("https://tuoitre.vn/rss.htm"));
                executorService.execute(new URLCrawler("https://thanhnien.vn/rss.html"));
                executorService.execute(new URLCrawler("https://nhandan.vn/"));
                executorService.execute(new URLCrawler("https://zingnews.vn/"));
                executorService.shutdown();
                if (stopThread) {
                    executorService.shutdownNow();
                    return;
                } else {
                    InitScraper.articles.sort(Comparator.comparing(Article::getDuration).reversed());

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
                System.out.println(e.toString());
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
