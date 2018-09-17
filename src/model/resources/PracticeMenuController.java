package model.resources;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class PracticeMenuController {

    @FXML
    private Button startButton;

    @FXML
    private Button backButton;

    @FXML
    private CheckBox randomiseCheckBox;

    private Parent parentRoot;

    //TODO: back button take back to database menu, not start menu
    @FXML
    void backButtonClicked() throws IOException {
        backButton.getScene().setRoot(parentRoot);
    }

    @FXML
    void randomiseClicked(MouseEvent event) {

    }

    @FXML
    void startButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PlayMenu.fxml"));
        startButton.getScene().setRoot(root);
    }

    //parentRoot is the Parent created be loading the database menu fxml. It's been passed through to here so the back button
    //doesn't cause the database menu to reload, just loads it with the same root as the first time it was loaded so
    //the treeview is preserved.
    void setParentRoot(Parent root) {
        parentRoot = root;
    }
}
