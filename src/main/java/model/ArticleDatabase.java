package model;

import model.get_article_behavior.Article;
import model.get_article_behavior.GetWithRSS;
import model.get_article_behavior.GetArticleBehavior;

import java.util.concurrent.CopyOnWriteArrayList;

public class ArticleDatabase {
    private CopyOnWriteArrayList<Article> articles;

    private GetArticleBehavior getArticleBehavior;

    public ArticleDatabase() {
        articles = new CopyOnWriteArrayList<>();
    }

    public void performGetArticle() {
        getArticleBehavior = new GetWithRSS();
        articles.addAll(getArticleBehavior.getArticle("https://vnexpress.net/rss/tin-moi-nhat.rss", 10));
        articles.addAll(getArticleBehavior.getArticle("https://vnexpress.net/rss/kinh-doanh.rss", 10));
        articles.addAll(getArticleBehavior.getArticle("https://vnexpress.net/rss/suc-khoe.rss", 10));
        articles.addAll(getArticleBehavior.getArticle("https://vnexpress.net/rss/giai-tri.rss", 10));
        articles.addAll(getArticleBehavior.getArticle("https://vnexpress.net/rss/the-gioi.rss", 10));
    }

    public CopyOnWriteArrayList<Article> getArticles() {
        return articles;
    }
}
