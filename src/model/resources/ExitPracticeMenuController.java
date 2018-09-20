package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ExitPracticeMenuController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    void cancelButtonClicked(MouseEvent event) throws IOException {
        Scene scene = SetUp.getInstance().practiceMenu;
        Stage window = (Stage) cancelButton.getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    void confirmButtonClicked(MouseEvent event) throws IOException {
        Scene scene = SetUp.getInstance().databaseMenu;
        Stage window = (Stage) confirmButton.getScene().getWindow();
        window.setScene(scene);
    }
}
