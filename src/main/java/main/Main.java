/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Ho Le Minh Thach s3877980
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat s3878174
        Acknowledgement:
        1. JavaFX FXML Loader
        https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/FXMLLoader.html
 */
package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.database.Article;
import primary_page.controller.PrimaryController;
import secondary_page.controller.SecondaryController;

import java.nio.file.FileSystems;

public class Main extends Application {

    private static Scene scene;
    private static FXMLLoader primaryFxmlLoader;
    private static FXMLLoader secondaryFxmlLoader;

    @Override
    public void start(Stage stage) throws Exception {
        String pathSeparator = FileSystems.getDefault().getSeparator();
        // Load and save the primaryFxmlLoader and secondaryFxmlLoader for further reusable
        String primaryView = "/PrimaryView.fxml".replaceAll("/", pathSeparator);
        String secondaryView = "/SecondaryView.fxml".replaceAll("/", pathSeparator);
        primaryFxmlLoader = new FXMLLoader(Main.class.getResource(primaryView));
        secondaryFxmlLoader = new FXMLLoader(Main.class.getResource(secondaryView));
        secondaryFxmlLoader.load();
        scene = new Scene(primaryFxmlLoader.load());
        // Get the stage into the primary controller to listen for app shutdown
        PrimaryController primaryController = primaryFxmlLoader.getController();
        primaryController.ready(stage);
        stage.setScene(scene);
        // Set the minimum width and height of the application
        stage.setMinWidth(720);
        stage.setMinHeight(730);
        stage.setTitle("L I T");
        stage.show();
    }


    // Change the root to secondary view
    public static void setRoot(Article article) {
        SecondaryController secondaryController = secondaryFxmlLoader.getController();
        secondaryController.setArticle(article);
        secondaryController.setupView();
        scene.setRoot(secondaryFxmlLoader.getRoot());
    }

    // Change the root to the primary view
    public static void setRoot() {
        scene.setRoot(primaryFxmlLoader.getRoot());
    }

    // Run main
    public static void main(String[] args) {
        launch();
    }
}
