package model.article_extraction;

import java.util.ArrayList;
import java.util.List;

public abstract class ArticleExtractor {

    // Workflow
    // Class Content has context and type
    // Creates 5 class to scrap content of each webpage
    // Store the main information of each link into a list called CONTENT
    //
    // Display to the UI:
    // To display, loop through the CONTENT array
    // If type is text then create a new label
    // If type is Image then create a ImageView
    // If type is Video then create a MediaView
    //
    // TODO: Design a new ArticleViewer.fxml

    final static List<ArticleFactory> ARTICLE_FACTORY = new ArrayList<ArticleFactory>();
    public abstract List<ArticleFactory> getContent(String linkPage);
}
