package org.example.boxes;

import org.example.buttons.CircleButtonWithSign;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Class for the AddRemoveBlock. Includes a label with quantity and two buttons
 * for adding and removing items.
 */
public class AddRemoveBlock extends HBox {
  private Label quantityLabel;
  private CircleButtonWithSign minusButton;
  private CircleButtonWithSign plusButton;
  private int quantity; // Current quantity (range 0-9)

  /**
   * Constructor for the AddRemoveBlock.
   *
   * @param initialQuantity Initial quantity (0-9)
   */
  public AddRemoveBlock(int initialQuantity) {
    // Clamp initial quantity between 0 and 9
    if (initialQuantity < 0) {
      initialQuantity = 0;
    }
    if (initialQuantity > 9) {
      initialQuantity = 9;
    }
    this.quantity = initialQuantity;

    // Initialize components
    quantityLabel = new Label(String.valueOf(quantity));
    quantityLabel.setMinWidth(20);
    quantityLabel.setStyle("-fx-alignment: center; -fx-font-size: 16px;");

    minusButton = new CircleButtonWithSign("-");
    plusButton = new CircleButtonWithSign("+");

    // Set up button actions
    setupButtonActions();

    // Layout setup
    this.setSpacing(10);
    this.setAlignment(Pos.CENTER);
    this.getChildren().addAll(minusButton, quantityLabel, plusButton);

    // Update button states initially
    updateButtonStates();
  }

  private void setupButtonActions() {
    minusButton.setOnAction(e -> {
      if (quantity > 0) {
        quantity--;
        quantityLabel.setText(String.valueOf(quantity));
        updateButtonStates();
      }
    });

    plusButton.setOnAction(e -> {
      if (quantity < 9) {
        quantity++;
        quantityLabel.setText(String.valueOf(quantity));
        updateButtonStates();
      }
    });
  }

  private void updateButtonStates() {
    minusButton.setInvalid(quantity <= 0);
    plusButton.setInvalid(quantity >= 9);
  }

  // getter for quantity
  public int getQuantity() {
    return quantity;
  }
}
