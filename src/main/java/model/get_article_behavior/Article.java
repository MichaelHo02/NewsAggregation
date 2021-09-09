/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author:
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat s3878174, Nguyen Dich Long s3879052
        Acknowledgement:
        https://www.w3schools.com/java/java_oop.asp
 */
package model.get_article_behavior;

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

    // check if the article belongs to any category by adding up the indices
    public boolean catIsEmpty() {
        int sum = 0;
        for(int i : categories) {
            sum += i;
        }
        return sum == 0;
    }
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
}
