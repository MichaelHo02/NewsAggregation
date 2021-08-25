package model.display;

import model.get_article_behavior.Article;

public abstract class DisplayArticle {
    //TODO:Implement a way to interact with webview
    private String tmpTemplate;
    public abstract String articleScraper(Article article);

    //public abstract WebView articleScraperr(String url);

    public String getTmpTemplate() {
        return tmpTemplate;
    }


    public void setTmpTemplate(String tmpTemplate) {
        this.tmpTemplate = tmpTemplate;
    }
}
