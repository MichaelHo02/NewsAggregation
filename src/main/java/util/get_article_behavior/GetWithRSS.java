/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author:
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat s3878174, Nguyen Dich Long s3879052
        Acknowledgement:
        https://mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
        https://www.baeldung.com/java-synchronized
 */
package util.get_article_behavior;

import com.apptastic.rssreader.Item;
import com.apptastic.rssreader.RssReader;
import com.github.sisyphsu.dateparser.DateParserUtils;

import util.filter.ArticleFilter;
import model.database.Article;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GetWithRSS extends GetArticleBehavior implements Runnable {

    private final String URL;
    private final List<Article> articles;

    public GetWithRSS(String url, List<Article> articles) {
        this.articles = articles;
        this.URL = url;
    }
    @Override
    public void scrapeArticle() {
        try {
            RssReader reader = new RssReader();
            Stream<Item> rssFeed = reader.read(URL);
            List<Item> itemList = rssFeed.collect(Collectors.toList());
            for (Item item : itemList) {
                // Get article title, article url, date, image url and category
                String title = item.getTitle().isPresent() ? item.getTitle().get() : null;
                String link = item.getLink().isPresent() ? item.getLink().get() : null;
                String pubDate = item.getPubDate().isPresent() ? item.getPubDate().get() : null;
                String image = item.getDescription().isPresent() ? item.getDescription().get() : null;
                String source = item.getChannel().getTitle().isBlank() ? null : item.getChannel().getTitle();
                String category = title + " " + scrapeCategory(URL, 3) + " " + scrapeCategory(URL, 4);
                if (title == null || link == null || pubDate == null || image == null || source == null) { continue; }
                Article article = new Article(title, link, DateParserUtils.parseDate(pubDate), GetArticleBehavior.getImage(image), getSource(source), category);
                // Stop all thread to write the array
                synchronized(this) { // handle selected articles
                    if (ArticleFilter.filterArticle(article)) {
//                        System.out.println(article.getLinkPage());
//                        System.out.println(article.getCategories());
//                        System.out.println(article.getDuration());
                        articles.add(article);
                    }
                }
            }
        } catch (MalformedURLException ignored) {
//            System.out.println("Url Error in GetWithRSS");
        } catch (IOException ignored) {
//            System.out.println("XML parser error in GetWithRSS");
        }
    }

    @Override
    public void run() {
        scrapeArticle();
    }

}