package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The main app class.
 */
public class App extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    
    // Initialize the welcome screen elements
    var mainWindow = new VBox();
    mainWindow.setPadding(new Insets(5));

    var rowOfButtons = new HBox();
    rowOfButtons.setPadding(new Insets(30));
    var eatHereBtn = new Button("Eat Here");
    var takeAwayBtn = new Button("Takeaway");

    // Setup the buttons
    eatHereBtn.setStyle(
        "-fx-background-color:rgb(0, 0, 0);"
        + "-fx-text-fill: white;"
        + "-fx-background-radius: 6;"
        + "-fx-padding: 10 20;"
    );

    takeAwayBtn.setStyle(
        "-fx-background-color:rgb(255, 255, 255);"
        + "-fx-text-fill: black;"
        + "-fx-background-radius: 6;"
        + "-fx-padding: 10 20;"
    );

    // Setup labels
    var welcome = new Label("Welcome to");
    var companyTitle = new Label("Bun & Patty");
    
    welcome.setStyle(
        "-fx-background-color: transparent;"
        + "-fx-text-fill: black;"
        + "-fx-font-weight: semibold;"
        + "-fx-font-size: 35;"
        + "-fx-padding: 5 10;"
        + "-fx-background-radius: 10;"
    );

    companyTitle.setStyle(
        "-fx-background-color: transparent;"
        + "-fx-text-fill: black;"
        + "-fx-font-weight: bold;"
        + "-fx-font-size: 60;"
        + "-fx-padding: 5 10;"
        + "-fx-background-radius: 10;"
    );

    // SQL Connection starts here
    Label mysql;

    try {
      Connection conn = DriverManager.getConnection(
          "jdbc:mysql://localhost/LordOfTheRings?user=python&password="
          + "$tarWar$&useSSL=false&allowPublicKeyRetrieval=true");
      mysql = new Label("Driver found and connected");

      conn.setAutoCommit(false);

    } catch (SQLException e) {
      mysql = new Label("An error has occurred: " + e.getMessage());

      mysql.setStyle(
          "-fx-background-color: transparent;"
          + "-fx-text-fill: black;"
          + "-fx-font-size: 10;"
          + "-fx-padding: 5 10;"
          + "-fx-background-radius: 10;"
      );
    }
    // End of SQL Section

    // Put the elements of the welcome screen together
    rowOfButtons.getChildren().addAll(eatHereBtn, takeAwayBtn);
    mainWindow.getChildren().addAll(welcome, companyTitle, rowOfButtons, mysql);

    // Render the welcome screen
    primaryStage.setScene(new Scene(mainWindow, 1920, 1080));
    primaryStage.setTitle("Self Check Kiosk by Clarke");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
