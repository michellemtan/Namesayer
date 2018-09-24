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

public class BadRecordingsMenuController {

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
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("BadRecordings.txt")))) {
            String line;
            StringBuilder fieldContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                //Concatenate each line of the file to the StringBuilder
                String name = line.substring(0, line.indexOf(":"));
                ratingMap.put(name, line);
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
            File f = new File("BadRecordings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.flush();
            bw.close();
        }
    }

    @FXML
    public void clearTextLog() throws IOException {
        File file = new File("BadRecordings.txt");
        PrintWriter writer = new PrintWriter(file);
        ratingMap = new HashMap<>();
        updateTextLog();
    }
}
