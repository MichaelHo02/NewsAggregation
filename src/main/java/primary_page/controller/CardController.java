/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Ho Le Minh Thach s3877980
        Last modified date: 10/09/2021
        Acknowledgement:
        Thank you, Professor Nick Wergeles for explaining the concept of Javafx RunLater
        https://youtube.com/playlist?list=PLpvL1C_oZsr-BMBvdtgipMCDZK14BNigd
 */

package primary_page.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import main.Main;
import model.database.Article;
import util.get_article_behavior.WebsiteURL;

public class CardController {
    @FXML
    private Text source;

    @FXML
    private Text title;

    @FXML
    private Text time;

    @FXML
    private ImageView imageView;

    public String websiteLink;

    public WebsiteURL websiteSource;

    private Article cardArticle;

    public void setData(Article article) {
        this.cardArticle = article;
        String titleStr = article.getTitlePage();
        String timeStr = null;
        websiteLink = article.getLinkPage();
        websiteSource = article.getSource();
        if (time != null) {
            if (article.getDuration() != null) {
                timeStr = Article.getFriendlyDate(article.getDuration());
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

    public void clickCard() {
        websiteLink = this.cardArticle.getLinkPage();
        websiteSource = this.cardArticle.getSource();
        Main.setRoot(cardArticle);
    }
}
