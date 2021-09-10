package model.get_article_behavior;
        import com.github.sisyphsu.dateparser.DateParserUtils;
        import model.database.Article;
        import model.database.ArticleFilter;
        import model.scrapping_engine.InitScraper;
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
        import java.time.LocalDate;
        import java.time.LocalDateTime;
        import java.time.ZoneId;
        import java.time.format.DateTimeFormatter;
        import java.util.ArrayList;
        import java.util.Date;

public class GetVnexpress extends GetArticleBehavior implements Runnable {
    private String url;

    public GetVnexpress(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        scrapeArticle(InitScraper.articles);
    }

    @Override
    public void scrapeArticle(ArrayList<Article> articles) {
        try {
            Document doc = Jsoup.connect(url).timeout(10000).get();
            for (Element element : doc.select("h3 > a[href]")) { // Fetch all links
                // get article url
                String tempLink = element.attr("href"); // Join links
                System.out.println(tempLink);
                if(tempLink.contains("javascript")) {
                    continue;
                }
                System.out.println("Get");
                try {
                    Document innerDoc = Jsoup.connect(tempLink).ignoreHttpErrors(true).get(); // Request to the destination link and extractor contents
                    // get title, date, image url and category meta
                    System.out.println("Ok");
                    String title = innerDoc.select("h3").text();
                    if (title.equals("")) {
                        title = innerDoc.select("h2").text();
                        if (title.equals("")) {
                            title = innerDoc.select("h1").text();
                            if (title.isEmpty()) {
                                continue;
                            }
                        }
                    }
                    System.out.println("Title:" + title);
                    String category = innerDoc.select("meta[name='keywords']").attr("content");
                    System.out.println(category);
                    String date = innerDoc.select("span.date").text();
                    date = extractor(date,", ","(GMT+7");
                    Date parse = DateParserUtils.parseDate(date);
                    String imageURL = innerDoc.select("article.fck_detail").select("img").attr("data-src");
                    System.out.println("Image : " + imageURL);
                    //Filtering out wierd stuff
                    if (title.equals("") || title.isBlank() || title.isEmpty()) { // Handle unpassable article
                        continue;
                    }
                    else if (date.equals("") || date.isBlank() || date.isEmpty()) { // Handle abnormal date format
                        continue;
                    }
                    if (!(imageURL.contains("jpg") || imageURL.contains("JPG") || imageURL.contains("jpeg") || imageURL.contains("png"))) {
                        imageURL = ""; // Prevent bugs with ImageView
                    }

                    Article article = new Article(title, tempLink, parse, imageURL, WebsiteURL.VNEXPRESS, category);
                    //Filter the article
                    synchronized(this) {
                        if (ArticleFilter.filterArticle(article)) {
                            articles.add(article);
                        }
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to connect in GetVNEXPRESS");
        }
    }

    public static String extractor(String input, String begin, String last) {
        //Determine the begin index to keep
        int start= input.indexOf(begin);
        int end = 0;
        String tmp = input.substring(start + begin.length());
        end = tmp.indexOf(last);
        // Trim from right side
        tmp = tmp.substring(0, end).replace(",","");
        //Put the string in to appropriate order
        String [] tmpStr = tmp.split(" ");
        tmp = tmpStr[0] + " " + tmpStr[1];
        return tmp;
    }
    //For testing
    //The content of this is similar in the content of the implementation
    public static void main(String[] args) {
        try {
            //Inpu URLS
            String  urls = "https://vnexpress.net/thoi-su";
            Document doc = Jsoup.connect(urls).timeout(10000).get();
            for (Element element : doc.select("h3 > a[href]")) { // Fetch all links
                // get article url
                String tempLink = element.attr("href"); // Join links
                System.out.println(tempLink);
                if(tempLink.contains("javascript")) {
                    continue;
                }
                System.out.println("Get");
                try {
                    Document innerDoc = Jsoup.connect(tempLink).ignoreHttpErrors(true).get(); // Request to the destination link and extractor contents
                    // get title, date, image url and category meta
                    System.out.println("Ok");
                    String title = innerDoc.select("h3").text();
                    if (title.equals("")) {
                        title = innerDoc.select("h2").text();
                        if (title.equals("")) {
                            title = innerDoc.select("h1").text();
                            if (title.isEmpty()) {
                                continue;
                            }
                        }
                    }
                    System.out.println("Title:" + title);
                    String category = innerDoc.select("meta[name='keywords']").attr("content");
                    System.out.println(category);
                    String date = innerDoc.select("span.date").text();
                    date = extractor(date,", ","(GMT+7");
                    Date parse = DateParserUtils.parseDate(date);
                    System.out.println(parse);
                    String imageURL = innerDoc.select("article.fck_detail").select("img").attr("data-src");
                    System.out.println("Image : " + imageURL);
                    Article article = new Article(title, tempLink, parse, imageURL, WebsiteURL.VNEXPRESS, category);
                    //Filter the article

                }catch(Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to connect in GetVNEXPRESS");
        }
    }
}
