package org.example.buttons;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.example.orders.Order;

/**
 * This button will confirm the order in the checkout
 * screen. The button will be used only in the checkout
 * screen. It will display the total price of the order
 * and its' functionality ("Confirm Order") as a label.
 */
public class ConfirmOrderButton extends Button {

  private Label priceLabel;
  
  /**
   * This is the constructor for the confirm order button.
   * so it would calculate the total price inside the class!
   */
  public ConfirmOrderButton() {
    // Set default size
    this.setPrefSize(590, 140);

    // Set astetics of button itself
    this.setStyle(
        "-fx-background-color: rgb(81, 173, 86);"
          + "-fx-border-color: rgb(81, 173, 86);"
          + "-fx-border-radius: 20;"
          + "-fx-background-radius: 20;"
          + "-fx-padding: 10;"
    );

    // Create total price label
    priceLabel = new Label();
    updatePriceLabel();

    // Label should be white, normal, and large fontb
    priceLabel.setStyle(
        "-fx-text-fill: white;"
        + "-fx-font-weight: normal;"
        + "-fx-font-size: 40;"
    );
      
    // Create Order confirmation label
    confirmLabel = new Label("Confirm Order");
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

  /**
   * Updates the full price label when initially called and
   * also in case of an event, such as quantity updates.
   */
  public void updatePriceLabel() {

    Order order = new Order();

    priceLabel.setText(
        "Total: " + String.format("%.2f", order.calculatePrice()) + "kr"
    );
  }

  private Label confirmLabel;

  public Label getConfirmLabel() {
    return confirmLabel;
  }

  /**
   * Sets the text of the button's label.
   */
  public void setButtonText(String text) {
    if (confirmLabel != null) {
      confirmLabel.setText(text);
    }
  }
}
