package model.resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class DeleteMenuController {

    @FXML private Button backBtn;

    //TODO: at the moment this causes the loaded data in the tree view to be wiped. Fix by adding model that stores
    //data and bind the data in the UI to the data in the model. This way reloading the FXML loads the same data.
    public void backBtnPressed() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("DatabaseMenu.fxml"));
        backBtn.getScene().setRoot(root);
    }

}
