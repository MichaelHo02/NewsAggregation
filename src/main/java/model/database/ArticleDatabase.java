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
import java.util.HashSet;

public class ArticleDatabase { // database contains category dictionary + articles for that category
    public static ArrayList<Article> articles;

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
        HashSet<String> articlesCheck = new HashSet<>();
        for (int i = 0; i < InitScraper.articles.size(); i++) {
            if (stopThread) {
                return;
            }
            if (!articlesCheck.contains(InitScraper.articles.get(i).getTitlePage())) {
                articlesCheck.add(InitScraper.articles.get(i).getTitlePage());
                articles.add(InitScraper.articles.get(i));
            }
        }
//        System.out.println("After remove: " + articles);
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
