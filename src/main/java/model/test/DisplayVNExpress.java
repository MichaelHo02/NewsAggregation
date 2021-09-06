package model.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class DisplayVNExpress extends JsoupArticleDisplay {
    @Override
    public List<Content> getContent(String linkPage) {
        try {
            CONTENT.clear();
            Document doc = Jsoup.connect(linkPage).get();
            Elements elements = doc.select("div.sidebar-1 *");
            System.out.println(elements.size());
            for (Element element: elements) {
                if (element.tagName().equals("p") || element.tagName().equals("h1")) {
                    Content tempP = new Content(element.text(), "p");
                    CONTENT.add(tempP);
//                    System.out.println(element.text());
                } else if (element.tagName().equals("img")) {
                    Content tempImg = new Content(element.attr("data-src"),"img");
                    CONTENT.add(tempImg);
//                    System.out.println(element.attr("data-src"));
                } else if (element.tagName().matches("h\\d")) {
                    Content tempH = new Content(element.text(), "h");
                    CONTENT.add(tempH);
                    System.out.println(tempH.getContext());
                } else if (element.hasAttr("src")) {
                    Content tempVideo = new Content(element.attr("src"), "video");
                    System.out.println(tempVideo.getContext());
                }
            }
            System.out.println("After scraped array size: " + CONTENT.size());
            // Bug with formatted article: https://vnexpress.net/doanh-nghiep-dong-hanh-cung-thanh-pho-chong-covid-19-4340046.html
            return CONTENT;
        } catch (Exception e) {
            System.out.println("Failed connection to the destination page");
            return null;
        }
    }

    public static void main(String[] args) {
        DisplayVNExpress vn = new DisplayVNExpress();
        vn.getContent("https://vnexpress.net/lap-bon-cau-tren-oto-mot-chi-co-o-an-do-4340989.html");
    }
}
