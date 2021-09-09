/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author:
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat s3878174
        Acknowledgement:
        https://www.w3schools.com/java/java_oop.asp
 */
package model.get_article_behavior;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Article {

    private String titlePage;
    private String linkPage;
    private Date date;
    private String imageURL;
    private WebsiteURL source;
    private String category; // Raw category data
    private List<Integer> categories;

    // Constructor
    public Article() {
    }

    public Article(String titlePage, String linkPage, Date date, String imageURL, WebsiteURL source, String category) {
        this.titlePage = titlePage;
        this.linkPage = linkPage;
        this.date = date;
        this.imageURL = imageURL;
        this.source = source;
        this.category = category;
        categories = new ArrayList<>();
    }

    public void addCategory(int category) { categories.add(category); }

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
