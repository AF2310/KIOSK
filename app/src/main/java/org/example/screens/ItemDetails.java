package org.example.screens;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.example.boxes.AddRemoveBlock;
import org.example.buttons.ArrowButton;
import org.example.buttons.MidButtonWithImage;
import org.example.buttons.RoundButton;
import org.example.buttons.SquareButtonWithImg;
import org.example.menu.Ingredient;
import org.example.menu.Single;
import org.example.orders.Cart;

/**
 * Screen for the details of an Item.
 * Customer should be able to adjust an Item here
 * (add, remove ingredients and such).
 */
public class ItemDetails {

  /**
   * Creating a scene for a specific item, displaying all item details.
   * Only for single items
   *
   * @param primaryStage what is the primary stage
   * @param prevScene what was the previous stage
   * @param item the item object itself
   * @param cart the cart where all items are
   * @return scene containing all item details
   */
  public Scene create(Stage primaryStage, Scene prevScene, Single item, Cart cart) 
      throws SQLException {
    item.setIngredients(DriverManager.getConnection(
        "jdbc:mysql://bdzvjxbmj2y2atbkdo4j-mysql.services"
          + ".clever-cloud.com:3306/bdzvjxbmj2y2atbkdo4j"
          + "?user=u5urh19mtnnlgmog"
          + "&password=zPgqf8o6na6pv8j8AX8r"
          + "&useSSL=true"
          + "&allowPublicKeyRetrieval=true"));
    List<Ingredient> ingredients = item.ingredients;
    List<Integer> quantities = item.quantity;

    VBox ingredientBox = new VBox(10);
    ingredientBox.setAlignment(Pos.TOP_RIGHT);

    int visibleCount = 7;
    // Wraps the index in an array
    final int[] currentStartIndex = {0};

    // Storing our AddRemoveBlocks to store quantities
    List<AddRemoveBlock> blocks = new ArrayList<>();

    // Pre-creating the Blocks corresponding to ingredients.size()
    // To populate blocks
    for (int i = 0; i < ingredients.size(); i++) {

      blocks.add(new AddRemoveBlock(quantities.get(i)));

    }

    // Adds the first 7 ingredients
    for (int i = 0; i < Math.min(visibleCount, ingredients.size()); i++) {
      Label ingrLabel = new Label(ingredients.get(i).getName());
      ingrLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: normal;");

      // Calling on corresponding block from List
      AddRemoveBlock addRemoveBlock = blocks.get(i);

      HBox row = new HBox(100, ingrLabel, addRemoveBlock);
      row.setAlignment(Pos.CENTER_RIGHT);
      ingredientBox.getChildren().add(row);
    }

    // Scroll button (initially facing down)
    ArrowButton scrollButton = new ArrowButton(true, false);
    scrollButton.setRotate(-90);

    // Set the action for the scroll button
    scrollButton.setOnAction(e -> {
      // Logic to handle scrolling
      if (currentStartIndex[0] + visibleCount >= ingredients.size()) {
        // If we are at the bottom, this resets the index to 0 and scrolls back to the top
        currentStartIndex[0] = 0;
        // And sets arrow to face down
        scrollButton.setRotate(-90);
      } else {
        // Otherwise, scroll down by incrementing the index
        currentStartIndex[0]++;
        // If it reaches the bottom, it turns the arrow up
        if (currentStartIndex[0] + visibleCount >= ingredients.size()) {
          scrollButton.setRotate(90);
        }
      }

      // Clear and refill VBox with updated ingredients
      ingredientBox.getChildren().clear();
      for (int i = currentStartIndex[0];
          i < Math.min(currentStartIndex[0] + visibleCount, ingredients.size());
          i++) {

        Label ingrLabel = new Label(ingredients.get(i).getName());
        ingrLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: normal;");

        // Calling on corresponding block from List
        AddRemoveBlock addRemoveBlock = blocks.get(i);

        HBox row = new HBox(100, ingrLabel, addRemoveBlock);
        row.setAlignment(Pos.CENTER_RIGHT);
        ingredientBox.getChildren().add(row);
      }
    });

    // Putting the ingredient box and the scroll button together in a vboc
    VBox ingredientListBox = new VBox(20, ingredientBox, scrollButton);
    ingredientListBox.setAlignment(Pos.CENTER);

    // Item label
    Label nameLabel = new Label(item.getName());
    nameLabel.setStyle(
        "-fx-font-size: 65px;"
        + "-fx-font-weight: bold;"
    );

    // TODO: Add description to the item once it has one. This is dummy text
    var descriptionLabel = new Label("This is a yummy " + item.getName().toLowerCase());
    descriptionLabel.setStyle(
        "-fx-font-size: 20px;"
        + "-fx-font-weight: normal;"
    );

    // Left side of the top part of the screen
    VBox nameAndDescriptionBox = new VBox(20);
    nameAndDescriptionBox.getChildren().addAll(nameLabel, descriptionLabel);
    VBox leftSide = new VBox(100);
    leftSide.setAlignment(Pos.TOP_CENTER);
    leftSide.setPadding(new Insets(0, 0, 0, 100));
    leftSide.getChildren().addAll(nameAndDescriptionBox, ingredientListBox);

    InputStream inputStream = getClass().getResourceAsStream(item.getImagePath());

    // Initiating the image view
    ImageView imageView;

    // Errorhandling when no image found
    if (inputStream == null) {
      System.err.println("ERROR: Image not found - " + item.getName());

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
    Label priceLabel = new Label(String.format("%.0f :-", item.getPrice()));
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
    
    backButton.setOnAction(e -> {
      save(blocks, quantities, item);
      primaryStage.setScene(prevScene);
    });
    
    // HBox for the upper part of the screen
    HBox topContainer = new HBox();
    topContainer.setPadding(new Insets(20));
    topContainer.setAlignment(Pos.CENTER);
    topContainer.getChildren().addAll(leftSide, rightSide);
    
    MidButtonWithImage addToCartButton = new MidButtonWithImage("Add To Cart",
        "cart_wh.png", 
        "rgb(81, 173, 86)");
    
    addToCartButton.setOnAction(e -> {
      save(blocks, quantities, item);
      cart.addProduct(item);
      primaryStage.setScene(prevScene);
    });

    // Box for add to cart and back
    HBox bottomRightBox = new HBox(30);
    bottomRightBox.setAlignment(Pos.CENTER_RIGHT);
    bottomRightBox.getChildren().addAll(addToCartButton, backButton);
    
    // Language Button
    // cycles images on click
    RoundButton langButton = new RoundButton("languages", 70);
    HBox bottomLeftBox = new HBox(langButton);
    bottomLeftBox.setAlignment(Pos.BOTTOM_LEFT);
    
    // Spacer for bottom part of the Screen
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

  /**
   * Method to save the quantites.
   *
   * @param blocks the list of add and remove blocks
   * @param quantitys the list of quantites
   * @param item the item
   */
  private void save(List<AddRemoveBlock> blocks, List<Integer> quantitys, Single item) {
    for (int i = 0; i < quantitys.size(); i++) {
      quantitys.set(i, blocks.get(i).getQuantity());
    }
    item.quantity = quantitys;
  }
}
