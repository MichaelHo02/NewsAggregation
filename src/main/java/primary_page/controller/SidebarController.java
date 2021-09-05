package primary_page.controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SidebarController implements Initializable {
    private PrimaryController primaryController;

    @FXML
    private VBox sidebar;

    @FXML
    private Button menuBar, newBar, covidBar, politicsBar, businessBar, technologyBar, healthBar, sportsBar, entertainmentBar, worldBar, othersBar;

    void injectMainController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sidebar.setVisible(false);
        sidebar.hoverProperty().addListener(toggleHover(10));
        menuBar.hoverProperty().addListener(toggleHover(-1));
        newBar.hoverProperty().addListener(toggleHover(0));
        covidBar.hoverProperty().addListener(toggleHover(1));
        politicsBar.hoverProperty().addListener(toggleHover(2));
        businessBar.hoverProperty().addListener(toggleHover(3));
        technologyBar.hoverProperty().addListener(toggleHover(4));
        healthBar.hoverProperty().addListener(toggleHover(5));
        sportsBar.hoverProperty().addListener(toggleHover(6));
        entertainmentBar.hoverProperty().addListener(toggleHover(7));
        worldBar.hoverProperty().addListener(toggleHover(8));
        othersBar.hoverProperty().addListener(toggleHover(9));
    }

    @FXML
    void toggleExtendedSidebarIn() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), sidebar);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), sidebar);
        translateTransition.play();
        translateTransition.setOnFinished(actionEvent -> {
            sidebar.setVisible(false);
        });
    }

    void toggleExtendedSidebarOut() {
        sidebar.setVisible(true);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), sidebar);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), sidebar);
        translateTransition.play();
    }

    public VBox getSidebar() {
        return sidebar;
    }

    public void cleanEffect(int n) {
        String style = "-fx-background-color:  rgba(0,0,0,0)";
        switch (n) {
            case -1:
                menuBar.setStyle(style);
                break;
            case 0:
                newBar.setStyle(style);
                break;
            case 1:
                covidBar.setStyle(style);
                break;
            case 2:
                politicsBar.setStyle(style);
                break;
            case 3:
                businessBar.setStyle(style);
                break;
            case 4:
                technologyBar.setStyle(style);
                break;
            case 5:
                healthBar.setStyle(style);
                break;
            case 6:
                sportsBar.setStyle(style);
                break;
            case 7:
                entertainmentBar.setStyle(style);
                break;
            case 8:
                worldBar.setStyle(style);
                break;
            case 9:
                othersBar.setStyle(style);
                break;
        }
    }

    public void setButtonEffect(int n) {
        String style = "-fx-background-color: #EFEFEF";
        switch (n) {
            case -1:
                menuBar.setStyle(style);
                break;
            case 0:
                newBar.setStyle(style);
                break;
            case 1:
                covidBar.setStyle(style);
                break;
            case 2:
                politicsBar.setStyle(style);
                break;
            case 3:
                businessBar.setStyle(style);
                break;
            case 4:
                technologyBar.setStyle(style);
                break;
            case 5:
                healthBar.setStyle(style);
                break;
            case 6:
                sportsBar.setStyle(style);
                break;
            case 7:
                entertainmentBar.setStyle(style);
                break;
            case 8:
                worldBar.setStyle(style);
                break;
            case 9:
                othersBar.setStyle(style);
                break;
        }
    }

    private ChangeListener<Boolean> toggleHover(int n) {
        return (observableValue, oldValue, newValue) -> {
            if (n == 10 && !newValue && oldValue && sidebar.isVisible() && primaryController.updateSideBar()) {
                toggleExtendedSidebarIn();
            }

            CategoryController categoryController = primaryController.getCategoryController();
            if (newValue) {
                setButtonEffect(n);
                categoryController.setButtonEffect(n);
            } else {
                cleanEffect(n);
                categoryController.cleanEffect(n);
            }
        };
    }

}
