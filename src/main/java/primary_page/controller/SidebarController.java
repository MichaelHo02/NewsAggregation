package primary_page.controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SidebarController implements Initializable {
    @FXML
    private VBox sidebar;

    @FXML
    private Button menuBar, newBar, covidBar, politicsBar, businessBar, technologyBar, healthBar, sportsBar, entertainmentBar, worldBar, othersBar;

    private PrimaryController primaryController;

    public void injectMainController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sidebar.setVisible(false);
        newBar = new Button();
        covidBar = new Button();
        politicsBar = new Button();
        businessBar = new Button();
        technologyBar = new Button();
        healthBar = new Button();
        sportsBar = new Button();
        entertainmentBar = new Button();
        worldBar = new Button();
        othersBar = new Button();
    }

    public void toggleExtendedSidebarByButton() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), sidebar);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), sidebar);
        translateTransition.setFromX(66);
        translateTransition.play();
        translateTransition.setOnFinished(actionEvent -> {
            sidebar.setVisible(false);
        });
    }

    public void toggleExtendedSidebarByIcon() {
        sidebar.setVisible(true);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), sidebar);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), sidebar);
        translateTransition.setFromX(66);
        translateTransition.play();
    }
}
