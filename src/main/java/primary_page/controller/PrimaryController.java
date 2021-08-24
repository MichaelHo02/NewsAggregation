package primary_page.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import primary_page.view_article_page.ArticlePageView;

import java.net.URL;
import java.util.*;

public class PrimaryController implements Initializable {
    @FXML
    List<ScrollPane> pageList;

    @FXML
    private NavigationController navigationController;

    @FXML
    private CategoryController categoryController;

    @FXML
    private SidebarController sidebarController;

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navigationController.injectMainController(this);
        categoryController.injectMainController(this);
        sidebarController.injectMainController(this);

        pageList = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            ScrollPane scrollPane = new ArticlePageView(i);
            pageList.add(scrollPane);
        }
        borderPane.setCenter(pageList.get(0));
    }

    public void setView() {
        borderPane.setCenter(pageList.get(navigationController.getCurrentPage()));
    }

    public void setSidebarOut() {
        sidebarController.toggleExtendedSidebarByIcon();
    }

    public void setSidebarIn() {
        sidebarController.toggleExtendedSidebarByButton();
    }
}
