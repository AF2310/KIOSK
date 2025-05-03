package org.example.screens;

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
import org.example.buttons.MidButton;
import org.example.buttons.RoundButton;

/**
 * Admin menu class.
 */
public class AdminMenuScreen {

  /**
   * Admin menu screen.
   */
  public Scene createAdminMenuScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene) {
    //the mainlayout
    VBox adminMenuLayout = new VBox(20);
    adminMenuLayout.setAlignment(Pos.TOP_CENTER);
    adminMenuLayout.setPadding(new Insets(10));
    // Making the title on top of the admin menu screen
    Label adminMenuText = new Label("Welcome, admin!");
    adminMenuText.setStyle(
        "-fx-font-size: 100px"
        + "-fx-font-weight: bold;");

    adminMenuLayout.getChildren().addAll(adminMenuText);

    // this gridpane is used for all the middle buttons in the admin menu,
    // to align tem properly in rows and columns.
    GridPane centerGrid = new GridPane();
    centerGrid.setHgap(50);
    centerGrid.setVgap(30);
    centerGrid.setAlignment(Pos.CENTER);

    // All the same instances of the MidButton
    MidButton updateMenuBtn = new MidButton("Update Menu Items", "rgb(255, 255, 255)", 30);
    MidButton changeTimerBtn = new MidButton("Change timer setting", "rgb(255, 255, 255)", 30);
    MidButton specialOffersBtn = new MidButton("Set special offers", "rgb(255, 255, 255)", 30);
    MidButton orderHistoryBtn = new MidButton("Order History", "rgb(255, 255, 255)", 30);
    MidButton salesSummaryBtn = new MidButton("See sales summary", "rgb(255, 255, 255)", 30);

    centerGrid.add(updateMenuBtn, 0, 0);
    centerGrid.add(changeTimerBtn, 0, 1);
    centerGrid.add(specialOffersBtn, 0, 2);
    centerGrid.add(orderHistoryBtn, 1, 0);
    centerGrid.add(salesSummaryBtn, 1, 1);

    // Adding the language button which already has the functionality of
    // changing the logo of the language
    RoundButton langButton = new RoundButton("languages", 70);
    HBox bottomLeftBox = new HBox(langButton);
    bottomLeftBox.setAlignment(Pos.BOTTOM_LEFT);

    // Spacer for bottom part of the screen
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);

    // Similar to a normal cancel button, it just has text under the image
    CancelButtonWithText cancelButton = new CancelButtonWithText();
    HBox bottomRightBox = new HBox(cancelButton);
    bottomRightBox.setAlignment(Pos.BOTTOM_RIGHT);

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

    Scene adminMenuScene = new Scene(mainBorderPane, windowWidth, windowHeight);

    return adminMenuScene;
  }
}

