package model;

import model.get_article_behavior.Article;
import model.get_article_behavior.GetWithRSS;
import model.get_article_behavior.GetZingNews;
import model.get_article_behavior.ScrappingEngine;

import java.util.concurrent.CopyOnWriteArrayList;

public class ArticleDatabase {
    private CopyOnWriteArrayList<Article> articles;

    private ScrappingEngine scrappingEngine;

    public ArticleDatabase() {
        articles = new CopyOnWriteArrayList<>();
    }

    public void performGetArticle() {
        scrappingEngine = new GetWithRSS();
        articles.addAll(scrappingEngine.getArticle("https://vnexpress.net/rss/tin-moi-nhat.rss", 10));
        articles.addAll(scrappingEngine.getArticle("https://vnexpress.net/rss/kinh-doanh.rss", 10));
        articles.addAll(scrappingEngine.getArticle("https://vnexpress.net/rss/suc-khoe.rss", 10));
        articles.addAll(scrappingEngine.getArticle("https://vnexpress.net/rss/giai-tri.rss", 10));
        articles.addAll(scrappingEngine.getArticle("https://vnexpress.net/rss/the-gioi.rss", 10));
    }

    public CopyOnWriteArrayList<Article> getArticles() {
        return articles;
    }
}
