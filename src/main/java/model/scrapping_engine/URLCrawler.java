package model.scrapping_engine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class URLCrawler {
    public static ArrayList<String> urlSelector(String url) {
        ArrayList<String> urlList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("menu.ul menu.li1 a[href], ul:first-of-type a[href]");
            //first().select("a[href]");
            for (Element element : elements) {
                urlList.add(element.attr("href"));
            }
        } catch (Exception e) {
            System.out.println("cannot connect to page");
        }
        return urlList;
    }
}
