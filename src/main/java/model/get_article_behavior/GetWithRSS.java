package model.get_article_behavior;

import com.apptastic.rssreader.Item;
import com.apptastic.rssreader.RssReader;
import com.github.sisyphsu.dateparser.DateParserUtils;
import model.database.ArticleFilter;
import model.scrapping_engine.InitScraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GetWithRSS extends GetArticleBehavior implements Runnable {

    private String url;
    public GetWithRSS(String url) {
        this.url = url;
    }
    @Override
    public void scrapeArticle(String url, ArrayList<Article> articles) {
        try {
            RssReader reader = new RssReader();
            Stream<Item> rssFeed = reader.read(url);
            List<Item> itemList = rssFeed.collect(Collectors.toList());
            for (Item item : itemList) {
                // get article title, article url, date, image url and category
                String title = item.getTitle().isPresent() ? item.getTitle().get() : null;
                String link = item.getLink().isPresent() ? item.getLink().get() : null;
                String pubDate = item.getPubDate().isPresent() ? item.getPubDate().get() : null;
                String image = item.getDescription().isPresent() ? item.getDescription().get() : null;
                String source = item.getChannel().getTitle().isBlank() ? null : item.getChannel().getTitle();
                String category = title;
                System.out.println("This is the category: " + category);
                // TODO: get category, not tested
                assert pubDate != null;
                Article article = new Article(title, link, DateParserUtils.parseDate(pubDate), GetArticleBehavior.getImage(image), getSource(source), category);
                synchronized(this) {
                    if (ArticleFilter.filterArticle(article)) {
                        articles.add(article);
                    }
                    System.out.println("This is the list for article category" + article.getCategories());
                }
            }
        } catch (MalformedURLException e) {
            System.out.println("Url Error");
        } catch (IOException e) {
            System.out.println("XML parser error");
        }
    }

    @Override
    public void run() {
        scrapeArticle(this.url, InitScraper.articles);
    }

}