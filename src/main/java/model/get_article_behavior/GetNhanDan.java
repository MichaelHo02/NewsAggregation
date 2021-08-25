package model.get_article_behavior;

import com.github.sisyphsu.dateparser.DateParserUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class GetNhanDan extends ScrappingEngine {
    @Override
    public List<Article> getArticle(String url, int qty) {
        List<Article> articles = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            for (Element element : doc.select("div.box-title > a[href]")) { // Fetch all links
                String tempLink = "https://nhandan.vn" + element.attr("href"); // Join links
//                System.out.println(tempLink);
                Document tempDoc = Jsoup.connect(tempLink).get(); // Request to the destination link and extract contents
                String title = tempDoc.select(".box-title-detail.entry-title").text();
                String date = tempDoc.select(".box-date.pull-left").text();
                String imageURL = tempDoc.select("img").attr("data-src");
                String category = tempDoc.select(".uk-breadcrumb a").text();

                // Uncomment these lines for testing purpose
//                System.out.println("Title: " + title);
//                System.out.println("Date: " + date);
//                System.out.println("Img: " + imageURL);
//                System.out.println("Category: " + category);
                if (title.equals("") || title.isBlank() || title.isEmpty()) { // Handle unpassable article
                    continue;
                }
                else if (date.equals("") || date.isBlank() || date.isEmpty()) { // Handle abnormal date format
                    continue;
                }
                if (!(imageURL.contains("jpg") || imageURL.contains("JPG") || imageURL.contains("jpeg") || imageURL.contains("png"))) {
                    imageURL = ""; // Prevent bugs with ImageView
                }
                Article article = new Article(title, tempLink, DateParserUtils.parseDate(date.substring(9)), imageURL, WebsiteURL.NHANDAN, category);
                articles.add(article);
                return articles;
            }
        } catch (Exception e) {
            System.out.println("Failed to connect");
        }
        return null;
    }

//    public static void main(String[] args) {
//        GetNhanDan test = new GetNhanDan();
//        test.getArticle("https://nhandan.vn/kinhte", 50);
//    }
}
