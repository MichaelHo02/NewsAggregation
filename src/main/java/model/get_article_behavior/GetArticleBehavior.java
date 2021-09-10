/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author:
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat_s3878174
        Acknowledgement:
https://stackoverflow.com/questions/3809401/what-is-a-good-regular-expression-to-match-a-url
https://jsoup.org/cookbook/extracting-data/dom-navigation
 */
package model.get_article_behavior;


import model.database.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public abstract class GetArticleBehavior {

    protected static WebsiteURL getSource(String source) {
        if (source.contains("VnExpress")) {
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
    protected static String scrapeCategory(String url, int token) {
        String regex = "/";
        Pattern pattern = Pattern.compile(regex);
        String[] result = pattern.split(url);
        return result[token].equals("rss") ? "" : result[token];
    }
    // Tools for getArticle
    protected static String getImage(String description) {
        //Create a storage document
        Document document = Jsoup.parse(description);
        Elements images = document.select("img");
        // Cleanup image url
        String urlForm = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
        for (Element image : images) {
            if (image.hasAttr("src") && !image.attr("src").isBlank() && image.attr("src").matches(urlForm)) {
                return image.attr("src");
            } else if(image.hasAttr("data-src")) {
                return image.attr("data-src");
            }
        }
        return null;
    }

    public static String getFriendlyDate(Date date) {

        // Turn normal date format to friendly date

        Date now = new Date();
        Duration duration = Duration.between(date.toInstant(), now.toInstant());
        if (duration == null) return null;

        long minute = duration.toMinutes();
        long hour = minute / 60;
        long day = hour / 24;
        long remaining_hour = hour % 24;
        long remaining_minute = minute % 60;
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

    public abstract void scrapeArticle(ArrayList<Article> articles);

}
