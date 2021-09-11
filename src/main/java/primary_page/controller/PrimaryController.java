/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author:
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat s3878174
        Acknowledgement:
        h
 */
package primary_page.controller;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import model.database.Article;
import model.scrapping_engine.ConnectionTest;
import model.database.ArticleDatabase;
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

    private ArticleDatabase articleDatabase;

    private int currentPage;

    private int currentCategory;

    private final boolean[] HAVE_CLICK = new boolean[5];

    private ConnectionTest connectionTest;

    private PropertyChangeSupport propertyChangeSupport;

    private Service<Integer> service;

    private void resetHaveClick() {
        Arrays.fill(HAVE_CLICK, false);
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
        Thread scrapingThread = new Thread(articleDatabase);
        scrapingThread.setDaemon(true);
        scrapingThread.start();

        service = new Service<>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<>() {
                    @Override
                    protected Integer call() {
                        if (!HAVE_CLICK[currentPage]) {
                            Platform.runLater(() -> {
                                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.2), progressBar);
                                fadeTransition.setToValue(1);
                                fadeTransition.play();
                            });
                            int i = 0;
                            for (Article article : articleDatabase.getArticles()) {
                                if (i > (currentPage + 1) * 10 - 1) {
                                    break;
                                }
                                if (article.getCategories().contains(currentCategory)) {
                                    if (i >= currentPage * 10) {
                                        CardController cardController = pageList.get(currentPage).FXML_LOADER_LIST.get(i % 10).getController();
                                        cardController.setData(article);
                                        updateProgress((i % 10) + 1, 10);
                                    }
                                    i++;
                                }
                            }
                            HAVE_CLICK[currentPage] = true;
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
        navigationController.injectController(this);
        categoryController.addPropertyChangeListener(this);
        categoryController.injectController(this);
        resetHaveClick();

        pageList = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            ArticlePageView articlePageView = new ArticlePageView(i);
            pageList.add(articlePageView);
        }
    }

    private void inputArticle(int scrollValue) {
        Platform.runLater(() -> {
            ScrollPane scrollPane = pageList.get(currentPage);
            if (scrollValue == 0) {
                scrollPane.setVvalue(scrollValue);
            }
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
            service.cancel();
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("updateScrapeDone") && (boolean) evt.getNewValue()) {
            System.out.println("Update scrapping");
            resetHaveClick();
            inputArticle(1);
        }
        if (evt.getPropertyName().equals("currentPage")) {
            currentPage = (int) evt.getNewValue();
            inputArticle(0);
        }
        if (evt.getPropertyName().equals("currentCategory")) {
            currentCategory = (int) evt.getNewValue();
            currentPage = 0;
            resetHaveClick();
            propertyChangeSupport.firePropertyChange("currentPage", null, currentPage);
            inputArticle(currentPage);
        }
        if (evt.getPropertyName().equals("Bad internet connection")) {
            if ((boolean) evt.getNewValue()) {
                System.out.println("Bad internet connection");
                Platform.runLater(() -> connectionCircle.setFill(Color.RED));
            } else if (connectionCircle.getFill().equals(Color.RED) && !((boolean) evt.getNewValue())) {
                Platform.runLater(() -> connectionCircle.setFill(Color.GREEN));
                inputArticle(0);
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
