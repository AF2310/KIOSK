package org.example.screens;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
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
    // Label
    // button change timer shop
    
    // VBOX RIGHT
    // Label
    // Button change timer popup

    // Combine both in timer editor HBox

    // confirmation text as label

    // Language + Cancel Buttons HBOX BOTTOM

    // VBOX main layout COMBINE ALL
    // timer editor box
    // confirmation text BOX
    // language + cancel box

    // Setting positioning of all the elements
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    //layout.setTop(topContainer);
    //layout.setBottom(bottomContainer);

    return new Scene(layout, 1920, 1080);
  }
}
