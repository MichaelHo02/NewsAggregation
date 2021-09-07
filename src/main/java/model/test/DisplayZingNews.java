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
            addZing(elements);
            //Get author info
            Content author = new Content(doc.getElementsByClass("author").text(), "p");
            CONTENT.add(author);
        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayZingNews");
        }
        return CONTENT;
    }

    private void addZing(Elements elements) {
        for (Element e : elements) {
            try {
                // Create and add label if element is text
                if (e.is("p")) {
                    Content tmp = new Content(e.text(), "p");
                    CONTENT.add(tmp);
                }
                // Create and add wrapnote if element is wrapnote
                else if (e.is("div") && e.attr("class").equals("notebox ncenter")) {
                    addZing(e.select("> *"));
                }
                // Create and add header label if element is header
                else if (e.is("h3")) {
                    Content tmp = new Content(e.text(), "h");
                    CONTENT.add(tmp);
                }
                // Create and add images if element is image/gallery
                else if (e.is("table") && e.attr("class").contains("picture")) {
                    for (Element i : e.select("td.pic > *")) {
                        String imageURL = i.select("img").attr("data-src");
                        if (imageURL.equals("")) imageURL = i.select("img").attr("src");
                        Content img = new Content(imageURL, "img");
                        CONTENT.add(img);
                        Content tmp = new Content(e.select("td[class=\"pCaption caption\"]").text(), "p");
                    }
                } else if (e.is("h1") && e.select("img").size() > 0) {
                    Content tmp = new Content(e.select("img").attr("data-src"), "img");
                    CONTENT.add(tmp);
                }
                // For covid graph
                else if (e.is("div") && e.attr("class").contains("widget")) {
                    Content tmp = new Content(e.attr("data-src"), "img");
                    CONTENT.add(tmp);
                }
                // Create and add group of text
                else if (e.is("ul") || e.is("div")) {
                    addZing(e.select("> *"));
                } else if (e.hasText() && e.is("li")) {
                    Content tmp = new Content(e.text(), "p");
                }
                // Create and add blockquote
                else if (e.is("blockquote")) {
                    addZing(e.select("> *"));
                }
            } catch (IllegalArgumentException ex) {
                continue;
            }
        }
    }

}
