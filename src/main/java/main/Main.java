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
       https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/FXMLLoader.html
 */
package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.get_article_behavior.Article;
import primary_page.controller.PrimaryController;
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
        PrimaryController primaryController = primaryFxmlLoader.getController();
        primaryController.ready(stage);
        stage.setScene(scene);
        stage.setMinWidth(720);
        stage.setMinHeight(730);
        stage.setTitle("Remarkable News Aggregator");
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
