package org.example;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class creates a language button with a Swedish flag as a graphic.
 */
public class LangButtonPlaceHolder extends Button {

  //private Button langButton;
  private ImageView sweFlag;

  /**
   * Constructor for the language button placeholder.
   * Initializes the button with a Swedish flag image.
   */
  public LangButtonPlaceHolder() {
    // Set button size
    this.setPrefSize(80, 80);

    // Image setup
    Image image = new Image("/swe.png");
    sweFlag = new ImageView(image);
    sweFlag.setFitWidth(100);
    sweFlag.setFitHeight(100);

    // Set the button graphic to the flag image
    this.setGraphic(sweFlag);

    // Set button style
    this.setStyle(
        "-fx-background-color: transparent;"
            + "-fx-border-color: transparent;"
            + "-fx-border-radius: 20;"
            + "-fx-background-radius: 20;");
  }
}
