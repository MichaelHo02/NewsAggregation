package model.display;

import model.get_article_behavior.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DisplayTuoiTre extends DisplayArticle{
        //Return the String of HTML of the article
    @Override
    public String articleScraper(Article article) {
        try {
            Document doc = Jsoup.connect(article.getLinkPage()).get();
            return "<html>\n"+
                    "<head>\n" +
                    doc.getElementsByTag("h1").select("*").toString() +
                    "</head>\n" +
                    "<body>\n"  +
                    "<h1 style = \"font-size:38px;font-weight:bold;\">\n" +
                    doc.select("h1.article-title").text() +
                    "</h1>\n" +
                    "<h2> \n" +
                    doc.select("h2.sapo").text() +
                    "</h2>\n" +
                    "<p>\n" +
                    doc.getElementsByClass("VCSortableInPreviewMode").select("p,img")+
                    "</p>\n" +
                    "<p>\n" +
                    doc.select("div.author").text() +
                    "</p>\n" +
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
    public static void main(String[] args) {
        DisplayTuoiTre test = new DisplayTuoiTre();
        Article k = new Article();
        System.out.println(test.articleScraper(k));
    }
//    @Override
//    public WebView articleScraperr(String url) {
//        return null;
//    }

}
