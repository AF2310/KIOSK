package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.kiosk.InactivityTimer;
import org.example.screens.WelcomeScreen;

/**
* The main app class.
*/
public class App extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    // Window dimensions
    double windowWidth = 1920;
    double windowHeight = 1080;

    // Create the WelcomeScreen object and get the scene
    WelcomeScreen welcomeScreen = new WelcomeScreen();
    Scene welcomeScene = welcomeScreen.createWelcomeScreen(primaryStage, windowWidth, windowHeight);

    // Reset timer globally when user is active in the kiosk
    primaryStage.addEventFilter(MouseEvent.ANY, e -> InactivityTimer.getInstance().resetTimer());
    primaryStage.addEventFilter(KeyEvent.ANY, e -> InactivityTimer.getInstance().resetTimer());

    // Force fullscreen mode
    primaryStage.setFullScreen(false);
    // primaryStage.setFullScreenExitHint("");
    // Optional: remove the default "press ESC to exit full screen" message
    // primaryStage.setResizable(false); 

    // Set the scene and show the stage
    primaryStage.setScene(welcomeScene);
    primaryStage.setTitle("Self Check Kiosk by Clarke");
    primaryStage.show();

    // Stop timer globally when application is closed,
    // so application wont run even after closing the window
    primaryStage.setOnCloseRequest(e -> InactivityTimer.getInstance().stopTimer());
  }

  /**
   * To run the app.
   *
   * @param args Input
   */
  public static void main(String[] args) {
    launch(args);
  }
}