package primary_page.controller;

import com.sun.glass.ui.Cursor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ProgressbarController  {
    @FXML
    ProgressBar progressBar = new ProgressBar();
    private BigDecimal progress = new BigDecimal(String.format("%.2f",0.0));
    //Increasing the prgress
    public void startProgress() {
        progressBar.setProgress(progress.doubleValue());
        System.out.println(progress);
    }
    public void increaseProgress() {
        progress = new BigDecimal(String.format("%.2f", progress.doubleValue() + 0.1));
        if(progress.doubleValue() == 0.8) {
            progress =  new BigDecimal(String.format("%.2f", progress.doubleValue()));
        }
        System.out.println(progress);
        progressBar.setProgress(progress.doubleValue());
    }

    public void finishProgress() {
        System.out.println(progress);
        progressBar.setProgress(1);
        progressBar.setVisible(false);
        progress =  new BigDecimal(String.format("%.2f",0.0));
    }

}
