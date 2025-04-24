package org.example;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This is the circlebutton mostly used for + and - symbols.
 */
public class CircleButtonWithSign extends Button {

  /**
   * Creates a small circular button with a sign (e.g. + or -).
   *
   * @param buttonSign  The symbol to display ("+" or "-")
   */
  public CircleButtonWithSign(String buttonSign) {
    // Set the sign as button text
    this.setText(buttonSign);
    this.setFont(Font.font(24)); // Slightly larger for clarity
    // Set circular style
    this.setPrefSize(50, 50); // Width and height equal = circle
    this.setMinSize(50, 50);
    this.setMaxSize(50, 50);
    // Makes a black button with white + inside
    if (buttonSign.equals("+")) {
      this.setTextFill(Color.WHITE);
      this.setStyle(
          "-fx-background-color: black;"
          + "-fx-background-radius: 25;"
          + "-fx-border-radius: 25;"
          + "-fx-border-color: black;"
          + "-fx-border-width: 2;"
          + "-fx-font-weight: bold;"
      );
    } else if (buttonSign.equals("-")) {
      this.setTextFill(Color.BLACK);
      this.setStyle(
          "-fx-background-color: white;"
          + "-fx-background-radius: 25;"
          + "-fx-border-radius: 25;"
          + "-fx-border-color: black;"
          + "-fx-border-width: 2;"
          + "-fx-font-weight: bold;"
      );
    }
  }
}
