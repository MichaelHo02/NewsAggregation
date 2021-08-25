package model.get_article_behavior;


import java.util.Date;

public class Article {
    public Article() {
    }

    private String titlePage;
    private String linkPage;
    private Date date;
    private String imageURL;
    private WebsiteURL source;
    private String category;

    public Article(String titlePage, String linkPage, Date date, String imageURL, WebsiteURL source, String category) {
        this.titlePage = titlePage;
        this.linkPage = linkPage;
        this.date = date;
        this.imageURL = imageURL;
        this.source = source;
        this.category = category;
    }



    public String getTitlePage() {
        return titlePage;
    }

    public String getLinkPage() {
        return linkPage;
    }

//    public String getPublishDate() {
//        if (publishDate != null) {
//            return publishDate.toString();
//        }
//        return "";
//    }

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
}
