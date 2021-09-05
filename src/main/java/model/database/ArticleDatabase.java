package model.database;

import model.get_article_behavior.Article;
import model.get_article_behavior.GetTuoiTre;
import model.get_article_behavior.GetWithRSS;
import model.get_article_behavior.GetArticleBehavior;
import model.database.ArticleFilter;
import model.scrapping_engine.InitScraper;
import model.scrapping_engine.URLCrawler;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import static model.database.ArticleFilter.loadDictionary;

public class ArticleDatabase { // database contains category dictionary + articles for that category
    private static String[] dictionary;

    private static CopyOnWriteArrayList<Article> articles;

    private GetArticleBehavior getArticleBehavior;

    public ArticleDatabase() {
        articles = new CopyOnWriteArrayList<>();
    }
    public ArticleDatabase(String dictFile) {
        dictionary = loadDictionary(dictFile); // load dictionary from file
        articles = new CopyOnWriteArrayList<>();
    }

    public void performGetArticle() {
        InitScraper in = new InitScraper();
        try {
            in.scrapeLinks();
        } catch (Exception e) {
            System.out.println("failed");
        }
        articles = InitScraper.articles;
        for (int i = 1; i < articles.size(); i++) {
            if (articles.get(i).equals(articles.get(i - 1))) {
                System.out.println("Yasuo yasuo");
                articles.remove(i);
                i--;
            }
        }
    }

    public String[] getDictionary() {
        return dictionary;
    }



    public CopyOnWriteArrayList<Article> getArticles() {
        return articles;
    }


}
