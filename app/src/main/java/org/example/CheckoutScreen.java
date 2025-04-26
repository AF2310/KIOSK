package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

  // TODO: fix later if needed otherwise remove and alter code accordingly
  @SuppressWarnings("unused")
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


    // Top of layout


    // Title of the checkout screen
    Label checkoutLabel = new Label("Checkout");
    checkoutLabel.setStyle(
        "-fx-font-size: 60px;"
        + "-fx-font-weight: bold;"
    );

    checkoutLabel.setAlignment(Pos.TOP_LEFT);
    checkoutLabel.setPadding(new Insets(50));


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

    // Make arrow buttons left and right most position
    HBox leftMostArrow = new HBox(leftArrowButton);
    leftMostArrow.setAlignment(Pos.CENTER_LEFT);
    HBox rightMostArrow = new HBox(rightArrowButton);
    rightMostArrow.setAlignment(Pos.CENTER_RIGHT);


    // Make item grid - CURRENTLY DUMMY CODE
    // TODO: insert item layout here

    // Making 2 rows of 4 item slots each
    VBox itemGrid = new VBox();
    for (int j = 0; j < 2; j++) {
      
      // Making row of 4 item slots
      HBox itemRow = new HBox();
      for (int i = 0; i < 5; i++) {
        // Fixed sized slot for image
        StackPane imageSlot = new StackPane();
        imageSlot.setPrefSize(200, 200);
        imageSlot.setMaxSize(200, 200);
        imageSlot.setMinSize(200, 200);
        
        // Slot for Label and Price
        HBox labelAndPrice = new HBox();
        labelAndPrice.setAlignment(Pos.CENTER);
        labelAndPrice.getChildren().addAll(
          new Label("name"),
          new Label("price")
        );
          
        // Slot for Plus-/Minus Buttons and Quantity value
        HBox quantityBox = new HBox();
        quantityBox.setAlignment(Pos.CENTER);
        quantityBox.getChildren().addAll(
          new CircleButtonWithSign("-"),
          new Label("quantity"),
          new CircleButtonWithSign("+")
        );
          
        // Adding it all together in one item slot
        VBox itemSlot = new VBox();
        itemSlot.setAlignment(Pos.CENTER);
        itemSlot.getChildren().addAll(
            imageSlot,
            labelAndPrice,
            quantityBox
        );
        itemRow.getChildren().add(itemSlot);
      }

      itemGrid.getChildren().add(itemRow);
    }

    
    // Middle section


    // Create spacers
    Region leftSpacer = new Region();
    Region rightSpacer = new Region();

    // Make spacers grow to fill space as much as needed
    // to make arrow buttons most left and most right positioned
    HBox.setHgrow(leftSpacer, Priority.ALWAYS);
    HBox.setHgrow(rightSpacer, Priority.ALWAYS);
    
    
    // Combine Arrow buttons and item grid
    HBox middleSection = new HBox();
    middleSection.setAlignment(Pos. CENTER);
    middleSection.getChildren().addAll(
        leftMostArrow, leftSpacer,
        itemGrid,
        rightSpacer, rightArrowButton
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

  
    // Box for add to cart and back
    HBox backAndCancel = new HBox(30);
    backAndCancel.setAlignment(Pos.BOTTOM_RIGHT);
    // bottomRightBox.getChildren().addAll(backButton, cancelButton);
    backAndCancel.getChildren().addAll(backButton);

    // Swedish flag on the left
    HBox languageBox = new HBox(langButton);
    languageBox.setAlignment(Pos.CENTER_LEFT);

    HBox bottomPart = new HBox();
    bottomPart.setPadding(new Insets(10, 75, 30, 5)); // Top, Right, Bottom, Left padding
    bottomPart.getChildren().addAll(languageBox, spacer, backAndCancel);


    HBox topBox = new HBox();
    topBox.setAlignment(Pos.TOP_LEFT);
    topBox.getChildren().addAll(spacer, checkoutLabel);


    // Stacking all Objects/Boxes vertically on each other
    VBox layout = new VBox();

    layout.setAlignment(Pos.CENTER);
    layout.setPadding(new Insets(20));
    layout.getChildren().addAll(
        topBox,
        middleSection,
        bottomPart
    );

    // Create final scene result
    return new Scene(layout, windowWidth, windowHeight);
  }
}
