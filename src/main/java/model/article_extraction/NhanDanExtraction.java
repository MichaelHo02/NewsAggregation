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
            Document doc = Jsoup.connect(linkPage).get();
            String firstImage = doc.getElementsByClass("box-detail-thumb uk-text-center").select("img").attr("src");
            ArticleFactory firstImg = new ArticleFactory(firstImage, "img");
            ARTICLE_FACTORY.add(firstImg);

            Elements elements = doc.getElementsByClass("detail-content-body").select("*");

            System.out.println(elements.size());

            for (Element element: elements) {
                if (element.tagName().equals("img")) {
                    ArticleFactory tempImg = new ArticleFactory(element.attr("src"),"img");
                    ARTICLE_FACTORY.add(tempImg);
                    System.out.println(element.attr("src"));
                } else if (element.tagName().equals("p")) {
                    ArticleFactory tempP = new ArticleFactory(element.text(), "p");
                    ARTICLE_FACTORY.add(tempP);
                    System.out.println(element.text());
                } else if (element.tagName().matches("h\\d")) {
                    ArticleFactory tempH = new ArticleFactory(element.text(), "h");
                    ARTICLE_FACTORY.add(tempH);
                    System.out.println(tempH.getContext());
                }
            }

            //Add the author element
            ArticleFactory author = new ArticleFactory(doc.select("div.box-author strong").text(),"author");
            ARTICLE_FACTORY.add(author);

        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayNhanDan");
        }
        return ARTICLE_FACTORY;
    }
}
