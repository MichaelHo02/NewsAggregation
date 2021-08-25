package model.display;

import model.get_article_behavior.Article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DisplayNhanDan extends DisplayArticle{
    public DisplayNhanDan() {
    }

    @Override
    public String articleScraper(Article article) {
        try {
            Document doc = Jsoup.connect(article.getLinkPage()).get();
            return "<html>\n" +
                    "<head>\n" +
                    doc.getElementsByTag("h1").select("*").toString() +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1 style = \"font-size:38px;font-weight:bold;\">\n" +
                    doc.select("h1.box-title-detail").text() + "</h1>\n" +
                    doc.getElementsByClass("box-des-detail").select("*").text() +
                    doc.getElementsByClass("detail-content-body").select("p, .light-img").text() +
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
