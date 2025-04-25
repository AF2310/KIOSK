package org.example;


import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * A reusable red cancel button component with a red X icon.
 */
public class CancelButton extends VBox {
  private final Button cancelButton;


  


  /**
   * Constructs a red cancel button with icon and styling.
   */
  public CancelButton() {
    ImageView cancelIcon = new ImageView(new Image(getClass().getResourceAsStream("/cancel.png")));
    cancelIcon.setFitWidth(30);
    cancelIcon.setFitHeight(30);


    cancelButton = new Button();
    cancelButton.setGraphic(cancelIcon);
    cancelButton.setStyle(
      "-fx-background-color: transparent;" +
      "-fx-border-color: red;" +
      "-fx-border-width: 3;" +
      "-fx-border-radius: 12;" +
      "-fx-padding: 10;" +
      "-fx-background-radius: 12;"


      );
      cancelButton.setMinSize(60, 60);








    this.getChildren().add(cancelButton);

  }


  
  /**
   * getter for button instance.
   *
   * @return  cancel button instance
   */
  public Button getButton() {
    return cancelButton;
  }
  
}
