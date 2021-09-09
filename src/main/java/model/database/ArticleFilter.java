package model.database;

import model.get_article_behavior.Article;
import model.scrapping_engine.InitScraper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static model.database.ArticleFilter.isWordMatches;


public class ArticleFilter {

    //Load prdefined world from file -> Store on file for future upgradability,
    public static String[] loadDictionary(String dictionaryFile) {
        String[] dictionary;
        try {
            //Need to read the dictionary in
            File input = new File(dictionaryFile);
            String delim = ", ";
            String src = Files.readString(Path.of(input.getPath()));
            //Need to split the string contain delim
//            System.out.println(src);
            dictionary = src.split(delim);
            return dictionary;
        } catch (Exception e) {
            System.out.println("Error with " + dictionaryFile);
            return null;
        }
    }

    public static boolean isMatch(String rawCategory, String dictionaryFile) {
        String[] dictionary = loadDictionary(dictionaryFile);
        // If have problem with any of the dictionary
        boolean res = false;
        //  Check if the dictionary is false
        if (dictionary == null) {
            return res;
        }
        for (int i = 0; i < dictionary.length; i++) {
            if (isWordMatches(rawCategory, dictionary[i])) {
                return true;
            }
        }
        return res;
    }

    public static boolean isWordMatches(String rawCategory, String words) { // check if articles's raw category data matches this database
        //Don't need t worry about the case of the file
        Pattern key = Pattern.compile(words, Pattern.CASE_INSENSITIVE);
        //We only need to matches 1 time
        String tmp = rawCategory.toLowerCase();
        if (key.matcher(tmp).find()) {
            return true;
        }
        //Don't found any matches
        return false;
    }

    //Use this to filter the article
    synchronized public static boolean filterArticle(Article article) {
        String[] category = {"Covid", "Politics", "Business", "Technology", "Health", "Sport", "Entertainment", "World"};
        //Need to ingest category into isMatch methode

        article.addCategory(0); // add to category "latest"
        for (int i = 0; i < category.length; i++) {
            //Excriminate video article
            if(isWordMatches(article.getLinkPage(),"video")) {
                continue;
            }
            // loop through all dictionaries, check if articles matches any
            if (isMatch(article.getCategory(), "src/main/java/model/database/dictionary/" + category[i] + ".txt")) {
                if (InitScraper.catCounter.get(i) < 50) { // if match then check if the category still has storage
                    article.addCategory(i + 1); // if yes then update category list + update counter
                    InitScraper.setValue(i, InitScraper.getValue(i) + 1);
                }
            }
        }

        final int others = 8;
        if (article.catIsEmpty() && InitScraper.getValue(others) < 50) { // if article matches no category and others still has storage
            article.addCategory(others + 1); // update category list and counter
            InitScraper.setValue(others, InitScraper.getValue(others) + 1);
        }

        return !article.catIsEmpty(); // return whether article belongs to any category
    }

    public static boolean filterArticle(String folderUrl) {
        return isMatch(folderUrl, "src/main/java/model/database/dictionary/" + "NavigationFolder.txt") &&!folderUrl.contains("video")  && !folderUrl.contains("game") && !folderUrl.contains("viec-lam");
    }

    public static void main(String[] args) {
        System.out.println(isWordMatches("https://thanhnien.vn/video/the-gioi/con-lai-gi-trong-can-cu-quan-su-my-o-afghanistan-166153v.html","video"));
    }
}
