package org.example.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.kiosk.InactivityTimer;
// import org.example.kiosk.LanguageSetting;

/**
 * This is the timer editor scene.
 * Used in the admin menu in 'AdminMenuScreen.java'.
 */
public class ChangeTimerScreen {

  // private LanguageSetting languageSetting = new LanguageSetting();

  private Stage primaryStage;
  private Scene prevScene;

  /**
   * The timer editor scene constructor.
   * ONLY used in the admin menu in 'AdminMenuScreen.java'.
   *
   * @param primaryStage the primary stage for the scenes
   * @param prevScene    the scene you were previously in
   */
  public ChangeTimerScreen(
      Stage primaryStage,
      Scene prevScene) {

    this.primaryStage = primaryStage;
    this.prevScene = prevScene;
  }

  /**
   * This is the method to create the scene for changing
   * the inactivity timers in the main menu for customers.
   *
   * @return change timer scene
   */
  public Scene getChangeTimerScene() {

    // Label for screen title
    Label windowTitle = new Label("Timer Editor:");
    windowTitle.setStyle(
      "-fx-font-size: 45px;"
      + "-fx-font-weight: bold;");
    windowTitle.setAlignment(Pos.TOP_CENTER);

    HBox titleBox = new HBox(windowTitle);
    titleBox.setAlignment(Pos. CENTER);
    titleBox.setPadding(new Insets(20, 0, 60, 0));


    // Timer editor kiosk - VBOX LEFT

    VBox timerShop = getEditorBox(true);

    /* // Label for kiosk timer
    Label kioskTimerTitle = new Label("Kiosk Timer:");
    kioskTimerTitle.setStyle(
      "-fx-font-size: 30px;"
      + "-fx-font-weight: bold;");

    // Current kiosk timer value
    Label currentKioskTimer = new Label(
      "Current inactivity timer (kiosk): "
      + InactivityTimer.getInstance().getInactivityTimer()
      + " seconds"
    );
    currentKioskTimer.setStyle("-fx-font-size: 20px;");

    // Input field to enter new value
    TextField kioskTimerInput = new TextField();
    kioskTimerInput.setPromptText("New timer value (in seconds)");
    kioskTimerInput.setMaxWidth(250);

    // Label for feedback after update
    Label updateFeedbackL = new Label();
    updateFeedbackL.setStyle("-fx-font-size: 18px;");
    
    // Button to submit new value
    Button updateButtonKiosk = new Button("Update Timer");

    updateButtonKiosk.setOnAction(e -> {
      String input = kioskTimerInput.getText();

      // Error handling of incorrect value input
      try {
        // If input is int value
        int newValue = Integer.parseInt(input);

        // If int value is not reasonable (realistic)
        if (newValue < 5) {
            updateFeedbackL.setText("Please enter a value >= 5 seconds.");
            // No value change
            return;
        }

        // User entered proper value -> execute value change

        // Set new inactivity timer
        InactivityTimer.getInstance().setNewInactivityTimer(newValue);

        // Update display label
        currentKioskTimer.setText(
          "Current inactivity timer (kiosk): "
          + newValue + " seconds");

        updateFeedbackL.setText("Timer updated successfully!");

        // User entered a letter -> no value change
      } catch (NumberFormatException ex) {
          updateFeedbackL.setText("Invalid input! Please enter a number.");
      }
    });

    // Combine left timer editor
    VBox timerShop = new VBox(
      15,
      kioskTimerTitle,
      currentKioskTimer,
      kioskTimerInput,
      updateButtonKiosk,
      updateFeedbackL
    );
    timerShop.setAlignment(Pos.TOP_LEFT); */


    // Timer editor popup - VBOX RIGHT

    VBox timerPopup = getEditorBox(false);

    /* // Label for kiosk timer
    Label popupTimerTitle = new Label("Popup Timer:");
    popupTimerTitle.setStyle(
      "-fx-font-size: 30px;"
      + "-fx-font-weight: bold;");

    // Current kiosk timer value
    Label currentPopupTimer = new Label(
      "Current inactivity timer (kiosk): "
      + InactivityTimer.getInstance().getInactivityTimerPopup()
      + " seconds"
    );
    currentPopupTimer.setStyle("-fx-font-size: 20px;");

    // Input field to enter new value
    TextField popupTimerInput = new TextField();
    popupTimerInput.setPromptText("New timer value (in seconds)");
    popupTimerInput.setMaxWidth(250);

    // Label for feedback after update
    Label updateFeedbackR = new Label();
    updateFeedbackR.setStyle("-fx-font-size: 18px;");
    
    // Button to submit new value
    Button updateButtonPopup = new Button("Update Timer");

    updateButtonPopup.setOnAction(e -> {
      String input = popupTimerInput.getText();

      // Error handling of incorrect value input
      try {
        // If input is int value
        int newValue = Integer.parseInt(input);

        // If int value is not reasonable (realistic)
        if (newValue < 5) {
            updateFeedbackR.setText("Please enter a value >= 5 seconds.");
            // No value change
            return;
        }

        // User entered proper value -> execute value change

        // Set new inactivity timer
        InactivityTimer.getInstance().setNewInactivityTimerPopup(newValue);

        // Update display label
        currentPopupTimer.setText(
          "Current inactivity timer (popup): "
          + newValue + " seconds");

        updateFeedbackR.setText("Timer updated successfully!");

        // User entered a letter -> no value change
      } catch (NumberFormatException ex) {
          updateFeedbackR.setText("Invalid input! Please enter a number.");
      }
    });

    // Combine right timer editor
    VBox timerPopup = new VBox(
      15,
      popupTimerTitle,
      currentPopupTimer,
      popupTimerInput,
      updateButtonPopup,
      updateFeedbackR
    );
    timerPopup.setAlignment(Pos.TOP_RIGHT); */


    // Combine both timer editors

    HBox timerEditor = new HBox(400, timerShop, timerPopup);
    timerEditor.setAlignment(Pos.CENTER);


    // Language + Cancel Buttons HBOX BOTTOM

    // Back button
    // Clicking button means user goes to previous screen
    var backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> {
      primaryStage.setScene(prevScene);
    });

