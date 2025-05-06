package org.example.buttons;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * A class for a drop box fow options with a label on top.
 */
public class DropBoxWithLabel extends VBox {
  private final Label label;
  private final ComboBox<String> comboBox;

  /**
   * A constructor for the drop box.
   *
   * @param labelText text that acts as a Label for the combo/drop box.
   */
  public DropBoxWithLabel(String labelText) {
    label = new Label(labelText);
    label.setStyle("-fx-font-size: 38; -fx-text-fill: black;");

    comboBox = new ComboBox<>();
    comboBox.setPrefWidth(300);
    comboBox.setStyle("-fx-background-color: white;");

    setSpacing(5);
    setAlignment(Pos.TOP_LEFT);
    getChildren().addAll(label, comboBox);
  }

  public ComboBox<String> getComboBox() {
    return comboBox;
  }

  public String getSelectedItem() {
    return comboBox.getSelectionModel().getSelectedItem();
  }
}
