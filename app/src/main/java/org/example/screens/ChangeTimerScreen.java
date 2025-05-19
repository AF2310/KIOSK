package org.example.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.example.kiosk.InactivityTimer;
// import org.example.kiosk.LanguageSetting;
import org.example.menu.Product;

/**
 * This is the timer editor scene.
 * Used in the admin menu in 'AdminMenuScreen.java'.
 */
public class ChangeTimerScreen {

  // private LanguageSetting languageSetting = new LanguageSetting();

  private Stage primaryStage;
  private Scene prevScene;
  private TableView<Product> productTable;

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


    // VBox LEFT

    // Label for kiosk timer
    Label kioskTimerTitle = new Label("Kiosk Timer:");
    kioskTimerTitle.setStyle(
      "-fx-font-size: 30px;"
      + "-fx-font-weight: bold;");

    // Current kiosk timer value
    Label currentKioskTimer = new Label(
      "Current inactivity timer (kiosk): "
      + InactivityTimer.getInstance().getInactivityTimer()
      + "seconds"
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

        // Set new inactivity timer
        InactivityTimer.getInstance().setNewInactivityTimer(newValue);

        // Update display label
        currentKioskTimer.setText(
          "Current inactivity timer (kiosk): "
          + newValue + " seconds");

        updateFeedbackL.setText("Timer updated successfully!");

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
    timerShop.setAlignment(Pos.TOP_LEFT);


    // VBOX RIGHT

    // Label for kiosk timer
    Label popupTimerTitle = new Label("Kiosk Timer:");
    popupTimerTitle.setStyle(
      "-fx-font-size: 30px;"
      + "-fx-font-weight: bold;");

    // Current kiosk timer value
    Label currentPopupTimer = new Label(
      "Current inactivity timer (kiosk): "
      + InactivityTimer.getInstance().getInactivityTimerPopup()
      + "seconds"
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

        // Set new inactivity timer
        InactivityTimer.getInstance().setNewInactivityTimerPopup(newValue);

        // Update display label
        currentPopupTimer.setText(
          "Current inactivity timer (kiosk): "
          + newValue + " seconds");

        updateFeedbackR.setText("Timer updated successfully!");

      } catch (NumberFormatException ex) {
          updateFeedbackR.setText("Invalid input! Please enter a number.");
      }
    });

    // Combine left timer editor
    VBox timerPopup = new VBox(
      15,
      popupTimerTitle,
      currentPopupTimer,
      popupTimerInput,
      updateButtonPopup,
      updateFeedbackR
    );
    timerPopup.setAlignment(Pos.TOP_RIGHT);

    HBox timerEditor = new HBox(500, timerShop, timerPopup);
    timerEditor.setAlignment(Pos.CENTER);

    // Language + Cancel Buttons HBOX BOTTOM
    
    // VBOX main layout COMBINE ALL
    // timer editor box
    // confirmation text BOX
    // language + cancel box

    // Spacer for title and timer editors
    // TODO make spacer work
    //Region spacerTop = new Region();
    //HBox.setHgrow(spacerTop, Priority.ALWAYS);

    //VBox topLayout = new VBox(windowTitle, spacerTop, timerEditor);

    // Setting positioning of all the elements
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    // layout.setTop(topLayout)
    layout.setTop(windowTitle);
    //layout.setCenter(timerPopup);
    layout.setCenter(timerEditor);  //-> later when right part is done and combined
    //layout.setBottom(bottomContainer);

    return new Scene(layout, 1920, 1080);
  }
}
