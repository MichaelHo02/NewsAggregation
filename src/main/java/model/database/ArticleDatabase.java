package model.database;

import model.get_article_behavior.Article;
import model.get_article_behavior.GetArticleBehavior;
import model.scrapping_engine.InitScraper;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import static model.database.ArticleFilter.loadDictionary;

public class ArticleDatabase { // database contains category dictionary + articles for that category
    private static String[] dictionary;

    public static ArrayList<Article> articles;

    private GetArticleBehavior getArticleBehavior;

    private final PropertyChangeSupport propertyChangeSupport;

    public ArticleDatabase() {
        articles = new ArrayList<>();
        propertyChangeSupport = new PropertyChangeSupport(this);
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
            System.out.println("failed");
        }

        // Remove duplicate articles
        System.out.println("Before remove: " + InitScraper.articles.size());
        HashSet<String> articlesCheck = new HashSet<>();
        for (int i = 0; i < InitScraper.articles.size(); i++) {
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

}
