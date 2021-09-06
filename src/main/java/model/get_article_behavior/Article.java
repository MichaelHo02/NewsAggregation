package model.get_article_behavior;


import java.util.ArrayList;
import java.util.Date;

public class Article {
    public Article() {
    }

    private String titlePage;
    private String linkPage;
    private Date date;
    private String imageURL;
    private WebsiteURL source;
    private String category; // raw category data
    private ArrayList<Integer> categories;

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

    public ArrayList<Integer> getCategories() {
        return categories;
    }
}
