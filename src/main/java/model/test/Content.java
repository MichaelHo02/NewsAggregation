package model.test;

import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.get_article_behavior.Article;
import model.get_article_behavior.WebsiteURL;
import primary_page.controller.CardController;

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

        model.test.JsoupArticleDisplay disp;
        switch (CardController.websiteSource) {
            case VNEXPRESS:
                disp = new model.test.DisplayVNExpress();
                break;
            case TUOITRE:
                disp = new model.test.DisplayTuoiTre();
                break;
            case ZINGNEWS:
                disp = new model.test.DisplayZingNews();
                break;
            case NHANDAN:
                disp = new model.test.DisplayNhanDan();
                break;
            case THANHNIEN:
                disp = new model.test.DisplayThanhNien();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " +article.getSource() );
        }
//        System.out.println(source.getUrl());
        List<Content> contentList = disp.getContent(CardController.websiteLink);
        return contentList;
    }

    //Add everythinf to vbox
    public static ScrollPane dispArt(List<Content> contentList) {
        System.out.println("Working");
        VBox articleVbox = new VBox();
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
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(articleVbox);

        return scroll;
    }
}
