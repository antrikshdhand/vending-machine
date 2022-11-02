package VendingMachine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class App extends Application {

    private Stage primaryStage;

    /**
     * Start method for the Vending Machine application.
     */
    @Override
    public void start(Stage primaryStage0) throws Exception {

        primaryStage = primaryStage0;
        // Stage is basically the window, and you are given the name of the window.
        primaryStage.setTitle("Vending Machine");

        // we do not want the window to be resizable/
        primaryStage.setResizable(false);


        // If you want to add an icon for the program:
        // Image icon = new Image('File path')
        // primaryStage.setIcon(icon);

        // with javafx, by default with window will appear in the middle, unlike Swing, so no changes are needed.

        URL url = new File("src/main/java/VendingMachine/fxml/Main.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(url);

        // DefaultPageController defaultPageController = loader.getController();
        // defaultPageController.setSceneManager(sceneManager);
        // primaryStage.setScene(sceneManager.getDefaultPageScene());

        Scene scene = new Scene(root);
        String css = new File("src/main/java/VendingMachine/css/style.css").toURI().toURL().toExternalForm();

        scene.getStylesheets().add(css);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main function to launch the Vending Machine app.
     */
    public static void main(String[] args) {
        launch(args);
    }

}