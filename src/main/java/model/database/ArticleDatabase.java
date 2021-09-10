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
package model.database;

import model.scrapping_engine.InitScraper;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class ArticleDatabase { // database contains category dictionary + articles for that category
    public static ArrayList<Article> articles;

    public static HashSet<String> articlesCheck;

    private final PropertyChangeSupport propertyChangeSupport;

    private boolean stopThread;

    public ArticleDatabase() {
        articles = new ArrayList<>();
        propertyChangeSupport = new PropertyChangeSupport(this);
        stopThread = false;
    }

    public void performGetArticle() {
        InitScraper in = new InitScraper();
        try {
            in.scrapeLinks();
        } catch (Exception e) {
            System.out.println("Failed to load data in ArticleDatabase");
            return;
        }
        if (stopThread) {
            in.stopThread();
            return;
        }
        // Remove duplicate articles
        // TODO: sync
        articlesCheck = new HashSet<>();
        for (int i = 0; i < InitScraper.articles.size(); i++) {
            String tmp = InitScraper.articles.get(i).getTitlePage() + " " + InitScraper.articles.get(i).getSource().getUrl();
            if (!articlesCheck.contains(tmp)) {
                System.out.println("Unique Article: " + InitScraper.articles.get(i).getLinkPage());
                articlesCheck.add(tmp);
                articles.add(InitScraper.articles.get(i));
            }
        }
        articles.sort(Comparator.comparing(Article::getDuration).reversed());
        System.out.println("After remove: " + articles);
        doNotify(true);
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private void doNotify(Boolean boo) {
        propertyChangeSupport.firePropertyChange("isScrapeDone", null, boo);
    }

    public void end() {
        stopThread = true;
    }
}
