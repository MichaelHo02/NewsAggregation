package model.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class DisplayTuoiTre extends JsoupArticleDisplay {

    @Override
    public List<Content> getContent(String linkPage) {
        try {
            CONTENT.clear();
            Document doc = Jsoup.connect(linkPage).get();
            Elements elements = doc.select("div#main-detail-body.content.fck:nth-child(3) *");

            System.out.println(elements.size());
            for (Element element : elements) {
                if (element.tagName().equals("img")) {
                    Content tempImg = new Content(element.attr("src"),"img");
                    CONTENT.add(tempImg);
                } else if (element.tagName().equals("p")) {
                    Content tempP = new Content(element.text(), "p");
                    CONTENT.add(tempP);
                } else if (element.tagName().matches("h\\d")) {
                    Content tempH = new Content(element.text(), "h");
                    CONTENT.add(tempH);
                    System.out.println(tempH.getContext());
                }
            }

        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayTuoiTre");
        }
        return CONTENT;
    }

    public static void main(String[] args) throws Exception {
        DisplayTuoiTre test = new DisplayTuoiTre();
        test.getContent("https://congnghe.tuoitre.vn/ve-tinh-nanodragon-made-in-vietnam-len-duong-sang-nhat-20210811195434019.htm");
    }
}
