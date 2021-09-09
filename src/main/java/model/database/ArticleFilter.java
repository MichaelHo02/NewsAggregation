/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Bui Minh Nhat s3878174
        Last modified date: 10/09/2021
        Contributor: Nguyen Dich Long s3879052
        Acknowledgement:
        https://webfocusinfocenter.informationbuilders.com/wfappent/TLs/TL_srv_dm/source/regex.htm
        https://www.tutorialspoint.com/java/java_files_io.htm
        https://www.javatpoint.com/java-io
 */

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
            // dictionary input
            File input = new File(dictionaryFile);
            String delim = ", ";
            String src = Files.readString(Path.of(input.getPath()));
            //Need to split the string contain delim
            dictionary = src.split(delim);
            return dictionary;
        } catch (Exception e) {
            System.out.println("Error with " + dictionaryFile);
            return null;
        }
    }

    public static boolean isMatch(String rawCategory, String dictionaryFile) {
        String[] dictionary = loadDictionary(dictionaryFile);
        //  Check if the dictionary is false
        if (dictionary == null) {
            return false;
        }
        for (int i = 0; i < dictionary.length; i++) {
            if (isWordMatches(rawCategory, dictionary[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWordMatches(String rawCategory, String words) { // check if articles's raw category data matches this database
        //Don't need t worry about the case of the file
        Pattern key = Pattern.compile(words, Pattern.CASE_INSENSITIVE);
        //We only need to match 1 time
        String tmp = rawCategory.toLowerCase();
        if (key.matcher(tmp).find()) {
            return true;
        }
        //Don't find any matches
        return false;
    }

    //Use this to filter the article
    synchronized public static boolean filterArticle(Article article) {
        String[] category = {"Covid", "Politics", "Business", "Technology", "Health", "Sport", "Entertainment", "World"};

        boolean hasCategory = false;
        article.addCategory(0); // add to category "latest" with index 0
        for (int i = 0; i < category.length; i++) {
            // Avoid video article
            if(isWordMatches(article.getLinkPage(),"video")) {
                return false;
            }
            // loop through all dictionaries, check if articles match any
            if (isMatch(article.getCategory(), "src/main/java/model/database/dictionary/" + category[i] + ".txt")) {
                article.addCategory(i + 1); // if yes then update category list + update counter
                InitScraper.setValue(i, InitScraper.getValue(i) + 1);
                hasCategory = true;
            }
        }
        if (!hasCategory) {
            final int others = 8;
            article.addCategory(others + 1); // update category list and counter
            InitScraper.setValue(others, InitScraper.getValue(others) + 1);
        }
        return article.getCategories().size() > 1; // return whether article belongs to any category
    }

    public static boolean filterArticle(String folderUrl) {
        return isMatch(folderUrl, "src/main/java/model/database/dictionary/" + "NavigationFolder.txt") &&!folderUrl.contains("video")  && !folderUrl.contains("game") && !folderUrl.contains("viec-lam");
    }

}
