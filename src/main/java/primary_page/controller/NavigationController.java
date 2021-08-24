package primary_page.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class NavigationController implements Initializable {
    private int currentPage;

    private PrimaryController primaryController;

    @FXML
    private Button page1, page2, page3, page4, page5, prevPage, nextPage;

    public void injectMainController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCurrentButton();
    }

    public int getCurrentPage() {
        return currentPage;
    }
    private void setCurrentButton() {
        currentPage = 0;
        setButtonEffect();
    }

    private void setButtonEffect() {
        Lighting lighting = new Lighting();
        switch (currentPage) {
            case 0:
                page1.setEffect(lighting);
                break;
            case 1:
                page2.setEffect(lighting);
                break;
            case 2:
                page3.setEffect(lighting);
                break;
            case 3:
                page4.setEffect(lighting);
                break;
            case 4:
                page5.setEffect(lighting);
                break;
        }
    }

    private void cleanEffect() {
        switch (currentPage) {
            case 0:
                page1.setEffect(null);
                break;
            case 1:
                page2.setEffect(null);
                break;
            case 2:
                page3.setEffect(null);
                break;
            case 3:
                page4.setEffect(null);
                break;
            case 4:
                page5.setEffect(null);
                break;
        }
    }

    @FXML
    private void setCurrentButton(MouseEvent event) {
        int oldPage = currentPage;
        cleanEffect();
        Object source = event.getSource();
        if (source == nextPage) {
            if (currentPage + 1 >= 0 && currentPage + 1 <= 4) {
                currentPage++;
            }
        }
        if (source == prevPage) {
            if (currentPage - 1 >= 0 && currentPage - 1 <= 4) {
                currentPage--;
            }
        }
        if (source == page1) {
            currentPage = 0;
        }
        if (source == page2) {
            currentPage = 1;
        }
        if (source == page3) {
            currentPage = 2;
        }
        if (source == page4) {
            currentPage = 3;
        }
        if (source == page5) {
            currentPage = 4;
        }
        if (oldPage != currentPage) {
            primaryController.setView();
        }
        // TODO: send the message to the model
        setButtonEffect();
    }
}
