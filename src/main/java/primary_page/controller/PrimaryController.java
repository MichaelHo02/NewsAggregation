package primary_page.controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import model.database.ArticleDatabase;
import model.scrapping_engine.BackgroundScraper;
import primary_page.view_article_page.ArticlePageView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.*;

public class PrimaryController implements Initializable, PropertyChangeListener {
    private List<ArticlePageView> pageList;

    private Stage stage;

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


    private int currentPage;

    private int currentCategory;

    private boolean[] haveClick = new boolean[5];

    private void resetHaveClick() {
        Arrays.fill(haveClick, false);
    }

    private BackgroundScraper backgroundScraper;

    Service<Integer> service;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        articleDatabase = new ArticleDatabase();
        articleDatabase.addPropertyChangeListener(this);

        Thread thread = new Thread(() -> articleDatabase.performGetArticle());
        thread.start();

        backgroundScraper = new BackgroundScraper();
        backgroundScraper.addPropertyChangeListener(this);
        Thread backgroundEngine = new Thread(backgroundScraper);
        backgroundEngine.start();

        service = new Service<>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<>() {
                    @Override
                    protected Integer call() {
                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.2), progressBar);
                        System.out.println("This is the update: " + haveClick[currentCategory]);
                        if (!haveClick[currentPage]) {
                            Platform.runLater(() -> {
                                fadeTransition.setToValue(1);
                                fadeTransition.play();
                            });
                            for (int i = currentPage * 10; i < currentPage * 10 + 10; i++) {
                                CardController cardController = pageList.get(currentPage).fxmlLoadersList.get(i % 10).getController();
                                cardController.setData(articleDatabase.getArticles().get(i));
                                updateProgress((i % 10) + 1, 10);
//                                System.out.println(progressBar.getProgress());
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
        resetHaveClick();
        pageList = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            ArticlePageView articlePageView = new ArticlePageView(i);
            pageList.add(articlePageView);
        }
    }

    void inputArticle() {
        currentPage = navigationController.getCurrentPage();
        borderPane.setCenter(pageList.get(currentPage));
        service.restart();
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


    public void ready(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(event -> {
            System.out.println("Stage will close");
            backgroundScraper.end();
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("isScrapeDone") && (boolean) evt.getNewValue()) {
            Platform.runLater(this::inputArticle);
        }
        if (evt.getPropertyName().equals("updateScrapeDone") && (boolean) evt.getNewValue()) {
            resetHaveClick();
            Platform.runLater(this::inputArticle);
        }
    }
}
