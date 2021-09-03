package model.database;

import model.get_article_behavior.Article;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public  class ArticleFilter {
    //Load prdefined world from file -> Store on file for future upgradability,
    public static String[] loadDictionary(String dictionaryFile) {
        String[] dictionary;
        try {
        //Need to read the dictionary in
            File input = new File(dictionaryFile);
            String delim = ",";
            String src = Files.readString(Path.of(input.getPath()));
            //Need to split the string contain delim
            dictionary = src.split(delim);
            return dictionary;
        } catch (Exception e) {
            System.out.println("Error with " + dictionaryFile);
            return null;
        }
    }

    public static boolean isMatch(Article article, String dictionaryFile) { // check if articles's raw category data matches this database
        String [] dictionary = loadDictionary(dictionaryFile);
        //If have problem with any of the dictionary
        if(dictionary == null) {
            return false;
        }

        for (String tmp : dictionary) {
            //Don't need t worry about the case of the file
            Pattern key = Pattern.compile(tmp, Pattern.CASE_INSENSITIVE);
            //We only need to matches 1 time
            if (key.matcher(article.getCategory()).find()) { return true; }
        }
        //Don't found any matches
        return false;
    }

    //Use this to filter the article
    public static  void filterArticle(Article article) {
        String [] category = {"Bussiness", "Covid","Entertainment", "Health", "Politcs", "Sport", "Technology", "World"};
        //Need to ingest category into isMatch methode
        for (String tmp: category ) {
            if(isMatch(article,"dictionary/" + category + ".txt"));
            //TODO: Devide into different category, idk how you guys display differnet article into different category. Need help on this
        }
    }

}
