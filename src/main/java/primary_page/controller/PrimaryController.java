package primary_page.controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
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
    private ProgressBar progressBar;

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
        service = new Service<>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<>() {
                    @Override
                    protected Integer call() {
                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.2), progressBar);
                        if (!haveClick[currentPage]) {
                            Platform.runLater(() -> {
                                fadeTransition.setToValue(1);
                                fadeTransition.play();
                            });
                            for (int i = currentPage * 10; i < currentPage * 10 + 10; i++) {
                                CardController cardController = pageList.get(currentPage).fxmlLoadersList.get(i % 10).getController();
                                cardController.setData(articleDatabase.getArticles().get(i));
                                updateProgress((i % 10) + 1, 10);
                                System.out.println(progressBar.getProgress());
                                System.out.println(i % 10 + 1);
                            }
                            haveClick[currentPage] = true;
                            Platform.runLater(() -> {
                                fadeTransition.setToValue(0);
                                fadeTransition.play();
                            });
                        }
                        return 1;
                    }
                };
            }
        };
        progressBar.progressProperty().bind(service.progressProperty());
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
        service.restart();
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
