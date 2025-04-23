package org.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Admin menu class.
 */
public class AdminMenuScreen {

  /**
   * Admin menu screen.
   */
  public Scene createAdminMenuScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene) {
    //the mainlayout
    VBox adminMenuLayout = new VBox(20);
    adminMenuLayout.setAlignment(Pos.CENTER);

    Label adminMenuText = new Label("Admin Menu");
  
    adminMenuLayout.getChildren().addAll(adminMenuText);
    StackPane mainPane = new StackPane(adminMenuLayout);
    mainPane.setPrefSize(windowWidth, windowHeight);

    Scene adminMenuScene = new Scene(mainPane, windowWidth, windowHeight);

    return adminMenuScene;
  }
}

