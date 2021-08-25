package model.display;


import model.get_article_behavior.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DisplayZingNews extends DisplayArticle {
    @Override
    public String articleScraper(Article article) {
        try {
            Document doc = Jsoup.connect(article.getLinkPage()).get();
            return "<html>\n"+
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
                    doc.getElementsByClass("the-article-body").select("td,p,blockquote") +
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
//    public static void main(String[] args) {
//        DisplayZingNews test = new DisplayZingNews();
//        Article k = new Article();
//        System.out.println(test.articleScraper(k));
//    }
//    @Override
//    public WebView articleScraperr(String url) {
//        return null;
//    }
}
