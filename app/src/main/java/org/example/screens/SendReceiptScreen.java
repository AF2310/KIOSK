package org.example.screens;

import java.sql.SQLException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.EmailSender;
import org.example.animations.FadingAnimation;
import org.example.boxes.CustomKeyboard;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButton;
import org.example.buttons.MidButtonWithImage;
import org.example.buttons.TitleLabel;
// import org.example.kiosk.InactivityTimer;
import org.example.kiosk.LanguageSetting;
// import org.example.orders.Cart;

/**
 * Screen for sending a receipt via email.
 */
public class SendReceiptScreen {

  /**
   * Creates the send receipt screen.
   */
  public CustomScene createSendReceiptScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene backScene,
      int orderId,
      String subject,
      String messageBody) {

    VBox layout = new VBox(20);
    layout.setAlignment(Pos.CENTER);

    TextField emailField = new TextField();
    emailField.setMaxWidth(800);
    emailField.setPromptText("Enter your email address");
    emailField.setStyle(
        "-fx-background-color: grey;"
            + "-fx-text-fill: black;"
            + "-fx-font-weight: lighter;"
            + "-fx-font-size: 60;"
            + "-fx-background-radius: 10;");

    Label feedbackLabel = new Label();
    feedbackLabel.setStyle("-fx-font-size: 30; -fx-text-fill: red;");
    feedbackLabel.setVisible(false);

    MidButton sendButton = new MidButton("Send", "rgb(0, 0, 0)", 40);
    MidButtonWithImage noThanksButton = new MidButtonWithImage(
        "No Thanks",
        "/back.png",
        "rgb(255, 255, 255)");

    HBox buttonBox = new HBox(20, sendButton, noThanksButton);
    buttonBox.setAlignment(Pos.CENTER);

    // Custom keyboard for username and password fields
    CustomKeyboard keyboard = new CustomKeyboard(primaryStage, emailField);

    // Keyboard functionality for emailfield
    emailField.setOnMouseClicked(e -> {
      emailField.requestFocus();
      keyboard.setTargetInput(emailField);
      keyboard.show();
    });

    // Send button action
    sendButton.setOnAction(e -> {
      String email = emailField.getText().trim();

      if (email.isEmpty()) {
        feedbackLabel.setText("Please enter an email address.");
        feedbackLabel.setStyle("-fx-text-fill: red; -fx-font-size: 20;");
        feedbackLabel.setVisible(true);
        return;
      }

      // Basic email format check
      if (!email.contains("@") || !email.contains(".")) {
        feedbackLabel.setText("Invalid email format.");
        feedbackLabel.setStyle("-fx-text-fill: red; -fx-font-size: 20;");
        feedbackLabel.setVisible(true);
        return;
      }

      try {
        EmailSender sender = new EmailSender();
        sender.sendMail(email, subject, messageBody);

        feedbackLabel.setText("Receipt sent successfully!");
        feedbackLabel.setStyle("-fx-text-fill: green; -fx-font-size: 20;");
        feedbackLabel.setVisible(true);
        // Clear field after sending
        emailField.clear();

        // Add a delay before moving to confirmation screen
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
          goToOrderConfirmation(primaryStage, windowWidth, windowHeight, orderId, keyboard);
        });
        pause.play();

      } catch (Exception ex) {
        feedbackLabel.setText("Failed to send receipt. Try again.");
        feedbackLabel.setStyle("-fx-text-fill: red; -fx-font-size: 20;");
        feedbackLabel.setVisible(true);
        ex.printStackTrace();
      }
    });

    // No thanks button action
    noThanksButton.setOnAction(e -> {
      goToOrderConfirmation(primaryStage, windowWidth, windowHeight, orderId, keyboard);
    });

    var promptLabel = new TitleLabel("Would you like a receipt emailed to you?");
    StackPane.setAlignment(promptLabel, Pos.TOP_CENTER);
    StackPane.setMargin(promptLabel, new Insets(30, 0, 0, 0));

    layout.getChildren().addAll(emailField, buttonBox, feedbackLabel);

    // Language button
    var langButton = new LangBtn();
    StackPane.setAlignment(langButton, Pos.BOTTOM_LEFT);
    StackPane.setMargin(langButton, new Insets(0, 0, 30, 30));

    langButton.addAction(event -> {
      LanguageSetting lang = LanguageSetting.getInstance();
      String newLang;
      if (lang.getSelectedLanguage().equals("en")) {
        newLang = "sv";
      } else {
        newLang = "en";
      }
      lang.changeLanguage(newLang);
      lang.smartTranslate(layout);
    });

    // Put everything into a StackPane
    StackPane mainPane = new StackPane(promptLabel, layout, langButton);
    mainPane.setPrefSize(windowWidth, windowHeight);

    // Register for translation
    LanguageSetting.getInstance().registerRoot(mainPane);
    LanguageSetting.getInstance().smartTranslate(mainPane);

    // Set focus of emailField when the scene is shown
    Platform.runLater(() -> promptLabel.requestFocus());

    return new CustomScene(mainPane, windowWidth, windowHeight);
  }

  private void goToOrderConfirmation(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      int orderId,
      CustomKeyboard keyboard) {

    // Create order confirmation screen
    OrderConfirmationScreen ordConfirmation = new OrderConfirmationScreen();
    CustomScene welcomeScrScene = null;
    try {
      welcomeScrScene = new WelcomeScreen().createWelcomeScreen(
          primaryStage,
          windowWidth,
          windowHeight);
    } catch (SQLException e1) {
      e1.printStackTrace();
    }

    Scene ordConfirmScene = ordConfirmation.createOrderConfirmationScreen(
        primaryStage,
        windowWidth,
        windowHeight,
        welcomeScrScene,
        orderId);

    primaryStage.setScene(ordConfirmScene);

    // Creating fading animation
    // to transition smoothly to welcome screen after order confirmation
    FadingAnimation fadingAnimation = new FadingAnimation(primaryStage);
    // Fading out of current scene
    fadingAnimation.fadeOutAnimation(
        "white",
        ordConfirmScene,
        welcomeScrScene);

    keyboard.close();

    // // Clear cart and stop timer after order has been done
    // InactivityTimer.getInstance().stopTimer();
    // Cart.getInstance().clearCart();
  }

}
