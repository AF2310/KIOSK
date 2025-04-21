package org.example;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.menu.Imenu;
import org.example.menu.Menu;
import org.example.menu.Single;

import javafx.geometry.Insets;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * The main menu screen.
 */
public class MainMenuScreen {

  private Stage primaryStage;

  private record SimpleItem(String name, String imagePath, double price) {}

  private final String[] categories = {"Burgers", "Sides", "Drinks", "Desserts", "Special Offers"};
  private final Map<String, List<SimpleItem>> categoryItems = new HashMap<>();
  private int currentCategoryIndex = 0;
  private final GridPane itemGrid = new GridPane();
  private final List<Button> categoryButtons = new ArrayList<>();

  /**
   * Creates the main menu scene.
   *
   * @param primaryStage the stage
   * @param windowWidth the width of the window
   * @param windowHeight the height of the window
   * @param welcomeScrScene the scene to return to on cancel
   * @return the created scene
   * Creates the main menu scene.
   *
   * @param primaryStage the stage
   * @param windowWidth the width of the window
   * @param windowHeight the height of the window
   * @param welcomeScrScene the scene to return to on cancel
   * @return the created scene
   * @throws SQLException error server quick fix
   */
  public Scene createMainMenuScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene) throws SQLException {

    this.primaryStage = primaryStage;

    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(20));

    // Top area layout
    VBox top = new VBox(10);
    top.setAlignment(Pos.CENTER);

    // Category bar

    // Alignment with horizontal box
    HBox categoryBar = new HBox(15);
    categoryBar.setAlignment(Pos.CENTER);

    // Setting category bar; making each category clickable
    for (int i = 0; i < categories.length; i++) {
      // Getting current category
      String cat = categories[i];

      // Making a button for each category
      Button btn = new Button(cat);
      
      // Special offers needs special handling for asthetics of button
      if (cat.equalsIgnoreCase("Special Offers")) {
        // 2-lined text needs centering
        btn.setTextAlignment(TextAlignment.CENTER);

        // Button will now decide its' own size -> label decides
        btn.setMaxWidth(Double.MAX_VALUE);

        // Button asthetics
        // Transparent to make circle behind button visible instead
        btn.setStyle(
            "-fx-background-color: transparent;"
            + "-fx-padding: 0px;"
        );

        // Make circle with noticible color
        Circle specialsCircle = new Circle(100);
        specialsCircle.setFill(Color.GOLD);

        // Creating Stackpane to stack label over circle
        StackPane specialsStack = new StackPane(specialsCircle, btn);

        // 
        specialsStack.setAlignment(Pos.CENTER);
        specialsStack.setPrefSize(200, 200);

        categoryBar.getChildren().add(specialsStack);

      // Any other category that is not special offers
      } else {

        categoryBar.getChildren().add(btn);
      }
      
      // Button asthetics -> Label highlighting
      styleCategoryButton(btn, i == currentCategoryIndex, i);

      // Action when button clicked
      final int index = i;
      btn.setOnAction(e -> {
        currentCategoryIndex = index;
        updateGrid();
        updateCategoryButtonStyles();
      });

      // Add final button to horizontal category bar
      //categoryBar.getChildren().add(btn);
      categoryButtons.add(btn);
    }
    
    // Adding it all together
    top.getChildren().add(categoryBar);

    // setting category bar on top of menu layout
    layout.setTop(top);

    // menu items - MIDDLE PART
    setupMenuData();
    updateGrid();

    // Arrow buttons to navigate menu

    // Arrow left
    Image leftArrow = new Image(getClass().getResourceAsStream("/nav_bl.png"));
    ImageView leftArrowView = new ImageView(leftArrow);
    leftArrowView.setFitHeight(40);
    leftArrowView.setPreserveRatio(true);

    // Wrap arrow ImageView in a StackPane to make borders of button visible
    StackPane leftArrowWrapper = new StackPane(leftArrowView);
    leftArrowWrapper.setStyle(
        "-fx-border-color: black;"
        + "-fx-border-width: 3px;"
        + "-fx-border-radius: 9px;" // For round borders
        + "-fx-padding: 10px;"      // For wider clickable area
    );
    // Setting border size (yes this much is needed)
    leftArrowWrapper.setMinWidth(60);
    leftArrowWrapper.setMaxWidth(60);
    leftArrowWrapper.setPrefWidth(60);
    leftArrowWrapper.setMinHeight(300);
    leftArrowWrapper.setMaxHeight(300);
    leftArrowWrapper.setPrefHeight(300);

    // left button clickable as long as it's still inside set bounds (>0)
    leftArrowWrapper.setOnMouseClicked(e -> {
      if (currentCategoryIndex > 0) {
        currentCategoryIndex--;
        updateGrid();
      } else {
        currentCategoryIndex = categoryButtons.size() - 1;
        updateGrid();
      }
    });

    // Arrow right
    // Uses same image as left button just mirrowed
    ImageView rightArrowView = new ImageView(leftArrow);
    rightArrowView.setFitHeight(40);
    rightArrowView.setPreserveRatio(true);
    rightArrowView.setScaleX(-1);

    // Wrap arrow ImageView in a StackPane to make borders of button visible
    StackPane rightArrowWrapper = new StackPane(rightArrowView);
    rightArrowWrapper.setStyle(
          "-fx-border-color: black;"
          + "-fx-border-width: 3px;"
          + "-fx-border-radius: 9px;" // For round borders
          + "-fx-padding: 10px;"      // For wider clickable area
    );
    // Setting border size (yes this much is needed)
    rightArrowWrapper.setMinWidth(60);
    rightArrowWrapper.setMaxWidth(60);
    rightArrowWrapper.setPrefWidth(60);
    rightArrowWrapper.setMinHeight(300);
    rightArrowWrapper.setMaxHeight(300);
    rightArrowWrapper.setPrefHeight(300);

    // right button clickable as long as its not in last category (Special offers)
    rightArrowWrapper.setOnMouseClicked(e -> {
      if (currentCategoryIndex < categories.length - 1
          && !categories[currentCategoryIndex].equals("Special Offers")) {
        currentCategoryIndex++;
        updateGrid();
      } else {
        currentCategoryIndex = 0;
        updateGrid();
      }
    });

    // Make arrow buttons left right centered vertically
    VBox leftArrowVcentered = new VBox(leftArrowWrapper);
    leftArrowVcentered.setAlignment(Pos.CENTER);
    VBox rightArrowVcentered = new VBox(rightArrowWrapper);
    rightArrowVcentered.setAlignment(Pos.CENTER);

    // Add all Menu items and left right buttons in center of menu in the right order
    // Locking arrows left and right and locking menu items in middle
    BorderPane centerMenuLayout = new BorderPane();
    centerMenuLayout.setLeft(leftArrowVcentered);
    centerMenuLayout.setCenter(itemGrid);
    centerMenuLayout.setRight(rightArrowVcentered);

    // Setting center menu content to center of actual menu
    layout.setCenter(centerMenuLayout);

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

    // Create cancel button
    Button cancelButton = new Button();
    ImageView cancelIcon = new ImageView(new Image(getClass().getResourceAsStream("/cancel.png")));

    // Adjust asthetics of button
    cancelIcon.setFitWidth(30);
    cancelIcon.setFitHeight(30);
    cancelButton.setGraphic(cancelIcon);
    cancelButton.setStyle("-fx-background-color: transparent;");
    cancelButton.setMinSize(40, 40);
    cancelButton.setOnAction(e -> primaryStage.setScene(welcomeScrScene));

    // Create Cart button
    Button cartButton = new Button();
    ImageView cartIcon = new ImageView(new Image(getClass().getResourceAsStream("/cart_bl.png")));

    // Adjust asthetics of button
    cartIcon.setFitWidth(30);
    cartIcon.setFitHeight(30);
    cartButton.setGraphic(cartIcon);
    cartButton.setStyle("-fx-background-color: transparent;");
    cartButton.setMinSize(40, 40);

    // Added all components for the bottom part
    bottomButtons.getChildren().addAll(langButton, spacer, cancelButton, cartButton);
    layout.setBottom(new VBox(bottomButtons));
  
    // Add layout to Stack Pane for dynamic sizing
    StackPane mainPane = new StackPane(layout);
    mainPane.setPrefSize(windowWidth, windowHeight);


    // Create final scene result
    return new Scene(mainPane, windowWidth, windowHeight);
  }

  /**
   * Adds all menu items. Filling each item category with items.
   * Added the items for the menu one by one for now, not through the database.
   */
  /*private void setupMenuData() {
    categoryItems.put("Burgers", List.of(
        new SimpleItem("Standard Burger", "/food/standard_burger.png", 25),
        new SimpleItem("Juicy Chicken Burger", "/food/chicken_burger.png", 25),
        new SimpleItem("All American Burger", "/food/all_american_burger.png", 25),
        new SimpleItem("Double Cheese & Bacon Burger", "/food/double_burger.png", 25),
        new SimpleItem("Extra Veggies Burger", "/food/extra_vegies_burger.png", 20),
        new SimpleItem("King Burger", "/food/king_burger.png", 25)));

    categoryItems.put("Sides", List.of(
        new SimpleItem("French Fries", "/food/french_fries.png", 15),
        new SimpleItem("Greek Salad", "/food/salad.png", 25),
        new SimpleItem("Country-Style Potatoes", "/food/cs_potatoes.png", 15),
        new SimpleItem("Fried Onion Rings", "/food/rings.png", 12)));

    categoryItems.put("Drinks", List.of(
        new SimpleItem("Cola Zero", "/food/cola.png", 10),
        new SimpleItem("Fanta", "/food/fanta.png", 10),
        new SimpleItem("Americano", "/food/coffee.png", 15)));

    categoryItems.put("Desserts", List.of(
        new SimpleItem("Milkshake", "/food/Milkshake.png", 29),
        new SimpleItem("Tiramisu", "/food/tiramisu.png", 18),
        new SimpleItem("Strawberry Cupcake", "/food/cupcake.png", 12)));

    categoryItems.put("Special Offers", List.of(
        new SimpleItem("Extra Veggies Burger", "/food/extra_vegies_burger.png", 20),
        new SimpleItem("Strawberry Cupcake", "/food/cupcake.png", 12)));
  }*/

  /**
   * Loading all items into the menu's item grid.
   */
  /*private void updateGrid() {
    // Empty grid and create new layout
    itemGrid.getChildren().clear();
    itemGrid.setHgap(20);
    itemGrid.setVgap(20);
    itemGrid.setPadding(new Insets(10));
    itemGrid.setAlignment(Pos.CENTER);

    // Fetch data
    String category = categories[currentCategoryIndex];
    List<SimpleItem> items = categoryItems.get(category);

    // Max item slots in rows and pages
    int maxItemsPerRow = 3;
    int totalItemsPerPage = 6;

    // Populate the grid with item and corresponding image one by one
    for (int i = 0; i < totalItemsPerPage; i++) {

      // Create fresh item Slot
      VBox box = new VBox(10);
      box.setAlignment(Pos.CENTER);

      // Make slot with fixed size
      StackPane imageSlot = new StackPane();
      imageSlot.setPrefSize(200, 200);
      imageSlot.setMaxSize(200, 200);
      imageSlot.setMinSize(200, 200);

      // Item exists
      if (i < items.size()) {
        // Get current item
        SimpleItem item = items.get(i);
  
        // Get image path
        String imagePath = item.imagePath();
        InputStream inputStream = getClass().getResourceAsStream(imagePath);

        // Errorhandling when no image found
        if (inputStream == null) {
          System.err.println("ERROR: Image not found - " + imagePath);
        }

        // Add image to View
        ImageView imageView = new ImageView(new Image(inputStream));

        // Adjust image size but not make it blurry
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        // Move image into image slot and center image
        imageSlot.getChildren().add(imageView);
        imageSlot.setAlignment(Pos.CENTER);

        // Give item a name
        Label name = new Label(item.name());
        name.setStyle("-fx-font-size: 16px;");
        
        // Format the price (with :-)
        // Put the price in an Hbox to align it to the right
        Label price = new Label(String.format("%.0f :-", item.price()));
        price.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        HBox priceBox = new HBox(price);
        priceBox.setAlignment(Pos.BASELINE_RIGHT);
        

        // Item details
        ItemDetails detailScreen = new ItemDetails();

        // Get item details when clicking on item
        imageSlot.setOnMouseClicked(e -> {
          Scene detailScene = detailScreen.create(
              this.primaryStage,
              this.primaryStage.getScene(),
              item.name(), item.imagePath()
          );
          this.primaryStage.setScene(detailScene);
        });
  
        // Connect it all and add to item grid
        box.getChildren().addAll(imageSlot, name, priceBox);
        
      // No more items exist -> Item list empty
      // Fill the rest up with empty slots until we reach 6 slots per page
      } else {
        box.getChildren().addAll(imageSlot, new Label(""));
      }

      // Add new slot to final item grid
      itemGrid.add(box, i % maxItemsPerRow, i / maxItemsPerRow);
    }

    updateCategoryButtonStyles();
  }*/

  /**
   * helper method for dynamic category button highlighting.
   *
   * @param button any given (category) button
   * @param current to check if currently selected
   */
  private void styleCategoryButton(Button button, boolean current, int currentIndex) {

    // Current category the user is in
    if (current) {

      // Special offers category has different asthetics
      if (categories[currentIndex].equalsIgnoreCase("Special Offers")) {
        button.setText("Special\nOffers");
        button.setStyle(
            "-fx-background-color: transparent;"
            + "-fx-font-size: 42px;"
            + "-fx-text-fill: rgb(153, 36, 36);"
            + "-fx-font-weight: bold;"
        );

      // Any other category except specials
      } else {
        button.setStyle(
            "-fx-background-color: transparent;"
            + "-fx-font-size: 50px;"
            + "-fx-text-fill: black;"
            + "-fx-font-weight: bold;"
        );
      }
  
    // Other categories the user isn't currently in
    } else {

      // Special offers category has different asthetics
      if (categories[currentIndex].equalsIgnoreCase("Special Offers")) {
        button.setText("Special\nOffers");
        button.setStyle(
            "-fx-background-color: transparent;"
            + "-fx-font-size: 34px;"
            + "-fx-text-fill: rgb(153, 36, 36);"
            + "-fx-font-weight: bold;"
        );

      // Any other category except specials
      } else {
        button.setStyle(
            "-fx-background-color: transparent;"
            + "-fx-font-size: 40px;"
            + "-fx-text-fill: rgba(0, 0, 0, 0.33);"
            + "-fx-font-weight: bold;"
        );
      }
    }
  } 

  /**
   * Helper method to update highlighting of category buttons.
   * Iterates through category button list to update them all at once.
   */
  private void updateCategoryButtonStyles() {

    for (int i = 0; i < categoryButtons.size(); i++) {
      styleCategoryButton(categoryButtons.get(i), i == currentCategoryIndex, i);
    }
//  }
//}

    // Create final scene result
    return new Scene(mainPane, windowWidth, windowHeight);
  }

  private List<SimpleItem> convert(List<Single> items){
    List<SimpleItem> result = new ArrayList<>();
    for (Single item : items) {
      String name = item.getName();
        String imagePath = getImagePathForItem(name);
        result.add(new SimpleItem(name, imagePath));
    }
    return result;
  }


  private String getImagePathForItem(String itemName) {
    String key = itemName.toLowerCase().replace(" ", "_").replace("&", "and");
    return "/food/" + key + ".png";
  }

  /**
   * Adds all menu items. Filling each item category with items.
   * Added the items for the menu one by one for now, not through the database.
   */
  private void setupMenuData() throws SQLException {
    Connection conn = DriverManager.getConnection("jdbc:sqlite:restaurant.db"); // change if MariaDB
    Imenu menu = new Menu(conn);

    categoryItems.put("Burgers", convert(menu.getMains()));
    categoryItems.put("Sides", convert(menu.getSides()));
    categoryItems.put("Drinks", convert(menu.getDrinks()));
    categoryItems.put("Desserts", convert(menu.getDesserts()));
    categoryItems.put("Special Offers", List.of());

  }

  /**
   * Loading all items into the menu's item grid.
   */
  private void updateGrid() {
    // Empty grid and create new layout
    itemGrid.getChildren().clear();
    itemGrid.setHgap(20);
    itemGrid.setVgap(20);
    itemGrid.setPadding(new Insets(10));
    itemGrid.setAlignment(Pos.CENTER);

    // Fetch data
    String category = categories[currentCategoryIndex];
    List<SimpleItem> items = categoryItems.get(category);

    // Populate the grid with item and corresponding image one by one
    for (int i = 0; i < items.size(); i++) {
      // Get item and set proper layout
      SimpleItem item = items.get(i);
      VBox box = new VBox(10);
      box.setAlignment(Pos.CENTER);

      // Get image path
      String imagePath = item.imagePath();
      InputStream inputStream = getClass().getResourceAsStream(imagePath);
      // Errorhandling when no image found
      if (inputStream == null) {
        System.err.println("ERROR: Image not found - " + imagePath);
      }

      // Add image to View
      ImageView imageView = new ImageView(new Image(inputStream));
      imageView.setFitHeight(150);
      imageView.setPreserveRatio(true);
      Label name = new Label(item.name());

      // Connect it all and add to item grid
      box.getChildren().addAll(imageView, name);
      itemGrid.add(box, i % 3, i / 3);
    }
  }
}

