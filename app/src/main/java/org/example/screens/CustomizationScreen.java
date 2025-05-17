package org.example.screens;

import java.sql.Connection;

import org.example.buttons.CancelButtonWithText;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButton;
import org.example.buttons.MidButtonWithImage;
import org.example.buttons.SqrBtnWithOutline;
import org.example.kiosk.LanguageSetting;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    
    // Making the title on top of the admin menu screen
    Label adminMenuText = new Label("Set & Test Design Settings");
    adminMenuText.setStyle(
        "-fx-font-size: 100px;"
            + "-fx-font-weight: bold;");

    adminMenuLayout.getChildren().addAll(adminMenuText);

    // this gridpane is used for all the middle buttons in the admin menu,
    // to align tem properly in rows and columns.
    GridPane centerGrid = new GridPane();
    centerGrid.setHgap(50);
    centerGrid.setVgap(30);
    centerGrid.setAlignment(Pos.CENTER);

    // Test Buttons
    var testBtn1 = new MidButton("Filled", "rgb(1, 176, 51)", 30);
    var testBtn2 = new MidButtonWithImage("With Image", "/eatHere.png", "rgb(0, 0, 0)");
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
    cancelButton.setOnAction(e -> {
      primaryStage.setScene(welcomeScrScene);
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
