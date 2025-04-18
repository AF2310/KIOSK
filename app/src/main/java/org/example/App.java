package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    // Set the scene and show the stage
    primaryStage.setScene(welcomeScene);
    primaryStage.setTitle("Self Check Kiosk by Clarke");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}