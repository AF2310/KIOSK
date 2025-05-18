package org.example.screens;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.CircleButtonWithSign;
import org.example.buttons.MidButton;
import org.example.buttons.RectangleButtonWithText;
import org.example.buttons.RoundButton;
import org.example.buttons.TitleLabel;


/**
 * Class for making the scene for global discounts.
 */
public class GlobalDiscountScreen {
  private Stage primaryStage;
  private Scene prevScene;

  /**
   * Constructor for the scene.
   *
   * @param primaryStage The screen itself.
   * @param prevScene Previous screen.
   */
  public GlobalDiscountScreen(Stage primaryStage, Scene prevScene) {
    this.primaryStage = primaryStage;
    this.prevScene = prevScene;

  }
  /**
   * Global discount screen.
   *
   * @return Discount screen scene.
   */

  public Scene getGlobalDiscountScreen() {
    // Create buttons
    RectangleButtonWithText fivePercentButton = new RectangleButtonWithText("5%");
    RectangleButtonWithText tenPercentButton = new RectangleButtonWithText("10%");
    RectangleButtonWithText twentyPercentButton = new RectangleButtonWithText("20%");
    RectangleButtonWithText customPercentButton = new RectangleButtonWithText("Custom");
    MidButton itemDiscountButton = new MidButton("Item Discounts", "rgb(255, 255, 255)", 45);

    // Add padding to the title button to match button widths
    itemDiscountButton.setPadding(new Insets(0, 0, 0, 10));

    // Create a container for the percentage buttons
    VBox percentageButtons = new VBox(20); 
    percentageButtons.getChildren().addAll(fivePercentButton, tenPercentButton,
         twentyPercentButton, customPercentButton);
         
    percentageButtons.setAlignment(Pos.CENTER);

    // Create a container that will center the buttons relative to the title
    VBox leftSide = new VBox(20); 
    leftSide.setAlignment(Pos.CENTER_LEFT); // Align everything to left of screen

    TitleLabel title = new TitleLabel("Select A Discount Type");
    HBox topHbox = new HBox(20);
    topHbox.setAlignment(Pos.TOP_CENTER);
    topHbox.getChildren().addAll(title);



    leftSide.getChildren().addAll(itemDiscountButton, percentageButtons);
    
    title.setAlignment(Pos.TOP_CENTER);
    BorderPane layout = new BorderPane();
    layout.setLeft(leftSide);
    layout.setTop(topHbox);
    
    // Add padding to center the whole group horizontally
    BorderPane.setMargin(leftSide, new Insets(0, 0, 0, 100));
    
    Scene globalDiscountScreen = new Scene(layout, 1920, 1080);
    return globalDiscountScreen;
  }
}
