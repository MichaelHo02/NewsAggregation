package model.display;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.get_article_behavior.Article;
import model.get_article_behavior.WebsiteURL;

public class DisplayEngine {
    //Switch to various HTML control
    public String getHTML(Article article) {
        WebsiteURL source = article.getSource();
        String htmlTemplate = "";
        switch (source) {
            case NHANDAN:
                //
                break;
            case TUOITRE:
                DisplayTuoiTre displayTuoiTre = new DisplayTuoiTre();

                htmlTemplate =  displayTuoiTre.articleScraper(article);
                break;
            case THANHNIEN:
                DisplayThanhNien displayThanhNien = new DisplayThanhNien();
                htmlTemplate =  displayThanhNien.articleScraper(article);
                break;
            case ZINGNEWS:
                DisplayZingNews displayZingNews = new DisplayZingNews();
                displayZingNews.articleScraper(article);
                htmlTemplate =  displayZingNews.articleScraper(article);
                break;
            case VNEXPRESS:
                System.out.println("This is where it is");
                DisplayVNExpress displayVNExpress = new DisplayVNExpress();
                htmlTemplate = displayVNExpress.articleScraper(article);
                break;
            default:
                break;
        }
        return htmlTemplate;
    }
}
