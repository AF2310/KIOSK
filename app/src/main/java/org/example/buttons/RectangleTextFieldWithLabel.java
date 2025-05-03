package org.example.buttons;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Textfield button class.
 */
public class RectangleTextFieldWithLabel extends VBox {
  private TextField textField;
    
  /**
  * Textfield "button" for inputting various data such as prices, names, descriptions etc.
  *
  * @param labelText Text for the label.
  * @param buttonColor color of the button.
  */
  public RectangleTextFieldWithLabel(String labelText, String buttonColor) {
    this.setPrefSize(460, 140);
    this.setAlignment(Pos.CENTER_LEFT);
    this.setSpacing(10);
    this.setPadding(new Insets(10));

    // Label setup
    Label buttonLabel = new Label(labelText);
    buttonLabel.setStyle("-fx-background-color: transparent;"
            + "-fx-font-weight: normal;"
            + "-fx-font-size: 38;"
            + "-fx-padding: 5 10;"
            + "-fx-background-radius: 10;");

    // TextField setup
    textField = new TextField();
    textField.setPrefHeight(60);
    textField.setStyle("-fx-font-size: 28;");

    // Set the white button
    if (buttonColor.equals("rgb(255, 255, 255)")) {
      buttonLabel.setStyle(buttonLabel.getStyle() + "-fx-text-fill: black;");
      textField.setStyle(textField.getStyle()  
                + "-fx-background-color: rgb(255, 255, 255);"
                + "-fx-border-color: black;"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 30;"
                + "-fx-background-radius: 30;"); }


    // Set the non-white button
    else {
      buttonLabel.setStyle(buttonLabel.getStyle() + "-fx-text-fill: white;");
      textField.setStyle(textField.getStyle() + 
                "-fx-background-color: " + buttonColor + ";"
                + "-fx-border-radius: 30;"
                + "-fx-background-radius: 30;");
    }

    this.getChildren().addAll(buttonLabel, textField);
  }
    
  public String getText() {
    return textField.getText();
  }
    
}