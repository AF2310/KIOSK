package org.example.buttons;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * This button will confirm the order in the checkout
 * screen. The button will be used only in the checkout
 * screen. It will display the total price of the order
 * and its' functionality ("Confirm Order") as a label.
 */
public class ConfirmOrderButton extends Button {
  
  /**
   * This is the constructor for the confirm order button.
   * TODO: total price could also be an array instead
   * so it would calculate the total price inside the class!
   *
   * @param totalPrice double variable of total price of order
   */
  public ConfirmOrderButton(double totalPrice) {
    // Set default size
    this.setPrefSize(590, 90);

    // Set astetics of button itself
    this.setStyle(
        "-fx-background-color: rgb(81, 173, 86);"
          + "-fx-border-color: rgb(81, 173, 86);"
          + "-fx-border-radius: 20;"
          + "-fx-background-radius: 20;"
          + "-fx-padding: 10;"
    );

    // Create total price label
    Label priceLabel = new Label("Total: " + totalPrice + "kr");
    // Label should be white, normal, and large fontb
    priceLabel.setStyle(
        "-fx-text-fill: white;"
        + "-fx-font-weight: normal;"
        + "-fx-font-size: 40;"
    );
      
    // Create Order confirmation label
    Label confirmLabel = new Label("Confirm Order");
    // Label should be white, normal, and large font
    confirmLabel.setStyle(
        "-fx-text-fill: white;"
        + "-fx-font-weight: normal;"
        + "-fx-font-size: 40;"
    );
    
    // Spacer to push labels to the right/left
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    
    // Combining both labels into one button label
    HBox buttonLabel = new HBox();
    buttonLabel.setAlignment(Pos.CENTER);
    buttonLabel.getChildren().addAll(priceLabel, spacer, confirmLabel);

    // Setting astetics and layout of button label
    buttonLabel.setStyle(
        "-fx-background-color: transparent;"
        + "-fx-padding: 20 20;"               // leftSpace rightSpace
    );

    // Add label to button
    this.setGraphic(buttonLabel);
  }
}
