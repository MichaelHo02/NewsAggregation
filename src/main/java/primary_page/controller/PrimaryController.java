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
import model.get_article_behavior.Article;
import model.scrapping_engine.BackgroundScraper;
import primary_page.view_article_page.ArticlePageView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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

    private BackgroundScraper backgroundScraper;

    private PropertyChangeSupport propertyChangeSupport;

    Service<Integer> service;

    private void resetHaveClick() {
        Arrays.fill(haveClick, false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        propertyChangeSupport = new PropertyChangeSupport(this);

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
                        if (!haveClick[currentPage]) {
                            Platform.runLater(() -> {
                                fadeTransition.setToValue(1);
                                fadeTransition.play();
                            });


                            if (currentCategory == 0) {
                                for (int i = currentPage * 10; i < currentPage * 10 + 10; i++) {
                                    CardController cardController = pageList.get(currentPage).fxmlLoadersList.get(i % 10).getController();
                                    cardController.setData(articleDatabase.getArticles().get(i));
                                    updateProgress((i % 10) + 1, 10);
                                }
                            } else {
                                for (int i = 0; i < 10; i++) {
                                    CardController cardController = pageList.get(currentPage).fxmlLoadersList.get(i).getController();
                                    for (Article article : articleDatabase.getArticles()) {
                                        if (article.getCategories().contains(currentCategory)) {
                                            cardController.setData(article);
                                        }
                                        updateProgress(i + 1, 10);
                                    }
                                }
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
        navigationController.addPropertyChangeListener(this);
        navigationController.injectMainController(this);
        categoryController.addPropertyChangeListener(this);
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
        Platform.runLater(() -> {
            borderPane.setCenter(pageList.get(currentPage));
            service.restart();
        });

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
            inputArticle();
        }
        if (evt.getPropertyName().equals("updateScrapeDone") && (boolean) evt.getNewValue()) {
            System.out.println("Category in Main");
            resetHaveClick();
            inputArticle();
        }
        if (evt.getPropertyName().equals("currentPage")) {
            currentPage = (int) evt.getNewValue();
            inputArticle();
        }
        if (evt.getPropertyName().equals("currentCategory")) {
            currentCategory = (int) evt.getNewValue();
            // Send currentpage back to navigation
            propertyChangeSupport.firePropertyChange("currentPage", null, currentPage);
            resetHaveClick();
            inputArticle();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
