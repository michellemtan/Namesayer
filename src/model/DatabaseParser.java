package model;

import java.io.File;

public class DatabaseParser {

    private String pathToDB;

    public DatabaseParser(String path) {
        pathToDB = path;
    }

    public void parseDB() {
        File dir = new File(pathToDB);
        File[] directoryListing = dir.listFiles();
        System.out.println(pathToDB);
        if(directoryListing != null) {
            for(File file : directoryListing) {
                System.out.println(file.getName());
            }
        }
    }

}
