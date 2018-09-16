package model;

import java.io.File;

public class DatabaseParser {

    private String pathToDB;

    public DatabaseParser(String path) {
        pathToDB = path;
    }

    //Iterates through files in database directory and converts to readable name
    public void parseDB() {
        File dir = new File(pathToDB);
        File[] directoryListing = dir.listFiles();
        if(directoryListing != null) {
            for(File file : directoryListing) {
                String fileName = file.getName();
                String fileTrim = fileName.replaceAll("[^A-Za-z]", "");
                String finalName = fileTrim.substring(2, fileTrim.length()-3);
                System.out.println(finalName);
            }
        }
    }

    //TODO: Instead of just printing the names, make a folder for each with said name and place corresponding file in folder
    //then add to tree view in database menu.
}
