package org.example;

import java.io.InputStream;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
  public Scene create(Stage primaryStage, Scene prevScene, String name, String imagePath) {

    ImageView sweFlag = new ImageView(new Image(getClass().getResourceAsStream("/swe.png")));
    // Set sizes
    sweFlag.setFitWidth(60);
    sweFlag.setFitHeight(60);
    sweFlag.setPreserveRatio(true);

    // Create actual language button - putting it all together
    Button langButton = new Button();
    langButton.setGraphic(sweFlag);
    langButton.setStyle("-fx-background-color: transparent;");
    langButton.setMinSize(40, 40);

    List<String> ingredients = List.of("Sesame bun", "Cheese",
        "Onion", "Tomatoes", "Celery", "Cucumber");

    VBox ingredientListBox = new VBox(10);

    for (String ingredientName : ingredients) {
      Label nameLabel = new Label(ingredientName);
      nameLabel.setMinWidth(120);

      Label quantityLabel = new Label("1");

      CircleButtonWithSign minusButton = new CircleButtonWithSign("-");
      CircleButtonWithSign plusButton = new CircleButtonWithSign("+");

      minusButton.setOnAction(e -> {
        int quantity = Integer.parseInt(quantityLabel.getText());
        if (quantity > 1) {
          quantity--;
          quantityLabel.setText(String.valueOf(quantity));
        }
        if (quantity == 1) {
          minusButton.setInvalid(true);
        } else {
          minusButton.setInvalid(false);
        }

        if (quantity < 9) {
          plusButton.setInvalid(false);
        }
      });

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

        if (quantity > 1) {
          minusButton.setInvalid(false);
        }
      });

      minusButton.setInvalid(true);

      HBox row = new HBox(10, nameLabel, minusButton, quantityLabel, plusButton);
      row.setAlignment(Pos.CENTER_LEFT);
      ingredientListBox.getChildren().add(row);
    }

    Label nameLabel = new Label(name);
    nameLabel.setStyle(
        "-fx-font-size: 20px;"
        + "-fx-font-weight: bold;"
    );

    VBox leftSide = new VBox(20);
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

    imageView.setFitHeight(200);
    imageView.setPreserveRatio(true);

    HBox itemDetails = new HBox(50, leftSide, imageView);
    itemDetails.setAlignment(Pos.CENTER);

    MidButtonWithImage addToCartButton = new MidButtonWithImage("Add To Cart", "cart_wh.png", 
          "rgb(81, 173, 86)");

    SquareButtonWithImg backButton = new SquareButtonWithImg("Back",
                    "back.png",
                    "rgb(255, 255, 255)");

    backButton.setOnAction(e -> primaryStage.setScene(prevScene));

    HBox bottomRightButtons = new HBox(30);
    bottomRightButtons.setAlignment(Pos.BOTTOM_RIGHT);
    bottomRightButtons.getChildren().addAll(addToCartButton, backButton);

    HBox bottomContainer = new HBox();
    bottomContainer.setPadding(new Insets(10, 20, 10, 5)); // Top, Right, Bottom, Left padding
    

    // Swedish flag on the left
    HBox bottomLeftBox = new HBox(langButton);
    bottomLeftBox.setAlignment(Pos.CENTER_LEFT);

    bottomContainer.getChildren().addAll(bottomLeftBox, bottomRightButtons);

    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(20));
    layout.setCenter(itemDetails);
    layout.setBottom(bottomContainer);

    return new Scene(layout, 1920, 1080);

  }
}
