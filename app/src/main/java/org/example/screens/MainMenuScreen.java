package org.example.screens;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.example.buttons.ArrowButton;
import org.example.buttons.CancelButtonWithText;
import org.example.buttons.LangBtn;
import org.example.buttons.SqrBtnImgOnly;
import org.example.menu.Imenu;
import org.example.menu.Meal;
import org.example.menu.Menu;
import org.example.menu.Product;
import org.example.menu.Single;
import org.example.menu.Type;
import org.example.orders.Cart;

/**
 * The main menu screen.
 */
public class MainMenuScreen {

  private Stage primaryStage;

  private String[] categories = {"Burgers", "Sides", "Drinks",
    "Desserts", "Meals", "Special Offers"};
  private Map<String, List<Product>> categoryItems = new HashMap<>();
  private int currentCategoryIndex = 0;
  private GridPane itemGrid = new GridPane();
  private List<Button> categoryButtons = new ArrayList<>();
  public Cart cart = Cart.getInstance();
  private String mode;
  private Connection conn;

  /**
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
      Scene welcomeScrScene,
      int dummyCode,
      String mode) throws SQLException {

    this.primaryStage = primaryStage;
    this.mode = mode;

    Connection conn = DriverManager.getConnection(
          "jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
          + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd"
          + "?user=u5urh19mtnnlgmog"
          + "&password=zPgqf8o6na6pv8j8AX8r"
          + "&useSSL=true"
          + "&allowPublicKeyRetrieval=true");
    this.conn = conn;

    ImageView modeIcon = new ImageView();
    Label modeLabel = new Label();
    if ("takeaway".equalsIgnoreCase(mode)) {
      modeIcon.setImage(new Image(getClass().getResourceAsStream("/takeaway.png")));
      modeLabel.setText("Take Away");
    } else {
      modeIcon.setImage(new Image(getClass().getResourceAsStream("/eatHere.png")));
      modeLabel.setText("Eat Here");
    }

    modeIcon.setFitWidth(100);
    modeIcon.setFitHeight(100);

    modeLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");

    VBox modeBox = new VBox(10, modeIcon, modeLabel);
    modeBox.setAlignment(Pos.CENTER);

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
    // Make instance of arrow button that points left
    ArrowButton leftArrowButton = new ArrowButton(true, false);

    // left button clickable as long as it's still inside set bounds (>0)
    leftArrowButton.setOnMouseClicked(e -> {
      if (currentCategoryIndex > 0) {
        currentCategoryIndex--;
        updateGrid();
      } else {
        currentCategoryIndex = categoryButtons.size() - 1;
        updateGrid();
      }
    });

    // Arrow right
    // Make instance of arrow button that points right
    ArrowButton rightArrowButton = new ArrowButton(false, false);

    // right button clickable as long as its not in last category (Special offers)
    rightArrowButton.setOnMouseClicked(e -> {
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
    VBox leftArrowVcentered = new VBox(leftArrowButton);
    leftArrowVcentered.setAlignment(Pos.CENTER);
    VBox rightArrowVcentered = new VBox(rightArrowButton);
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

    // Spacer to push right buttons
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    // Create cancel button
    var cancelButton = new CancelButtonWithText();

    cancelButton.setOnAction(e -> {
      Cart.getInstance().clearCart();    
      System.out.println("Order canceled!");
      primaryStage.setScene(welcomeScrScene);
    });

    // Create Cart button
    var cartButton = new SqrBtnImgOnly();

    // Checkout screen
    CheckoutScreen checkoutScreen = new CheckoutScreen();

    // Get Checkout menu when clicking on cart
    cartButton.setOnMouseClicked(e -> {
      Scene checkoutScene = checkoutScreen.createCheckoutScreen(
          this.primaryStage,
          windowWidth,
          windowHeight,
          this.primaryStage.getScene(),
          welcomeScrScene,
          this.mode,
          cart,
          conn
        );
      this.primaryStage.setScene(checkoutScene);
    });

    // Create actual language button - putting it all together
    var langButton = new LangBtn();

    // Added all components for the bottom part
    bottomButtons.getChildren().addAll(langButton, spacer, cartButton, cancelButton);
    layout.setBottom(new VBox(bottomButtons));
  
    // Add layout to Stack Pane for dynamic sizing
    StackPane mainPane = new StackPane(layout);
    mainPane.setPrefSize(windowWidth, windowHeight);


    // Create final scene result
    return new Scene(mainPane, windowWidth, windowHeight);
  }

  private List<Product> convert(Connection conn, List<Single> items) throws SQLException {
    List<Product> result = new ArrayList<>();

    for (Single item : items) {
      int id = item.getId();
      String name = item.getName();
      double price = item.getPrice();
      Type type = item.getType();
      String imagePath = item.getImagePath();
      
      result.add(new Single(id, name, price, type, imagePath));
    }
    return result;
  }

  /**
   * Adds all menu items. Filling each item category with items.
   * Added the items for the menu one by one for now, not through the database.
   */
  private void setupMenuData() throws SQLException {
    Imenu menu = new Menu(conn);

    categoryItems.put("Burgers", convert(conn, menu.getMains()));
    categoryItems.put("Sides", convert(conn, menu.getSides()));
    categoryItems.put("Drinks", convert(conn, menu.getDrinks()));
    categoryItems.put("Desserts", convert(conn, menu.getDesserts()));
    categoryItems.put("Special Offers", List.of());
    categoryItems.put("Meals", null);

  
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
    List<Product> items = categoryItems.get(category);
    if ("Meals".equals(category)) {
      System.out.println("Loading Meals category");

      items = new ArrayList<>();
      try {
        String sql = """
            SELECT meal_id, name, description, price, image_url
            FROM meal
            """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        boolean found = false;

        while (rs.next()) {
          found = true;
          Meal meal = new Meal(rs.getString("name"));
          meal.setId(rs.getInt("meal_id"));
          meal.setName(rs.getString("name"));
          meal.setPrice(rs.getFloat("price"));
          meal.setImagePath(rs.getString("image_url"));
          System.out.println("Meal image path: " + meal.getImagePath());
          meal.setType(Type.MEAL);

          items.add(meal);
        }

        if (!found) {
          System.out.println("No meals returned");
        }
        rs.close();
        ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    // Max item slots in rows and pages
    int maxItemsPerRow = 3;
    int totalItemsPerPage = 6;

    // Populate the grid with item and corresponding image one by one
    for (int i = 0; i < totalItemsPerPage; i++) {

      // Create fresh item Slot
      VBox itemBox = new VBox(10);
      itemBox.setAlignment(Pos.CENTER);

      // Make slot with fixed size
      StackPane imageSlot = new StackPane();
      imageSlot.setPrefSize(200, 200);
      imageSlot.setMaxSize(200, 200);
      imageSlot.setMinSize(200, 200);

      // Item exists
      if (i < items.size()) {
        // Get current item
        Product item = items.get(i);

        // Get image path
        String imagePath = item.getImagePath();
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
          // Add image to View
          imageView = new ImageView(new Image(inputStream));
        }
    
        // Adjust image size but not make it blurry
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        // Move image into image slot and center image
        imageSlot.getChildren().add(imageView);
        imageSlot.setAlignment(Pos.CENTER);

        // Give item a name
        Label name = new Label(item.getName());
        name.setStyle("-fx-font-size: 16px;");

        // Format the price (with :-)
        // Put the price in an Hbox to align it to the right
        Label price = new Label(String.format("%.0f :-", item.getPrice()));
        price.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        HBox priceBox = new HBox(price);
        priceBox.setAlignment(Pos.BASELINE_RIGHT);

        // Item details
        ItemDetails detailScreen = new ItemDetails();

        // Get item details when clicking on item
        imageSlot.setOnMouseClicked(e -> {
          try {
            if (item instanceof Meal) {
              Meal meal = (Meal) item;
              MealCustomizationScreen mealScreen = new MealCustomizationScreen();
              Scene sideScene = mealScreen.createSideSelectionScene(
                  this.primaryStage,
                  this.primaryStage.getScene(),
                  meal);
              this.primaryStage.setScene(sideScene);
            } else if (item instanceof Single) {
              Scene detailScene = detailScreen.create(
                  this.primaryStage,
                  this.primaryStage.getScene(),
                  (Single) item,
                  cart
              );
              this.primaryStage.setScene(detailScene);
            }
          } catch (SQLException ex) {
            ex.printStackTrace();
          }
        });

        // Connect it all and add to item grid
        itemBox.getChildren().addAll(imageSlot, name, priceBox);

      // No more items exist -> Item list empty
      // Fill the rest up with empty slots until we reach 6 slots per page
      } else {
        itemBox.getChildren().addAll(imageSlot, new Label(""));
      }

      // Add new slot to final item grid
      itemGrid.add(itemBox, i % maxItemsPerRow, i / maxItemsPerRow);
    }
    updateCategoryButtonStyles();
  }

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
  }

}

