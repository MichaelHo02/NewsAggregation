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
        backgroundConnectionTest.start();

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
                            int i = 0;
                            System.out.println("This is the size: " + articleDatabase.getArticles().size());
                            for (Article article : articleDatabase.getArticles()) {
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
            currentPage = 0;
            // Send currentpage back to navigation
            resetHaveClick();
            propertyChangeSupport.firePropertyChange("currentPage", null, currentPage);
            inputArticle();
        }
        if (evt.getPropertyName().equals("Bad internet connection")) {
            System.out.println("Bad internet connection");
            connectionAlert();
        }
    }

    public void connectionAlert() {
//        statusLabel.setText("Connection status: Disconnected");
        connectionCircle.setFill(Color.RED);
//        System.out.println("This is a text for changing connection status");
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
