package primary_page.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;

public class NavigationController implements Initializable, PropertyChangeListener {

    private int currentPage;

    private PrimaryController primaryController;

    @FXML
    private Button page1, page2, page3, page4, page5, prevPage, nextPage;

    private PropertyChangeSupport propertyChangeSupport;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCurrentButton();
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    void injectController(PrimaryController primaryController) {
        this.primaryController = primaryController;
        this.primaryController.addPropertyChangeListener(this);
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

    void setCurrentButton() {
        cleanEffect();
        currentPage = 0;
        setButtonEffect();
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
        setButtonEffect();
        doNotify(oldPage);
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private void doNotify(int oldPage) {
        propertyChangeSupport.firePropertyChange("currentPage", oldPage, currentPage);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("currentPage")) {
            setCurrentButton();
        }
    }
}
