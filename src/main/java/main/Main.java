package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.get_article_behavior.Article;
import secondary_page.controller.SecondaryController;

public class Main extends Application {

    private static Scene scene;
    private static FXMLLoader primaryFxmlLoader;
    private static FXMLLoader secondaryFxmlLoader;

    @Override
    public void start(Stage stage) throws Exception {
        primaryFxmlLoader = new FXMLLoader(Main.class.getResource("/PrimaryView.fxml"));
        secondaryFxmlLoader = new FXMLLoader(Main.class.getResource("/SecondaryView.fxml"));
        secondaryFxmlLoader.load();
        scene = new Scene(primaryFxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }


    public static void setRoot(Article article) {
        SecondaryController secondaryController = secondaryFxmlLoader.getController();
        secondaryController.setArticle(article);
        secondaryController.setupView();
        scene.setRoot(secondaryFxmlLoader.getRoot());
    }

    public static void setRoot() {
        scene.setRoot(primaryFxmlLoader.getRoot());
    }

    public static void main(String[] args) {
        launch();
    }
}