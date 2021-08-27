package primary_page.view_article_page;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticlePageView extends ScrollPane {
    public final List<FXMLLoader> fxmlLoadersList;

    private final int page;

    public ArticlePageView(int page) {
        fxmlLoadersList = new ArrayList<>(10);
        this.page = page;
        getStylesheets().add("style/style.css");
        getStyleClass().add("dark_background");
        setFitToHeight(true);
        setFitToWidth(true);
        createPage();
    }

    private void createPage() {
        StackPane stackPane = new StackPane();
        stackPane.getStylesheets().add("style/style.css");
        stackPane.getStyleClass().add("dark_background");
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        int change = 0;
        if (page == 0) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.TOP_CENTER);
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.TOP_CENTER);
            VBox subSection = new VBox();
            subSection.setAlignment(Pos.TOP_CENTER);
            subSection.setPadding(new Insets(0, 0, 0, 10));
            subSection.setSpacing(10);
            hBox.getChildren().add(createSpecialCard(0));
            for (int i = 0; i < 3; i++) {
                subSection.getChildren().add(createSpecialCard(1));
            }
            hBox.getChildren().add(subSection);
            hBox.setPadding(new Insets(10, 10, 0, 10));
            vBox.getChildren().add(hBox);
            vBox.getChildren().add(gridPane);
            vBox.setAlignment(Pos.TOP_CENTER);
            stackPane.getChildren().add(vBox);
            change = 4;
        } else {
            stackPane.getChildren().add(gridPane);
        }
        setContent(stackPane);
        int column = 0;
        int row = 1;
        try {
            for (int i = change; i < 10; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("NormalCardView.fxml"));
                fxmlLoadersList.add(fxmlLoader);
                AnchorPane pane = fxmlLoader.load();
                if (column == 2) {
                    column = 0;
                    row++;
                }
                gridPane.add(pane, column++, row);
            }
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(0, 10, 0, 10));
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
        fxmlLoadersList.add(fxmlLoader);
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
