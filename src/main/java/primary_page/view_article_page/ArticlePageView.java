package primary_page.view_article_page;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticlePageView extends ScrollPane {
    private final List<FXMLLoader> fxmlLoadersList;

    private final int page;

    public ArticlePageView(int page) {
        fxmlLoadersList = new ArrayList<>(10);
        this.page = page;
        createPage();
    }

    private void createPage() {
        GridPane gridPane = new GridPane();
        int change = 0;
        if (page == 0) {
            HBox hBox = new HBox();
            VBox vBox = new VBox();
            VBox subSection = new VBox();
            subSection.setSpacing(10);
            hBox.getChildren().add(createSpecialCard(0));
            for (int i = 0; i < 3; i++) {
                subSection.getChildren().add(createSpecialCard(1));
            }
            hBox.getChildren().add(subSection);
            vBox.getChildren().add(hBox);
            vBox.getChildren().add(gridPane);
            vBox.setAlignment(Pos.TOP_CENTER);
            setContent(vBox);
            change = 4;
        } else {
            setContent(gridPane);
        }
        int column = 0;
        int row = 1;
        try {
            for (int i = change; i < 10; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("NormalCardView.fxml"));
                fxmlLoadersList.add(fxmlLoader);
                AnchorPane anchorPane = fxmlLoader.load();
                if (column == 2) {
                    column = 0;
                    row++;
                }
                gridPane.add(anchorPane, column++, row);
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
