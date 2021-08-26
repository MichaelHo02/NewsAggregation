package primary_page.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import model.ArticleDatabase;
import primary_page.view_article_page.ArticlePageView;

import java.net.URL;
import java.util.*;

public class PrimaryController implements Initializable {
    private List<ArticlePageView> pageList;

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

    private int currentPage;

    private int currentCategory;

    private boolean[] haveClick = new boolean[5];

    private void resetHaveClick() {
        Arrays.fill(haveClick, false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navigationController.injectMainController(this);
        categoryController.injectMainController(this);
        sidebarController.injectMainController(this);
        articleDatabase = new ArticleDatabase();
        articleDatabase.performGetArticle();

        resetHaveClick();
        pageList = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            articlePageView = new ArticlePageView(i);
            pageList.add(articlePageView);
        }
        borderPane.setCenter(pageList.get(0));
        inputArticle();
    }


    private void inputArticle() {
        // TODO: do not have enough article bug
        if (!haveClick[currentPage]) {
            for (int i = currentPage * 10; i < currentPage * 10 + 10; i++) {
                CardController cardController = pageList.get(currentPage).fxmlLoadersList.get(i % 10).getController();
                cardController.setData(articleDatabase.getArticles().get(i));
            }
            haveClick[currentPage] = true;
        }
        borderPane.setCenter(pageList.get(currentPage));
    }

    void setView() {
        currentPage = navigationController.getCurrentPage();
        inputArticle();
    }

    public void setCurrentCategory(int currentCategory) {
        this.currentCategory = currentCategory;
        currentPage = 0;
        navigationController.setCurrentButton();
        resetHaveClick();
        inputArticle();
    }

    void setSidebarOut() {
        sidebarController.toggleExtendedSidebarByIcon();
    }

    void setSidebarIn() {
        sidebarController.toggleExtendedSidebarByButton();
    }
}
