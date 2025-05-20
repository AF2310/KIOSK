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
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import org.example.buttons.CartSquareButton;
import org.example.buttons.ColorSquareButtonWithImage;
import org.example.buttons.LangBtn;
import org.example.buttons.SearchBar;
import org.example.kiosk.LabelManager;
import org.example.kiosk.LanguageSetting;
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

  // SEARCH BAR STUFF START
  private String currentSearchName = "";
  private String currentSearchCategory = "";
  private double currentSearchPrice = -1;
  // SEARCH BAR STUFF END

  private String[] categories = { "Burgers", "Sides", "Drinks",
      "Desserts", "Meals", "Special Offers" };
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
   * @param primaryStage    the stage
   * @param windowWidth     the width of the window
   * @param windowHeight    the height of the window
   * @param welcomeScrScene the scene to return to on cancel
   * @return the created scene
   * @throws SQLException error server quick fix
   */
  public CustomScene createMainMenuScreen(
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

    // SEARCH BAR STUFF START
    SearchBar mainSearch = new SearchBar(this.conn);
    mainSearch.setMaxWidth(1000);
    mainSearch.setMinWidth(600);

    Button searchButton = new Button("Search");
    searchButton.setStyle("-fx-font-size: 16px; -fx-padding: 8px 15px;");

    TextField gridSearchField = new TextField();
    gridSearchField.setPromptText("Quick filter items...");

    gridSearchField.setStyle("-fx-font-size: 14px; -fx-pref-width: 250px;");
    Button gridSearchButton = new Button("Filter");
    gridSearchButton.setStyle("-fx-font-size: 14px; -fx-padding: 6px 12px;");

    TextField priceFilterField = new TextField();
    priceFilterField.setPromptText("Max price");
    priceFilterField.setStyle("-fx-font-size: 14px; -fx-pref-width: 100px;");

    ComboBox<String> gridCategoryBox = new ComboBox<>();
    gridCategoryBox.getItems().addAll(
        "-- Any Category --", "Burgers", "Sides", "Drinks", "Desserts");
    gridCategoryBox.setPromptText("Select Category...");
    gridCategoryBox.setStyle("-fx-font-size: 14px; -fx-pref-width: 150px;");

    gridSearchButton.setOnAction(e -> {
      currentSearchName = gridSearchField.getText().trim();

      currentSearchCategory = "";

      try {
        currentSearchPrice = Double.parseDouble(priceFilterField.getText().trim());
      } catch (NumberFormatException ex) {
        currentSearchPrice = -1;
      }
      String selectedCategory = gridCategoryBox.getValue();
      if (selectedCategory != null && !selectedCategory.isEmpty()) {
        for (int i = 0; i < categories.length; i++) {
          if (categories[i].equalsIgnoreCase(selectedCategory)) {
            currentCategoryIndex = i;
            break;
          }

        }
        currentSearchCategory = selectedCategory;
      } else {
        currentSearchCategory = "";
      }

      updateGrid();

    });

    HBox gridSearchBox = new HBox(
        30,
        gridSearchField,
        priceFilterField,
        gridCategoryBox,
        gridSearchButton);
    gridSearchBox.setAlignment(Pos.CENTER);

    Button showSearchBtn = new Button();

    showSearchBtn.setMinSize(80, 80);
    showSearchBtn.setMaxSize(80, 80);

    Circle searchCircle = new Circle(50);
    searchCircle.setFill(Color.GOLD);
    searchCircle.setStroke(Color.RED);

    Label searchLabel = new Label("FILTER\nITEMS");
    searchLabel.setTextAlignment(TextAlignment.CENTER);
    searchLabel.setStyle("-fx-font-size: 20px; "
        + "-fx-font-weight: bold; "
        + "-fx-text-fill: gold; "
        + "-fx-underline: true;");

    searchLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    StackPane searchGraphic = new StackPane(searchCircle, searchLabel);

    showSearchBtn.setGraphic(searchGraphic);
    final boolean[] isSearchVisible = { false };
    VBox.setMargin(gridSearchBox, new Insets(5));
    showSearchBtn.setOnAction(e -> {
      isSearchVisible[0] = !isSearchVisible[0];
      if (isSearchVisible[0]) {
        if (!top.getChildren().contains(gridSearchBox)) {
          top.getChildren().add(gridSearchBox);
          searchCircle.setFill(Color.GOLD);

        } else {
          top.getChildren().remove(gridSearchBox);
          searchCircle.setFill(Color.RED);

          currentSearchName = "";
          currentSearchCategory = "";
          currentSearchPrice = -1;

          gridSearchField.clear();
          priceFilterField.clear();
          gridCategoryBox.getSelectionModel().clearSelection();
          updateGrid();
        }
      }
    });
    showSearchBtn.setStyle(
        "-fx-background-color: transparent;"
        + "-fx-padding: 0;"
        + "-fx-border-width: 0;"
        + "-fx-content-display: GRAPHIC_ONLY;");

    gridSearchBox.setStyle(
        "-fx-background-color: rgba(255, 255, 255, 0.9);"
        + "-fx-border-color: gold;"
        + "-fx-border-width: 2px;"
        + "-fx-border-radius: 20px;"
        + "-fx-background-radius: 20px;"
        + "-fx-padding: 10px;");

    // Align the search button to top-right
    StackPane.setAlignment(showSearchBtn, Pos.TOP_RIGHT);
    // Add margin: 20 pixels from top and right
    StackPane.setMargin(showSearchBtn, new Insets(20, 20, 0, 0));

    // SEARCH BAR STUFF ENDS

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
                + "-fx-padding: 0px;");
        
        // For the special offers button color check
        Color backgroundColor = BackgroundColorStore.getCurrentBackgroundColor();
        int r = (int) (backgroundColor.getRed() * 255);
        int g = (int) (backgroundColor.getGreen() * 255);
        int b = (int) (backgroundColor.getBlue() * 255);

        // Make circle with noticible color
        Circle specialsCircle = new Circle(100);

        // If warm background --> Silver
        // Else --> Gold
        if (r >= 100 && g <= 200 && b <= 100) {

          specialsCircle.setFill(Color.SILVER);

        } else {

          specialsCircle.setFill(Color.GOLD);

        }

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
      // categoryBar.getChildren().add(btn);
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

    // Add all Menu items and left right buttons in center of menu in the right
    // order
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

    // Spacer to push right buttons
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    // Create cancel button
    var cancelButton = new ColorSquareButtonWithImage("Cancel", "/cancel.png");

    cancelButton.setOnAction(e -> {
      Cart.getInstance().clearCart();
      System.out.println("Order canceled!");
      primaryStage.setScene(welcomeScrScene);
    });

    // Create Cart button
    var cartButton = new CartSquareButton();

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
          conn);
      this.primaryStage.setScene(checkoutScene);
    });

    // Create language button
    var langButton = new LangBtn();
    langButton.updateImage();

    // Translate all the text
    langButton.addAction(event -> {
      LanguageSetting lang = LanguageSetting.getInstance();
      String newLang = lang.getSelectedLanguage().equals("en") ? "sv" : "en";
      lang.changeLanguage(newLang);
      lang.updateAllLabels(layout);
    });

    LanguageSetting.getInstance().updateAllLabels(layout);

    // Added all components for the bottom part
    bottomButtons.getChildren().addAll(langButton, spacer, cartButton, cancelButton);
    layout.setBottom(new VBox(bottomButtons));

    // Add layout to Stack Pane for dynamic sizing
    StackPane mainPane = new StackPane(layout, showSearchBtn);
    mainPane.setPrefSize(windowWidth, windowHeight);

    // Create final scene result
    CustomScene scene = new CustomScene(mainPane, windowWidth, windowHeight);

    // Reads and applies the customized background color
    Color bgColor = BackgroundColorStore.getCurrentBackgroundColor();

    if (bgColor != null) {

      scene.setBackgroundColor(bgColor);

    }

    // Update the language for the scene upon creation
    Parent root = scene.getRoot();

    LanguageSetting.getInstance().registerRoot(root);
    LanguageSetting.getInstance().updateAllLabels(root);

    return scene;
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
    itemGrid.getChildren().clear();
    itemGrid.setHgap(20);
    itemGrid.setVgap(20);
    itemGrid.setPadding(new Insets(10));
    itemGrid.setAlignment(Pos.CENTER);

    // Load items depending on category
    List<Product> items = new ArrayList<>();

    if ("Meals".equals(categories[currentCategoryIndex])) {
      try {
        String sql = """
            SELECT meal_id, name, description, price, image_url
            FROM meal
            """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
          Meal meal = new Meal(rs.getString("name"), conn);
          meal.setId(rs.getInt("meal_id"));
          meal.setName(rs.getString("name"));
          meal.setPrice(rs.getFloat("price"));
          meal.setImagePath(rs.getString("image_url"));
          meal.setType(Type.MEAL);

          items.add(meal);
        }

        rs.close();
        ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      // Get from preloaded categories
      String category = categories[currentCategoryIndex];
      List<Product> categoryList = categoryItems.getOrDefault(category, new ArrayList<>());
      items.addAll(categoryList);
    }

    // FILTERS: Apply name, price, and category filter
    List<Product> filteredProducts = new ArrayList<>(items);

    if (!currentSearchName.isEmpty()) {
      String searchTerm = currentSearchName.toLowerCase();
      filteredProducts = filteredProducts.stream()
          .filter(p -> p.getName().toLowerCase().contains(searchTerm))
          .collect(Collectors.toList());
    }

    if (currentSearchPrice >= 0) {
      final double priceLimit = currentSearchPrice;
      filteredProducts = filteredProducts.stream()
          .filter(p -> p.getPrice() <= priceLimit)
          .collect(Collectors.toList());
    }

    if (!currentSearchCategory.isEmpty()
        && !currentSearchCategory.equalsIgnoreCase("-- Any Category --")) {
      String targetCategory = currentSearchCategory.toLowerCase();
      filteredProducts = filteredProducts.stream()
          .filter(p -> {
            if (p instanceof Single single) {
              return single.getType().toString().toLowerCase().equals(targetCategory);
            }
            return false;
          })
          .collect(Collectors.toList());
    }

    // Adjust layout if filters are used
    int maxItemsPerRow = 4;
    int totalItemsPerPage = 8;

    // boolean filtersActive = !currentSearchName.isEmpty() || currentSearchPrice >= 0
    //     || (!currentSearchCategory.isEmpty()
    //     && !currentSearchCategory.equalsIgnoreCase("-- Any Category --"));

    // // if (filtersActive) {
    // //   totalItemsPerPage = filteredProducts.size();
    // //   maxItemsPerRow = 4;
    // // }

    // Create the empty image to fill the grid slots
    Image emptyImage = new Image(
        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABC"
            + "AQAAAC1HAwCAAAAC0lEQVR42mP8/wcAAwAB/hd5JnkAAAAASUVORK5CYII=");

    // Populate grid with items or empty image
    for (int i = 0; i < totalItemsPerPage; i++) {

      VBox itemBox = new VBox(10);
      itemBox.setAlignment(Pos.CENTER);

      StackPane imageSlot = new StackPane();
      imageSlot.setPrefSize(200, 200);
      imageSlot.setMaxSize(200, 200);
      imageSlot.setMinSize(200, 200);

      Product item = (i < filteredProducts.size()) ? filteredProducts.get(i) : null;

      // Load image or use empty image
      ImageView imageView;
      if (item != null) {
        String imagePath = item.getImagePath();
        InputStream inputStream = getClass().getResourceAsStream(imagePath);

        if (inputStream == null) {
          System.err.println("ERROR: Image not found - " + imagePath);
          imageView = new ImageView(emptyImage);
        } else {
          imageView = new ImageView(new Image(inputStream));
        }
      } else {
        imageView = new ImageView(emptyImage);
      }

      imageView.setFitHeight(150);
      imageView.setPreserveRatio(true);
      imageSlot.getChildren().add(imageView);
      imageSlot.setAlignment(Pos.CENTER);

      // If an item exists, add its name and price
      if (item != null) {
        Label name = new Label(item.getName());
        name.setStyle("-fx-font-size: 16px;");
        // Add to label mangager, to be able to change colors
        LabelManager.register(name);

        Label price = new Label(String.format("%.0f :-", item.getPrice()));
        price.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        HBox priceBox = new HBox(price);
        priceBox.setAlignment(Pos.BASELINE_RIGHT);
        // Add to label mangager, to be able to change colors
        LabelManager.register(price);

        ItemDetails detailScreen = new ItemDetails();

        imageSlot.setOnMouseClicked(e -> {
          try {
            if (item instanceof Meal meal) {
              MealCustomizationScreen mealScreen = new MealCustomizationScreen();
              Scene sideScene = mealScreen.createSideSelectionScene(
                  this.primaryStage,
                  this.primaryStage.getScene(),
                  meal);
              this.primaryStage.setScene(sideScene);
            } else if (item instanceof Single single) {
              Scene detailScene = detailScreen.create(
                  this.primaryStage,
                  this.primaryStage.getScene(),
                  single,
                  cart);
              this.primaryStage.setScene(detailScene);
            }
          } catch (SQLException ex) {
            ex.printStackTrace();
          }
        });

        itemBox.getChildren().addAll(imageSlot, name, priceBox);
      } else {
        // For empty slots, just add the image slot with empty image
        itemBox.getChildren().add(imageSlot);
      }

      itemGrid.add(itemBox, i % maxItemsPerRow, i / maxItemsPerRow);
    }

    LanguageSetting.getInstance().updateAllLabels(itemGrid);

    updateCategoryButtonStyles();
  }

  /**
   * helper method for dynamic category button highlighting.
   *
   * @param button  any given (category) button
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
                + "-fx-font-weight: bold;");

        // Any other category except specials
      } else {
        button.setStyle(
            "-fx-background-color: transparent;"
                + "-fx-font-size: 50px;"
                + "-fx-font-weight: bold;");
        LabelManager.register(button);
        button.setOpacity(1);
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
                + "-fx-font-weight: bold;");

        // Any other category except specials
      } else {
        button.setStyle(
            "-fx-background-color: transparent;"
                + "-fx-font-size: 40px;"
                + "-fx-font-weight: bold;");
        LabelManager.register(button);
        button.setOpacity(0.3);
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
