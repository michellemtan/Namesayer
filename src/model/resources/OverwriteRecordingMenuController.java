package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class OverwriteRecordingMenuController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    private boolean overwriteRecording;

    @FXML
    void cancelButtonClicked(MouseEvent event) throws IOException {
        overwriteRecording = false;
        Scene scene = SetUp.getInstance().recordCreationMenu;
        Stage window = (Stage) cancelButton.getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    void confirmButtonClicked(MouseEvent event) throws IOException {
        overwriteRecording = true;
        Scene scene = SetUp.getInstance().recordCreationMenu;
        Stage window = (Stage) confirmButton.getScene().getWindow();
        window.setScene(scene);
    }

    public boolean isOverwriteRecording(){
        if (overwriteRecording == true){
            return true;
        } else {
            return false;
        }
    }
}
