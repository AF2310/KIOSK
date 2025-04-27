package org.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This screen displays the confirmation of the order
 * after the user has clicked on confirm order.
 * It not only shows a confirmation message but also
 * displays the order number.
 * This is the last scene of a normal order process.
 * After a few seconds, the screen should fade out and
 * the user gets put back into the welcome screen,
 * resetting all data again.
 */
public class OrderConfirmationScreen {
  
  /**
   * This is the constructor of the order confirmation
   * screen. It will screate a scene for it.
   * TODO: we still need proper database queries for orderid for e.g.
   *
   * @param primaryStage the primary stage of this scene
   * @param windowWidth width of window
   * @param windowHeight height of window
   * @param welcomeScrScene the welcome screen
   * @param orderId the id of the order (database)
   * @return scene containing the order confirmation
   */
  public Scene createOrderConfirmationScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene,
      int orderId) {

    // Title of the screen -> Order confirmation message
    Label orderConfirmationLabel = new Label("Ordered Successfully!");
    orderConfirmationLabel.setAlignment(Pos.CENTER);
    orderConfirmationLabel.setStyle(
        "-fx-font-size: 60px;"
        + "-fx-font-weight: bold;"
    );

    // Order id label
    Label orderIdLabel = new Label("Order number: " + orderId);
    orderIdLabel.setStyle(
        "-fx-font-size: 30px;"
        + "-fx-font-weight: normal;"
    );

    // Combining both labels
    VBox screenLabelBox = new VBox();
    screenLabelBox.setAlignment(Pos.CENTER);
    screenLabelBox.getChildren().addAll(
        orderConfirmationLabel,
        orderIdLabel
    );

    // Making layout into a stackpane to create fade overlay later on
    StackPane oconfirmStack = new StackPane();
    oconfirmStack.getChildren().addAll(screenLabelBox);
    
    // Create final scene result
    return new Scene(oconfirmStack, windowWidth, windowHeight);
  }
}
