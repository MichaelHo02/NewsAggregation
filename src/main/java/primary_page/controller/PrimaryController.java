package primary_page.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import model.ArticleDatabase;
import primary_page.view_article_page.ArticlePageView;

import java.net.URL;
import java.util.*;

public class PrimaryController implements Initializable {
    @FXML
    List<ArticlePageView> pageList;

    @FXML
    private NavigationController navigationController;

    @FXML
    private CategoryController categoryController;

    @FXML
    private SidebarController sidebarController;

    @FXML
    private BorderPane borderPane;

    ArticleDatabase articleDatabase;

    ArticlePageView articlePageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navigationController.injectMainController(this);
        categoryController.injectMainController(this);
        sidebarController.injectMainController(this);
        articleDatabase = new ArticleDatabase();
        articleDatabase.performGetArticle();

        pageList = new ArrayList<>(5);
        System.out.println("a");
        for (int i = 0; i < 5; i++) {
            articlePageView = new ArticlePageView(i);
            pageList.add(articlePageView);
        }
        borderPane.setCenter(pageList.get(0));
        inputArticle();
    }


    private void inputArticle() {
        // TODO: do not have enough article bug
        int currentPage = navigationController.getCurrentPage();
        List<FXMLLoader> fxmlLoaderList = pageList.get(currentPage).getFxmlLoadersList();
        for (int i = currentPage * 10; i < currentPage * 10 + 10; i++) {
            CardController cardController = fxmlLoaderList.get(i % 10).getController();
            System.out.println(articleDatabase.getArticles().get(i));
            cardController.setData(articleDatabase.getArticles().get(i));
        }
    }

    void setView() {
        borderPane.setCenter(pageList.get(navigationController.getCurrentPage()));
        inputArticle();
    }

    void setSidebarOut() {
        sidebarController.toggleExtendedSidebarByIcon();
    }

    void setSidebarIn() {
        sidebarController.toggleExtendedSidebarByButton();
    }
}
