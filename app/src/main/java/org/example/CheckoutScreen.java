package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This is the Screen that displays the order
 * of the customer. Here, the customer can check, 
 * edit and confirm his order. It also displays the
 * final price of the order.
 */
public class CheckoutScreen {

  private Stage primaryStage;

  /**
   * Creating a scene for the checkout menu.
   * TODO: These are most likely not all the variables that are needed.
   * We still need the other database variables etc..
   *
   * @param primaryStage the primary stage of this scene
   * @param windowWidth width of window
   * @param windowHeight height of window
   * @param mainMenuScreen the previous scene of this scene
   * @param orderId the id of the order (database)
   * @return scene containing all the order details
   */
  public Scene createCheckoutScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene mainMenuScreen,
      int orderId) {

    // Set primary stage
    this.primaryStage = primaryStage;


    // Set layout limits
    //BorderPane layout = new BorderPane();
    //layout.setPadding(new Insets(20));


    // Title of the checkout screen
    Label checkoutLabel = new Label("Checkout");
    checkoutLabel.setStyle(
        "-fx-font-size: 20px;"
        + "-fx-font-weight: bold;"
    );


    // Arrow buttons to navigate through order pages

    // Arrow left
    // Make instance of arrow button that points left
    ArrowButton leftArrowButton = new ArrowButton(true, false);

    /*leftArrowButton.setOnMouseClicked(e -> {
      if (currentCategoryIndex > 0) {
        currentCategoryIndex--;
        updateGrid();
      } else {
        currentCategoryIndex = categoryButtons.size() - 1;
        updateGrid();
      }
    });*/

    // Arrow right
    // Make instance of arrow button that points right
    ArrowButton rightArrowButton = new ArrowButton(false, false);

    // right button clickable as long as its not in last category (Special offers)
    /*rightArrowButton.setOnMouseClicked(e -> {
      if (currentCategoryIndex < categories.length - 1
          && !categories[currentCategoryIndex].equals("Special Offers")) {
        currentCategoryIndex++;
        updateGrid();
      } else {
        currentCategoryIndex = 0;
        updateGrid();
      }
    });*/

    // Make arrow buttons left + right centered vertically
    VBox leftArrowVcentered = new VBox(leftArrowButton);
    leftArrowVcentered.setAlignment(Pos.CENTER);
    VBox rightArrowVcentered = new VBox(rightArrowButton);
    rightArrowVcentered.setAlignment(Pos.CENTER);


    // TODO: insert item layout here


    // Middle section

    // Adding all Objects/Boxes into middle part of layout
    // TODO: add items in between arrow buttons
    HBox middleSection = new HBox();
    middleSection.setAlignment(Pos. CENTER);
    middleSection.getChildren().addAll(
        leftArrowButton, rightArrowButton
    );


    // Bottom buttons

    HBox bottomButtons = new HBox();
    bottomButtons.setPadding(new Insets(10));
    

    // Swedish Flag - Language button
    // Get image
    ImageView sweFlag = new ImageView(new Image(getClass().getResourceAsStream("/swe.png")));

    // Set sizes
    sweFlag.setFitWidth(30);
    sweFlag.setFitHeight(30);
    sweFlag.setPreserveRatio(true);

    // Create actual language button - putting it all together
    Button langButton = new Button();
    langButton.setGraphic(sweFlag);
    langButton.setStyle("-fx-background-color: transparent;");
    langButton.setMinSize(40, 40);


    // Spacer to push right buttons
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);


    // Create back button
    Button backButton = new Button();
    ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/back.png")));

    // Adjust asthetics of button
    backIcon.setFitWidth(30);
    backIcon.setFitHeight(30);
    backButton.setGraphic(backIcon);
    backButton.setStyle("-fx-background-color: transparent;");
    backButton.setMinSize(40, 40);
    backButton.setOnAction(e -> primaryStage.setScene(mainMenuScreen));


    // TODO: Create Cancel Button that cancels the whole order and sends 
    //       user back to Welcome screen


    // Added all components for the bottom part
    // TODO: insert cancel button here
    bottomButtons.getChildren().addAll(langButton, spacer, backButton);



    //layout.setBottom(new VBox(bottomButtons));
  
    // Add layout to Stack Pane for dynamic sizing
    //StackPane mainPane = new StackPane(layout);
    //mainPane.setPrefSize(windowWidth, windowHeight);

    //HBox.setHgrow(spacer, Priority.ALWAYS);

    //HBox itemDetails = new HBox(50, leftSide);
    //itemDetails.setAlignment(Pos.CENTER_LEFT);

    //HBox topRightImage = new HBox(30);
    //topRightImage.setAlignment(Pos.TOP_RIGHT);
    //topRightImage.getChildren().addAll(imageView);
  
    // Box for add to cart and back
    //HBox bottomRightBox = new HBox(30);
    //bottomRightBox.setAlignment(Pos.BOTTOM_RIGHT);
    //bottomRightBox.getChildren().addAll(backButton, cancelButton);
    //bottomRightBox.getChildren().addAll(backButton);

    // Swedish flag on the left
    //HBox bottomLeftBox = new HBox(langButton);
    //bottomLeftBox.setAlignment(Pos.CENTER_LEFT);

    //HBox bottomContainer = new HBox();
    //bottomContainer.setPadding(new Insets(10, 75, 30, 5)); // Top, Right, Bottom, Left padding
    //bottomContainer.getChildren().addAll(bottomLeftBox, spacer, bottomRightBox);

    //BorderPane layout = new BorderPane();
    //layout.setPadding(new Insets(20));
    //layout.setCenter(itemDetails);
    //layout.setBottom(bottomContainer);
    //layout.setTop(topRightImage);


    // Stacking all Objects/Boxes vertically on each other
    VBox layout = new VBox();

    layout.setAlignment(Pos.CENTER);
    layout.getChildren().addAll(
        checkoutLabel,
        middleSection,
        bottomButtons
    );

    // Create final scene result
    return new Scene(layout, windowWidth, windowHeight);
  }
}
