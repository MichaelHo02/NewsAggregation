/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Ho Le Minh Thach s3877980
        Last modified date: 10/09/2021
        Acknowledgement:
        https://www.baeldung.com/java-observer-pattern help me to apply the observable pattern into MVC
        Thank you, Professor Nick Wergeles for explaining the concept of:
            - JavaThread Service and Task
            - MVC model
        https://youtube.com/playlist?list=PLpvL1C_oZsr-BMBvdtgipMCDZK14BNigd
 */

package primary_page.view_article_page;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticlePageView extends ScrollPane {
    public final List<FXMLLoader> FXML_LOADER_LIST;

    private final int PAGE;

    public ArticlePageView(int PAGE) {
        FXML_LOADER_LIST = new ArrayList<>(10);
        this.PAGE = PAGE;
        getStylesheets().add("style/style.css");
        getStyleClass().add("edge-to-edge");
        setFitToWidth(true);
        vbarPolicyProperty().setValue(ScrollBarPolicy.AS_NEEDED);
        createPage();
    }

    private void createPage() {
        StackPane stackPane = new StackPane();
        stackPane.getStylesheets().add("style/style.css");
        stackPane.getStyleClass().add("dark_background");
        stackPane.setPadding(new Insets(10, 10, 10, 10));
        FlowPane flowPane = new FlowPane();
        flowPane.setOrientation(Orientation.HORIZONTAL);
        flowPane.setAlignment(Pos.CENTER);
        int change = 0;
        if (PAGE == 0) {
            flowPane.getChildren().add(createSpecialCard(0));
            VBox subSection = new VBox();
            subSection.setAlignment(Pos.CENTER);
            subSection.setSpacing(10);
            for (int i = 0; i < 3; i++) {
                subSection.getChildren().add(createSpecialCard(1));
            }
            flowPane.getChildren().add(subSection);
            change = 4;
        }
        stackPane.getChildren().add(flowPane);
        setContent(stackPane);
        try {
            for (int i = change; i < 10; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("MainCardView.fxml"));
                FXML_LOADER_LIST.add(fxmlLoader);
                AnchorPane pane = fxmlLoader.load();
                flowPane.getChildren().add(pane);
            }
            flowPane.setHgap(10);
            flowPane.setVgap(10);
            flowPane.setPadding(new Insets(0, 10, 0, 10));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AnchorPane createSpecialCard(int i) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        String cardType;
        switch (i) {
            case 0:
                cardType = "MainCardView.fxml";
                break;
            case 1:
                cardType = "SubCardView.fxml";
                break;
            default:
                return null;
        }
        fxmlLoader.setLocation(getClass().getResource(cardType));
        FXML_LOADER_LIST.add(fxmlLoader);
        AnchorPane anchorPane = null;
        try {
            anchorPane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (i == 0) {
            AnchorPane articleMain = new AnchorPane();
            articleMain.getChildren().clear();
            articleMain.getChildren().add(anchorPane);
            return articleMain;
        } else {
            return anchorPane;
        }
    }
}
