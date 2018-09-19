package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

    //This method displays the contents of the text file containing a list of bad recordings
    void initialize(){

        //Read in the file containing the list of bad quality recordings
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("BadRecordings.txt")))) {
            String line;
            StringBuilder fieldContent = new StringBuilder("");

            while ((line = reader.readLine()) != null) {
                //Concatenate each line of the file to the StringBuilder
                fieldContent.append(line + "\n");
            }

            //Ensure the textArea is not editable by the user
            textArea.setText(fieldContent.toString());
            textArea.setEditable(false);
            textArea.setDisable(true);
            textArea.setMouseTransparent(true);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}

