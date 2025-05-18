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

import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.BackButton;
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
    // Create title
    TitleLabel title = new TitleLabel("Select A Discount Type");
    HBox topHbox = new HBox(20);
    topHbox.setAlignment(Pos.TOP_CENTER);
    topHbox.getChildren().add(title);
    topHbox.setPadding(new Insets(0, 0, 75, 0));

    // Create discount sections
    VBox itemDiscounts = createDiscountSection("Item Discounts");
    VBox orderDiscounts = createDiscountSection("Order Discounts");

    // Create main content area
    HBox content = new HBox(100); // Space between the two sections
    content.setAlignment(Pos.CENTER);
    content.getChildren().addAll(itemDiscounts, orderDiscounts);

    // Create back button
    
    BackBtnWithTxt backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> primaryStage.setScene(prevScene));
    HBox bottomBox = new HBox(backButton);
    bottomBox.setAlignment(Pos.BOTTOM_CENTER);
    bottomBox.setPadding(new Insets(0, 0, 50, 0));

    // Setup main layout
    BorderPane layout = new BorderPane();
    layout.setTop(topHbox);
    layout.setCenter(content);
    layout.setBottom(bottomBox);
    
    return new Scene(layout, 1920, 1080);
}

  /**
 * Creates a discount section with title and percentage buttons.
 *
 * @param title The title for the section
 * @return VBox containing the complete discount section
 */
  private VBox createDiscountSection(String title) {
    // Create buttons
    RectangleButtonWithText fivePercentButton = new RectangleButtonWithText("5%");
    RectangleButtonWithText tenPercentButton = new RectangleButtonWithText("10%");
    RectangleButtonWithText twentyPercentButton = new RectangleButtonWithText("20%");
    RectangleButtonWithText customPercentButton = new RectangleButtonWithText("Custom");
    MidButton titleButton = new MidButton(title, "rgb(255, 255, 255)", 45);

    // Style the title button
    titleButton.setPadding(new Insets(0, 0, 0, 10));

    // Create button container
    VBox percentageButtons = new VBox(20);
    percentageButtons.getChildren().addAll(
        fivePercentButton, 
        tenPercentButton,
        twentyPercentButton, 
        customPercentButton
    );
    percentageButtons.setAlignment(Pos.CENTER);

    customPercentButton.setOnAction(e -> {
        
    });

    // Create section container
    VBox section = new VBox(20);
    section.setAlignment(Pos.TOP_CENTER);
    section.getChildren().addAll(titleButton, percentageButtons);
    
    return section;
  }
}
