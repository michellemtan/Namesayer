package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AudioRatingsController {

    HashMap<String, String> ratingMap = new HashMap<>();
    @FXML
    private Button backButton;
    @FXML
    private TextArea textArea;
    private String previousScene = "";
    @FXML
    private Button clearTextButton;

    //Return the user to the start menu when the back button is clicked
    @FXML
    void backButtonClicked() throws IOException {
        if (previousScene.equals("practiceMenu")) {
            Scene scene = SetUp.getInstance().practiceMenu;
            Stage window = (Stage) backButton.getScene().getWindow();
            window.setScene(scene);
        } else if (previousScene.equals("nameDetailsMenu")) {
            Scene scene = SetUp.getInstance().nameDetailsMenu;
            Stage window = (Stage) backButton.getScene().getWindow();
            window.setScene(scene);
        } else if (previousScene.equals("startMenu")) {
            Scene scene = SetUp.getInstance().startMenu;
            Stage window = (Stage) backButton.getScene().getWindow();
            window.setScene(scene);
        } else if (previousScene.equals("compareMenu")) {
            Scene scene = SetUp.getInstance().compareMenu;
            Stage window = (Stage) backButton.getScene().getWindow();
            window.setScene(scene);
        }
    }

    public void setPreviousScene(String name) throws IOException {
        previousScene = name;
        updateTextLog();
    }

    //This method displays the contents of the text file containing a list of bad recordings

    public void updateTextLog() throws IOException {

        List<String> lineList = new ArrayList<String>();

        //Read in the file containing the list of bad quality recordings
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("AudioRatings.txt")))) {
            String line;
            StringBuilder fieldContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                //Concatenate each line of the file to the StringBuilder
                String name = line.substring(0, line.indexOf(":"));
                ratingMap.put(name, line.trim());
            }

            for (String value : ratingMap.values()) {
                lineList.add(value);
            }

            Collections.sort(lineList);

            for (String outputLine : lineList) {
                fieldContent.append(outputLine + "\n");
            }

            //Ensure the textArea is not editable by the user
            textArea.setText(fieldContent.toString());
            textArea.setEditable(false);
            //textArea.setMouseTransparent(true);

        } catch (IOException e) {
            //If there are no bad recordings saved, create a new text file to store them
            File f = new File("AudioRatings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.flush();
            bw.close();
        }
    }

    @FXML
    public void clearTextLog() throws IOException {
        File file = new File("AudioRatings.txt");
        PrintWriter writer = new PrintWriter(file);
        ratingMap = new HashMap<>();
        updateTextLog();
    }


    public void deleteName(List<String> toBeDeletedList) throws IOException {

//        File inputFile = new File("AudioRatings.txt");
//        File tempFile = new File("myTempFile.txt");
//
//        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
//        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
//
//        String currentLine;
//        while ((currentLine = reader.readLine()) != null) {
//            // trim newline when comparing with lineToRemove
//            for (String name : toBeDeletedList) {
//                String lineToRemove = name.concat(".wav");
//                if (currentLine.contains(lineToRemove)) {
//                    System.out.println("Remove: " + lineToRemove);
//                } else {
//                    writer.write(currentLine + System.getProperty("line.separator"));
//                    System.out.println("Add: " + currentLine);
//                }
//            }
//        }
//        writer.close();
//        reader.close();
//        boolean successful = tempFile.renameTo(inputFile);
//        updateTextLog();

        List<String> lineList = new ArrayList<String>();

        //Read in the file containing the list of bad quality recordings
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("AudioRatings.txt")))) {
            String line;
            StringBuilder fieldContent = new StringBuilder();

            //Read audio ratings text file
            while ((line = reader.readLine()) != null) {
                //Concatenate each line of the file to the StringBuilder
                String name = line.substring(0, line.indexOf(":"));
                ratingMap.put(name, line.trim());
            }

            //Iterate through names to be deleted and remove from hash map
            for (String key : toBeDeletedList) {
                System.out.println("Removed " + key + ".wav");
                ratingMap.remove(key + ".wav");
            }

            //Create list of lines to be printed and sort alphabetically
            for (String value : ratingMap.keySet()) {
                lineList.add(ratingMap.get(value));
            }
            Collections.sort(lineList);


            //Write lines to display and text file
            PrintWriter print = new PrintWriter(new FileWriter("AudioRatings.txt"));
            for (String outputLine : lineList) {
                fieldContent.append(outputLine + "\n");
                print.write(outputLine + "\n");
            }

            //Ensure the textArea is not editable by the user
            textArea.setText(fieldContent.toString());
            textArea.setEditable(false);
            print.close();
            //textArea.setMouseTransparent(true);

        } catch (IOException e) {
            //If there are no bad recordings saved, create a new text file to store them
            File f = new File("AudioRatings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.flush();
            bw.close();
        }
    }

    public void addName(String name) throws IOException {
        File f = new File("AudioRatings.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
        bw.append(name);
        bw.flush();
        bw.close();
        updateTextLog();
    }
}

