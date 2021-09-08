package secondary_page.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import main.Main;
import model.display.DisplayEngine;
import model.display.DisplayVNExpress;
import model.get_article_behavior.Article;
import model.test.Content;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SecondaryController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    public Button secondaryButton;

    @FXML
    public Label title;

    private Article article;

    public void setArticle(Article article) {
        this.article = article;
    }

    @FXML
    private void switchToPrimary() {
        Main.setRoot();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setupView() {
        //Clear article vbox
//        DisplayEngine displayEngine = new DisplayEngine();
//        WebView webView = new WebView();
//        WebEngine webEngine = webView.getEngine();
//        //Display through the web view
//        webEngine.loadContent(displayEngine.getHTML(article));
//        articleVbox.getChildren().add(webView)

        //Add the new article
        borderPane.setCenter(Content.dispArt(Content.articleSwitcher(article)));
        title.setFont(new Font(20));
        title.setText(article.getTitlePage());
    }

    @FXML
    public void zoomIn(MouseEvent e) {
        title.setScaleX(1.5);
        title.setScaleY(1.5);
        System.out.println("Mouse in");
    }

    @FXML
    public void  zoomOut(MouseEvent e) {
        title.setScaleX(1);
        title.setScaleY(1);
        System.out.println("Mouse out");
    }
}
