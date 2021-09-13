/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Ho Le Minh Thach s3877980
        Last modified date: 10/09/2021
        Acknowledgement:
        https://www.baeldung.com/java-observer-pattern help me to apply the observable pattern into MVC
        Thank you, Professor Nick Wergeles for explaining the concept of:
            - JavaThread Service and Task
            - MVC model
        https://youtube.com/playlist?list=PLpvL1C_oZsr-BMBvdtgipMCDZK14BNigd
 */
package primary_page.controller;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {

    private int currentCategory;

    @FXML
    private Button newButton, covidButton, politicsButton, businessButton, technologyButton, healthButton, sportsButton, entertainmentButton, worldButton, othersButton;

    @FXML
    private VBox categoryBox;

    private PropertyChangeSupport propertyChangeSupport;

    private SidebarController sidebarController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        propertyChangeSupport = new PropertyChangeSupport(this);

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

    private void setCurrentButton() {
        currentCategory = 0;
        selectedButton();
    }

    @FXML
    private void setCurrentCategory(ActionEvent event) {
        cleanSelectedButton();
        int oldCategory = currentCategory;
        cleanSelectedButton();
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
        selectedButton();
        doNotify(oldCategory);
    }

    private void cleanSelectedButton() {
        String style = null;
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

    private void selectedButton() {
        String style = "-fx-border-color: transparent transparent transparent #000000";
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

    private void doNotify(int oldCategory) {
        propertyChangeSupport.firePropertyChange("currentCategory", oldCategory, currentCategory);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private ChangeListener<Boolean> toggleHover(int n) {
        return (observableValue, oldValue, newValue) -> {
            if (newValue) {
                sidebarController.setButtonEffect(n);
            } else {
                sidebarController.cleanEffect(n);
            }
        };
    }

    void injectController(PrimaryController primaryController) {
        sidebarController = primaryController.getSidebarController();
    }
}
