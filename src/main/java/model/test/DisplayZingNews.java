package model.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class DisplayZingNews extends JsoupArticleDisplay {

    @Override
    public List<Content> getContent(String linkPage) {
        try {
            CONTENT.clear();
            Document doc = Jsoup.connect(linkPage).get();
            Elements elements = doc.getElementsByClass("the-article-body").select("*");

            System.out.println(elements.size());
            for (Element element : elements) {
                if (element.tagName().equals("img")) {
                    Content tempImg = new Content(element.attr("data-src"),"img");
                    CONTENT.add(tempImg);
                    System.out.println(element.attr("data-src"));
                } else if (element.tagName().equals("p")) {
                    Content tempP = new Content(element.text(), "p");
                    CONTENT.add(tempP);
                    System.out.println(element.text());
                } else if (element.tagName().equals("blockquote")) {
                    Content tempQuote = new Content(element.text(), "quote");
                    CONTENT.add(tempQuote);
                    System.out.println(tempQuote.getContext());
                } else if (element.tagName().matches("h\\d")) {
                    Content tempH = new Content(element.text(), "h");
                    CONTENT.add(tempH);
                    System.out.println(tempH.getContext());
                }
                //Display the content
//                }else if (element.tagName().equals("td")) {
//                    Content tempCaption = new Content(element.text(), "p");
//                    System.out.println(element.text());
//                }
            }

        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayZingNews");
        }
        return CONTENT;
    }

    public static void main(String[] args) throws Exception {
        DisplayZingNews test = new DisplayZingNews();
        test.getContent("https://zingnews.vn/tphcm-muon-som-mo-lai-cho-quan-huyen-noi-kho-post1250905.html");
    }
}
