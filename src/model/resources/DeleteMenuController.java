package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class DeleteMenuController {

    @FXML private Button backBtn;
    private Parent parentRoot;

    //TODO: at the moment this causes the loaded data in the tree view to be wiped. Fix by adding model that stores
    //data and bind the data in the UI to the data in the model. This way reloading the FXML loads the same data.
    public void backBtnPressed() throws IOException {
        backBtn.getScene().setRoot(parentRoot);
    }


    //parentRoot is the Parent created be loading the database menu fxml. It's been passed through to here so the back button
    //doesn't cause the database menu to reload, just loads it with the same root as the first time it was loaded so
    //the treeview is preserved.
    void setParentRoot(Parent root) {
        parentRoot = root;
    }

}
