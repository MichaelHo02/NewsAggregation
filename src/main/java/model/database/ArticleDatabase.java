package model.database;

import model.get_article_behavior.Article;
import model.get_article_behavior.GetTuoiTre;
import model.get_article_behavior.GetWithRSS;
import model.get_article_behavior.GetArticleBehavior;
import model.database.ArticleFilter;

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
        getArticleBehavior = new GetWithRSS("https://vnexpress.net/rss/tin-moi-nhat.rss");
        getArticleBehavior.scrapeArticle("https://vnexpress.net/rss/tin-moi-nhat.rss", articles);
        getArticleBehavior = new GetWithRSS("https://vnexpress.net/rss/kinh-doanh.rss");
        getArticleBehavior.scrapeArticle("https://vnexpress.net/rss/kinh-doanh.rss", articles);
        getArticleBehavior = new GetWithRSS("https://vnexpress.net/rss/suc-khoe.rss");
        getArticleBehavior.scrapeArticle("https://vnexpress.net/rss/suc-khoe.rss", articles);
        getArticleBehavior = new GetWithRSS("https://vnexpress.net/rss/giai-tri.rss");
        getArticleBehavior.scrapeArticle("https://vnexpress.net/rss/giai-tri.rss", articles);
        getArticleBehavior = new GetWithRSS("https://vnexpress.net/rss/the-gioi.rss");
        getArticleBehavior.scrapeArticle("https://vnexpress.net/rss/the-gioi.rss", articles);
        getArticleBehavior = new GetTuoiTre("https://tuoitre.vn");
        getArticleBehavior.scrapeArticle("https://tuoitre.vn",articles);
    }

    public String[] getDictionary() {
        return dictionary;
    }



    public CopyOnWriteArrayList<Article> getArticles() {
        return articles;
    }


}
