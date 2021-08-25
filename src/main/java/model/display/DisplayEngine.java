package model.display;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.get_article_behavior.Article;
import model.get_article_behavior.WebsiteURL;

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
                break;
            case ZINGNEWS:
                DisplayZingNews displayZingNews = new DisplayZingNews();
                displayZingNews.articleScraper(article);
                htmlTemplate = displayZingNews.getTmpTemplate();
                break;
            case VNEXPRESS:
                System.out.println("This is where it is");
                DisplayVNExpress displayVNExpress = new DisplayVNExpress();
                System.out.println(displayVNExpress.articleScraper(article));
                htmlTemplate = displayVNExpress.getTmpTemplate();
                break;
            default:
                break;
        }
        webEngine.loadContent(htmlTemplate);
        System.out.println("This is B: " + htmlTemplate);
        return webView;
    }
}
