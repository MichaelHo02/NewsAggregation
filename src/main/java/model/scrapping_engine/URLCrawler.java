package model.scrapping_engine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class URLCrawler {

    public ArrayList<String> getURLList(String url) {
        ArrayList<String> urlList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            // thanh nien full link
            // vnexpress, tuoi tre /rss/link (needs to be joined)
            // nhan dan needs to be joined
            // zingnews needs to be joined
            Elements elements = doc.select(".list-rss li a[href], .rss-list li a[href], .category-menu li a[href], .uk-nav li a[href]");
            for (Element element : elements) {
                if (url.contains("vnexpress")) {
                    urlList.add("https://vnexpress.net" + element.attr("href"));
                } else if (url.contains("tuoitre")) {
                    urlList.add("https://tuoitre.vn" + element.attr("href"));
                } else if (url.contains("nhandan")) {
                    urlList.add("https://nhandan.vn" + element.attr("href"));
                } else if (url.contains("zingnews")) {
                    urlList.add("https://zingnews.vn" + element.attr("href"));
                } else {
                    String temp = element.attr("href");
                    if (temp.contains("video")) { continue; }
                    urlList.add(temp);
                }
            }
        } catch (Exception e) {
            System.out.println("cannot connect to page");
        }
        return urlList;
    }



}
