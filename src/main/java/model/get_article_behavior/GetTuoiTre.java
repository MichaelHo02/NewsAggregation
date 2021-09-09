/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Bui Minh Nhat s3878174
        Last modified date: 10/09/2021
        Contributor: Student name, Student ID
        Acknowledgement:
         https://www.w3schools.com/cssref/css_selectors.asp
        https://openplanning.net/10399/jsoup-java-html-parser
        https://www.youtube.com/watch?v=l1mER1bV0N0&ab_channel=WebDevSimplified
        https://jsoup.org/cookbook/extracting-data/selector-syntax
        https://nira.com/chrome-developer-tools/#:~:text=From%20the%20Chrome%20menu%3A,web%20page%20you're%20on.
 */
package model.get_article_behavior;

import com.github.sisyphsu.dateparser.DateParserUtils;

import model.database.ArticleFilter;
import model.scrapping_engine.InitScraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class GetTuoiTre extends GetArticleBehavior implements Runnable {
    private String url;

    public GetTuoiTre(String url) {
        this.url = url;
    }

    @Override
    public void scrapeArticle(String url, ArrayList<Article> articles) {
        try {
            Document doc;
            if (url.contains("https")) {
                doc = Jsoup.connect(url).get();
            } else {
                doc = Jsoup.connect("https://beta.tuoitre.vn" + url).get();
            }
            for (Element element : doc.select("h2 > a[href]")) { // Fetch all links
                // get article url
                String tempLink = WebsiteURL.TUOITRE.getUrl() + element.attr("href"); // Join links
                if(tempLink.contains("javascript")) {
                    continue;
                }
                Document tempDoc = Jsoup.connect(tempLink).get(); // Request to the destination link and extract contents
                // get title, date, image url and category meta
                String title = tempDoc.select(".article-title").text();
                String date = crazyDateString(tempDoc.select(".date-time").text());
                String imageURL = tempDoc.select(".VCSortableInPreviewMode").select("img").attr("src");
                String category = tempDoc.select("meta[name='keywords']" ).attr("content");

                if (title.equals("") || title.isBlank() || title.isEmpty()) { // Handle unpassable article
                    continue;
                }
                else if (date.equals("") || date.isBlank() || date.isEmpty()) { // Handle abnormal date format
                    continue;
                }
                if (!(imageURL.contains("jpg") || imageURL.contains("JPG") || imageURL.contains("jpeg") || imageURL.contains("png"))) {
                    imageURL = ""; // Prevent bugs with ImageView
                }
                // construct and add article to collection
                Article article = new Article(title, tempLink, DateParserUtils.parseDate(date), imageURL, WebsiteURL.TUOITRE, category);
                synchronized(this) {
                    if (ArticleFilter.filterArticle(article)) {
                        articles.add(article);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to connect in GetTuoiTre");
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
