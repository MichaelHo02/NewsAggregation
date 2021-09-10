package model.database;

import model.get_article_behavior.GetArticleBehavior;
import model.scrapping_engine.InitScraper;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;

public class ArticleDatabase { // database contains category dictionary + articles for that category
    private static String[] dictionary;

    public static ArrayList<Article> articles;

    private GetArticleBehavior getArticleBehavior;

    private final PropertyChangeSupport propertyChangeSupport;

    private boolean stopThread;

    public ArticleDatabase() {
        articles = new ArrayList<>();
        propertyChangeSupport = new PropertyChangeSupport(this);
        stopThread = false;
    }
//    public ArticleDatabase(String dictFile) {
//        dictionary = loadDictionary(dictFile); // load dictionary from file
//        articles = new ArrayList<>();
//    }

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

    public String[] getDictionary() {
        return dictionary;
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
