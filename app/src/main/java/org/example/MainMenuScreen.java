package org.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The main menu screen.
 */
public class MainMenuScreen {

  /**
   * The main menu scene.
   */
  public Scene createMainMenuScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene) {
    // Initialize the main menu layout (VBox)
    VBox mainMenuLayout = new VBox(20);
    mainMenuLayout.setAlignment(Pos.CENTER);

    // Create the menu label
    Label mainMenuLabel = new Label("This is the Main Menu");

    // Style the label
    mainMenuLabel.setStyle(
        "-fx-background-color: transparent;"
        + "-fx-text-fill: black;"
        + "-fx-font-weight: bolder;"
        + "-fx-font-size: 60;"
        + "-fx-background-radius: 10;");

    // Create the back button
    var backButton = new MidButtonWithImage(
        "Back",
        "/back.png",
        "rgb(255, 255, 255)");

    // Set action for back button (to go back to the welcome screen)
    backButton.setOnAction(e -> {
      primaryStage.setScene(welcomeScrScene);
    });

    // Add the label and the back button to the layout
    mainMenuLayout.getChildren().addAll(mainMenuLabel, backButton);

    // Wrap in a stackpane
    StackPane mainPane = new StackPane(mainMenuLayout);
    mainPane.setPrefSize(windowWidth, windowHeight);

    // Create the main menu scene and go there
    Scene mainMenuScene = new Scene(mainPane, windowWidth, windowHeight);

    return mainMenuScene;
  }
}
