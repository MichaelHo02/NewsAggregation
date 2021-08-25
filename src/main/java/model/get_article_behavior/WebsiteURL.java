package model.get_article_behavior;

public enum WebsiteURL {
    VNEXPRESS("https://vnexpress.net/"),
    ZINGNEWS("https://zingnews.vn/"),
    TUOITRE("https://tuoitre.vn/"),
    THANHNIEN("https://thanhnien.vn/"),
    NHANDAN("https://nhandan.vn/"),

    RSS_NEW("rss/tin-moi-nhat.rss"),
    RSS_BUSINESS("rss/kinh-doanh.rss"),
    RSS_HEALTH("rss/suc-khoe.rss"),
    RSS_ENTERTAINMENT("rss/giai-tri.rss"),
    RSS_WORLD("rss/the-gioi.rss"),

    VNEXPRESS_COVID("abc"),
    VNEXPRESS_POLITICS("thoi-su/chinh-tri"),
    VNEXPRESS_TECHNOLOGY("rss/khoa-hoc.rss"),
    VNEXPRESS_SPORTS("rss/the-thao.rss"),
    VNEXPRESS_BUSINESS("kinh-doanh"),
    VNEXPRESS_OTHERS_1("phap-luat"),
    VNEXPRESS_OTHERS_2("giao-duc"),
    VNEXPRESS_OTHERS_3("doi-song"),


    ZINGNEWS_NEW(""),
    ZINGNEWS_COVID(""),
    ZINGNEWS_POLITICS("chinh-tri.html"),
    ZINGNEWS_BUSINESS("kinh-doanh-tai-chinh.html"),
    ZINGNEWS_TECHNOLOGY("cong-nghe.html"),
    ZINGNEWS_HEALTH("suc-khoe.html"),
    ZINGNEWS_SPORTS("the-thao.html"),
    ZINGNEWS_ENTERTAINMENT("giai-tri.html"),
    ZINGNEWS_WORLD("the-gioi.html"),
    ZINGNEWS_OTHERS_1("tin-tuc-xuat-ban.html"),
    ZINGNEWS_OTHERS_2("doi-song.html"),
    ZINGNEWS_OTHERS_3("thoi-su.html"),

    TUOITRE_COVID(""),
    TUOITRE_POLITICS(""),
    TUOITRE_TECHNOLOGY("rss/khoa-hoc.rss"),
    TUOITRE_SPORTS("rss/the-thao.rss"),
    TUOITRE_OTHERS(""),


    THANHNIEN_COVID(""),
    THANHNIEN_POLITICS("rss/thoi-su/chinh-tri.rss"),
    THANHNIEN_TECHNOLOGY("rss/cong-nghe.rss"),
    THANHNIEN_SPORTS("the-thao/rss/home.rss"),
    THANHNIEN_BUSINESS("rss/tai-chinh-kinh-doanh.rss"),
    THANHNIEN_OTHERS_1("game/rss/home.rss"),
    THANHNIEN_OTHERS_2("xe/rss/home.rss"),
    THANHNIEN_OTHERS_3("rss/du-lich.rss"),

    NHANDAN_POLITICS("chinhtri"),
    NHANDAN_TECHNOLOGY("khoahoc-congnghe"),
    NHANDAN_SPORTS("thethao"),
    NHANDAN_BUSINESS("chuyen-lam-an"),
    NHANDAN_OTHERS_1("vanhoa"),
    NHANDAN_OTHERS_2("phapluat"),
    NHANDAN_OTHERS_3("giaoduc")
    ;


    private final String url;

    WebsiteURL(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
