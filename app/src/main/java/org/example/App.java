package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
    //mainWindow.setAlignment(Pos.CENTER);

    // HStack buttons for dining choice
    var rowOfButtons = new HBox();
    rowOfButtons.setPadding(new Insets(30));
    rowOfButtons.setAlignment(Pos.CENTER);
    var eatHereBtn = new Button("Eat Here");
    var takeAwayBtn = new Button("Takeaway");

    // Setup the buttons
    eatHereBtn.setStyle(
        "-fx-background-color:rgb(0, 0, 0);"
        + "-fx-text-fill: white;"
        + "-fx-background-radius: 6;"
        + "-fx-padding: 10 20;"
    );

    // Customize button
    takeAwayBtn.setStyle(
        "-fx-background-color:rgb(255, 255, 255);"
        + "-fx-text-fill: black;"
        + "-fx-background-radius: 6;"
        + "-fx-padding: 10 20;"
    );

    // Setup labels
    var welcome = new Label("Welcome to");
    var companyTitle = new Label("Bun & Patty");
    
    // Customize labels
    welcome.setStyle(
        "-fx-background-color: transparent;"
        + "-fx-text-fill: black;"
        + "-fx-font-weight: bold;"
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

    // VStack for Burger images
    var rowOfBurgers = new HBox(500);
    rowOfBurgers.setPadding(new Insets(100));
    rowOfBurgers.setTranslateX(-250);

    // Setup images
    Image burger1 = new Image(getClass().getResourceAsStream("/burger1.png"));
    Image burger2 = new Image(getClass().getResourceAsStream("/burger2.png"));
    Image burger3 = new Image(getClass().getResourceAsStream("/burger3.png"));

    ImageView burgerView1 = new ImageView(burger1);
    ImageView burgerView2 = new ImageView(burger2);
    ImageView burgerView3 = new ImageView(burger3);

    // Make size 40% smaller on all axis of Burger1+3
    burgerView1.setScaleX(0.6);
    burgerView1.setScaleY(0.6);
    burgerView3.setScaleX(0.6);
    burgerView3.setScaleY(0.6);

    // Position Burger1+3 more up to make up for size decrease
    burgerView1.setTranslateY(30);
    burgerView3.setTranslateY(30);

    burgerView1.setFitWidth(400);
    burgerView2.setFitWidth(400);
    burgerView3.setFitWidth(400);

    burgerView1.setPreserveRatio(true);
    burgerView2.setPreserveRatio(true);
    burgerView3.setPreserveRatio(true);

    // SQL Connection starts here
    Label mysql;

    try {
      Connection conn = DriverManager.getConnection(
          "jdbc:mysql://localhost/LordOfTheRings?user=python&password="
          + "$tarWar$&useSSL=false&allowPublicKeyRetrieval=true");
      mysql = new Label("Driver found and connected");

      conn.setAutoCommit(false);

    // Errorhandling
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
    rowOfBurgers.getChildren().addAll(burgerView1, burgerView2, burgerView3);

    // Layout editor to force specific layouts for top and bottom of burgers
    Region topSpace = new Region();
    Region bottomSpace = new Region();
    VBox.setVgrow(topSpace, Priority.ALWAYS);
    VBox.setVgrow(bottomSpace, Priority.ALWAYS);
    
    mainWindow.getChildren().addAll(
      welcome, companyTitle, rowOfBurgers, rowOfButtons, mysql
      );

    // Render the welcome screen
    primaryStage.setScene(new Scene(mainWindow, 1920, 1080));
    primaryStage.setTitle("Self Check Kiosk by Clarke");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
