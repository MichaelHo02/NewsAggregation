package model.display;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.scrapping_engine.Article;
import model.scrapping_engine.WebsiteURL;

public class DisplayEngine {
    public WebView getWebView(Article article) {
        WebView webView = new WebView();
        WebEngine webEngine = new WebEngine();
        WebsiteURL source = article.getSource();
        String htmlTemplate = "";
        switch (source) {
            case NHANDAN:
                //
                break;
            case TUOITRE:
                DisplayTuoiTre displayTuoiTre = new DisplayTuoiTre();
                displayTuoiTre.articleScraper(article);
                htmlTemplate = displayTuoiTre.getTmpTemplate();
                break;
            case THANHNIEN:
                DisplayThanhNien displayThanhNien = new DisplayThanhNien();
                displayThanhNien.articleScraper(article);
                htmlTemplate = displayThanhNien.getTmpTemplate();
            case ZINGNEWS:
                DisplayZingNews displayZingNews = new DisplayZingNews();
                displayZingNews.articleScraper(article);
                htmlTemplate = displayZingNews.getTmpTemplate();
            case VNEXPRESS:
                //
                break;
            default:
                break;
        }
        webEngine.loadContent(htmlTemplate);
        return webView;
    }
}
