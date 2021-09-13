/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Ho Le Minh Thach s3877980
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat s3878174, Nguyen Dich Long s3879052
        Acknowledgement:
        https://www.w3schools.com/java/java_oop.asp
 */
package model.database;


import util.get_article_behavior.WebsiteURL;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Article {
    // article attributes
    private String titlePage;
    private String linkPage;
    private Date date;
    private String imageURL;
    private WebsiteURL source;
    private String category; // Raw category data to filter
    private List<Integer> categories; // store category indices
    //    Category indices are as follow
    //    1 Covid
    //    2 Politics
    //    3 Business
    //    4 Technology
    //    5 Health
    //    6 Sport
    //    7 Entertainment
    //    8 World
    //    9 Others

    // Constructor
    public Article(String titlePage, String linkPage, Date date, String imageURL, WebsiteURL source, String category) {
        this.titlePage = titlePage;
        this.linkPage = linkPage;
        this.date = date;
        this.imageURL = imageURL;
        this.source = source;
        this.category = category;
        categories = new ArrayList<>();
    }

    // add a category to the category list
    public void addCategory(int category) { categories.add(category); }

    // getters
    public String getTitlePage() {
        return titlePage;
    }

    public String getLinkPage() {
        return linkPage;
    }

    public Date getDuration() {
        return date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public WebsiteURL getSource() {
        return source;
    }

    public String getCategory() {
        return category;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    // method to format date
    public static String getFriendlyDate(Date date) {
        // Turn normal date format to friendly date
        Date now = new Date();
        Duration duration = Duration.between(date.toInstant(), now.toInstant());
        if (duration == null) return null;

        // get time quantities in units of convention (day, hour, minute)
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
}
