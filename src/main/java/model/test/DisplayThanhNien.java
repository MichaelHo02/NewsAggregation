package model.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class DisplayThanhNien extends JsoupArticleDisplay {

    @Override
    public List<Content> getContent(String linkPage) {
        try {
            CONTENT.clear();
            Document doc = Jsoup.connect(linkPage).get();
//          // TODO: Scrap sapo
            String firstImage = doc.select("#contentAvatar img").attr("src");
            Elements elements = doc.getElementsByClass("cms-body detail").select("*");

            for (Element element : elements) {
                if (element.tagName().equals("img")) {
                    Content tempImg = new Content(element.attr("src"),"img");
                    CONTENT.add(tempImg);
                } else if (element.tagName().equals("p")) {
                    Content tempP = new Content(element.text(), "p");
                    CONTENT.add(tempP);
                } else if (element.hasClass("clearfix cms-video")) {
                    Content tempVideo = new Content(element.attr("data-video-src"), "video");
                    System.out.println(tempVideo.getContext());
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
        DisplayThanhNien test = new DisplayThanhNien();
//        DisplayNhanDan test2 = new DisplayNhanDan();
        test.getContent("https://thanhnien.vn/thoi-su/tphcm-dat-muc-tieu-kiem-soat-dich-covid-19-o-7-quan-huyen-vao-cuoi-thang-8-1430885.html");
//        System.out.println(test2.getContent("https://nhandan.vn/chuyen-lam-an/gia-van-tai-bien-tang-phi-ma-doanh-nghiep-xuat-khau-lao-dao-659336/").get(0).getContext());
    }
}
