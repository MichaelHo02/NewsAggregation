package model.filter;

import model.get_article_behavior.Article;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public  class ArticleFilter {
    //Load prdefined world from file -> Store on file for future upgradability,

    public static String []  loadDictionary(String dictionaryFile) {
        String [] dictionary;
        //Create a scnner to read file
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
    
    //Implement ís category to get the category
    //TODO: Need 8more methode and define dictionary like thiss
    public void isWorld(Article article){
        String [] dict = loadDictionary("dictionary/World");
        for (String tmp: dict) {
            if(article.getCategory().matches(tmp)) {
            //Add to category
            }
        }
    }

    //Filter action
    //Call all of the is category
    public static void  filteringCategory(Article article) {
        //Call all of other methid
    }
    
    
}
