package org.example.kiosk;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 * Manages label-related operations in the kiosk application.
 */
public class LabelManager {
  private static final List<Label> allLabels = new ArrayList<>();
  private static Color currentColor = Color.BLACK;

  public static void register(Label label) {
    label.setTextFill(currentColor);
    allLabels.add(label);
  }

  /**
   * Sets the text color for all registered labels and updates the current color.
   */
  public static void setTextColor(Color color) {
    currentColor = color;
    for (Label label : allLabels) {
      label.setTextFill(color);
    }
  }

  public static Color getCurrentColor() {
    return currentColor;
  }
}
