package org.example.kiosk;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This is the timer that starts counting after a
 * certain amount of inactivity on the terminal.
 * It will also reset the terminal if nothing is
 * clicked during an activity check.
 */
public class InactivityTimer {

  private Timer timer = new Timer();
  private Stage primaryStage;

  /**
   * The timer constructor.
   *
   * @param primaryStage the current stage to put
   *      the inactivity popup
   *      in later on
   */
  public InactivityTimer(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  /**
   * Start the timer.
   * Timer has sceduled delay in miliseconds, so we
   * convert to seconds -> 30 * 1000 = 30 seconds.
   * After time has passed, inactivity popup will be
   * displayed (TimerTask).
   */
  public void startTimer() {
    timer.schedule(displayInactivityPopup(), 30 * 1000);
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
      Button button = new Button("Yes");
      button.setOnAction(e -> popup.close());

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
    // Terminate the timer
    timer.cancel();

    // Make a new timer and start it
    timer = new Timer();
    startTimer();
  }
}
