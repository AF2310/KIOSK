package org.example;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class EatHereButton extends VBox {
  private final Button button;

  public EatHereButton() {
    this.setAlignment(Pos.CENTER);
    this.setSpacing(10);

    ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/takeaway.png")));
    icon.setFitWidth(60);
    icon.setFitHeight(60);
    button = new Button();
    button.setGraphic(icon);



  }

  public Button getButton() {
    return button;
  }
  
}
