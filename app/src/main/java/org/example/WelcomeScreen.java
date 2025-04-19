package org.example;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The welcome screen class.
 */
public class WelcomeScreen {

  /**
   * The welcome class scene.
   */
  public Scene createWelcomeScreen(Stage primaryStage, double windowWidth, double windowHeight) {
    
    // Initialize the welcome screen elements
    var mainWindow = new VBox();
    mainWindow.setAlignment(Pos.CENTER);

    // Set background color and size
    mainWindow.setStyle("-fx-background-color: grey;");
    mainWindow.setPrefSize(windowWidth, windowHeight);

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
    var rowOfBurgers = new HBox(220);
    rowOfBurgers.setAlignment(Pos.CENTER);

    // Setup side images
    Image burger1 = new Image(getClass().getResourceAsStream("/burger1.png"));
    Image burger3 = new Image(getClass().getResourceAsStream("/burger3.png"));

    ImageView burgerView1 = new ImageView(burger1);
    var burgerContainer1 = new HBox(burgerView1);
    burgerContainer1.setAlignment(Pos.BASELINE_LEFT);
    ImageView burgerView3 = new ImageView(burger3);
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
    rowOfBurgers.getChildren().addAll(burgerView1, burgerView2, burgerView3);

    // Test sql connection
    SqlConnectionCheck connectionCheck = new SqlConnectionCheck();
    Label mysql = connectionCheck.getMysqlLabel();

    mainWindow.getChildren().addAll(
        welcome, companyTitle, rowOfBurgers, rowOfButtons, mysql);

    // Put everythng in a stackpane
    StackPane mainPane = new StackPane(mainWindow);
    mainPane.setPrefSize(windowWidth, windowHeight);

    Scene scene = new Scene(mainPane, windowWidth, windowHeight);

    // Fix aspect ratio
    final double aspect_ratio = 1920.0 / 1080.0;
    final boolean[] resizing = {false};

    // Adjust width on the go
    primaryStage.widthProperty().addListener((observer, givenValue, upddValue) -> {
      if (!resizing[0]) {
        resizing[0] = true;
        double newWidth = upddValue.doubleValue();
        double newHeight = newWidth / aspect_ratio;
        primaryStage.setHeight(newHeight);
        resizing[0] = false;
      }
    });

    // Adjust height on the go
    scene.heightProperty().addListener((oberver, oldVal, updtValue) -> {
      double newWidth = scene.getWidth() / windowWidth;
      double newHeight = updtValue.doubleValue() / windowHeight;
      double newScale = Math.min(newWidth, newHeight);
      mainWindow.setScaleX(newScale);
      mainWindow.setScaleY(newScale);

      ScaleTransition st = new ScaleTransition(Duration.millis(200), mainWindow);
      st.setToX(newScale);
      st.setToY(newScale);
      st.play();
    });

    // Set the main menu
    MainMenuScreen mainMenuScreen = new MainMenuScreen();
    Scene mainMenuScene = mainMenuScreen.createMainMenuScreen(
        primaryStage,
        windowWidth,
        windowHeight,
        scene
      );

    // Set up action for eat here
    eatHereBtn.setOnAction(e -> {
      primaryStage.setScene(mainMenuScene);
    });

    AdminLoginScreen adminLoginScreen = new AdminLoginScreen();
    Scene adminMenuScene = adminLoginScreen.createAdminLoginScreen(primaryStage, windowWidth, windowHeight, scene);
    //Temporary Button to get to the admin menu
    takeAwayBtn.setOnAction(e -> {
      primaryStage.setScene(adminMenuScene);
    });

    return scene;
  }
}
