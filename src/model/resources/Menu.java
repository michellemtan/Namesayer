package model.resources;
import javafx.fxml.FXMLLoader;

import java.net.URL;

public enum Menu {

    //Load menus to be used throughout the program
    BADRECORDINGSMENU("model/resources/BadRecordingsMenu.fxml"),
    COMPAREMENU("model/resources/CompareMenu.fxml"),
    CREATEMENU("model/resources/CreateMenu.fxml"),
    DATABASEMENU("model/resources/DatabaseMenu.fxml"),
    DATABASESELECTMENU("model/resources/CompareMenu.fxml"),
    DELETEMENU("model/resources/DeleteMenu.fxml"),
    EXITPRACTICEMENU("model/resources/ExitPracticeMenu.fxml"),
    FINISHEDMENU("model/resources/FinishedMenu.fxml"),
    INSTRUCTIONSMENU("model/resources/InstructionsMenu.fxml"),
    MICROPHONECHECKMENU("model/resources/MicrophoneCheckMenu.fxml"),
    NAMEDETAILSMENU("model/resources/NameDetailsMenu.fxml"),
    PRACTICEMENU("model/resources/PracticeMenu.fxml"),
    RECORDMENU("model/resources/RecordMenu.fxml"),
    STARTMENU("model/resources/StartMenu.fxml");

    private URL url;

    Menu(String url) {
        this.url = this.getClass().getClassLoader().getResource(url);
    }

    //This method returns the FXML loader for the menu
    public FXMLLoader loader() {
        return new FXMLLoader(url);
    }
}
