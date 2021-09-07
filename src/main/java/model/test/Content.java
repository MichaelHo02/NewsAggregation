package model.test;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import model.get_article_behavior.Article;
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
        ScrollPane scroll = new ScrollPane();
        scroll.setPrefHeight(Region.USE_COMPUTED_SIZE);
        scroll.setPrefWidth(Region.USE_COMPUTED_SIZE);
        scroll.setFitToWidth(true);
        VBox articleVbox = new VBox();
        articleVbox.setSpacing(20);
        articleVbox.setPadding(new Insets(10, 10, 10, 10));
        for (Content cnt : contentList) {
            if (cnt.getType().equals("p")) {
                Text text = new Text(cnt.getContext());
                text.setStyle("-fx-font: 24 Helvetica");
                articleVbox.getChildren().add(text);
                text.wrappingWidthProperty().bind(scroll.widthProperty().subtract(40));
            } else if (cnt.getType().equals("img")) {
                try {
                    ImageView imageView = new ImageView();
                    imageView.setSmooth(true);
                    imageView.setPreserveRatio(true);
                    imageView.fitWidthProperty().bind(scroll.widthProperty().subtract(40));
                    imageView.setImage(new Image(cnt.getContext()));
                    articleVbox.getChildren().add(imageView);
                } catch (Exception e) {
                    System.out.println("Failed to load img");
                }
            }
        }
        scroll.setContent(articleVbox);
        return scroll;
    }
}
