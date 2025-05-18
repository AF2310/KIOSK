package org.example.buttons;

import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.screens.BackgroundColorStore;

/**
 * A reusable component for the three color pickers used in CustomizationScreen.
 */
public class ColorPickersPane extends HBox {
  private ColorPicker primClrPicker;
  private ColorPicker secClrPicker;
  private ColorPicker sceneColorPicker;

  /**
   * Constructs a ColorPickersPane with 
   * three color pickers for primary, secondary, and background colors.
   */
  public ColorPickersPane() {
    setSpacing(40);
    setAlignment(Pos.CENTER);

    // Primary Color Picker
    Label primClrLabel = new Label("Prime color: ");
    primClrLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
    primClrPicker = new ColorPicker(Color.BLACK);
    primClrPicker.setPrefSize(200, 50);
    VBox primColorVbox = new VBox(5, primClrLabel, primClrPicker);
    primColorVbox.setAlignment(Pos.CENTER);

    // Secondary Color Picker
    Label secPickerLbl = new Label("Secondary color: ");
    secPickerLbl.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
    secClrPicker = new ColorPicker(Color.BLACK);
    secClrPicker.setPrefSize(200, 50);
    VBox secColorVbox = new VBox(5, secPickerLbl, secClrPicker);
    secColorVbox.setAlignment(Pos.CENTER);

    // Background Color Picker
    Label scenePicker = new Label("Background color: ");
    scenePicker.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
    sceneColorPicker = new ColorPicker(Color.BLACK);
    sceneColorPicker.setPrefSize(200, 50);
    VBox sceneColorVbox = new VBox(5, scenePicker, sceneColorPicker);
    sceneColorVbox.setAlignment(Pos.CENTER);

    getChildren().addAll(primColorVbox, secColorVbox, sceneColorVbox);

    // Setup color change listeners
    setupListeners();
  }

  private void setupListeners() {
    primClrPicker.setOnAction(e -> {
      Color selectedColor = primClrPicker.getValue();
      BlackButtonWithImage.setButtonBackgroundColor(selectedColor);
      ColorSquareButtonWithImage.setButtonColor(selectedColor);
      TitleLabel.setTextColor(selectedColor);
      CartSquareButton.setButtonColor(selectedColor);
      ColorBtnOutlineImage.setButtonColor(selectedColor);
      ArrowButton.setButtonColor(selectedColor);
    });

    secClrPicker.setOnAction(e -> {
      Color selectedColor = secClrPicker.getValue();
      ColorButtonWithImage.setButtonBackgroundColor(selectedColor);
    });

    sceneColorPicker.setOnAction(e -> {
      Color selectedColor = sceneColorPicker.getValue();
      BackgroundColorStore.setCurrentBackgroundColor(selectedColor);
    });
  }

  // You can add getters if needed to expose the ColorPickers externally
  public ColorPicker getPrimaryColorPicker() {
    return primClrPicker;
  }

  public ColorPicker getSecondaryColorPicker() {
    return secClrPicker;
  }

  public ColorPicker getBackgroundColorPicker() {
    return sceneColorPicker;
  }
}
