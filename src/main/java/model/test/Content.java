package model.test;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.get_article_behavior.Article;
import model.get_article_behavior.WebsiteURL;

import java.util.List;

public class Content {
    private String context;
    private String type;

    public Content(String context, String type) {
        this.context = context;
        this.type = type;
    }

    public String getContext() {
        return context;
    }

    public String getType() {
        return type;
    }

    //Extract the article in the content list
    public static List<Content> articleSwitcher(Article article) {
        WebsiteURL source = article.getSource();
        model.test.JsoupArticleDisplay disp;
        switch (source) {
            case VNEXPRESS:
                disp = new model.test.DisplayVNExpress();
                break;
            case TUOITRE:
                disp = new model.test.DisplayTuoiTre();
                break;
            case ZINGNEWS:
                disp = new DisplayZingNews();
                break;
            case NHANDAN:
                disp = new DisplayNhanDan();
                break;
            case THANHNIEN:
                disp = new DisplayThanhNien();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " +article.getSource() );
        }
        List<Content> contentList = disp.getContent(source.getUrl());
        return contentList;
    }

    //Add everythinf to vbox
    public static void dispArt(List<Content> contentList, VBox articleVbox) {
        for (Content cnt : contentList) {
            if (cnt.getType().equals("p")) {
                Text text = new Text(cnt.getContext());
                text.setWrappingWidth(800);
                text.setStyle("-fx-font: 24 Helvetica");
                articleVbox.getChildren().add(text);
            } else if (cnt.getType().equals("video")) {
                System.out.println("I don't know how to do dis, fuck");
            } else if (cnt.getType().equals("img")) {
                try {
                    ImageView imageView = new ImageView();
                    imageView.setImage(new Image(cnt.getContext()));
                    articleVbox.getChildren().add(imageView);
                } catch (Exception e) {
                    System.out.println("Failed to load img");
                }
            }
        }
    }
}
