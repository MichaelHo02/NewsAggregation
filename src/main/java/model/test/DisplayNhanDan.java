package model.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class DisplayNhanDan extends JsoupArticleDisplay {

    @Override
    public List<Content> getContent(String linkPage) {
        try {
            CONTENT.clear();
            Document doc = Jsoup.connect(linkPage).get();
            String firstImage = doc.getElementsByClass("box-detail-thumb uk-text-center").select("img").attr("src");
            Content firstImg = new Content(firstImage, "img");
            CONTENT.add(firstImg);

            Elements elements = doc.getElementsByClass("detail-content-body").select("*");

            System.out.println(elements.size());

            for (Element element: elements) {
                if (element.tagName().equals("img")) {
                    Content tempImg = new Content(element.attr("src"),"img");
                    CONTENT.add(tempImg);
                    System.out.println(element.attr("src"));
                } else if (element.tagName().equals("p")) {
                    Content tempP = new Content(element.text(), "p");
                    CONTENT.add(tempP);
                    System.out.println(element.text());
                } else if (element.tagName().matches("h\\d")) {
                    Content tempH = new Content(element.text(), "h");
                    CONTENT.add(tempH);
                    System.out.println(tempH.getContext());
                }
            }

            //Add the author element
            Content author = new Content(doc.select("div.box-author strong").text(),"author");
            CONTENT.add(author);

        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayNhanDan");

        }
        return CONTENT;
    }
}
