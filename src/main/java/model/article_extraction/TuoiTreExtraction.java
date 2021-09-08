package model.article_extraction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class TuoiTreExtraction extends ArticleExtractor {

    @Override
    public List<ArticleFactory> getContent(String linkPage) {
        try {
            ARTICLE_FACTORY.clear();
            Document doc = Jsoup.connect(linkPage).get();
            Elements elements = doc.select("div#mainContentDetail");
            //Add Description
            ArticleFactory des = new ArticleFactory(elements.select("h2").text(), "p");
            ARTICLE_FACTORY.add(des);
            //Get the main body of the article
            articleBody( doc.select("div#main-detail-body > *"));
            //Get tauthor
            if (elements.select("div.author").size() > 0) {
                ArticleFactory author = new ArticleFactory(elements.select("div.author").text(), "author");
                ARTICLE_FACTORY.add(author);
            }

        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayTuoiTre");
        }
        return ARTICLE_FACTORY;
    }

    public void articleBody(Elements div) {
        for (Element ele : div) {
            try {
                if (ele.is("p") && ele.hasText()) { //If element is a ordinary p element
                    ArticleFactory tmp = new ArticleFactory(ele.text(), "p");
                    ARTICLE_FACTORY.add(tmp);
                } else if (ele.is("div")) { //If element is a p tag
                    // Add image if element is image
                    if (ele.attr("type").equals("Photo")) {
                        String address = ele.select("img").attr("src");
                        try {
                            //Clean up image url
                            address = address.replace("thumb_w/586/", "");
                        } catch (Exception ignored) {}
                        ArticleFactory img = new ArticleFactory(address, "img");
                        ARTICLE_FACTORY.add(img);
                        ArticleFactory cap = new ArticleFactory(ele.select("p").text(), "caption");
                        ARTICLE_FACTORY.add(cap);
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
