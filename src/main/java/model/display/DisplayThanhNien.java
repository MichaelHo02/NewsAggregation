package model.display;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.scrapping_engine.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DisplayThanhNien extends DisplayArticle {
    public DisplayThanhNien() {
    }

    @Override
    public String articleScraper(Article article) {
        try {
            Document doc = Jsoup.connect(article.getLinkPage()).get();
            String tempTemplate = "<html>\n" +
                    "<head>\n" +
                    doc.getElementsByTag("h1").select("*").toString() +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1 style = \"font-size:38px;font-weight:bold;\">\n" +
                    doc.select(".details__headline").text() + "</h1>\n" +
                    "<p>\n" +
                    doc.select(".sapo").toString() +
                    doc.getElementsByClass("cms-body detail").select("div") +
                    "</p>\n" +
                    "</body>\n" +
                    "</html>";
            return tempTemplate;
        } catch (Exception e) {
            return  "<html>" +
                    "<head>" +
                    "Article Scrapping Error" +
                    "</head>\n" +
                    "<body>" +
                    "<h1 style=\"font-size:30px;font-weight:bold;\">" + "Unexpected errors" +
                    "</h1>" +
                    "</body>" +
                    "</html>";
        }

    }

    public static void main(String[] args) {
        DisplayThanhNien test = new DisplayThanhNien();
        Article k = new Article();
        test.articleScraper(k);
    }
}
