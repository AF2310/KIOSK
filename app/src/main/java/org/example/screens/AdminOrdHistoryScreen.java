package org.example.screens;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.example.buttons.BackButton;
import org.example.sql.SqlConnectionCheck;

/**
 * Order History class.
 */
public class AdminOrdHistoryScreen {

  private Stage primaryStage;
  private Connection connection;

  /**
   * Scene to display the order history.
   *
   * @param primaryStage this window
   * @param prevScene pevious scene to go back to
   * @return the scene itself
   */
  public Scene showHistoryScene(Stage primaryStage, Scene prevScene) {

    this.primaryStage = primaryStage;


    // Back button
    BackButton backButton = new BackButton();
    // clicking button means user goes to previous screen
    backButton.setOnAction(e -> primaryStage.setScene(prevScene));

    Region bottomSpacer = new Region()


    HBox bottomContainer = new HBox();
    bottomContainer.getChildren().addAll(bottomSpacer, backButton);

    return new Scene(layout, 1920, 1080);
  }

}
