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
            Elements elements = doc.select("div.the-article-body > *");

            //Conntet
            Content tmp = new Content(doc.select("p.the-article-summary").text(), "h");
            CONTENT.add(tmp);
            addZingArt(elements);
            //Get author info
            Content author = new Content(doc.getElementsByClass("author").text(), "p");
            CONTENT.add(author);
        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayZingNews");
        }
        return CONTENT;
    }
//Scrape content of ZingNews
    private void addZingArt(Elements elements) {
        for (Element ele : elements) {
            try {
                if (ele.is("p")) { //Check if element not author
                    Content tmp = new Content(ele.text(), "p");
                    CONTENT.add(tmp);
                } else if (ele.is("div") && ele.attr("class").equals("notebox ncenter")) {
                    addZingArt(ele.select("> *"));
                } else if (ele.is("h3")) { //Check header
                    Content tmp = new Content(ele.text(), "h");
                    CONTENT.add(tmp);
                } else if (ele.is("table") && ele.attr("class").contains("picture")) { //For full picture post
                    for (Element inner : ele.select("td.pic > *")) {
                        String imageURL = inner.select("img").attr("data-src");
                        if (imageURL.equals("")) imageURL = inner.select("img").attr("src");
                        Content img = new Content(imageURL, "img");
                        CONTENT.add(img);
                        Content tmp = new Content(ele.select("td[class=\"pCaption caption\"]").text(), "p");
                        CONTENT.add(tmp);
                    }
                } else if (ele.is("h1") && ele.select("img").size() > 0) {
                    Content tmp = new Content(ele.select("img").attr("data-src"), "img");
                    CONTENT.add(tmp);
                } else if (ele.is("div") && ele.attr("class").contains("widget")) { //Check graph covid
                    Content tmp = new Content(ele.attr("data-src"), "img");
                    CONTENT.add(tmp);
                } else if (ele.is("ul") || ele.is("div")) { //If see a block of tag
                    addZingArt(ele.select("> *"));
                } else if (ele.hasText() && ele.is("li")) {
                    Content tmp = new Content(ele.text(), "p");
                } else if (ele.is("blockquote")) {
                    addZingArt(ele.select("> *"));
                }
            } catch (Exception ex) {
                continue;
            }
        }
    }

}
