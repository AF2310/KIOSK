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
  public ArrowButton(boolean pointingLeft) {
    // Default button sizes
    this.setPrefSize(60, 300);

    // Set default style of button
    this.setStyle(
        "-fx-background-color: rgb(255, 255, 255);"
        + "-fx-border-color: black;"
        + "-fx-border-width: 3px;"
        + "-fx-border-radius: 9px;" // For round borders
        + "-fx-padding: 10px;"      // For wider clickable area
    );

    // Using hard coded arrow image
    Image arrowImage = new Image(getClass().getResourceAsStream("/nav_bl.png"));
    ImageView buttonImage = new ImageView(arrowImage);

    // Set size and scalability of image
    buttonImage.setFitHeight(40);
    buttonImage.setPreserveRatio(true);

    // Arrow will point left
    if (pointingLeft) {
      // image points left by default -> no need for setScaleX

      // TODO: Functionality added in main code or here with more
      //       constructor variables?
      
    // Arrow will point right
    } else {
      // Mirrowing image to make it point right
      buttonImage.setScaleX(-1);

      // TODO: Functionality added in main code or here with more
      //       constructor variables?
    }

    // Put the button (arrow) image inside the button
    this.setGraphic(buttonImage);
  }
}
