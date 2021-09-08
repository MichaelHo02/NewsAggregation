package secondary_page.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import main.Main;
import model.get_article_behavior.Article;
import model.article_extraction.ArticleFactory;
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
        borderPane.setCenter(ArticleFactory.dispArt(ArticleFactory.articleSwitcher(article)));
        title.setFont(new Font(18));
        title.setStyle("-fx-font-weight: bold");
        title.setText(article.getTitlePage());
    }

    @FXML
    public void zoomIn(MouseEvent e) {
        title.setScaleX(1.2);
        title.setScaleY(1.2);
        System.out.println("Mouse in");
    }

    @FXML
    public void  zoomOut(MouseEvent e) {
        title.setScaleX(1);
        title.setScaleY(1);
        System.out.println("Mouse out");
    }
}
