package primary_page.controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    private PrimaryController primaryController;

    private int currentCategory;

    @FXML
    Button menuIcon;

    @FXML
    private Button newButton, covidButton, politicsButton, businessButton, technologyButton, healthButton, sportsButton, entertainmentButton, worldButton, othersButton;

    @FXML
    private VBox categoryBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuIcon.hoverProperty().addListener(toggleHover(-1));
        newButton.hoverProperty().addListener(toggleHover(0));
        covidButton.hoverProperty().addListener(toggleHover(1));
        politicsButton.hoverProperty().addListener(toggleHover(2));
        businessButton.hoverProperty().addListener(toggleHover(3));
        technologyButton.hoverProperty().addListener(toggleHover(4));
        healthButton.hoverProperty().addListener(toggleHover(5));
        sportsButton.hoverProperty().addListener(toggleHover(6));
        entertainmentButton.hoverProperty().addListener(toggleHover(7));
        worldButton.hoverProperty().addListener(toggleHover(8));
        othersButton.hoverProperty().addListener(toggleHover(9));
        setCurrentButton();
    }

    void injectMainController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    private void cleanEffect() {
        String style = "-fx-background-color:  rgba(0,0,0,0)";
        switch (currentCategory) {
            case 0:
                newButton.setStyle(style);
                break;
            case 1:
                covidButton.setStyle(style);
                break;
            case 2:
                politicsButton.setStyle(style);
                break;
            case 3:
                businessButton.setStyle(style);
                break;
            case 4:
                technologyButton.setStyle(style);
                break;
            case 5:
                healthButton.setStyle(style);
                break;
            case 6:
                sportsButton.setStyle(style);
                break;
            case 7:
                entertainmentButton.setStyle(style);
                break;
            case 8:
                worldButton.setStyle(style);
                break;
            case 9:
                othersButton.setStyle(style);
                break;
        }
    }

    void cleanEffect(int n) {
        String style = "-fx-background-color:  rgba(0,0,0,0)";
        switch (n) {
            case -1:
                menuIcon.setStyle(style);
                break;
            case 0:
                newButton.setStyle(style);
                break;
            case 1:
                covidButton.setStyle(style);
                break;
            case 2:
                politicsButton.setStyle(style);
                break;
            case 3:
                businessButton.setStyle(style);
                break;
            case 4:
                technologyButton.setStyle(style);
                break;
            case 5:
                healthButton.setStyle(style);
                break;
            case 6:
                sportsButton.setStyle(style);
                break;
            case 7:
                entertainmentButton.setStyle(style);
                break;
            case 8:
                worldButton.setStyle(style);
                break;
            case 9:
                othersButton.setStyle(style);
                break;
        }
    }

    private void setButtonEffect() {
        String style = "-fx-background-color: #EFEFEF";
        switch (currentCategory) {
            case 0:
                newButton.setStyle(style);
                break;
            case 1:
                covidButton.setStyle(style);
                break;
            case 2:
                politicsButton.setStyle(style);
                break;
            case 3:
                businessButton.setStyle(style);
                break;
            case 4:
                technologyButton.setStyle(style);
                break;
            case 5:
                healthButton.setStyle(style);
                break;
            case 6:
                sportsButton.setStyle(style);
                break;
            case 7:
                entertainmentButton.setStyle(style);
                break;
            case 8:
                worldButton.setStyle(style);
                break;
            case 9:
                othersButton.setStyle(style);
                break;
        }
    }

    void setButtonEffect(int n) {
        String style = "-fx-background-color: #EFEFEF";
        switch (n) {
            case -1:
                menuIcon.setStyle(style);
                break;
            case 0:
                newButton.setStyle(style);
                break;
            case 1:
                covidButton.setStyle(style);
                break;
            case 2:
                politicsButton.setStyle(style);
                break;
            case 3:
                businessButton.setStyle(style);
                break;
            case 4:
                technologyButton.setStyle(style);
                break;
            case 5:
                healthButton.setStyle(style);
                break;
            case 6:
                sportsButton.setStyle(style);
                break;
            case 7:
                entertainmentButton.setStyle(style);
                break;
            case 8:
                worldButton.setStyle(style);
                break;
            case 9:
                othersButton.setStyle(style);
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
        int oldCategory = currentCategory;
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
        if (oldCategory != currentCategory) {
            primaryController.setCurrentCategory(currentCategory);
        }
        // TODO: send the message to the model
        setButtonEffect();
    }

    private ChangeListener<Boolean> toggleHover(int n) {
        return (observableValue, oldValue, newValue) -> {
            if (newValue && !oldValue && !primaryController.updateSideBar()) {
                primaryController.setSidebarOut();
            }

            SidebarController sidebarController = primaryController.getSidebarController();
            if (newValue) {
                cleanEffect();
                setButtonEffect(n);
                sidebarController.setButtonEffect(n);
            } else {
                cleanEffect(n);
                sidebarController.cleanEffect(n);
                setButtonEffect();
            }
        };
    }

    @FXML
    private void clickMenu() {
        if (primaryController.updateSideBar()) {
            primaryController.setSidebarIn();
        } else {
            primaryController.setSidebarOut();
        }
    }
}
