package model.resources;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateMenuController {

    @FXML
    private Button backBtn;
    @FXML
    private Button createBtn;
    @FXML
    private TextField textInput;
    private String name;

    public void backBtnClicked() throws IOException {
        textInput.clear();
        Scene scene = SetUp.getInstance().databaseMenu;
        Stage window = (Stage) backBtn.getScene().getWindow();
        window.setScene(scene);
    }

    public void setCreatePrompt(String name) {
        textInput.setText(name);
    }

    public void createBtnPressed() throws IOException {

        name = textInput.getText();

        if (checkName(name)) {

            name = reformat(name);
            SetUp.getInstance().recordCreationMenuController.setUp(name);

            Scene scene = SetUp.getInstance().recordCreationMenu;
            Stage window = (Stage) createBtn.getScene().getWindow();
            window.setScene(scene);
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR,
                    "Please only use letters (a-z), numbers, spaces" +
                            "\nunderscores, and hyphens.", ButtonType.OK);
            error.setHeaderText("ERROR: Invalid Creation Name");
            error.setTitle("Invalid Creation Name");
            error.showAndWait();
        }
    }

    //Create binding to create button is disabled if no input yet
    public void setUpButton() {
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(textInput.textProperty());
            }

            protected boolean computeValue() {
                return textInput.getText().isEmpty() || textInput.getText() == null;
            }
        };
        //Bind disable property to ok button so it will be disabled if invalid input
        createBtn.disableProperty().bind(bb);
    }

    public String getName() {
        return name;
    }

    private boolean checkName(String newName) {
        //Check if the new creation name is empty or null
        if (newName.equals("") || newName.equals(null)) {
            return false;
        }

        //Check if the new creation name only consists of white spaces
        int count = 0;
        for (char ch : newName.toCharArray()) {
            if (Character.isWhitespace(ch)) {
                count++;
            }
        }
        if (count == newName.length()) {
            return false;
        }

        //Check for leading/trailing whitespaces
        if (newName.startsWith(" ") || newName.endsWith(" ")) {
            return false;
        }

        if (newName.matches("uncut_files")) {
            return false;
        }

        //Check for valid characters
        return newName.matches("^[a-zA-Z0-9 _-]+$");

    }

    private String reformat(String name) {
        char[] chars = name.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '-' || chars[i] == '_') {
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}
