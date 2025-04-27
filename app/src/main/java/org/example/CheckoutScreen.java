package org.example;

//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import com.mysql.cj.protocol.Resultset;
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
import org.example.menu.*;

/**
 * This is the Screen that displays the order
 * of the customer. Here, the customer can check, 
 * edit and confirm his order. It also displays the
 * final price of the order.
 */
public class CheckoutScreen {

  private Stage primaryStage;
  // private String mode;

  /**
   * Creating a scene for the checkout menu.
   * TODO: We still need the other database variables etc..
   *       Most likely not all the variables that are needed.
   *
   * @param primaryStage the primary stage of this scene
   * @param windowWidth width of window
   * @param windowHeight height of window
   * @param mainMenuScreen the previous scene of this scene
   * @param welcomeScrScene the welcome screen (for cancel order button)
   * @param orderId the id of the order (database)F
   * @return scene containing all the order details
   */
  public Scene createCheckoutScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene mainMenuScreen,
      Scene welcomeScrScene,
      String mode,
      Cart cart) {

    // Setting primary stage and welcome screen
    this.primaryStage = primaryStage;
    // this.mode = mode;

    // Spacer to elements away from each other
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox modeIndicatorBox = new HBox();
    modeIndicatorBox.setAlignment(Pos.CENTER);

    if ("takeaway".equalsIgnoreCase(mode)) {
      TakeAwayButton takeawayButton = new TakeAwayButton();
      modeIndicatorBox.getChildren().add(takeawayButton);
    } else {
      EatHereButton eatHereButton = new EatHereButton();
      modeIndicatorBox.getChildren().add(eatHereButton);
    }


    // Top of layout - creating elements


    // Title of the checkout screen
    Label checkoutLabel = new Label("Checkout");
    checkoutLabel.setStyle(
        "-fx-font-size: 60px;"
        + "-fx-font-weight: bold;"
    );
    // Alignment of label
    checkoutLabel.setAlignment(Pos.TOP_LEFT);
    checkoutLabel.setPadding(new Insets(50, 100, 50, 100));

    // Eat here or takeaway choice
    EatHereButton eatHereButton = new EatHereButton();
    TakeAwayButton takeawayButton = new TakeAwayButton();
    
    // Combine both in horizontal layout
    HBox eatHereTakeawayBox = new HBox(
        50,
        eatHereButton,
        takeawayButton
    );
    // Align box properly
    eatHereTakeawayBox.setAlignment(Pos.CENTER);

    // TODO: Insert promo code section here


    // Top of layout - combining elements


    HBox topBox = new HBox();
    topBox.setAlignment(Pos.TOP_LEFT);
    topBox.getChildren().addAll(
        //spacer,
        checkoutLabel,
        eatHereTakeawayBox
        // TODO: add promocode box here
    );


    // Middle part - creating elements


    // Arrow buttons to navigate through order pages

    // Arrow left
    // Make instance of arrow button that points left
    ArrowButton leftArrowButton = new ArrowButton(true, false);

    // TODO: insert action of button
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
    
    // TODO: insert action of button
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

    // Make arrow buttons left and right most position horizontally
    HBox leftMostArrow = new HBox(leftArrowButton);
    leftMostArrow.setAlignment(Pos.CENTER_LEFT);
    HBox rightMostArrow = new HBox(rightArrowButton);
    rightMostArrow.setAlignment(Pos.CENTER_RIGHT);


    // Make item grid - CURRENTLY DUMMY CODE
    // TODO: Fix layout, make multiple pages
    // TODO: Replace dummy code with actual item grid code

    VBox itemGrid = new VBox();
    SimpleItem[] items = cart.getItems();
    int[] quantitys = cart.getQuantity();
    HBox itemRow = new HBox();
    for (int i = 0; i < items.length; i++) {
      SimpleItem item = items[i];
      int quantity = quantitys[i];
      // Fixed sized slot for image
      StackPane imageSlot = new StackPane();
      imageSlot.setPrefSize(200, 200);
      imageSlot.setMaxSize(200, 200);
      imageSlot.setMinSize(200, 200);
      Image itemImage = new Image(item.imagePath());
      ImageView image = new ImageView(itemImage);
      image.setFitHeight(150);
      image.setPreserveRatio(true);
      imageSlot.getChildren().addAll(image);
      
      // Slot for Label and Price
      HBox labelAndPrice = new HBox();
      labelAndPrice.setAlignment(Pos.CENTER);
      labelAndPrice.getChildren().addAll(
        new Label(item.name()),
        new Label(String.format(" %.0f :-", item.price()))
      );
      
      // Slot for Plus-/Minus Buttons and Quantity value
      HBox quantityBox = new HBox();
      quantityBox.setAlignment(Pos.CENTER);
      quantityBox.getChildren().addAll(
        new AddRemoveBlock(quantity)
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

    
    // Middle section - combining all elements


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
    
    // Swedish Flag - Language button
    // Set image
    ImageView sweFlag = new ImageView(new Image(getClass().getResourceAsStream("/swe.png")));

    // Set sizes and make it scalable in a proper way
    sweFlag.setFitWidth(30);
    sweFlag.setFitHeight(30);
    sweFlag.setPreserveRatio(true);

    // Create actual language button - putting it all together
    Button langButton = new Button();
    langButton.setGraphic(sweFlag);
    langButton.setStyle("-fx-background-color: transparent;");
    langButton.setMinSize(40, 40);

    // TODO: add price fetching from database to insert correct price

    // Retrieve total price with query
    /* String sqlQuery = "SELECT amount_total FROM product WHERE order_ID = ?";

    // try () ensures automatic closing
    try (PreparedStatement s = conn.prepareStatement(sqlQuery)) {

      // Bind order id to sql query
      s.setString((orderId), sqlQuery);

      // Execute query
      try (ResultSet r = s.executeQuery()) {
        // Store total price in variable
        int price = r.getInt("amount_total");
      }
    } */

    // Create confirm order button instance
    ConfirmOrderButton confirmOrderButton = new ConfirmOrderButton(100);

    // User confirms order
    confirmOrderButton.setOnAction(e -> {
      // Create order confirmation screen
      OrderConfirmationScreen ordConfirmation = new OrderConfirmationScreen();

      Scene ordConfirmScene = ordConfirmation.createOrderConfirmationScreen(
          this.primaryStage,
          windowWidth,
          windowHeight,
          welcomeScrScene,
          50  // Dummy code for price variable
      );
      this.primaryStage.setScene(ordConfirmScene);

      // Creating fading animation
      // to transition smoothly to welcome screen after order confirmation
      FadingAnimation fadingAnimation = new FadingAnimation(primaryStage);
      // Fading out of current scene
      fadingAnimation.fadeOutAnimation(
          "white",
          ordConfirmScene,
          welcomeScrScene
      );
    });

    // Back button
    BackButton backButton = new BackButton();
    // clicking button means user goes to previous screen
    backButton.setOnAction(e -> primaryStage.setScene(mainMenuScreen));

    // Cancel button
    CancelButton cancelButton = new CancelButton();
    // clicking button means cancellation of order
    // and user gets send back to welcome screen
    cancelButton.setOnAction(e -> {
      System.out.println("Order canceled!");
      primaryStage.setScene(welcomeScrScene);
    });

    // Bottom part - adding all elements together
    
    // Swedish flag on the left
    HBox languageBox = new HBox(langButton);
    languageBox.setAlignment(Pos.BOTTOM_LEFT);

    HBox confirmOrderBox = new HBox(confirmOrderButton);
    confirmOrderBox.setAlignment(Pos.BOTTOM_CENTER);
    
    // Box for cancel order and back button
    HBox backAndCancel = new HBox(30);
    backAndCancel.setAlignment(Pos.BOTTOM_RIGHT);
    backAndCancel.getChildren().addAll(backButton, cancelButton);

    // Combine all
    HBox bottomPart = new HBox();
    // Top, Right, Bottom, Left padding
    bottomPart.setPadding(new Insets(30, 75, 10, 5));
    bottomPart.getChildren().addAll(
        languageBox,
        confirmOrderBox,
        backAndCancel
    );


    // Stacking all Objects/Boxes vertically on each other
    VBox layout = new VBox();

    layout.setAlignment(Pos.CENTER);
    layout.setPadding(new Insets(20));
    layout.getChildren().addAll(
        topBox,
        modeIndicatorBox,
        middleSection,
        bottomPart
    );

    // Create final scene result
    return new Scene(layout, windowWidth, windowHeight);
  }
}