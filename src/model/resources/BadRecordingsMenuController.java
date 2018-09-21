package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashSet;

public class BadRecordingsMenuController {

    @FXML
    private Button backButton;

    @FXML
    private TextArea textArea;

    //Return the user to the start menu when the back button is clicked
    @FXML
    void backButtonClicked(MouseEvent event) throws IOException {
        Scene scene = SetUp.getInstance().startMenu;
        Stage window = (Stage) backButton.getScene().getWindow();
        window.setScene(scene);

    }

    //TODO: ENSURE THAT IF A RECORDING ALREADY EXISTS IN THE TEXT FILE, SKIP REPRINTING IT ONTO THE SCREEN
    //TODO: DON'T PRINT WHITE SPACE
    //TODO: MAKE IT BIND SO IF USER DELETES THE FILE, IT WILL CREATE ITSELF AGAIN
    //This method displays the contents of the text file containing a list of bad recordings
    void initialize() throws IOException {
            updateTextLog();
    }

    public void updateTextLog() throws IOException {

        //Set storing unique values of creations
        HashSet<String> hs = new HashSet<String>();

            //Read in the file containing the list of bad quality recordings
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("BadRecordings.txt")))) {
            String line;
            StringBuilder fieldContent = new StringBuilder("");

            while ((line = reader.readLine()) != null) {
                //Concatenate each line of the file to the StringBuilder
                if(hs.add(line)) {
                    fieldContent.append(line + "\n");
                }
            }

            //Ensure the textArea is not editable by the user
            textArea.setText(fieldContent.toString());
            textArea.setEditable(false);
            textArea.setDisable(true);
            textArea.setMouseTransparent(true);

        } catch (IOException e) {
            //If there are no bad recordings saved, create a new text file to store them
            File f = new File("BadRecordings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.flush();
            bw.close();
        }
    }
}

