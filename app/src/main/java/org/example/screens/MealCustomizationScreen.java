package org.example.screens;

import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.CancelButtonWithText;
import org.example.buttons.LangBtn;
import org.example.buttons.SqrBtnWithOutline;

/**
 * A Class for picking side and drink option for the meal.
 */
public class MealCustomizationScreen {


  /**
   * Constructor for selecting the side option for a meal.
   *
   * @param stage the primary stage
   * @param returnScene the scene to return to in this case if we click cancel
   * @param mealName name of the selected meal
   * @param imagePath path to the selected meal's image
   * @return returns a scene for side options.
   */
  public Scene createSideSelectionScene(Stage stage, Scene returnScene,
      String mealName, String imagePath) {
    Label title = new Label("Pick a Side for your Meal");
    title.setStyle("-fx-font-size: 40px;"
        + "-fx-font-weight: bold;");

    HBox titleBox = new HBox(title);
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setPadding(new Insets(30, 0, 0, 0));
    BorderPane layout = new BorderPane();
    layout.setTop(titleBox);
    
    HBox centerBox = new HBox(50);
    centerBox.setPadding(new Insets(20));
    centerBox.setAlignment(Pos.CENTER);

    GridPane sideOptionsGrid = new GridPane();
    sideOptionsGrid.setHgap(20);
    sideOptionsGrid.setVgap(20);
    sideOptionsGrid.setAlignment(Pos.CENTER_LEFT);
    sideOptionsGrid.setPadding(new Insets(10));

    //for now they are normal buttons,
    // i decided the 2x3 grid looks fine for this
    for (int i = 0; i < 6; i++) {
      Button sideBtn = new Button("Side " + (i + 1));
      sideBtn.setPrefSize(150, 100);
      sideOptionsGrid.add(sideBtn, i % 2, i / 2);
    }
    VBox mealDisplay = new VBox(10);
    mealDisplay.setAlignment(Pos.CENTER);
    InputStream imageStream = getClass().getResourceAsStream(imagePath);
    ImageView mealImage;

    mealImage = new ImageView(new Image(imageStream));

    Label mealLabel = new Label(mealName);
    mealLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    mealDisplay.getChildren().addAll(mealImage, mealLabel);
    centerBox.getChildren().addAll(sideOptionsGrid, mealDisplay);
    layout.setCenter(centerBox);

    var languageBtn = new LangBtn();
    HBox bottomLeftBox = new HBox(languageBtn);
    bottomLeftBox.setAlignment(Pos.BOTTOM_LEFT);
    Region leftSpacer = new Region();
    Region rightSpacer = new Region();
    HBox.setHgrow(leftSpacer, Priority.ALWAYS);
    HBox.setHgrow(rightSpacer, Priority.ALWAYS);
  
    SqrBtnWithOutline confirmButton = new SqrBtnWithOutline("Confirm",
        "green_tick.png", "rgb(81, 173, 86)");
    CancelButtonWithText cancelBtn = new CancelButtonWithText();
    HBox confirmBox = new HBox(confirmButton);
    confirmBox.setAlignment(Pos.BOTTOM_CENTER);
    HBox cancelBox = new HBox(cancelBtn);
    cancelBox.setAlignment(Pos.BOTTOM_RIGHT);

    HBox bottomLayout = new HBox();
    bottomLayout.setPadding(new Insets(20));
    bottomLayout.getChildren().addAll(languageBtn, leftSpacer, confirmBox, rightSpacer, cancelBox);
    layout.setBottom(bottomLayout);

    cancelBtn.setOnMouseClicked(e -> {
      stage.setScene(returnScene);
    });

    confirmButton.setOnMouseClicked(e -> {
      Scene drinkScene = createDrinkSelectionScene(stage, returnScene, mealName, imagePath);
      stage.setScene(drinkScene);
    });

    return new Scene(layout, 1920, 1080);
  }

  /**
   * This constructor creates the scene for selecting a drink for a meal.
   *
   * @param stage primary stage
   * @param returnScene goes back to the side options
   * @param mealName name of the meal
   * @param imagePath image of the selected meal
   * @return it returns the scene for drink options.
   */
  public Scene createDrinkSelectionScene(Stage stage, Scene returnScene,
      String mealName, String imagePath) {

    Label title = new Label("Pick a Drink for your Meal");
    title.setStyle("-fx-font-size: 40px;"
        + "-fx-font-weight: bold;");

    HBox titleBox = new HBox(title);
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setPadding(new Insets(30, 0, 0, 0));
    BorderPane layout = new BorderPane();
    layout.setTop(titleBox);

    HBox centerBox = new HBox(50);
    centerBox.setPadding(new Insets(20));
    centerBox.setAlignment(Pos.CENTER);

    GridPane sideOptionsGrid = new GridPane();
    sideOptionsGrid.setHgap(20);
    sideOptionsGrid.setVgap(20);
    sideOptionsGrid.setAlignment(Pos.CENTER_LEFT);
    sideOptionsGrid.setPadding(new Insets(10));

    //for now they are normal buttons,
    // i decided the 2x3 grid looks fine for this
    for (int i = 0; i < 6; i++) {
      Button sideBtn = new Button("Side " + (i + 1));
      sideBtn.setPrefSize(150, 100);
      sideOptionsGrid.add(sideBtn, i % 2, i / 2);
    }
    VBox mealDisplay = new VBox(10);
    mealDisplay.setAlignment(Pos.CENTER);
    InputStream imageStream = getClass().getResourceAsStream(imagePath);
    ImageView mealImage;

    mealImage = new ImageView(new Image(imageStream));

    Label mealLabel = new Label(mealName);
    mealLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    mealDisplay.getChildren().addAll(mealImage, mealLabel);
    centerBox.getChildren().addAll(sideOptionsGrid, mealDisplay);
    layout.setCenter(centerBox);


    // Bottom buttons
    var languageBtn = new LangBtn();
    var confirmBtn = new SqrBtnWithOutline("Confirm", "green_tick.png", "rgb(81, 173, 86)");
    var cancelBtn = new CancelButtonWithText();

    Region spacer1 = new Region();
    Region spacer2 = new Region();
    HBox.setHgrow(spacer1, Priority.ALWAYS);
    HBox.setHgrow(spacer2, Priority.ALWAYS);

    HBox bottomBar = new HBox(20, languageBtn, spacer1, confirmBtn, spacer2, cancelBtn);
    bottomBar.setPadding(new Insets(20));
    layout.setBottom(bottomBar);


    cancelBtn.setOnMouseClicked(e -> {
      Scene sideScene = createSideSelectionScene(stage, returnScene, mealName, imagePath);
      stage.setScene(sideScene);
    });

    return new Scene(layout, 1920, 1080);
  }
}


