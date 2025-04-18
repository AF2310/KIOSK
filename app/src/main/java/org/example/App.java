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

// package org.example;

// import javafx.animation.ScaleTransition;
// import javafx.application.Application;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.StackPane;
// import javafx.scene.layout.VBox;
// import javafx.stage.Stage;
// import javafx.util.Duration;

// /**
// * The main app class.
// */
// public class App extends Application {
// @Override
// public void start(Stage primaryStage) throws Exception {

// // Screen demensions
// double windowWidth = 1920;
// double windowHeight = 1080;

// // Initialize the welcome screen elements
// var mainWindow = new VBox();
// mainWindow.setAlignment(Pos.CENTER);

// // !! THIS LINE IS TO TEST THE SCENE LAYOUT. USE: white/grey
// // TO SEE THE OUTLINE OF THE SCENE
// mainWindow.setStyle("-fx-background-color: grey;");
// mainWindow.setPrefSize(windowWidth, windowHeight);

// // Setup labels
// var welcome = new Label("Welcome to");
// var companyTitle = new Label("Bun & Patty");

// // Customize labels
// welcome.setStyle(
// "-fx-background-color: transparent;"
// + "-fx-text-fill: black;"
// + "-fx-font-weight: lighter;"
// + "-fx-font-size: 100;"
// + "-fx-background-radius: 10;");

// companyTitle.setStyle(
// "-fx-background-color: transparent;"
// + "-fx-text-fill: black;"
// + "-fx-font-weight: bolder;"
// + "-fx-font-size: 160;"
// + "-fx-background-radius: 10;");

// // HStack for Burger images
// var rowOfBurgers = new HBox(220);
// rowOfBurgers.setAlignment(Pos.CENTER);

// // Setup side images
// Image burger1 = new Image(getClass().getResourceAsStream("/burger1.png"));
// Image burger3 = new Image(getClass().getResourceAsStream("/burger3.png"));

// ImageView burgerView1 = new ImageView(burger1);
// var burgerContainer1 = new HBox(burgerView1);
// burgerContainer1.setAlignment(Pos.BASELINE_LEFT);
// ImageView burgerView3 = new ImageView(burger3);
// var burgerContainer3 = new HBox(burgerView3);
// burgerContainer3.setAlignment(Pos.BASELINE_RIGHT);

// // Make size 50% smaller on all axis of Burger1+3
// burgerView1.setScaleX(0.5);
// burgerView1.setScaleY(0.5);
// burgerView3.setScaleX(0.5);
// burgerView3.setScaleY(0.5);

// // Setup buttons
// var rowOfButtons = new HBox(20);
// rowOfButtons.setPadding(new Insets(30));
// rowOfButtons.setAlignment(Pos.CENTER);

// var eatHereBtn = new MidButtonWithImage(
// "Eat Here",
// "/eatHere.png",
// "rgb(0, 0, 0)");

// var takeAwayBtn = new MidButtonWithImage(
// "Takeaway",
// "/takeaway.png",
// "rgb(255, 255, 255)");

// rowOfButtons.getChildren().addAll(eatHereBtn, takeAwayBtn);

// // Add centre image
// Image burger2 = new Image(getClass().getResourceAsStream("/burger2.png"));
// ImageView burgerView2 = new ImageView(burger2);
// rowOfBurgers.getChildren().addAll(burgerView1, burgerView2, burgerView3);

// // Test sql connection
// // Create an instance of SqlConnectionCheck
// SqlConnectionCheck connectionCheck = new SqlConnectionCheck();

// // Retrieve the mysql label using the getter
// Label mysql = connectionCheck.getMysqlLabel();

// mainWindow.getChildren().addAll(
// welcome, companyTitle, rowOfBurgers, rowOfButtons, mysql);

// // --- Wrap in StackPane for centered scaling ---
// StackPane mainPane = new StackPane(mainWindow);
// mainPane.setPrefSize(windowWidth, windowHeight);

// Scene scene = new Scene(mainPane, windowWidth, windowHeight);
// primaryStage.setScene(scene);
// primaryStage.setTitle("Self Check Kiosk by Clarke");
// primaryStage.show();

// // --- Lock aspect ratio (16:9) ---
// final double aspect_ratio = 1920.0 / 1080.0;
// final boolean[] resizing = {false}; // To prevent infinite loop

// primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
// if (!resizing[0]) {
// resizing[0] = true;
// double newWidth = newVal.doubleValue();
// double newHeight = newWidth / aspect_ratio;
// primaryStage.setHeight(newHeight);
// resizing[0] = false;
// }
// });

// // Create second screen content
// VBox secondScreen = new VBox(20);
// secondScreen.setAlignment(Pos.CENTER);
// secondScreen.setStyle("-fx-background-color: lightyellow;");

// Label secondLabel = new Label("You're on the Eat Here screen!");
// secondLabel.setStyle("-fx-font-size: 40;");

// Button backButton = new Button("Back");
// backButton.setStyle("-fx-font-size: 20;");

// // Define second scene
// Scene secondScene = new Scene(secondScreen, windowWidth, windowHeight);
// secondScreen.getChildren().addAll(secondLabel, backButton);

// // Go to second screen on button click
// eatHereBtn.setOnAction(e -> {
// primaryStage.setScene(secondScene);
// });

// // Back button returns to main scene
// backButton.setOnAction(e -> {
// primaryStage.setScene(scene); // 'scene' is your original scene
// });

// scene.heightProperty().addListener((obs, oldVal, newVal) -> {
// double newScaleX = scene.getWidth() / windowWidth;
// double newScaleY = newVal.doubleValue() / windowHeight;
// double newScale = Math.min(newScaleX, newScaleY);
// mainWindow.setScaleX(newScale);
// mainWindow.setScaleY(newScale);

// ScaleTransition st = new ScaleTransition(Duration.millis(200), mainWindow);
// st.setToX(newScale);
// st.setToY(newScale);
// st.play();
// });

// }

// public static void main(String[] args) {
// launch(args);
// }
// }
