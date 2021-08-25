package primary_page.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import main.Main;
import model.get_article_behavior.Article;
import model.get_article_behavior.ScrappingEngine;
import model.get_article_behavior.WebsiteURL;

import java.io.IOException;

public class CardController {
    @FXML
    private Text title;

    @FXML
    private Text time;

    @FXML
    private ImageView imageView;

    public static String websiteLink;

    public static WebsiteURL websiteSource;

    private Article cardArticle;

    public void setData(Article article) {
        this.cardArticle = article;
        websiteLink = article.getLinkPage();
        websiteSource = article.getSource();
        title.setText(article.getTitlePage());

        if (time != null) {
            if (article.getDuration() != null) {
                time.setText(ScrappingEngine.getFriendlyDate(article.getDuration()));
            } else {
                time.setText("No Time");
            }
        }

        if (imageView != null) {
            if (article.getImageURL() != null) {
                imageView.setImage(new Image(article.getImageURL()));
            } else {
                imageView.setImage(null);
            }
        }
    }

    public void clickTitle() throws IOException {
        websiteLink = this.cardArticle.getLinkPage();
        websiteSource = this.cardArticle.getSource();
        Main.setRoot(cardArticle);
    }
}
