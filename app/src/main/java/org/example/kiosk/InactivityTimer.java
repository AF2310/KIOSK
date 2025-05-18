package org.example.kiosk;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.buttons.MidButton;

/**
 * This is the timer that starts counting after a
 * certain amount of inactivity on the terminal.
 * It will also reset the terminal if nothing is
 * clicked during an activity check.
 */
public class InactivityTimer {

  private static InactivityTimer instance;
  private Timer timer = new Timer();
  private Stage primaryStage;
  private Scene welcomeScene;
  private boolean isActive = false;

  /**
   * The timer constructor.
   * No need for extra variables as this is a singleton.
   */
  private InactivityTimer() {}

  /**
   * Method to get the single instance of the
   * InactivityTimer.
   *
   * @return the InactivityTimer instance
   */
  public static InactivityTimer getInstance() {
    if (instance == null) {
      instance = new InactivityTimer();
    }
    return instance;
  }

  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void setWelcomeScene(Scene welcomeScene) {
    this.welcomeScene = welcomeScene;
  }

  /**
   * Start the timer.
   * Timer has sceduled delay in miliseconds, so we
   * convert to seconds -> 30 * 1000 = 30 seconds.
   * After time has passed, inactivity popup will be
   * displayed (TimerTask).
   */
  public void startTimer() {
    // Timer only active in Scenes that aren't the WelcomeScreen
    // and timer cannot be already active
    System.out.println("DEBUG_SCENE: " + primaryStage.getScene());
    System.out.println("DEBUG_EQUALS: " + primaryStage.getScene().equals(welcomeScene));

    if (!isActive && !(primaryStage.getScene().equals(welcomeScene))) {
      timer = new Timer();
      timer.schedule(displayInactivityPopup(), 30 * 1000);
      System.out.println("DEBUG_0: TIMER ACTIVE");
      isActive = true;
    }
  }

  /**
   * The timer task is an inactivity popup, asking
   * 'Are you still there?'.
   * Uses private helper methods for popup.
   *
   * @return TimerTask containing popup
   */
  private TimerTask displayInactivityPopup() {
    return new TimerTask() {
      public void run() {
        inactivityPopup();
      }
    };
  }

  /**
   * Helper method for displayInactivityPopup().
   * This method contains the inactivity popup itself.
   */
  private void inactivityPopup() {

    // Run action after time; needed to prevent crashes
    Platform.runLater(() -> {

      // Make the popup
      Stage popup = new Stage();

      // Set stage the popup will go over
      popup.initOwner(primaryStage);

      // User cannot interact with anything else until popup is gone
      popup.initModality(Modality.APPLICATION_MODAL);

      // No title section for popup
      popup.initStyle(StageStyle.UNDECORATED);

      // Label of the popup
      Label label = new Label("Are you still there?");

      // Button for confirmation that user is still active
      MidButton button = new MidButton("Yes", "green", 40);

      // User confirms he's still active
      // -> popup gets removed and timer resets
      button.setOnAction(e -> {
        popup.close();
        resetTimer();
      });

      // Layout and style of popup elements
      VBox layout = new VBox(20, label, button);
      layout.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: white;");

      // Set finished scene in popup
      Scene scene = new Scene(layout, 300, 150);
      popup.setScene(scene);
      popup.show();
    });
  }

  /**
   * Method to reset the inactivity timer and re-start
   * the timer.
   */
  public void resetTimer() {
    System.out.println("DEBUG_SCENE: " + primaryStage.getScene());
    System.out.println("DEBUG_EQUALS: " + primaryStage.getScene().equals(welcomeScene));

    // Skip if timer wasn't even active to prevent unwanted timer start
    if (!isActive) {
      System.out.println("DEBUG: Ignored reset, timer not active.");
      return;
    }

    // Timer only active in Scenes that aren't the WelcomeScreen
    if (!(primaryStage.getScene().equals(welcomeScene))) {
      // Terminate the timer
      timer.cancel();
      isActive = false;
      System.out.println("DEBUG_0: TIMER RESET");
  
      // Make a new timer and start it
      startTimer();
    }
  }

  /**
   * Method to stop the inactivity timer completely.
   */
  public void stopTimer() {
    System.out.println("TIMER STATUS BEFORE STOP: " + isActive);
    System.out.println("Current Scene: " + primaryStage.getScene());
    if (timer != null) {
      timer.cancel();
      timer = null;
      System.out.println("DEBUG_0: TIMER STOP");
    }
    isActive = false;
  }
}
