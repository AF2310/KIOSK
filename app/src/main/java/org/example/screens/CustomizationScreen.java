package org.example.screens;

import java.sql.Connection;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.example.buttons.ArrowButton;
import org.example.buttons.BlackButtonWithImage;
import org.example.buttons.CancelButtonWithText;
import org.example.buttons.CartSquareButton;
import org.example.buttons.ColorBtnOutlineImage;
import org.example.buttons.ColorButtonWithImage;
import org.example.buttons.ColorSquareButtonWithImage;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButton;
import org.example.buttons.TitleLabel;
import org.example.kiosk.LanguageSetting;

/**
 * Screen for customizing and testing the kiosk design.
 */
public class CustomizationScreen {
  private LanguageSetting languageSetting = new LanguageSetting();

  /**
   * Admin menu screen.
   */
  public CustomScene showCustomizationScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene,
      Connection conn) {

    // the mainlayout
    VBox adminMenuLayout = new VBox(20);
    adminMenuLayout.setAlignment(Pos.TOP_CENTER);
    adminMenuLayout.setPadding(new Insets(10));

    // Color picker for main color
    Label primClrLabel = new Label("Prime color: ");
    primClrLabel.setStyle(
        "-fx-font-size: 25px;"
            + "-fx-font-weight: bold;");

    ColorPicker primClrPicker = new ColorPicker(Color.BLACK);
    primClrPicker.setOnAction(e -> {
      Color selectedColor = primClrPicker.getValue();
      BlackButtonWithImage.setButtonBackgroundColor(selectedColor);
      ColorSquareButtonWithImage.setButtonColor(selectedColor);
      TitleLabel.setTextColor(selectedColor);
      CartSquareButton.setButtonColor(selectedColor);
      ColorBtnOutlineImage.setButtonColor(selectedColor);
      ArrowButton.setButtonColor(selectedColor);
    });
    primClrPicker.setPrefWidth(200);
    primClrPicker.setPrefHeight(50);
    VBox primColorVbox = new VBox(5, primClrLabel, primClrPicker);
    primColorVbox.setAlignment(Pos.CENTER);

    // Color picker for compliment color
    Label secPickerLbl = new Label("Secondary color: ");
    secPickerLbl.setStyle(
        "-fx-font-size: 25px;"
            + "-fx-font-weight: bold;");

    ColorPicker secClrPicker = new ColorPicker(Color.BLACK);
    secClrPicker.setOnAction(e -> {
      Color selectedColor = secClrPicker.getValue();
      ColorButtonWithImage.setButtonBackgroundColor(selectedColor);
    });
    secClrPicker.setPrefWidth(200);
    secClrPicker.setPrefHeight(50);
    VBox secColorVbox = new VBox(5, secPickerLbl, secClrPicker);
    secColorVbox.setAlignment(Pos.CENTER);

    // Color picker for background
    Label scenePicker = new Label("Background color: ");
    scenePicker.setStyle(
        "-fx-font-size: 25px;"
            + "-fx-font-weight: bold;");

    ColorPicker sceneColorPicker = new ColorPicker(Color.BLACK);
    sceneColorPicker.setOnAction(e -> {

      Color selectedColor = sceneColorPicker.getValue();
      BackgroundColorStore.setCurrentBackgroundColor(selectedColor);

    });
    sceneColorPicker.setPrefWidth(200);
    sceneColorPicker.setPrefHeight(50);

    VBox sceneColorVbox = new VBox(5, scenePicker, sceneColorPicker);
    sceneColorVbox.setAlignment(Pos.CENTER);

    // Put all pairs side by side in an HBox
    HBox colorPickersHbox = new HBox(40, primColorVbox, secColorVbox, sceneColorVbox);
    colorPickersHbox.setAlignment(Pos.CENTER);

    // Making the title on top of the admin menu screen
    Label adminMenuText = new TitleLabel("Set & Test Design");

    adminMenuLayout.getChildren().addAll(adminMenuText, colorPickersHbox);

    // this gridpane is used for all the middle buttons in the admin menu,
    // to align tem properly in rows and columns.
    GridPane centerGrid = new GridPane();
    centerGrid.setHgap(50);
    centerGrid.setVgap(30);
    centerGrid.setAlignment(Pos.CENTER);

    // Test Buttons
    var testBtn1 = new MidButton("Filled", "rgb(1, 176, 51)", 30);
    var testBtn2 = new ColorButtonWithImage("With Image", "/eatHere.png");
    var testBtn3 = new BlackButtonWithImage("BlBtn", "/eatHere.png");
    var testBtn4 = new ColorSquareButtonWithImage("With Image", "/back.png");
    var testBtn5 = new ColorBtnOutlineImage("Outline", "/back.png");

    centerGrid.add(testBtn1, 0, 0);
    centerGrid.add(testBtn4, 0, 1);
    centerGrid.add(testBtn2, 1, 0);
    centerGrid.add(testBtn3, 1, 1);
    centerGrid.add(testBtn5, 0, 2);

    // Adding the language button which already has the functionality of
    // changing the logo of the language
    var langButton = new LangBtn();
    HBox bottomLeftBox = new HBox(langButton);
    bottomLeftBox.setAlignment(Pos.BOTTOM_LEFT);

    // Spacer for bottom part of the screen
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);

    // Similar to a normal cancel button, it just has text under the image
    CancelButtonWithText cancelButton = new CancelButtonWithText();
    HBox bottomRightBox = new HBox(cancelButton);
    bottomRightBox.setAlignment(Pos.BOTTOM_RIGHT);

    testBtn1.setOnAction(e -> {
      Scene updateMenuScene = new UpdateMenuItems().adminUpdateMenuItems(
          primaryStage,
          adminMenuLayout.getScene());
      primaryStage.setScene(updateMenuScene);
    });

    // go back to the main screen if clicked
    // reinstantiates welcome screen, so the color change takes effect
    cancelButton.setOnAction(e -> {

      try {

        CustomScene welcomeScreen = new WelcomeScreen().createWelcomeScreen(primaryStage,
            windowWidth, windowHeight);
        primaryStage.setScene(welcomeScreen);

      } catch (SQLException ex) {

        ex.printStackTrace();

      }

    });

    HBox bottomLayout = new HBox();
    bottomLayout.setPadding(new Insets(0, 0, 0, 0));
    bottomLayout.getChildren().addAll(bottomLeftBox, spacerBottom, bottomRightBox);

    // Assigning the positions of elements for the Admin menu screen
    BorderPane mainBorderPane = new BorderPane();
    mainBorderPane.setPadding(new Insets(50));
    mainBorderPane.setTop(adminMenuLayout);
    mainBorderPane.setCenter(centerGrid);
    mainBorderPane.setBottom(bottomLayout);

    // Translate all the text
    langButton.addAction(event -> {
      // Toggle the language in LanguageSetting
      languageSetting.changeLanguage(
          languageSetting.getSelectedLanguage().equals("en") ? "sv" : "en");
      languageSetting.updateAllLabels(mainBorderPane);
    });

    var adminMenuScene = new CustomScene(mainBorderPane, windowWidth, windowHeight);

    return adminMenuScene;
  }
}