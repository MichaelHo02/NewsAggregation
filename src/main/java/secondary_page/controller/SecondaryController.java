package secondary_page.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
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
    public VBox articleVbox;

    @FXML
    public Button secondaryButton;

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
        if (articleVbox.getChildren().size() > 1) {
            articleVbox.getChildren().remove(articleVbox.getChildren().size() - 1);
        }
//        DisplayEngine displayEngine = new DisplayEngine();
//        WebView webView = new WebView();
//        WebEngine webEngine = webView.getEngine();
//        //Display through the web view
//        webEngine.loadContent(displayEngine.getHTML(article));
//        articleVbox.getChildren().add(webView)

        //Add the new article
    articleVbox.getChildren().addAll(Content.dispArt(Content.articleSwitcher(article)));

    }
}
