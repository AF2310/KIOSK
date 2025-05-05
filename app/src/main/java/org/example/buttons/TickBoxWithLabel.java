package org.example.buttons;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * A tick box with a label on top of it.
 */
public class TickBoxWithLabel extends VBox {
  private final CheckBox checkBox;
  private final Label label;

  /**
   * The constructor for a tick box with a label.
   *
   * @param labelString the string which acts as a label for the tick box.
   */
  public TickBoxWithLabel(String labelString) {
    this.label = new Label(labelString);
    this.label.setStyle("-fx-font-size: 38px; -fx-text-fill: black;");

    this.checkBox = new CheckBox();
    this.setAlignment(Pos.CENTER);
    this.setSpacing(5);
    checkBox.setStyle("-fx-background-color: transparent;"
        + "-fx-min-width: 50px;"
        + "-fx-min-height: 50px;"
        + "-fx-max-width: 50px;"        
        + "-fx-max-height: 50px;");

    checkBox.setScaleX(3.0);
    checkBox.setScaleY(3.0);

    this.getChildren().addAll(label, checkBox);

    this.setPrefHeight(80);
    this.setPrefWidth(300);
  }

  public boolean isSelected() {
    return checkBox.isSelected();
  }

  public CheckBox getCheckBox() {
    return checkBox;
  }

}
