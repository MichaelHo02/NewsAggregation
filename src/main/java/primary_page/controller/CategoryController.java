package primary_page.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    private PrimaryController primaryController;

    private int currentCategory;

    @FXML
    private Button newButton, covidButton, politicsButton, businessButton, technologyButton, healthButton, sportsButton, entertainmentButton, worldButton, othersButton;

    @FXML
    private VBox categoryBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryBox.hoverProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                primaryController.setSidebarOut();
            } else {
                primaryController.setSidebarIn();
            }
        });
        setCurrentButton();
    }

    void injectMainController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    private void cleanEffect() {
        switch (currentCategory) {
            case 0:
                newButton.setEffect(null);
                break;
            case 1:
                covidButton.setEffect(null);
                break;
            case 2:
                politicsButton.setEffect(null);
                break;
            case 3:
                businessButton.setEffect(null);
                break;
            case 4:
                technologyButton.setEffect(null);
                break;
            case 5:
                healthButton.setEffect(null);
                break;
            case 6:
                sportsButton.setEffect(null);
                break;
            case 7:
                entertainmentButton.setEffect(null);
                break;
            case 8:
                worldButton.setEffect(null);
                break;
            case 9:
                othersButton.setEffect(null);
                break;
        }
    }

    private void setButtonEffect() {
        Lighting lighting = new Lighting();
        switch (currentCategory) {
            case 0:
                newButton.setEffect(lighting);
                break;
            case 1:
                covidButton.setEffect(lighting);
                break;
            case 2:
                politicsButton.setEffect(lighting);
                break;
            case 3:
                businessButton.setEffect(lighting);
                break;
            case 4:
                technologyButton.setEffect(lighting);
                break;
            case 5:
                healthButton.setEffect(lighting);
                break;
            case 6:
                sportsButton.setEffect(lighting);
                break;
            case 7:
                entertainmentButton.setEffect(lighting);
                break;
            case 8:
                worldButton.setEffect(lighting);
                break;
            case 9:
                othersButton.setEffect(lighting);
                break;
        }
    }

    private void setCurrentButton() {
        currentCategory = 0;
        setButtonEffect();
    }

    @FXML
    private void setCurrentCategory(MouseEvent event) {
        cleanEffect();
        Object source = event.getSource();
        if (source == newButton) {
            currentCategory = 0;
        }
        if (source == covidButton) {
            currentCategory = 1;
        }
        if (source == politicsButton) {
            currentCategory = 2;
        }
        if (source == businessButton) {
            currentCategory = 3;
        }
        if (source == technologyButton) {
            currentCategory = 4;
        }
        if (source == healthButton) {
            currentCategory = 5;
        }
        if (source == sportsButton) {
            currentCategory = 6;
        }
        if (source == entertainmentButton) {
            currentCategory = 7;
        }
        if (source == worldButton) {
            currentCategory = 8;
        }
        if (source == othersButton) {
            currentCategory = 9;
        }
        // TODO: send the message to the model
        setButtonEffect();
    }
}
