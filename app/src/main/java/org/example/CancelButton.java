package org.example;


import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class CancelButton extends VBox {
  private final Button cancelButton;

  public CancelButton() {
    ImageView cancelIcon = new ImageView(new Image(getClass().getResourceAsStream("/cancel.png")));
    cancelIcon.setFitWidth(30);
    cancelIcon.setFitHeight(30);


    cancelButton = new Button();
    cancelButton.setGraphic(cancelIcon);
    cancelButton.setStyle(
      "-fx-background-color: transparent;" +
        "-fx-border-color: red;" +
        "-fx-border-width: 10;" +
        "-fx-border-radius: 10;" +
        "-fx-padding: 10;" +
        "-fx-background-radius: 10;"


      );
      cancelButton.setMinSize(60, 60);








    this.getChildren().add(cancelButton);

  }

  public Button getButton() {
    return cancelButton;
  }
  
}
