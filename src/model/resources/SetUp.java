package model.resources;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class SetUp {

    //Single instance of Namesayer can be running at a time
    private static SetUp setUp;

    //Initialise controllers to allow for data to be passed between scenes
    public DatabaseMenuController dbController;
    public DeleteMenuController deleteController;
    public MainMenuController mainController;
    public PlayMenuController playController;

    public Scene compareMenu;
    public Scene createMenu;
    public Scene databaseMenu;
    public Scene deleteMenu;
    public Scene finishedMenu;
    public Scene mainMenu;
    public Scene playMenu;
    public Scene practiceMenu;
    public Scene recordMenu;
    public Scene startMenu;

    private SetUp() throws IOException {

        //Load menus to be used throughout the program
        compareMenu = new Scene(FXMLLoader.load(getClass().getResource("CompareMenu.fxml")));
        createMenu = new Scene(FXMLLoader.load(getClass().getResource("CreateMenu.fxml")));
        databaseMenu = new Scene(FXMLLoader.load(getClass().getResource("DatabaseMenu.fxml")));
        deleteMenu = new Scene(FXMLLoader.load(getClass().getResource("DeleteMenu.fxml")));
        finishedMenu = new Scene(FXMLLoader.load(getClass().getResource("FinishedMenu.fxml")));
        mainMenu = new Scene(FXMLLoader.load(getClass().getResource("MainMenu.fxml")));
        playMenu = new Scene(FXMLLoader.load(getClass().getResource("PlayMenu.fxml")));
        practiceMenu = new Scene(FXMLLoader.load(getClass().getResource("PracticeMenu.fxml")));
        recordMenu = new Scene(FXMLLoader.load(getClass().getResource("RecordMenu.fxml")));
        startMenu = new Scene(FXMLLoader.load(getClass().getResource("StartMenu.fxml")));

    }

    //Constructor implementing Singleton pattern to create one instance of SetUp class where different scenes are created
    public static SetUp getInstance() throws IOException {
            if (setUp == null){
                setUp = new SetUp();
            }

        return setUp;
    }
}
