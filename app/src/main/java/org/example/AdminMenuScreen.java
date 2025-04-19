package org.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMenuScreen {
  /*
   * The admin scene
   */
  public Scene createAdminScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene) {
    //the mainlayout
    VBox adminMenuLayout = new VBox(20);
    adminMenuLayout.setAlignment(Pos.CENTER);

    //the field to enter the username
    TextField usernameField = new TextField();
    usernameField.setMaxSize(460, 140);
    usernameField.setPromptText("Username");
    usernameField.setStyle(
      "-fx-background-color: grey;"
      + "-fx-text-fill: black;"
      + "-fx-font-weight: lighter;"
      + "-fx-font-size: 50;"
      + "-fx-background-radius: 10;"
    );
      
    //the field to enter the password
    TextField passwordField = new TextField();
    passwordField.setMaxSize(460, 140);
    passwordField.setPromptText("Password");
    passwordField.setStyle(
      "-fx-background-color: grey;"
      + "-fx-text-fill: black;"
      + "-fx-font-weight: lighter;"
      + "-fx-font-size: 50;"
      + "-fx-background-radius: 10;"
    );

    var loginButton = new MidButtonWithImage(
      "Login",
      "/back.png",
      "rgb(255, 255, 255)");

    //back button
    var backButton = new MidButtonWithImage(
      "Back",
      "/back.png",
      "rgb(255, 255, 255)");
  
    // Set action for back button (to go back to the welcome screen)
    backButton.setOnAction(e -> {
      primaryStage.setScene(welcomeScrScene);
    });
    
    adminMenuLayout.getChildren().addAll(usernameField, passwordField, loginButton, backButton);

    //put everything into a stackpane
    StackPane mainPane = new StackPane(adminMenuLayout);
    mainPane.setPrefSize(windowWidth, windowHeight);

    // Create the main menu scene and go there
    Scene adminMenuScene = new Scene(mainPane, windowWidth, windowHeight);

    return adminMenuScene;
  }
}
