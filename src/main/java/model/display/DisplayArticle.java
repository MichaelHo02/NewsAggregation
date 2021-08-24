package model.display;

import com.sun.javafx.iio.ImageLoader;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.scrapping_engine.Article;
import org.jsoup.nodes.Document;

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
