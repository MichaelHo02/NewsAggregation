package model.display;

import model.scrapping_engine.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DisplayVNExpress extends DisplayArticle {
    @Override
    public String articleScraper(Article article) {
        try {
            Document doc = Jsoup.connect(article.getLinkPage()).get();
            return "<html>\n" +
                    "<head>\n" +
                    doc.getElementsByTag("head").select("*").toString() +
                    "</head>\n" +
                    "<body>\n" +
                    doc.select("h1.title-detail").toString() +
                    "<h1 style=\"font-size:30px;font-weight:bold;\">" + doc.select("h1.title-detail").text() + "</h1>" +
                    doc.select("p.description").toString() +
                    doc.getElementsByClass("container").select(".fig-picture, p.Normal, style, script").toString() +
                    "</body>\n" +
                    "</html>";
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
}
