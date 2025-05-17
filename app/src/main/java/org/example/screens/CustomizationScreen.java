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
import org.example.buttons.CancelButtonWithText;
import org.example.buttons.ColorButtonWithImage;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButton;
import org.example.buttons.SqrBtnWithOutline;
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
  public Scene showCustomizationScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene,
      Connection conn) {

    // the mainlayout
    VBox adminMenuLayout = new VBox(20);
    adminMenuLayout.setAlignment(Pos.TOP_CENTER);
    adminMenuLayout.setPadding(new Insets(10));

    // Label for label color picker
    Label labelPicker = new Label("Text color: ");
    labelPicker.setStyle(
        "-fx-font-size: 25px;"
        + "-fx-font-weight: bold;");

    // Color picker
    ColorPicker colorPicker = new ColorPicker(Color.BLACK);
    colorPicker.setOnAction(e -> {
      Color selectedColor = colorPicker.getValue();
      ColorButtonWithImage.setButtonBackgroundColor(selectedColor);
      TitleLabel.setTextColor(selectedColor);
    });
    colorPicker.setPrefWidth(200);
    colorPicker.setPrefHeight(50);

    // Label for label color picker
    Label scenePicker = new Label("Background color: ");
    scenePicker.setStyle(
        "-fx-font-size: 25px;"
        + "-fx-font-weight: bold;");

    // Color picker for scenes
    ColorPicker sceneColorPicker = new ColorPicker(Color.BLACK);
    sceneColorPicker.setOnAction(e -> {

      Color selectedColor = sceneColorPicker.getValue();
      BackgroundColorStore.setCurrentBackgroundColor(selectedColor);

    });
    sceneColorPicker.setPrefWidth(200);
    sceneColorPicker.setPrefHeight(50);

    // Making the title on top of the admin menu screen
    Label adminMenuText = new TitleLabel("Set & Test Design");

    adminMenuLayout.getChildren().addAll(adminMenuText, labelPicker, colorPicker,
        scenePicker, sceneColorPicker);

    // this gridpane is used for all the middle buttons in the admin menu,
    // to align tem properly in rows and columns.
    GridPane centerGrid = new GridPane();
    centerGrid.setHgap(50);
    centerGrid.setVgap(30);
    centerGrid.setAlignment(Pos.CENTER);

    // Test Buttons
    var testBtn1 = new MidButton("Filled", "rgb(1, 176, 51)", 30);
    var testBtn2 = new ColorButtonWithImage("With Image", "/eatHere.png");
    var testBtn3 = new MidButton("Outlined", "rgb(255, 255, 255)", 30);
    var testBtn4 = new SqrBtnWithOutline("With Image", "/cancel.png", "rgb(195, 4, 4)");

    centerGrid.add(testBtn1, 0, 0);
    centerGrid.add(testBtn4, 0, 1);
    centerGrid.add(testBtn2, 1, 0);
    centerGrid.add(testBtn3, 1, 1);

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

    Scene adminMenuScene = new Scene(mainBorderPane, windowWidth, windowHeight);

    return adminMenuScene;
  }
}