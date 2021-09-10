package model.get_article_behavior;

import com.github.sisyphsu.dateparser.DateParserUtils;
import model.database.ArticleFilter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//
//public class GetVnexpress extends GetArticleBehavior implements Runnable {
//
//    @Override
//    public void run() {
//
//    }
//
//    @Override
//    public void scrapeArticle(String url, ArrayList<Article> articles) {
//        try {
//            Document doc;
//            if (url.contains("https")) {
//                doc = Jsoup.connect(url).get();
//            } else {
//                doc = Jsoup.connect("https://beta.tuoitre.vn" + url).get();
//            }
//            for (Element element : doc.select("h2 > a[href]")) { // Fetch all links
//                // get article url
//                String tempLink = "https://tuoitre.vn/" + element.attr("href"); // Join links
//                if(tempLink.contains("javascript")) {
//                    continue;
//                }
//                Document tempDoc = Jsoup.connect(tempLink).get(); // Request to the destination link and extract contents
//                // get title, date, image url and category meta
//                String title = tempDoc.select(".article-title").text();
//                String date = crazyDateString(tempDoc.select(".date-time").text());
//                String imageURL = tempDoc.select(".VCSortableInPreviewMode").select("img").attr("src");
//                String category = tempDoc.select("meta[name='keywords']" ).attr("content");
//
//                if (title.equals("") || title.isBlank() || title.isEmpty()) { // Handle unpassable article
//                    continue;
//                }
//                else if (date.equals("") || date.isBlank() || date.isEmpty()) { // Handle abnormal date format
//                    continue;
//                }
//                if (!(imageURL.contains("jpg") || imageURL.contains("JPG") || imageURL.contains("jpeg") || imageURL.contains("png"))) {
//                    imageURL = ""; // Prevent bugs with ImageView
//                }
//                // construct and add article to collection
//                Article article = new Article(title, tempLink, DateParserUtils.parseDate(date), imageURL, WebsiteURL.TUOITRE, category);
//                synchronized(this) {
//                    if (ArticleFilter.filterArticle(article)) {
//                        articles.add(article);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Failed to connect in GetTuoiTre");
//        }
//    }
//    private static String extract(String line, String start, String end) {
//        // Trim from left side
//        int firstPos = line.indexOf(start);
//        String temp = line.substring(firstPos + start.length());
//
//        // Trim from right side
//        int lastPos = temp.indexOf(end);
//        temp = temp.substring(0, lastPos);
//        return temp;
//    }
//
//}