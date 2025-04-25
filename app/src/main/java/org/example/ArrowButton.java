package org.example;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class represents the arrow buttons.
 * Used in:
 * menu screen to browse
 * checkout screen to switch between item pages
 *                 if order is larger than max items per checkout page
 * item detail screen to browse between ingredient list of an item
 */
public class ArrowButton extends Button {
  
  /**
   * Arrow button constructor.
   * OBS! Arrow button uses hard coded local image and
   * not image from database!
   *
   * @param pointingLeft boolean that gives you 
   *                     left arrow if set to true and
   *                     right arrow if set to false
   */
  public ArrowButton(boolean pointingLeft, boolean setGrey) {
    // Default button sizes
    this.setPrefSize(60, 300);

    // Border of button will be black if setGrey is false and 
    // gey if set grey is true
    String borderColor = setGrey ? "grey" : "black";
    
    // Set normal style of button
    this.setStyle(
        "-fx-background-color: rgb(255, 255, 255);"
        + "-fx-border-color: " + borderColor + ";"
        + "-fx-border-width: 3px;"
        + "-fx-border-radius: 9px;" // For round borders
        + "-fx-padding: 10px;"      // For wider clickable area
    );

    // Using hard coded arrow image
    ImageView buttonImage = new ImageView(new Image("/nav_bl.png"));

    
    // Set size and scalability of image
    buttonImage.setFitHeight(40);
    buttonImage.setPreserveRatio(true);
    
    // Arrow will point right
    if (!pointingLeft) {
      // Arrow image points left by default
      // -> Mirrowing image to make it point right
      buttonImage.setScaleX(-1);
    }

    // Make image inside button (arrow) grey and unclickable
    if (setGrey) {
      buttonImage.setOpacity(0.5);
      this.setDisable(true);
    }

    // Put the button (arrow) image inside the button
    this.setGraphic(buttonImage);
  }
}
