package org.example;

import org.example.users.Admin;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMenuScreen {
  public Scene createAdminMenuScreen(
    Stage primaryStage,
    double windowWidth,
    double windowHeight,
    Scene welcomeScrScene) {
  //the mainlayout
  VBox adminMenuLayout = new VBox(20);
  adminMenuLayout.setAlignment(Pos.CENTER);

  Label AdminMenuText = new Label("Admin Menu");
  
  adminMenuLayout.getChildren().addAll(AdminMenuText);
  StackPane mainPane = new StackPane(adminMenuLayout);
  mainPane.setPrefSize(windowWidth, windowHeight);

  Scene adminMenuScene = new Scene(mainPane, windowWidth, windowHeight);

  return adminMenuScene;
  }
}

