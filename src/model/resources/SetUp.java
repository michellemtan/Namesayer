package model.resources;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class SetUp {

    //Single instance of NameSayer can be running at a time
    private static SetUp setUp;

    //Initialise controllers to allow for data to be passed between scenes
    public DatabaseMenuController dbMenuController;
    public DeleteMenuController deleteMenuController;
    public DatabaseSelectMenuController databaseSelectMenuController;
    public PlayMenuController playMenuController;
    public PracticeMenuController practiceMenuController;
    public RecordMenuController recordMenuController;
    public StartMenuController startMenuController;
    public BadRecordingsMenuController badRecordingsMenuController;
    //public CompareMenuController compareMenuController;
    //public CreateMenuController createMenuController;
    //public FinishedMenuController finishedMenuController;

    public Scene compareMenu;
    public Scene createMenu;
    public Scene databaseMenu;
    public Scene deleteMenu;
    public Scene finishedMenu;
    public Scene databaseSelectMenu;
    public Scene playMenu;
    public Scene practiceMenu;
    public Scene recordMenu;
    public Scene startMenu;
    public Scene badRecordingsMenu;

    private SetUp() throws IOException {

        //Load menus to be used throughout the program
        compareMenu = new Scene(FXMLLoader.load(getClass().getResource("CompareMenu.fxml")));
        createMenu = new Scene(FXMLLoader.load(getClass().getResource("CreateMenu.fxml")));
        databaseMenu = new Scene(FXMLLoader.load(getClass().getResource("DatabaseMenu.fxml")));
        deleteMenu = new Scene(FXMLLoader.load(getClass().getResource("DeleteMenu.fxml")));
        finishedMenu = new Scene(FXMLLoader.load(getClass().getResource("FinishedMenu.fxml")));
        databaseSelectMenu = new Scene(FXMLLoader.load(getClass().getResource("DatabaseSelectMenu.fxml")));
        playMenu = new Scene(FXMLLoader.load(getClass().getResource("PlayMenu.fxml")));
        practiceMenu = new Scene(FXMLLoader.load(getClass().getResource("PracticeMenu.fxml")));
        recordMenu = new Scene(FXMLLoader.load(getClass().getResource("RecordMenu.fxml")));
        startMenu = new Scene(FXMLLoader.load(getClass().getResource("StartMenu.fxml")));
        badRecordingsMenu = new Scene(FXMLLoader.load(getClass().getResource("BadRecordingsMenu.fxml")));

        //Load load menu
        databaseMenuLoader();
        deleteMenuLoader();
        mainMenuLoader();
        playMenuLoader();
        practiceMenuLoader();
        recordMenuLoader();
        startMenuLoader();
        badRecordingsMenuLoader();
        //compareMenuLoader();
        //createMenuLoader();
        //finishedMenuLoader();

    }

    //Constructor implementing Singleton pattern to create one instance of SetUp class where different scenes are created
    public static SetUp getInstance() throws IOException {
            if (setUp == null){
                setUp = new SetUp();
            }

        return setUp;
    }

    private void startMenuLoader() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartMenu.fxml"));
        startMenu = new Scene(loader.load());
        startMenuController = loader.getController();
    }

    private void recordMenuLoader() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordMenu.fxml"));
        recordMenu = new Scene(loader.load());
        recordMenuController = loader.getController();
    }

    private void practiceMenuLoader() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PracticeMenu.fxml"));
        practiceMenu = new Scene(loader.load());
        practiceMenuController = loader.getController();
    }

    private void playMenuLoader() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayMenu.fxml"));
        playMenu = new Scene(loader.load());
        playMenuController = loader.getController();
    }

    private void mainMenuLoader() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DatabaseSelectMenu.fxml"));
        databaseSelectMenu = new Scene(loader.load());
        databaseSelectMenuController = loader.getController();
    }

    private void deleteMenuLoader() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DeleteMenu.fxml"));
        deleteMenu = new Scene(loader.load());
        deleteMenuController = loader.getController();
    }

    private void databaseMenuLoader() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DatabaseMenu.fxml"));
        databaseMenu = new Scene(loader.load());
        dbMenuController = loader.getController();
    }

    private void badRecordingsMenuLoader() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BadRecordingsMenu.fxml"));
        badRecordingsMenu = new Scene(loader.load());
        badRecordingsMenuController = loader.getController();
        badRecordingsMenuController.initialize();
    }

    /*private void createMenuLoader() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateMenu.fxml"));
        createMenu = new Scene(loader.load());
        createMenuController = loader.getController();
    }*/

    /*private void compareMenuLoader() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CompareMenu.fxml"));
        compareMenu = new Scene(loader.load());
        compareMenuController = loader.getController();
    }*/

    /*private void finishedMenuLoader() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinishedMenu.fxml"));
        finishedMenu = new Scene(loader.load());
        deleteMenuController = loader.getController();
    }*/

}
