package org.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Admin login screen.
 */
public class AdminLoginScreen {
  /**
   * Admin login screen.
   *
   * @param primaryStage The actual window itself.
   * @param windowWidth Size of window width
   * @param windowHeight Size of window height
   * @param welcomeScrScene Welcome screen scene
   * @return Returns the admin login screen
   */
  public Scene createAdminLoginScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene) {
    //the mainlayout
    VBox adminMenuLayout = new VBox(20);
    adminMenuLayout.setAlignment(Pos.CENTER);

    var adminMenuTitle = new Label();
    adminMenuTitle.setText("Admin Menu");
    adminMenuTitle.setStyle(
        "-fx-text-fill: black;"
        + "-fx-font-weight: lighter;"
        + "-fx-font-size: 50;"
        + "-fx-background-radius: 10;"
    );

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
    PasswordField passwordField = new PasswordField();
    passwordField.setMaxSize(460, 140);
    passwordField.setPromptText("Password");
    passwordField.setStyle(
        "-fx-background-color: grey;"
        + "-fx-text-fill: black;"
        + "-fx-font-weight: lighter;"
        + "-fx-font-size: 50;"
        + "-fx-background-radius: 10;"
    );
    
    Image errorIcon = new Image(getClass().getResourceAsStream("/errorLogin.png"));
    ImageView errorIconView = new ImageView(errorIcon);
    errorIconView.setFitWidth(40);
    errorIconView.setFitHeight(40);
    errorIconView.setPreserveRatio(true);

    var errorLabel = new Label();
    errorLabel.setStyle(
          "-fx-text-fill: black;"
        + "-fx-font-weight: lighter;"
        + "-fx-font-size: 50;"
        + "-fx-background-radius: 10;"
    );
    errorLabel.setGraphic(errorIconView);
    errorLabel.setGraphicTextGap(10);
    //Initially hidden
    errorLabel.setVisible(false);

    var loginButton = new MidButtonWithImage(
        "Login",
        "/back.png",
        "rgb(255, 255, 255)");    
    
    //login button functionality
    loginButton.setOnAction(e -> {
      String username = usernameField.getText();
      String password = passwordField.getText();
      SqlConnectionCheck connection = new SqlConnectionCheck();
      AdminAuth adminauth = new AdminAuth(connection.getConnection());
      Boolean checkLogin = adminauth.verifyAdmin(username, password);
      if (checkLogin) {
        AdminMenuScreen adminMenuScreen = new AdminMenuScreen();
        Scene adminMenuScene = adminMenuScreen.createAdminMenuScreen(primaryStage,
                       windowWidth, windowHeight, welcomeScrScene);
        primaryStage.setScene(adminMenuScene);
      } else { 
        // Shows error if a wrong username or password is entered
        errorLabel.setText("Invalid login details");
        errorLabel.setVisible(true);
        passwordField.clear();


      }
    });

    //back button
    var backButton = new MidButtonWithImage(
        "Back",
        "/back.png",
        "rgb(255, 255, 255)");   
  
    // Set action for back button (to go back to the welcome screen)
    backButton.setOnAction(e -> {
      primaryStage.setScene(welcomeScrScene);
    });
    
    adminMenuLayout.getChildren().addAll(adminMenuTitle, usernameField, passwordField, loginButton,
             backButton, errorLabel);


    //put everything into a stackpane
    StackPane mainPane = new StackPane(adminMenuLayout);
    mainPane.setPrefSize(windowWidth, windowHeight);
    

    // Create the admin login scene and go there
    Scene adminLoginScene = new Scene(mainPane, windowWidth, windowHeight);

    // Puts focus on the admin menu label so that the username field isn't by default focused
    adminMenuTitle.requestFocus(); 

    return adminLoginScene;
  }
}
