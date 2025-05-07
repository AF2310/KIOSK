package org.example.buttons;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SqrBtnImgOnly extends Button {

  public SqrBtnImgOnly() {
    ImageView cartIcon = new ImageView(new Image(getClass().getResourceAsStream("/cart_bl.png")));

    this.setPrefWidth(100);
    this.setPrefHeight(100);
    this.setGraphic(cartIcon);
    this.setStyle("-fx-background-color: transparent;");
    this.setMinSize(140, 140);
  }
}
