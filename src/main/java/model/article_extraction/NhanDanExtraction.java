package model.article_extraction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class NhanDanExtraction extends ArticleExtractor {

    @Override
    public List<ArticleFactory> getContent(String linkPage) {
        try {
            ARTICLE_FACTORY.clear();
            Document document = Jsoup.connect(linkPage).get();
            String firstImage = document.getElementsByClass("box-detail-thumb uk-text-center").select("img").attr("src");
            ARTICLE_FACTORY.add(new ArticleFactory(firstImage, "img"));

            Elements elements = document.getElementsByClass("detail-content-body").select("*");

            System.out.println(elements.size());

            for (Element element: elements) {
                if (element.tagName().equals("img")) {
                    ARTICLE_FACTORY.add( new ArticleFactory(element.attr("src"),"img"));
                    System.out.println(element.attr("src"));
                } else if (element.tagName().equals("p")) {
                    ARTICLE_FACTORY.add(new ArticleFactory(element.text(), "p"));
                } else if (element.tagName().matches("h\\d")) {
                    ARTICLE_FACTORY.add(new ArticleFactory(element.text(), "h"));
                }
            }
            //Add the author element
            ARTICLE_FACTORY.add(new ArticleFactory(document.select("div.box-author strong").text(),"author"));

        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayNhanDan");
        }
        return ARTICLE_FACTORY;
    }
}
