package model;

import model.get_article_behavior.Article;
import model.get_article_behavior.GetTuoiTre;
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

    public CopyOnWriteArrayList<Article> getArticles() {
        return articles;
    }
}
