package org.example.kiosk;

import java.util.Timer;
import java.util.TimerTask;
import javafx.stage.Stage;

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
   */
  public void startTimer() {
    
    //timer.schedule(displayInactivityPopup(), 60);
  }

  /**
   * The timer task is an inactivity popup, asking
   * 'Are you still there?'.
   */
  private TimerTask displayInactivityPopup() {
    // TODO: display inactivity method (use helper method below)
  }

  private void inactivityPopup() {
    // TODO: popup code
  }
}
