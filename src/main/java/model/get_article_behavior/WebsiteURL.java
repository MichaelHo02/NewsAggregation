package model.get_article_behavior;

public enum WebsiteURL {
    VNEXPRESS("https://vnpress.net/"),
    ZINGNEWS("https://zingnexews.vn/"),
    TUOITRE("https://tuoitre.vn/"),
    THANHNIEN("https://thanhnien.vn/"),
    NHANDAN("https://nhandan.vn/");

    private final String url;

    WebsiteURL(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
