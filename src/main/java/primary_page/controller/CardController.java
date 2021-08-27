package primary_page.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import main.Main;
import model.get_article_behavior.Article;
import model.get_article_behavior.GetArticleBehavior;
import model.get_article_behavior.WebsiteURL;

public class CardController {

    @FXML
    private Text source;

    @FXML
    private Text title;

    @FXML
    private Text time;

    @FXML
    private ImageView imageView;



    private Article cardArticle;

    public void setData(Article article) {
        this.cardArticle = article;

        title.setText(article.getTitlePage());

        if (time != null) {
            if (article.getDuration() != null) {
                time.setText(GetArticleBehavior.getFriendlyDate(article.getDuration()));
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

        String sourceName = "";
        switch (cardArticle.getSource()) {
            case VNEXPRESS:
                sourceName = "VNExpress";
                break;
            case NHANDAN:
                sourceName = "NhanDan";
                break;
            case TUOITRE:
                sourceName = "TuoiTre";
                break;
            case ZINGNEWS:
                sourceName = "ZingNews";
                break;
            case THANHNIEN:
                sourceName = "ThanhNien";
                break;
        }
        source.setText(sourceName);
    }

    public void clickTitle() {
        Main.setRoot(cardArticle);
    }
}