package org.example;

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
import javafx.stage.Stage;

/**
 * The main menu screen.
 */
public class MainMenuScreen {

  private Stage primaryStage;

  private record SimpleItem(String name, String imagePath) {}

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
   */
  public Scene createMainMenuScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene) {

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

    // 
    for (int i = 0; i < categories.length; i++) {
      String cat = categories[i];
      Button btn = new Button(cat);
      styleCategoryButton(btn, i == currentCategoryIndex);
      final int index = i;
      btn.setOnAction(e -> {
        currentCategoryIndex = index;
        updateGrid();
        updateCategoryButtonStyles();
      });
      categoryButtons.add(btn);
      categoryBar.getChildren().add(btn);
    }
    top.getChildren().add(categoryBar);

    //
    layout.setTop(top);
    setupMenuData();
    updateGrid();
    //layout.setCenter(itemGrid);

    // Center/Middle of the Menu items
    // Spacing between arrow buttons and menu items will be 40
    HBox centerMenuContent = new HBox(40);
    centerMenuContent.setAlignment(Pos. CENTER);
    centerMenuContent.setPadding(new Insets(10));

    // Arrow buttons to navigate

    // Alignment with horizontal box
    HBox arrows = new HBox(500);
    // arrows.setPadding(new Insets(30));
    arrows.setAlignment(Pos.CENTER);

    // Arrow left
    Image leftArrow = new Image(getClass().getResourceAsStream("/nav_bl.png"));
    ImageView leftArrowView = new ImageView(leftArrow);
    leftArrowView.setFitHeight(40);
    leftArrowView.setPreserveRatio(true);

    // User can click left button as long as it's still inside the set bounds (>0)
    leftArrowView.setOnMouseClicked(e -> {
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

    // User can click the right button as long as its not in the last category (Special offers)
    rightArrowView.setOnMouseClicked(e -> {
      if (currentCategoryIndex < categories.length - 1
          && !categories[currentCategoryIndex].equals("Special Offers")) {
        currentCategoryIndex++;
        updateGrid();
      } else {
        currentCategoryIndex = 0;
        updateGrid();
      }
    });

    // Add Arrow buttons together
    //arrows.getChildren().addAll(leftArrowView, rightArrowView);
    //layout.setBottom(arrows);
    //BorderPane.setAlignment(arrows, Pos.CENTER);

    // Add all Menu items and left right buttons in center of menu in the right order
    centerMenuContent.getChildren().addAll(leftArrowView, itemGrid, rightArrowView);
    // Setting center menu content to center of actual menu
    layout.setCenter(centerMenuContent);

    // Bottom buttons
    HBox bottomButtons = new HBox();
    bottomButtons.setPadding(new Insets(10));
    
    // Swedish Flag - Language button
    ImageView sweFlag = new ImageView(new Image(getClass().getResourceAsStream("/swe.png")));
    sweFlag.setFitWidth(30);
    sweFlag.setFitHeight(30);
    sweFlag.setPreserveRatio(true);
    Button langButton = new Button();
    langButton.setGraphic(sweFlag);
    langButton.setStyle("-fx-background-color: transparent;");
    langButton.setMinSize(40, 40);

    // Spacer to push right buttons
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    
    Button cancelButton = new Button();
    ImageView cancelIcon = new ImageView(new Image(getClass().getResourceAsStream("/cancel.png")));
    cancelIcon.setFitWidth(30);
    cancelIcon.setFitHeight(30);
    cancelButton.setGraphic(cancelIcon);
    cancelButton.setStyle("-fx-background-color: transparent;");
    cancelButton.setMinSize(40, 40);
    cancelButton.setOnAction(e -> primaryStage.setScene(welcomeScrScene));

    Button cartButton = new Button();
    ImageView cartIcon = new ImageView(new Image(getClass().getResourceAsStream("/cart_bl.png")));
    cartIcon.setFitWidth(30);
    cartIcon.setFitHeight(30);
    cartButton.setGraphic(cartIcon);
    cartButton.setStyle("-fx-background-color: transparent;");
    cartButton.setMinSize(40, 40);

    // Added all components for the bottom part
    bottomButtons.getChildren().addAll(langButton, spacer, cancelButton, cartButton);
    //layout.setBottom(new VBox(arrows, bottomButtons));
    layout.setBottom(new VBox(bottomButtons));
  

    StackPane mainPane = new StackPane(layout);
    mainPane.setPrefSize(windowWidth, windowHeight);

    return new Scene(mainPane, windowWidth, windowHeight);
  }

  /**
   * Adds all menu items.
   * Added the items for the menu one by one for now, not through the database.
   */
  private void setupMenuData() {
    categoryItems.put("Burgers", List.of(
        new SimpleItem("Standard Burger", "/food/standard_burger.png"),
        new SimpleItem("Juicy Chicken Burger", "/food/chicken_burger.png"),
        new SimpleItem("All American Burger", "/food/all_american_burger.png"),
        new SimpleItem("Double Cheese & Bacon Burger", "/food/double_burger.png"),
        new SimpleItem("Extra Veggies Burger", "/food/extra_vegies_burger.png"),
        new SimpleItem("King Burger", "/food/king_burger.png")));

    categoryItems.put("Sides", List.of(
        new SimpleItem("French Fries", "/food/french_fries.png"),
        new SimpleItem("Greek Salad", "/food/salad.png"),
        new SimpleItem("Country-Style Potatoes", "/food/cs_potatoes.png"),
        new SimpleItem("Fried Onion Rings", "/food/rings.png")));

    categoryItems.put("Drinks", List.of(
        new SimpleItem("Cola Zero", "/food/cola.png"),
        new SimpleItem("Fanta", "/food/fanta.png"),
        new SimpleItem("Americano", "/food/coffee.png")));

    categoryItems.put("Desserts", List.of(
        new SimpleItem("Milkshake", "/food/Milkshake.png"),
        new SimpleItem("Tiramisu", "/food/tiramisu.png"),
        new SimpleItem("Strawberry Cupcake", "/food/cupcake.png")));

    categoryItems.put("Special Offers", List.of(
        new SimpleItem("Extra Veggies Burger", "/food/extra_vegies_burger.png"),
        new SimpleItem("Strawberry Cupcake", "/food/cupcake.png")));
  }

  private void updateGrid() {
    itemGrid.getChildren().clear();
    itemGrid.setHgap(20);
    itemGrid.setVgap(20);
    itemGrid.setPadding(new Insets(10));
    itemGrid.setAlignment(Pos.CENTER);

    String category = categories[currentCategoryIndex];
    List<SimpleItem> items = categoryItems.get(category);

    for (int i = 0; i < items.size(); i++) {
      SimpleItem item = items.get(i);
      VBox box = new VBox(10);
      box.setAlignment(Pos.CENTER);

      String imagePath = item.imagePath();
      InputStream inputStream = getClass().getResourceAsStream(imagePath);
      if (inputStream == null) {
        System.err.println("ERROR: Image not found - " + imagePath);
      }

      ImageView imageView = new ImageView(new Image(inputStream));
      imageView.setFitHeight(150);
      imageView.setPreserveRatio(true);
      Label name = new Label(item.name());

      ItemDetails detailScreen = new ItemDetails();
      imageView.setOnMouseClicked(e -> {
        Scene detailScene = detailScreen.create(this.primaryStage, this.primaryStage.getScene(), item.name(), item.imagePath());
        this.primaryStage.setScene(detailScene);
      });

      box.getChildren().addAll(imageView, name);
      itemGrid.add(box, i % 3, i / 3);
    }

    updateCategoryButtonStyles();

  }

  /**
   * helper method for dynamic category button highlighting
   * @param button any given (category) button
   * @param current to check if currently selected
   */
  private void styleCategoryButton(Button button, boolean current) {

    if (current) {

      button.setStyle(
        "-fx-background-color: transparent;"
        + "-fx-font-size: 20px;"
        + "-fx-text-fill: black;"
        + "-fx-font-weight: bold;"
      );

    } else {

      button.setStyle(
        "-fx-background-color: transparent;"
        + "-fx-font-size: 18px;"
        + "-fx-text-fill: rgba(0, 0, 0, 0.33);"
        + "-fx-font-weight: bold;"
      );

    }

  } 

  /**
   * Helper method to update highlighting of category buttons
   * iterates through category button list to update them all at once
   */
  private void updateCategoryButtonStyles() {

    for (int i = 0; i < categoryButtons.size(); i++) {

      styleCategoryButton(categoryButtons.get(i), i == currentCategoryIndex);

    }

  }

}
