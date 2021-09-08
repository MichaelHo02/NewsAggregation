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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import model.database.ArticleDatabase;
import model.get_article_behavior.Article;
import model.scrapping_engine.BackgroundScraper;
import model.scrapping_engine.ConnectionTest;
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

    @FXML
    private Label statusLabel;

    @FXML
    private Circle connectionCircle;

    ArticleDatabase articleDatabase;


    private int currentPage;

    private int currentCategory;

    private boolean[] haveClick = new boolean[5];

    private Thread backgroundEngine;

    private BackgroundScraper backgroundScraper;

    private ConnectionTest connectionTest;

    private PropertyChangeSupport propertyChangeSupport;

    Service<Integer> service;

    private void resetHaveClick() {
        Arrays.fill(haveClick, false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectionCircle.setFill(Color.GREEN);
        propertyChangeSupport = new PropertyChangeSupport(this);

        connectionTest = new ConnectionTest();
        connectionTest.addPropertyChangeListener(this);
        Thread backgroundConnectionTest = new Thread(connectionTest);
        backgroundConnectionTest.setDaemon(true);
        backgroundConnectionTest.start();

        articleDatabase = new ArticleDatabase();
        articleDatabase.addPropertyChangeListener(this);
        Thread databaseThread = new Thread(() -> articleDatabase.performGetArticle());
        databaseThread.setDaemon(true);
        databaseThread.start();

        backgroundScraper = new BackgroundScraper();
        backgroundScraper.addPropertyChangeListener(this);
        backgroundEngine = new Thread(backgroundScraper);
        backgroundEngine.setDaemon(true);
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
        categoryController.addPropertyChangeListener(this);
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

    SidebarController getSidebarController() {
        return sidebarController;
    }

    public void ready(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(event -> {
            System.out.println("Stage will close");
            connectionTest.end();
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
            resetHaveClick();
            propertyChangeSupport.firePropertyChange("currentPage", null, currentPage);
            inputArticle();
        }
        if (evt.getPropertyName().equals("Bad internet connection")) {
            System.out.println("Get here");
            if ((boolean) evt.getNewValue()) {
                System.out.println("Bad internet connection");
                Platform.runLater(() -> connectionCircle.setFill(Color.RED));
            } else if (connectionCircle.getFill().equals(Color.RED) && !((boolean) evt.getNewValue())) {
                Platform.runLater(() -> connectionCircle.setFill(Color.GREEN));
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
