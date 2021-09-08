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
            Elements elements = doc.select("div#mainContentDetail");
            //Add Description
            Content des = new Content(elements.select("h2").text(), "p");
            CONTENT.add(des);
            //Get the main body of the article
            articleBody( doc.select("div#main-detail-body > *"));
            //Get tauthor
            if (elements.select("div.author").size() > 0) {
                Content author = new Content(elements.select("div.author").text(), "author");
                CONTENT.add(author);
            }

        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayTuoiTre");
        }
        return CONTENT;
    }

    public void articleBody(Elements div) {
        for (Element ele : div) {
            try {
                if (ele.is("p") && ele.hasText()) { //If element is a ordinary p element
                    Content tmp = new Content(ele.text(), "p");
                    CONTENT.add(tmp);
                } else if (ele.is("div")) { //If element is a p tag
                    // Add image if element is image
                    if (ele.attr("type").equals("Photo")) {
                        String address = ele.select("img").attr("src");
                        try {
                            //Clean up image url
                            address = address.replace("thumb_w/586/", "");
                        } catch (Exception ignored) {}
                        Content img = new Content(address, "img");
                        CONTENT.add(img);
                        Content cap = new Content(ele.select("p").text(), "caption");
                        CONTENT.add(cap);
                    } else if (ele.attr("type").equals("wrapnote")) {
                        articleBody(ele.select("> *"));
                    }
                }
            } catch (Exception e) {
                System.out.println("Error display tuoitre");
                continue;
            }
        }
    }
}
