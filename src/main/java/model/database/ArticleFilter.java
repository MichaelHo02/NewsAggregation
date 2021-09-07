package model.database;

import model.get_article_behavior.Article;
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
            if (key.matcher(rawCategory).find()) {
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
        // ToDo: Do you need the flag?
        boolean flag = false; // boolean to flag if an article belongs to at least 01 category besides "Others"
        article.addCategory(0);
        for (int i = 0; i < category.length; i++) {
            if (InitScraper.catCounter.get(i) < 50) {
                if (isMatch(article.getCategory(), "src/main/java/model/database/dictionary/" + category[i] + ".txt")) {
                    article.addCategory(i + 1);
                    InitScraper.setValue(i, InitScraper.getValue(i) + 1);
                    flag = true; // set flag
                } else {
                    //Set the category counter for other if it doesn't match any of the category
                    InitScraper.setValue(8, InitScraper.getValue(i) + 1);
                }
            } else {
                System.out.println("Category " + category[i] + " is full!");
            }
        }
        return flag;
    }

    public static boolean filterArticle(String folderUrl) {
        //            System.out.println("Get filter");
        return isMatch(folderUrl, "src/main/java/model/database/dictionary/" + "NavigationFolder.txt") && !folderUrl.contains("video") && !folderUrl.contains("game") && !folderUrl.contains("viec-lam");
    }
}
