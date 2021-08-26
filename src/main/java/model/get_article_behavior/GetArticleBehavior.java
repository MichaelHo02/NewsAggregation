package model.get_article_behavior;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.Duration;
import java.util.Date;
import java.util.List;

public abstract class GetArticleBehavior {

    protected static WebsiteURL getSource(String source) {
        if (source.contains("VnExpress")) {
            System.out.println("check");
            return WebsiteURL.VNEXPRESS;
        } else if (source.contains("Tuổi Trẻ Online")) {
            return WebsiteURL.TUOITRE;
        } else if (source.contains("Thanh Niên")) {
            return WebsiteURL.THANHNIEN;
        } else if (source.contains("ZingNews")) {
            return WebsiteURL.ZINGNEWS;
        } else if (source.contains("nhandan")) {
            return WebsiteURL.NHANDAN;
        }
        return null;
    }
    //Tools for getArticle
    public static String getImage(String description) {
        //Create a storage document
        Document document = Jsoup.parse(description);
//        System.out.println(description);
        Elements images = document.select("img");
        //Cleanup image url
        String urlForm = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
        for (Element image : images) {
            if (image.hasAttr("src") && !image.attr("src").isBlank() && image.attr("src").matches(urlForm)) {
                return image.attr("src");
            }
        }
        return null;
    }

    public static String getFriendlyDate(Date date) {
        Date now = new Date();
        Duration duration = Duration.between(date.toInstant(), now.toInstant());
        if (duration == null) return null;

        long minute = duration.toMinutes();
        long hour = minute / 60;
        long day = hour / 24;
        long remaining_hour = hour % 24;
        long remaining_minute = minute % 60;
//            System.out.printf("Day: %d Hour: %d and Minute: %d and Total: %d\n", day, remaining_hour, remaining_minute, minute);
        String format = "";

        if (day == 1) {
            format += day + " day ";
        } else if (day > 1) {
            format += day + " days ";
        }
        if (remaining_hour == 1) {
            format += remaining_hour + " hour ";
        } else if (hour > 1) {
            format += remaining_hour + " hours ";
        }
        if (remaining_minute > 1) {
            format += remaining_minute + " minutes";
        } else {
            format += remaining_minute + " minute";
        }
        return format + " ago";


    }

    public abstract List<Article> getArticle(String url, int qty);

}
