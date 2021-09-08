package model.article_extraction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class ZingNewsExtraction extends ArticleExtractor {

    @Override
    public List<ArticleFactory> getContent(String linkPage) {
        try {
            ARTICLE_FACTORY.clear();
            Document doc = Jsoup.connect(linkPage).get();
            Elements elements = doc.select("div.the-article-body > *");

            //Conntet
            ArticleFactory tmp = new ArticleFactory(doc.select("p.the-article-summary").text(), "h");
            ARTICLE_FACTORY.add(tmp);
            addZingArt(elements);
            //Get author info
            ArticleFactory author = new ArticleFactory(doc.getElementsByClass("author").text(), "author");
            ARTICLE_FACTORY.add(author);
        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayZingNews");
        }
        return ARTICLE_FACTORY;
    }
//Scrape content of ZingNews
    private void addZingArt(Elements elements) {
        for (Element ele : elements) {
            try {
                if (ele.is("p")) { //Check if element not author
                    ArticleFactory tmp = new ArticleFactory(ele.text(), "p");
                    ARTICLE_FACTORY.add(tmp);
                } else if (ele.is("div") && ele.attr("class").equals("notebox ncenter")) {
                    addZingArt(ele.select("> *"));
                } else if (ele.is("h3")) { //Check header
                    ArticleFactory tmp = new ArticleFactory(ele.text(), "h");
                    ARTICLE_FACTORY.add(tmp);
                } else if (ele.is("table") && ele.attr("class").contains("picture")) { //For full picture post
                    for (Element inner : ele.select("td.pic > *")) {
                        //Extract the URL
                        String imageURL = inner.select("img").attr("data-src");
                        if (imageURL.equals("")) imageURL = inner.select("img").attr("src");
                        ArticleFactory img = new ArticleFactory(imageURL, "img");
                        ARTICLE_FACTORY.add(img);
                        //Image catption
                        ArticleFactory tmp = new ArticleFactory(ele.select("td[class=\"pCaption caption\"]").text(), "caption");
                        ARTICLE_FACTORY.add(tmp);
                    }
                } else if (ele.is("h1") && ele.select("img").size() > 0) {
                    ArticleFactory tmp = new ArticleFactory(ele.select("img").attr("data-src"), "img");
                    ARTICLE_FACTORY.add(tmp);
                } else if (ele.is("div") && ele.attr("class").contains("widget")) { //Check graph covid
                    ArticleFactory tmp = new ArticleFactory(ele.attr("data-src"), "img");
                    ARTICLE_FACTORY.add(tmp);
                } else if (ele.is("ul") || ele.is("div")) { //If see a block of tag
                    addZingArt(ele.select("> *"));
                } else if (ele.hasText() && ele.is("li")) {
                    ArticleFactory tmp = new ArticleFactory(ele.text(), "p");
                } else if (ele.is("blockquote")) {
                    addZingArt(ele.select("> *"));
                }
            } catch (Exception e) {
                System.out.println("Error displaying the article");
                continue;
            }
        }
    }

}
