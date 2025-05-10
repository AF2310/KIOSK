package org.example.screens;

import java.sql.SQLException;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButtonWithImage;
import org.example.sql.SqlConnectionCheck;

/**
 * The welcome screen class.
 */
public class WelcomeScreen {

  /**
   * The welcome class scene.
   *
   * @throws SQLException if the server has issues
   */
  public Scene createWelcomeScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight) throws SQLException {

    // Initialize the welcome screen elements
    var mainWindow = new VBox();
    mainWindow.setAlignment(Pos.CENTER);

    // Set background color and size
    // mainWindow.setStyle("-fx-background-color: grey;");
    // mainWindow.setPrefSize(windowWidth, windowHeight);

    // Setup labels
    var welcome = new Label("Welcome to");
    var companyTitle = new Label("Bun & Patty");

    // Customize labels
    welcome.setStyle(
        "-fx-background-color: transparent;"
            + "-fx-text-fill: black;"
            + "-fx-font-weight: lighter;"
            + "-fx-font-size: 100;"
            + "-fx-background-radius: 10;");

    companyTitle.setStyle(
        "-fx-background-color: transparent;"
            + "-fx-text-fill: black;"
            + "-fx-font-weight: bolder;"
            + "-fx-font-size: 160;"
            + "-fx-background-radius: 10;");

    // HBox for Burger images
    var rowOfBurgers = new HBox(300);
    rowOfBurgers.setAlignment(Pos.CENTER);

    // Setup side images
    // Image burger3 = new Image(getClass().getResourceAsStream("/burger3.png"));

    // Create a button with the burger image as its graphic
    Button burgerButton = new Button();
    Image burger3 = new Image(getClass().getResourceAsStream("/burger3.png"));
    ImageView burgerView3 = new ImageView(burger3);
    burgerView3.setScaleX(0.5);
    burgerView3.setScaleY(0.5);
    burgerButton.setGraphic(burgerView3);
    burgerButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    Image burger1 = new Image(getClass().getResourceAsStream("/burger1.png"));

    // Set the button action
    burgerButton.setOnAction(e -> {
      System.out.println("Right burger clicked!");
      // You can handle the click action here, such as navigating to another scene
    });

    // Add the button to the HBox
    // rowOfBurgers.getChildren().addAll(burgerView1, burgerView2, burgerButton);

    ImageView burgerView1 = new ImageView(burger1);
    var burgerContainer1 = new HBox(burgerView1);
    burgerContainer1.setAlignment(Pos.BASELINE_LEFT);
    // ImageView burgerView3 = new ImageView(burger3);
    var burgerContainer3 = new HBox(burgerView3);
    burgerContainer3.setAlignment(Pos.BASELINE_RIGHT);

    // Make size 50% smaller on all axis of Burger1+3
    burgerView1.setScaleX(0.5);
    burgerView1.setScaleY(0.5);
    burgerView3.setScaleX(0.5);
    burgerView3.setScaleY(0.5);

    // Setup buttons
    var rowOfButtons = new HBox(20);
    rowOfButtons.setPadding(new Insets(30));
    rowOfButtons.setAlignment(Pos.CENTER);

    var eatHereBtn = new MidButtonWithImage(
        "Eat Here",
        "/eatHere.png",
        "rgb(0, 0, 0)");

    var takeAwayBtn = new MidButtonWithImage(
        "Takeaway",
        "/takeaway.png",
        "rgb(255, 255, 255)");

    rowOfButtons.getChildren().addAll(eatHereBtn, takeAwayBtn);

    // Add centre image
    Image burger2 = new Image(getClass().getResourceAsStream("/burger2.png"));
    ImageView burgerView2 = new ImageView(burger2);
    rowOfBurgers.getChildren().addAll(burgerView1, burgerView2, burgerButton);
    
    // Test sql connection
    SqlConnectionCheck connectionCheck = new SqlConnectionCheck();
    Label mysql = connectionCheck.getMysqlLabel();

    var langButton = new LangBtn();

    // Just pass in the Labeled components to translate
    langButton.addAction(event -> {
      langButton.updateLanguage(List.of(
          welcome,
          companyTitle,
          eatHereBtn.getButtonLabel(),
          takeAwayBtn.getButtonLabel(),
          mysql
      ));
    });

    // Position the language button in the bottom-left corner
    StackPane.setAlignment(langButton, Pos.BOTTOM_LEFT);
    StackPane.setMargin(langButton, new Insets(0, 0, 30, 30));

    mainWindow.getChildren().addAll(
        welcome, companyTitle, rowOfBurgers, rowOfButtons, mysql);

    // Put everythng in a stackpane
    StackPane mainPane = new StackPane(mainWindow, langButton);
    mainPane.setPrefSize(windowWidth, windowHeight);

    Scene scene = new Scene(mainPane, windowWidth, windowHeight);

    // Set up action for eat here
    eatHereBtn.setOnAction(e -> {
      try {
        MainMenuScreen mainMenuScreen = new MainMenuScreen();
        Scene mainMenuScene = mainMenuScreen.createMainMenuScreen(
            primaryStage,
            windowWidth,
            windowHeight,
            scene,
            0,
            "eatHere");
        primaryStage.setScene(mainMenuScene);
      } catch (SQLException ex) {
        ex.printStackTrace();
      }

    });

    // Set up action for takeaway
    takeAwayBtn.setOnAction(e -> {
      try {
        MainMenuScreen mainMenuScreen = new MainMenuScreen();
        Scene mainMenuScene = mainMenuScreen.createMainMenuScreen(
            primaryStage,
            windowWidth,
            windowHeight,
            scene,
            0,
            "takeaway");
        primaryStage.setScene(mainMenuScene);
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    });

    AdminLoginScreen adminLoginScreen = new AdminLoginScreen();
    Scene adminMenuScene = adminLoginScreen.createAdminLoginScreen(primaryStage,
        windowWidth, windowHeight, scene);

    // Temporary Button to get to the admin menu
    burgerButton.setOnAction(e -> {
      primaryStage.setScene(adminMenuScene);
    });

    return scene;
  }
}
