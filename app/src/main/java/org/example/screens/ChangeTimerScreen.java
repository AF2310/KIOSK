package org.example.screens;

import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.menu.Product;

/**
 * This is the timer editor scene.
 * Used in the admin menu in 'AdminMenuScreen.java'.
 */
public class ChangeTimerScreen {

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
}
