package model.database;

import model.get_article_behavior.Article;
import model.get_article_behavior.WebsiteURL;
import model.scrapping_engine.InitScraper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;


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


    public static boolean isMatch(String rawCategory, String dictionaryFile) { // check if articles's raw category data matches this database
        String[] dictionary = loadDictionary(dictionaryFile);
        // If have problem with any of the dictionary
        if (dictionary == null) {
            return false;
        }

        for (int i = 0; i < dictionary.length; i++) {
            //Don't need t worry about the case of the file
            Pattern key = Pattern.compile(dictionary[i], Pattern.CASE_INSENSITIVE);
            //We only need to matches 1 time
            String tmp = rawCategory.toLowerCase();
            if (key.matcher(tmp).find()) {
                return true;
            }
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
            // loop through all dictionaries, check if articles matches any
            if (isMatch(article.getCategory(), "src/main/java/model/database/dictionary/" + category[i] + ".txt")) {
                if (InitScraper.catCounter.get(i) < 50) { // if match then check if the category still has storage
                    article.addCategory(i + 1); // if yes then update category list + update counter
                    InitScraper.setValue(i, InitScraper.getValue(i) + 1);
                }
                else { // if no then print out the storage is full
                    System.out.println("Category " + category[i] + " is full");
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
        //            System.out.println("Get filter");
        return isMatch(folderUrl, "src/main/java/model/database/dictionary/" + "NavigationFolder.txt") && !folderUrl.contains("video") && !folderUrl.contains("game") && !folderUrl.contains("viec-lam");
    }
}
