package org.example;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * This is the medium sized button with an image.
 */
public class MidButtonWithImage extends Button {

  /**
  * Class' constructor.
  */
  public MidButtonWithImage(String buttonText, String imagePath, String backgroundColor) {
    // Set button size
    this.setPrefSize(460, 140);

    // Image setup
    Image image = new Image(imagePath);
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(100);
    imageView.setFitHeight(100);

    // Label setup
    Label label = new Label(buttonText);
    label.setStyle("-fx-background-color: transparent;"
        + "-fx-font-weight: normal;"
        + "-fx-font-size: 50;"
        + "-fx-padding: 5 10;"
        + "-fx-background-radius: 10;");

    // Set the white button
    if (backgroundColor.equals("rgb(255, 255, 255)")) {
      label.setStyle(label.getStyle() + "-fx-text-fill: black;");
      this.setStyle(
          "-fx-background-color: rgb(255, 255, 255);"
              + "-fx-border-color: black;"
              + "-fx-border-width: 2;"
              + "-fx-border-radius: 30;"
              + "-fx-background-radius: 30;"
              + "-fx-padding: 10 20;");
      // Set the non-white button
    } else {
      label.setStyle(label.getStyle() + "-fx-text-fill: white;");
      this.setStyle(
          "-fx-background-color: " + backgroundColor + ";"
              + "-fx-border-radius: 30;"
              + "-fx-background-radius: 30;"
              + "-fx-padding: 10 20;");
    }

    // Spacer to push image to the right
    Region hSpacer = new Region();
    HBox.setHgrow(hSpacer, Priority.ALWAYS);

    // Create the label & image row
    HBox contentRow = new HBox(10, label, hSpacer, imageView);
    contentRow.setPadding(new Insets(10));
    contentRow.setPrefHeight(140);
    contentRow.setStyle("-fx-alignment: center;");

    // Set button graphics
    this.setGraphic(contentRow);
  }
}
