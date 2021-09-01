package model.get_article_behavior;

import com.github.sisyphsu.dateparser.DateParserUtils;
import model.scrapping_engine.InitScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.concurrent.CopyOnWriteArrayList;

public class GetTuoiTre extends GetArticleBehavior implements Runnable {
    private String url;

    public GetTuoiTre(String url) {
        this.url = url;
    }

    @Override
    public void scrapeArticle(String url, CopyOnWriteArrayList<Article> articles) {
        try {
//            if(url.contains("javascript")) {
//                throw   ;
//            }
            Document doc = Jsoup.connect("https://beta.tuoitre.vn/").get();
            for (Element element : doc.select("h2 > a[href]")) { // Fetch all links
                String tempLink = "https://tuoitre.vn/" + element.attr("href"); // Join links
                if(tempLink.contains("javascript")) {
                    continue;
                }
                System.out.println(tempLink);
                Document tempDoc = Jsoup.connect(tempLink).get(); // Request to the destination link and extract contents
                String title = tempDoc.select(".article-title").text();
                String date = crazyDateString(tempDoc.select(".date-time").text());
                String imageURL = tempDoc.select(".VCSortableInPreviewMode").select("img").attr("src");
                String category = tempDoc.select("meta[name='keywords']" ).attr("content");

                // Uncomment these lines for testing purpose
                System.out.println("Title: " + title);
                System.out.println("Date: " + date);
                System.out.println("Img: " + imageURL);
                System.out.println("Category: " + category);
                if (title.equals("") || title.isBlank() || title.isEmpty()) { // Handle unpassable article
                    continue;
                }
                else if (date.equals("") || date.isBlank() || date.isEmpty()) { // Handle abnormal date format
                    continue;
                }
                if (!(imageURL.contains("jpg") || imageURL.contains("JPG") || imageURL.contains("jpeg") || imageURL.contains("png"))) {
                    imageURL = ""; // Prevent bugs with ImageView
                }
                Article article = new Article(title, tempLink, DateParserUtils.parseDate(date), imageURL, WebsiteURL.TUOITRE, category);
                articles.add(article);
            }
        } catch (Exception e) {
            //Uncomment to see stacktrace
//            e.printStackTrace();
            System.out.println("Failed to connect");
        }
    }

    @Override
    public void run() {
        scrapeArticle(this.url, InitScraper.articles);
    }

    public String crazyDateString(String dateString) {
        String[] tmp = dateString.split(" ");
        return tmp[0] +" " + tmp[1];
    }
}