    // Language Button
    // cycles images on click
    var langButton = new LangBtn();

    // Spacer for Bottom Row
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);

    // Bottom row of the screen
    HBox bottomContainer = new HBox();
    bottomContainer.setAlignment(Pos.BOTTOM_LEFT);
    bottomContainer.getChildren().addAll(langButton, spacerBottom, backButton);


    // Setting positioning of all the elements
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setTop(titleBox);
    BorderPane.setMargin(timerEditor, new Insets(200, 0, 50, 0));
    layout.setCenter(timerEditor);
    
    layout.setBottom(bottomContainer);

    return new Scene(layout, 1920, 1080);
  }

  /**
   * Flexible helper method for the timer editor boxes.
   *
   * @param title title of the editor
   * @param isKioskTimer true if it's the kiosk timer
   *                     false it it's the popup timer
   * @return
   */
  private VBox getEditorBox(boolean isKioskTimer) {

    // Getting name depending on what kind of timer
    String name;
    if (isKioskTimer) name = "Kiosk ";
    else name = "Popup ";

    // Label for timer
    Label timerTitle = new Label(name + "Timer:");
    timerTitle.setStyle(
      "-fx-font-size: 30px;"
      + "-fx-font-weight: bold;");

    // Get current timer value depending on what kind of timer
    int currentTimerValue;
    if (isKioskTimer) {
      currentTimerValue = InactivityTimer.getInstance().getInactivityTimer();
    } else {
      currentTimerValue = InactivityTimer.getInstance().getInactivityTimerPopup();
    }

    // Current timer value
    Label currentTimer = new Label(
      "Current inactivity timer: " + currentTimerValue + " seconds"
    );
    currentTimer.setStyle("-fx-font-size: 20px;");

    // Input field to enter new value
    TextField timerInput = new TextField();
    timerInput.setPromptText("New timer value (in seconds)");
    timerInput.setMaxWidth(250);

    // Label for feedback after update
    Label consoleOutput = new Label();
    consoleOutput.setStyle("-fx-font-size: 18px;");
    
    // Button to submit new value
    Button updateButton = new Button("Update Timer");

    updateButton.setOnAction(e -> {
      String input = timerInput.getText();

      // Error handling of incorrect value input
      try {
        // If input is int value
        int newValue = Integer.parseInt(input);

        // If int value is not reasonable (realistic)
        if (newValue < 5) {
            consoleOutput.setText("Please enter a value >= 5 seconds.");
            // No value change
            return;
        }

        // User entered proper value -> execute value change

        // Set new inactivity timer depending on what kind of timer
        if (isKioskTimer) {
          InactivityTimer.getInstance().setNewInactivityTimer(newValue);
        } else {
          InactivityTimer.getInstance().setNewInactivityTimerPopup(newValue);
        }

        // Update display label
        currentTimer.setText(
          "Current inactivity timer: "
          + newValue + " seconds");

        consoleOutput.setText("Timer updated successfully!");

        // User entered a letter -> no value change
      } catch (NumberFormatException ex) {
          consoleOutput.setText("Invalid input! Please enter a number.");
      }
    });

    // Combine left timer editor
    VBox timer = new VBox(
      20,
      timerTitle,
      currentTimer,
      timerInput,
      updateButton,
      consoleOutput
    );
    timer.setAlignment(Pos.TOP_LEFT);

    return timer;
  }
}
