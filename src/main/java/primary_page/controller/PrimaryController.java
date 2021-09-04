package primary_page.controller;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import model.database.ArticleDatabase;
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

    //Progress bar

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

    Service<Integer> service;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        if (!haveClick[currentPage]) {
                            for (int i = currentPage * 10; i < currentPage * 10 + 10; i++) {
                                CardController cardController = pageList.get(currentPage).fxmlLoadersList.get(i % 10).getController();
                                cardController.setData(articleDatabase.getArticles().get(i));
                            }
                            System.out.println("Cancel " + isCancelled());
                            haveClick[currentPage] = true;
                        }
                        return 1;
                    }
                };
            }
        };
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
        System.out.println("Check 1 " + service.stateProperty());
        service.restart();
        System.out.println("Check 2" + service.stateProperty());
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
        sidebarController.toggleExtendedSidebarOut();
    }

    void setSidebarIn() {
        sidebarController.toggleExtendedSidebarIn();
    }

    boolean updateSideBar() {
        return sidebarController.getSidebar().isVisible();
    }

    SidebarController getSidebarController() {
        return sidebarController;
    }

    public CategoryController getCategoryController() {
        return categoryController;
    }


}
