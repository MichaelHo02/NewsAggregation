package model.scrapping_engine;

import com.apptastic.rssreader.Item;
import com.apptastic.rssreader.RssReader;
import com.github.sisyphsu.dateparser.DateParserUtils;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetWithRSS extends ScrappingEngine {

    @Override
    public List<Article> getArticle(String url, int qty) {
        List<Article> articles = new ArrayList<>();
        try {
            RssReader reader = new RssReader();
            Stream<Item> rssFeed = reader.read(url);
            List<Item> itemList = rssFeed.collect(Collectors.toList());
            int count = 0;
            for (Item item : itemList) {
                if (count == qty) {
                    break;
                }
                count++;
                String title = item.getTitle().isPresent() ? item.getTitle().get() : null;
                String link = item.getLink().isPresent() ? item.getLink().get() : null;
//                Date publishDate = null;
                String pubDate = item.getPubDate().isPresent() ? item.getPubDate().get() : null;
                String image = item.getDescription().isPresent() ? item.getDescription().get() : null;
                String source = item.getChannel().getTitle().isBlank() ? null : item.getChannel().getTitle();
                System.out.println(source);
                assert pubDate != null;
                try {
                    System.out.println("RSS Stock:" + pubDate);
                    System.out.println("RSS: " + DateParserUtils.parseDate(pubDate).toString());
                } catch (Exception e) {
                    System.out.println("Failed");
                }
                Article article = new Article(title, link, DateParserUtils.parseDate(pubDate), ScrappingEngine.getImage(image), getSource(source), "");

                articles.add(article);
            }
        } catch (MalformedURLException e) {
            System.out.println("Url Error");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("XML parser error");
        }
        return articles;
    }

//    public static void main(String[] args) {
//        GetWithRSS getWithRSS = new GetWithRSS();
//        getWithRSS.getArticle("");
//    }
}