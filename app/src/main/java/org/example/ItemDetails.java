package org.example;

import java.io.InputStream;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Screen for the details of an Item.
 * Customer should be able to adjust an Item here
 * (add, remove ingredients and such).
 */
public class ItemDetails {

  /**
   * Creating a scene for a specific item, displaying all item details.
   *
   * @param primaryStage what is the primary stage
   * @param prevScene what was the previous stage
   * @param name name of the item
   * @param imagePath path to the item's image
   * @return scene containing all item details
   */
  public Scene create(Stage primaryStage, Scene prevScene, String name,
      String imagePath, double price) {

    //Just a test list of ingredients
    List<String> ingredients = List.of("Sesame bun", "Cheese",
        "Onion", "Tomatoes", "Celery", "Cucumber");

    VBox ingredientListBox = new VBox(10);

    // Making a line with the ingredient name, the minus and plus buttons and the quantity,
    // for every ingredient in the test list.
    for (String ingredientName : ingredients) {
      Label ingredientLabel = new Label(ingredientName);
      ingredientLabel.setMinWidth(120);
      ingredientLabel.setStyle("-fx-font-size: 25px;");
      ingredientLabel.setPadding(new Insets(0, 0, 0, 100));

      Label quantityLabel = new Label("1");
      quantityLabel.setStyle("-fx-font-size: 25px;");

      CircleButtonWithSign minusButton = new CircleButtonWithSign("-");
      CircleButtonWithSign plusButton = new CircleButtonWithSign("+");

      // If the quantity is 0, this makes the minus button unclickable then,
      // and the plus button is clickable.
      minusButton.setOnAction(e -> {
        int quantity = Integer.parseInt(quantityLabel.getText());
        if (quantity > 0) {
          quantity--;
          quantityLabel.setText(String.valueOf(quantity));
        }
        if (quantity == 0) {
          minusButton.setInvalid(true);
        } else {
          minusButton.setInvalid(false);
        }

        if (quantity < 9) {
          plusButton.setInvalid(false);
        }
      });

      // If the quantity is 9, this makes the plus button unclickable.
      plusButton.setOnAction(e -> {
        int quantity = Integer.parseInt(quantityLabel.getText());

        if (quantity < 9) {
          quantity++;
          quantityLabel.setText(String.valueOf(quantity));
        }
        
        if (quantity == 9) {
          plusButton.setInvalid(true);
        } else {
          plusButton.setInvalid(false);
        }

        if (quantity > 0) {
          minusButton.setInvalid(false);
        }
      });

      Region detailsSpacer = new Region();
      HBox.setHgrow(detailsSpacer, Priority.ALWAYS);

      // Putting the ingredient elements in an hbox for every element.
      HBox row = new HBox(25, ingredientLabel, detailsSpacer,
          minusButton, quantityLabel, plusButton);
      row.setAlignment(Pos.CENTER_RIGHT);
      ingredientListBox.getChildren().add(row);
    }

    // Item labe
    Label nameLabel = new Label(name);
    nameLabel.setStyle(
        "-fx-font-size: 65px;"
        + "-fx-font-weight: bold;"
    );

    // Left side of the top part of the screen
    VBox leftSide = new VBox(100);
    leftSide.setAlignment(Pos.TOP_CENTER);
    leftSide.setPadding(new Insets(0, 0, 0, 100));
    leftSide.getChildren().addAll(nameLabel, ingredientListBox);

    InputStream inputStream = getClass().getResourceAsStream(imagePath);

    // Initiating the image view
    ImageView imageView;

    // Errorhandling when no image found
    if (inputStream == null) {
      System.err.println("ERROR: Image not found - " + imagePath);

      // using Base64-encoded string to generate 1x1 transparent PNG
      // This Base64 string is decoded into a transparent image when
      // the Image constructor is called.
      // This prevents fetching some empty image from the database.
      Image emptyImage = new Image(
          "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABC"
          + "AQAAAC1HAwCAAAAC0lEQVR42mP8/wcAAwAB/hd5JnkAAAAASUVORK5CYII="
        );

      // Add empty generated image to View
      imageView = new ImageView(emptyImage);

    // Image found (input stream not empty)
    } else {
      imageView = new ImageView(new Image(inputStream));
    }

    imageView.setFitHeight(400);
    imageView.setPreserveRatio(true);

    // Price underneath the picture
    Label priceLabel = new Label(String.format("%.0f :-", price));
    priceLabel.setStyle(
        "-fx-font-size: 35px;"
        + "-fx-font-weight: bold;"
    );
        
    // Wrapper to align the Label in its VBox
    HBox priceWrapper = new HBox(priceLabel);
    priceWrapper.setAlignment(Pos.BOTTOM_RIGHT);

    // VBox for the Picture
    VBox rightSide = new VBox(20);
    rightSide.setPadding(new Insets(0, 200, 0, 0));
    rightSide.setAlignment(Pos.CENTER);
    rightSide.getChildren().addAll(imageView, priceWrapper);


    
    SquareButtonWithImg backButton = new SquareButtonWithImg("Back",
        "back.png",
        "rgb(255, 255, 255)");
    
    backButton.setOnAction(e -> primaryStage.setScene(prevScene));
    
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    
    HBox itemDetails = new HBox(50, leftSide);
    itemDetails.setAlignment(Pos.CENTER_LEFT);
    
    HBox topRightImage = new HBox(30);
    topRightImage.setAlignment(Pos.TOP_RIGHT);
    topRightImage.getChildren().addAll(imageView);
    
    // HBox for the upper part of the screen
    HBox topContainer = new HBox();
    topContainer.setPadding(new Insets(20));
    topContainer.setAlignment(Pos.CENTER);
    topContainer.getChildren().addAll(leftSide, rightSide);
    
    MidButtonWithImage addToCartButton = new MidButtonWithImage(
          "Add To Cart",
          "cart_wh.png", 
          "rgb(81, 173, 86)");

    // Box for add to cart and back
    HBox bottomRightBox = new HBox(30);
    bottomRightBox.setAlignment(Pos.CENTER_RIGHT);
    bottomRightBox.getChildren().addAll(addToCartButton, backButton);
    
    // Language Button
    // cycles images on click
    RoundButton langButton = new RoundButton("languages", 70);

    // Swedish flag on the left 
    HBox bottomLeftBox = new HBox(langButton);
    bottomLeftBox.setAlignment(Pos.BOTTOM_LEFT);
    
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);

    // Bottom part of the screen
    HBox bottomContainer = new HBox();
    bottomContainer.setPadding(new Insets(0, 0, 0, 0));
    bottomContainer.getChildren().addAll(bottomLeftBox, spacerBottom,  bottomRightBox);
    bottomContainer.setAlignment(Pos.CENTER);
  
    // Setting positioning of all the elements
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setTop(topContainer);
    layout.setLeft(leftSide);
    layout.setRight(rightSide);
    layout.setBottom(bottomContainer);

    return new Scene(layout, 1920, 1080);

  }
}
