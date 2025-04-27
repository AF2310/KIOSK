package org.example;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This is the circle button mostly used for + and - symbols.
 */
public class CircleButtonWithSign extends Button {

  /**
   * Construtor that creates either a + or a - button with its own settings.
   *
   * @param buttonSign  The symbol to display ("+" or "-")
   */
  public CircleButtonWithSign(String buttonSign) {
    // Classic setting up of the size and playing with the style of the buttons
    this.setText(buttonSign);
    this.setFont(Font.font(24));
    this.setPrefSize(50, 50);
    this.setMaxSize(50, 50);
    // Both buttons have almost everything the same except the colors of the background, border,
    //and the text fill.
    this.setStyle(
        "-fx-background-radius: 25;"
        + "-fx-border-radius: 25;"
        + "-fx-border-width: 2;"
        + "-fx-font-weight: bold;"
        + "-fx-content-display: center;"
        + "-fx-padding: 0;"
    );
    if (buttonSign.equals("+")) {
      this.setTextFill(Color.WHITE);
      this.setStyle(this.getStyle()
          + "-fx-background-color: black;"
          + "-fx-border-color: black;");
    } else if (buttonSign.equals("-")) {
      this.setTextFill(Color.BLACK);
      this.setStyle(this.getStyle()
          + "-fx-background-color: white;"
          + "-fx-border-color: black;");
    }
  }

  /**
   * This method disables the button and dims it if disabled.
   *
   * @param disabled true to make the button invalid, otherwise false
   */
  public void setInvalid(boolean disabled) {
    this.setDisable(disabled);

    if (disabled) {
      this.setOpacity(0.5); 
    } else {
      this.setOpacity(1);
    }
  }
}
