package org.example.kiosk;

import java.util.Timer;
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
}
