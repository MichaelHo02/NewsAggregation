package primary_page.controller;

import javafx.application.Platform;
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

    public static String websiteLink;

    public static WebsiteURL websiteSource;

    private Article cardArticle;

    public void setData(Article article) {
        this.cardArticle = article;
        String titleStr = article.getTitlePage();
        String timeStr = null;
        websiteLink = article.getLinkPage();
        websiteSource = article.getSource();
        if (time != null) {
            if (article.getDuration() != null) {
                timeStr = GetArticleBehavior.getFriendlyDate(article.getDuration());
            } else {
                timeStr = "No Time";
            }
        }
        Image imageURL = null;
        if (imageView != null) {
            if (article.getImageURL() != null && !article.getImageURL().isEmpty()) {
                imageURL = new Image(article.getImageURL());
            } else {
                imageURL = null;
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
        String finalSourceName = sourceName;
        String finalTimeStr = timeStr;
        Image finalImageURL = imageURL;
        Platform.runLater(() -> {
            title.setText(titleStr);
            source.setText(finalSourceName);
            time.setText(finalTimeStr);
            imageView.setImage(finalImageURL);

        });
    }

    public void clickTitle() {
        websiteLink = this.cardArticle.getLinkPage();
        websiteSource = this.cardArticle.getSource();
        Main.setRoot(cardArticle);
    }
}
