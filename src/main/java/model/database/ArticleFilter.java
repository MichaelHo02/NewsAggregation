package model.database;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public  class ArticleFilter {
    //Load prdefined world from file -> Store on file for future upgradability,
    public static String[] loadDictionary(String dictionaryFile) {
        String[] dictionary;
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
}
