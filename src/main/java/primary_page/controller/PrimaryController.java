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
import javafx.scene.control.ScrollPane;
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

        Thread databaseThread = new Thread(() -> articleDatabase.performGetArticle());
        databaseThread.start();

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
                        if (!haveClick[currentPage]) {
                            Platform.runLater(() -> {
                                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.2), progressBar);
                                fadeTransition.setToValue(1);
                                fadeTransition.play();
                            });
                            int i = 0;
                            for (Article article : articleDatabase.getArticles()) {
                                if (isCancelled()) {
                                    return 0;
                                }
                                if (i > (currentPage + 1) * 10 - 1) {
                                    break;
                                }
                                if (article.getCategories().contains(currentCategory)) {
                                    if (i >= currentPage * 10) {
                                        CardController cardController = pageList.get(currentPage).fxmlLoadersList.get(i % 10).getController();
                                        cardController.setData(article);
                                        updateProgress((i % 10) + 1, 10);
                                        System.out.println(progressBar.getProgress());
                                    }
                                    i++;
                                }
                            }
                            haveClick[currentPage] = true;
                            Platform.runLater(() -> {
                                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.2), progressBar);
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
            ScrollPane scrollPane = pageList.get(currentPage);
            scrollPane.setVvalue(0);
            borderPane.setCenter(scrollPane);
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
            articleDatabase.end();
            backgroundScraper.end();
            service.cancel();
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
            currentPage = 0;
            // Send currentpage back to navigation
            resetHaveClick();
            propertyChangeSupport.firePropertyChange("currentPage", null, currentPage);
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
