package org.example.screens;

import java.sql.Connection;
import java.util.List;
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
import org.example.buttons.CancelButtonWithText;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButton;
import org.example.kiosk.LanguageSetting;

/**
 * Admin menu class.
 */
public class AdminMenuScreen {

  private LanguageSetting languageSetting = new LanguageSetting();

  /**
   * Admin menu screen.
   */
  public Scene createAdminMenuScreen(
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
    Label adminMenuText = new Label("Welcome, Admin!");
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

    // All the same instances of the MidButton

    MidButton orderHistoryBtn = new MidButton("Order History", "rgb(255, 255, 255)", 30);
    orderHistoryBtn.setOnAction(e -> {
      Scene historyScene = new AdminOrdHistoryScreen().showHistoryScene(
          primaryStage,
          adminMenuLayout.getScene());
      primaryStage.setScene(historyScene);
    });

    MidButton updateMenuBtn = new MidButton("Update Menu Items", "rgb(255, 255, 255)", 30);
    
    MidButton salesSummaryBtn = new MidButton("See Sales Summary", "rgb(255, 255, 255)", 30);
    salesSummaryBtn.setOnAction(e -> {
      Scene statsScene = new SalesStatsScreen().showStatsScene(
          primaryStage,
          adminMenuLayout.getScene());
      primaryStage.setScene(statsScene);
    });

    MidButton changeTimerBtn = new MidButton("Change Timer Setting", "rgb(255, 255, 255)", 30);
    changeTimerBtn.setOnAction(e -> {
      Scene timerEditor = new ChangeTimerScreen(
          primaryStage,
          adminMenuLayout.getScene()).getChangeTimerScene();
      primaryStage.setScene(timerEditor);
    });

    MidButton specialOffersBtn = new MidButton("Set Special Offers", "rgb(255, 255, 255)", 30);
    
    centerGrid.add(updateMenuBtn, 0, 0);
    centerGrid.add(changeTimerBtn, 0, 1);
    centerGrid.add(specialOffersBtn, 0, 2);
    centerGrid.add(orderHistoryBtn, 1, 0);
    centerGrid.add(salesSummaryBtn, 1, 1);

    MidButton searchBarBtn = new MidButton("Search", "rgb(255, 255, 255)", 30);

    searchBarBtn.setOnAction(e -> {
      Scene searchBarScreen = new SeachBarScreen().showSearchScene(
          primaryStage, 
          adminMenuLayout.getScene());
      primaryStage.setScene(searchBarScreen);
    });

    centerGrid.add(searchBarBtn, 1, 2);

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

    updateMenuBtn.setOnAction(e -> {
      Scene updateMenuScene = new UpdateMenuItems().adminUpdateMenuItems(
          primaryStage,
          adminMenuLayout.getScene());
      primaryStage.setScene(updateMenuScene);
    });

    // go back to the main screen if clicked
    cancelButton.setOnAction(e -> {
      primaryStage.setScene(welcomeScrScene);
    });

    // Pass in the Labeled components to translate
    langButton.addAction(event -> {
      langButton.updateLanguage(List.of(
          adminMenuText,
          updateMenuBtn.getButtonLabel(),
          changeTimerBtn.getButtonLabel(),
          orderHistoryBtn.getButtonLabel(),
          specialOffersBtn.getButtonLabel(),
          salesSummaryBtn.getButtonLabel(),
          cancelButton.getButtonLabel()
      ));
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
