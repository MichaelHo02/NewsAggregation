package model.article_extraction;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import model.get_article_behavior.Article;
import primary_page.controller.CardController;

import java.util.List;

public class ArticleFactory {
    private String context;
    private String type;

    public ArticleFactory(String context, String type) {
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
    public static List<ArticleFactory> articleSwitcher(Article article) {

        ArticleExtractor disp;
        switch (CardController.websiteSource) {
            case VNEXPRESS:
                disp = new VnExpressExtraction();
                break;
            case TUOITRE:
                disp = new TuoiTreExtraction();
                break;
            case ZINGNEWS:
                disp = new ZingNewsExtraction();
                break;
            case NHANDAN:
                disp = new NhanDanExtraction();
                break;
            case THANHNIEN:
                disp = new ThanhNienExtraction();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " +article.getSource() );
        }
//        System.out.println(source.getUrl());
        List<ArticleFactory> articleFactoryList = disp.getContent(CardController.websiteLink);
        return articleFactoryList;
    }

    //Add everythinf to vbox
    public static ScrollPane dispArt(List<ArticleFactory> articleFactoryList) {
        final double PAD = 100;
        System.out.println("Working");
        ScrollPane scroll = new ScrollPane();
        scroll.getStylesheets().add("style/style.css");
        scroll.getStyleClass().add("edge-to-edge");
        scroll.setPrefHeight(Region.USE_COMPUTED_SIZE);
        scroll.setPrefWidth(Region.USE_COMPUTED_SIZE);
        scroll.setFitToWidth(true);
        VBox articleVbox = new VBox();
        articleVbox.getStylesheets().add("style/style.css");
        articleVbox.getStyleClass().add("dark_background");
        articleVbox.setAlignment(Pos.CENTER);
        articleVbox.setSpacing(20);
        articleVbox.setPadding(new Insets(10, 10, 10, 10));
        for (ArticleFactory cnt : articleFactoryList) {
            if (cnt.getType().equals("p")) {
                Text text = new Text(cnt.getContext());
                text.setStyle("-fx-font-size: 20pt");
                articleVbox.getChildren().add(text);
                text.wrappingWidthProperty().bind(scroll.widthProperty().subtract(PAD));
            } else if (cnt.getType().equals("caption")) {
                Text text = new Text(cnt.getContext());
                text.setStyle("-fx-font-size: 18pt; -fx-font-style: italic;");
                text.wrappingWidthProperty().bind(scroll.widthProperty().subtract(PAD));
               StackPane stackPane = new StackPane();
               stackPane.getChildren().add(text);
               stackPane.setPadding(new Insets(10, 0, 10, 0));
               articleVbox.getChildren().add(stackPane);
            } else if (cnt.getType().equals("img")) {
                try {
                    ImageView imageView = new ImageView();
                    imageView.setSmooth(true);
                    imageView.setPreserveRatio(true);
                    imageView.fitWidthProperty().bind(scroll.widthProperty().subtract(PAD));
                    imageView.setImage(new Image(cnt.getContext()));
                    imageView.setStyle("-fx-padding: 10px");
                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().add(imageView);
                    stackPane.setPadding(new Insets(20, 0,10,0));
                    articleVbox.getChildren().add(stackPane);
                } catch (Exception e) {
                    System.out.println("Failed to load img");
                }
            }else if (cnt.getType().equals("author")) {
                Text text = new Text(cnt.getContext());
                text.setStyle("-fx-font-size: 17pt; -fx-font-weight: bold;");
                articleVbox.getChildren().add(text);
                text.wrappingWidthProperty().bind(scroll.widthProperty().subtract(PAD));
            } else if (cnt.getType().equals("h")) {
                Text text = new Text(cnt.getContext());
                text.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold;");
                articleVbox.getChildren().add(text);
                text.wrappingWidthProperty().bind(scroll.widthProperty().subtract(PAD));
            }
        }
        scroll.setContent(articleVbox);
        return scroll;
    }
}