package model.display;

import javafx.scene.web.WebView;
import model.scrapping_engine.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DisplayZingNews extends DisplayArticle {
    @Override
    public String articleScraper(Article article) {
        try {
            Document doc = Jsoup.connect(article.getLinkPage()).get();
            String html = "<html>\n"+
                    "<head>\n" +
                    doc.getElementsByTag("h1").select("*").toString() +
                    "</head>\n" +
                    "<body>\n"  +
                    "<h1 style = \"font-size:38px;\">\n" +
                    doc.select("ul.the-article-meta").text() +
                    "</h1>\n" +
                    "<h2 style = \"font-size:20px;font-weight : bold;\">\n" +
                    doc.select("p.the-article-summary").text() +
                    "</h2>\n" +
                    "<p>\n" +
                    doc.getElementsByClass("the-article-body").select("td,p").text() +
                    "</p>\n" +
                    "</body>\n" +
                    "</html>";
            return html;
        } catch (Exception e) {
            return  "<html>" +"<head>" + "Article Scrapping Error" + "</head>\n" + "</html>";
        }
    }

//    @Override
//    public WebView articleScraperr(String url) {
//        return null;
//    }
}
