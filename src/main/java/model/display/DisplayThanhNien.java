package model.display;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.scrapping_engine.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DisplayThanhNien extends DisplayArticle {
    @Override
    public String articleScraper(Article article) {
        return null;
    }

    //@Override
    public WebView articleScraperr(String url) {
        WebView webView = new WebView();
        WebEngine webEng = webView.getEngine();
        try {
            Document doc = Jsoup.connect(url).get();
            String tempTemplate = "<html>\n" +
                    "<head>\n" +
                    doc.getElementsByTag("h1").select("*").toString() +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1 style = \"font-size:38px;font-weight:bold;\">\n" +
                    doc.select(".details__headline").text() + "</h1>\n" +
                    doc.select(".sapo").toString() +
                    doc.getElementsByClass("cms-body detail").select("div").text() +
                    "</body>\n" +
                    "</html>";
            webEng.loadContent(tempTemplate);
            return webView;
        }
        catch (Exception e) {
            System.out.println("Cannot connect to page");
            return null;
        }

    }
}
