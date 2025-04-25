package org.example;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


/**
 * A reusable black back button component with a back arrow icon.
 */
public class ReturnButton extends VBox {

  private final Button RButton;

  /**
   * Constructs a styled back button.
   */
  public ReturnButton() {
    ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/back.png")));
    backIcon.setFitWidth(30);
    backIcon.setFitHeight(30);


    RButton = new Button();
    RButton.setGraphic(backIcon);
    RButton.setStyle(
      "-fx-background-color: transparent;" +
      "-fx-border-color: black;" +
      "-fx-border-width: 3;" +
      "-fx-border-radius: 12;" +
      "-fx-padding: 10;" +
      "-fx-background-radius: 12;"
    );

    RButton.setMinSize(60, 60);

    this.getChildren().add(RButton);


  }

  
  /**
   * Getter for return back black button.
   *
   * @return the button
   */
  public Button getButton() {
    return RButton;
  }
  
}
