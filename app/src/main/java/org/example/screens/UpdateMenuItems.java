package org.example.screens;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.DropBoxWithLabel;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButton;
import org.example.buttons.RectangleTextFieldWithLabel;
import org.example.buttons.SqrBtnWithOutline;
import org.example.buttons.TickBoxWithLabel;
import org.example.menu.Product;
import org.example.menu.Type;
import org.example.sql.SqlConnectionCheck;

/**
 * Updating menu class.
 */
public class UpdateMenuItems {
  private Stage primaryStage;
  /**
   * Scene for adding/removing items on the menu.
   *
   * @param primaryStage Window itself.
   * @param prevScene Previous scene to return to.
   * @return The scene itself.
   */

  public Scene adminUpdateMenuItems(Stage primaryStage, Scene prevScene) {
    this.primaryStage = primaryStage;

    // All the buttons for updating menu items
    MidButton addProductButton = new MidButton("Add product to menu", "rgb(255, 255, 255)", 30);
    MidButton changePriceButton = new MidButton("Change prices", "rgb(255, 255, 255)", 30);
    MidButton removeProductButton = new MidButton("Remove product from menu", 
                                                  "rgb(255, 255, 255)", 30);


    // Action for clicking on adding a product
    addProductButton.setOnAction(e -> {
      Scene productScene = new UpdateMenuItems().addProductScene(
            this.primaryStage,
            prevScene);
      primaryStage.setScene(productScene);
    });

    changePriceButton.setOnAction(e -> {
      Scene productScene = new UpdateMenuItems().changePriceScene(
            this.primaryStage,
            prevScene);
      primaryStage.setScene(productScene);
    });

    removeProductButton.setOnAction(e -> {
      Scene productDeletionScene = new UpdateMenuItems().deleteProduct(
            this.primaryStage,
            prevScene);
      primaryStage.setScene(productDeletionScene);
    });

    // Layout for arranging buttons in a grid
    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(20);
    gridPane.setVgap(20);
    gridPane.add(addProductButton, 0, 0);
    gridPane.add(changePriceButton, 0, 1);
    gridPane.add(removeProductButton, 0, 2);

                                
    BorderPane layout = new BorderPane();
    layout.setCenter(gridPane);

    Scene updateItemScene = new Scene(layout, 1920, 1080);
;
    return updateItemScene;

  }
  /**
   * Scene for adding a product to the menu.
   *
   * @param primaryStage Stage itself.
   * @param prevScene previous scene to return to.
   * @return The add product scene.
   */

  public Scene addProductScene(Stage primaryStage, Scene prevScene) {

    // List view box for showing all the ingredients upon category selection
    ListView<String> ingredientListView = new ListView<>();
    ingredientListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    ingredientListView.setPrefSize(300, 400);
    Label ingredientListLabel = new Label("Ingredient List");
    ingredientListLabel.setAlignment(Pos.CENTER);
    ingredientListLabel.setStyle("-fx-font-size: 30");
    VBox ingredientBox = new VBox(10, ingredientListLabel, ingredientListView);
    ingredientBox.setAlignment(Pos.CENTER_LEFT);
    ingredientBox.setPadding(new Insets(0, 150, 50, 0));

    // Creates a dropdown for selecting a product category
    DropBoxWithLabel productCategoryDropBox = new DropBoxWithLabel("Product Category:");
    //Map to store category name and its corresponding ID from the database.
    Map<String, Integer> categoryMap = new HashMap<>();

    // This is a query to fetch all the categories from the database
    try {
      SqlConnectionCheck connection = new SqlConnectionCheck();
      String sql = "SELECT category_id, name FROM category";
      PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("category_id");
        String name = rs.getString("name");
        categoryMap.put(name, id);
        productCategoryDropBox.getComboBox().getItems().add(name);
      }
      // This is an event handler when a category is selected
      // When users selects a category, the ingredient list is updated
      productCategoryDropBox.getComboBox().setOnAction(e -> {
        // Gets selected category
        String selectedCategory = productCategoryDropBox.getSelectedItem();
        if (selectedCategory != null) {
          try {
            // Joins category onto ingredients so we have ingredient_name + ingredient_id
            String categoryOnIngredientsql = 
                 "SELECT i.ingredient_id, i.ingredient_name " 
                + "FROM ingredient i "
                + "JOIN categoryingredients ci ON i.ingredient_id = ci.ingredient_id " 
                + "JOIN category c ON ci.category_id = c.category_id " 
                + "WHERE c.name = ?";

            PreparedStatement statement = connection.getConnection().prepareStatement(
                    categoryOnIngredientsql);
            // Selects the category ingredients
            statement.setString(1, selectedCategory);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<String> ingredients = FXCollections.observableArrayList();
            // Here we populate the ingredient list view with the results
            while (resultSet.next()) {
              // Only shows the ingredients for the selected category
              String ingredientName = resultSet.getString("ingredient_name");
              ingredients.add(ingredientName);

            }
            ingredientListView.setItems(ingredients);
          } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Database error", "Failed to load categories", Alert.AlertType.ERROR);
          }
        }
      });
    } catch (SQLException ex) {
      ex.printStackTrace();
      showAlert("Database error", "Failed to load categories", Alert.AlertType.ERROR);
    } // TODO: Make connection to sql a singleton so we don't create new connections each time.

    // Maps categories to default image paths for now
    // TODO: Make an implementation for putting in new images for products
    Map<String, String> categoryImageMap = new HashMap<>();
    categoryImageMap.put("Burger", "/food/default_burger.png");
    categoryImageMap.put("Side", "/food/default_side.png");
    categoryImageMap.put("Drink", "/food/default_drink.png");
    categoryImageMap.put("Dessert", "/food/default_dessert.png");

    SqrBtnWithOutline confirmButton = new SqrBtnWithOutline("Confirm",
        "green_tick.png", "rgb(81, 173, 86)");

    // Textfields for the information to put into the SQL query
    RectangleTextFieldWithLabel productName = new RectangleTextFieldWithLabel("Product Name:",
        "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel productPrice = new RectangleTextFieldWithLabel("Product Price:",
        "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel productDescription = new RectangleTextFieldWithLabel(
        "Product Description:", "rgb(255, 255, 255)");
    TickBoxWithLabel productIsActive = new TickBoxWithLabel("Is active?");
    TickBoxWithLabel productIsLimited = new TickBoxWithLabel("Is limited?");

    // Handler for when the confirm button is clicked, it adds that new product
    confirmButton.setOnAction(e -> {
      try {
        SqlConnectionCheck connection = new SqlConnectionCheck();
        connection.getConnection().setAutoCommit(false);



        ObservableList<String> selectedItems = ingredientListView
            .getSelectionModel().getSelectedItems();

        String selectedCategory = productCategoryDropBox.getSelectedItem();
        // Validating user inputs first before attempting to add the product
        if (productName.getText().isEmpty() || productPrice.getText().isEmpty() 
            || selectedCategory == null) {
          showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
          return;
        } 
        if (selectedItems.isEmpty()) {
          showAlert("Info", "No ingredients selected!", Alert.AlertType.INFORMATION);
        }     
        
        // Build an SQL query to insert a new product into the 'product table' 
        String sqlProduct = "INSERT INTO"
                   + " product (name, description, price, category_id,"
                   + " is_active, is_limited, image_url)"
                   + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmtProduct = connection.getConnection()
            .prepareStatement(sqlProduct, PreparedStatement.RETURN_GENERATED_KEYS);


        String imageFileName;
        // And constructs the image file path based on the selected category
        if (selectedCategory.equalsIgnoreCase("burgesr")) {
          imageFileName = "default_burger.png";
        } else if (selectedCategory.equalsIgnoreCase("desserts")) {
          imageFileName = "default_dessert.png";
        } else if (selectedCategory.equalsIgnoreCase("sides")) {
          imageFileName = "default_side.png";
        } else if (selectedCategory.equalsIgnoreCase("drinks")) {
          imageFileName = "default_drink.png";
        } else {
          imageFileName = "default_burger.png"; // fallback in case no match
        }

        String imageUrl = "/food/" + imageFileName;

        byte isActive = (byte) (productIsActive.isSelected() ? 1 : 0);
        byte isLimited = (byte) (productIsLimited.isSelected() ? 1 : 0);

        stmtProduct.setString(1, productName.getText());
        stmtProduct.setString(2, productDescription.getText());
        // Must parse the string as a double
        stmtProduct.setDouble(3, Double.parseDouble(productPrice.getText()));
        stmtProduct.setInt(4, categoryMap.get(selectedCategory));
        stmtProduct.setByte(5, isActive);
        stmtProduct.setByte(6, isLimited);
        stmtProduct.setString(7, imageUrl);

        int affectedRows = stmtProduct.executeUpdate();

        // Notifying the admin if the adding proccess was successful or not
        if (affectedRows > 0) {
          showAlert("Success!", "Product Added Successfully!", Alert.AlertType.INFORMATION);
        }

        int productId;
        // Retrieving auto generated product ID to use for ingredient mapping
        try (ResultSet generatedKeys = stmtProduct.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            productId = generatedKeys.getInt(1);
          } else {
            throw new SQLException("Creating producted failed, no ID obtained");
          }
        }
        // Inserting selected ingredients into the linking table with the new product ID
        String sqlIngredients = "INSERT INTO productingredients (product_id, "
                                + "ingredient_id, ingredientCount) "
                                + "VALUES (?, (SELECT ingredient_id FROM "
                                + "ingredient WHERE ingredient_name = ?), ?)";

        PreparedStatement stmtIngredients = connection.getConnection()
              .prepareStatement(sqlIngredients);

        int ingredientCount = 1;
        for (String ingredientName : selectedItems) {
          stmtIngredients.setInt(1, productId);
          stmtIngredients.setString(2, ingredientName);
          stmtIngredients.setInt(3, ingredientCount);
          stmtIngredients.addBatch();
          ingredientCount++;
        }

        stmtIngredients.executeBatch();
        connection.getConnection().commit();
                
      } catch (NumberFormatException ex) {
        showAlert("Input error", "Enter valid numbers for price and category ID",
                  Alert.AlertType.ERROR);
      } catch (SQLException ex) {
        // Full error for debugging
        ex.printStackTrace();
        showAlert("Database Error", "Failed to save product", Alert.AlertType.ERROR);
      }
    });

    SqrBtnWithOutline backButton = new SqrBtnWithOutline("Cancel",
        "cancel.png", "rgb(255, 0, 0)");   

    backButton.setOnAction(e -> {
      primaryStage.setScene(prevScene);
    });

    // The top part of the scene, the label name
    var menuLabel = new Label("Add A Product to the Menu");
    menuLabel.setStyle("-fx-font-size: 40; -fx-font-weight: bold;");
    HBox menuTitle = new HBox(menuLabel);
    menuTitle.setAlignment(Pos.CENTER);
    menuTitle.setPadding(new Insets(70, 0, 20, 0));

    BorderPane layout = new BorderPane();

    layout.setTop(menuTitle);

    VBox menuLayoutLeft = new VBox(20);
    // Setting the name, description and price to left of the screen
    menuLayoutLeft.getChildren().addAll(productName,
                                    productDescription, 
                                    productPrice);
    menuLayoutLeft.setAlignment(Pos.BASELINE_LEFT);
    layout.setLeft(menuLayoutLeft);
    // Active and limited tick boxes are center but to the left and
    // category drop box is center to the right
    VBox activeLimitedBox = new VBox(20);
    activeLimitedBox.getChildren().addAll(productIsActive, productIsLimited);

    activeLimitedBox.setAlignment(Pos.CENTER_LEFT);

    VBox categoryIdBox = new VBox(productCategoryDropBox);
    categoryIdBox.setAlignment(Pos.CENTER_RIGHT);
    categoryIdBox.setPadding(new Insets(0, 0, 160, 0));
    // Adding them together and setting them to center of the border pane
    HBox menuLayoutCenter = new HBox();
    menuLayoutCenter.getChildren().addAll(activeLimitedBox, categoryIdBox);
    menuLayoutCenter.setPadding(new Insets(0, 10, 280, 30));
    layout.setCenter(menuLayoutCenter);

    layout.setRight(ingredientBox);

    // Bottom container for add and back button
    HBox bottomContainer = new HBox(20);
    bottomContainer.setAlignment(Pos.BOTTOM_CENTER);
    bottomContainer.getChildren().addAll(confirmButton, backButton);
    productName.setPrefWidth(300);

    layout.setBottom(bottomContainer);

    layout.setPadding(new Insets(0, 0, 50, 0));

    Scene addProductScene = new Scene(layout, 1920, 1080);
    return addProductScene;
  }

  // Helper method for showing alerts on incorrect input
  private void showAlert(String title, String message, Alert.AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * This is a method to get a product table for
   * admin menu sections like price editor and product
   * deletion menu.
   *
   * @param priceEditable true if the price should be editable
   *                      false if the price should not be editable
   * @return a table filled with products and data about them, like
   *         product id, name, type, activity and price
   */
  private TableView<Product> getProductTable(boolean priceEditable) {

    // Product ID column
    TableColumn<Product, Integer> idColumn = new TableColumn<>("Product ID");
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);

    // Product name column
    TableColumn<Product, String> nameColumn = new TableColumn<>("Product Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    // Product category column
    TableColumn<Product, String> categoryColumn = new TableColumn<>("Product Category");
    categoryColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

    // Product activity column
    TableColumn<Product, Integer> activityColumn = new TableColumn<>("Product Active");
    activityColumn.setCellValueFactory(new PropertyValueFactory<>("activity"));
    activityColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);

    // Product price column
    TableColumn<Product, Double> priceColumn = new TableColumn<>("Product Price");
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    
    // If product price is editable
    if (priceEditable) {
      priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
      priceColumn.setOnEditCommit(event -> {
        Product product = event.getRowValue();
        Double newPrice = event.getNewValue();
        int productId = product.getId();
        product.setPrice(newPrice);
  
        // TODO: This will be moved later
        Connection conn;
        try {
          conn = DriverManager.getConnection(
              "jdbc:mysql://bdzvjxbmj2y2atbkdo4j-mysql.services"
              + ".clever-cloud.com:3306/bdzvjxbmj2y2atbkdo4j"
              + "?user=u5urh19mtnnlgmog"
              + "&password=zPgqf8o6na6pv8j8AX8r"
              + "&useSSL=true"
              + "&allowPublicKeyRetrieval=true"
          );
  
          // update the newly inserted price in database
          updateProductPrice(newPrice, productId, conn);
  
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
    }
    priceColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);

    // Creating Table
    TableView<Product> productTable = new TableView<>();
    productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    productTable.setMaxWidth(Double.MAX_VALUE);
    productTable.setPrefWidth(Region.USE_COMPUTED_SIZE);
    productTable.setEditable(true);
    
    // Combining columns in table
    productTable.getColumns().add(idColumn);
    productTable.getColumns().add(nameColumn);
    productTable.getColumns().add(categoryColumn);
    productTable.getColumns().add(activityColumn);
    productTable.getColumns().add(priceColumn);

    // Querys data into the table
    try {
      // TODO: This will be moved later
      Connection conn = DriverManager.getConnection(
          "jdbc:mysql://bdzvjxbmj2y2atbkdo4j-mysql.services"
          + ".clever-cloud.com:3306/bdzvjxbmj2y2atbkdo4j"
          + "?user=u5urh19mtnnlgmog"
          + "&password=zPgqf8o6na6pv8j8AX8r"
          + "&useSSL=true"
          + "&allowPublicKeyRetrieval=true"
      );
      
      // Gets products with needed data
      ArrayList<Product> products = fetchAllProductData(conn);

      // Insert fetched data in table
      productTable.getItems().addAll(products);
      
    } catch (SQLException e) {    
      e.printStackTrace();
    }

    return productTable;
  }

  private Scene changePriceScene(Stage primaryStage, Scene prevScene) {

    // Label for screen
    Label historyLabel = new Label("Price Editor:");
    historyLabel.setStyle(
        "-fx-font-size: 45px;"
        + "-fx-font-weight: bold;"
    );

    // Table for item lisitngs
    TableView<Product> productTable = getProductTable(true);

    // VBox for the table
    VBox productListings = new VBox(productTable);
    VBox.setVgrow(productListings, Priority.ALWAYS);
    productTable.prefWidthProperty().bind(productListings.widthProperty());
    productListings.setPadding(new Insets(20, 0, 0, 0));

    // VBox to align screen label and table
    VBox topBox = new VBox();
    topBox.setMaxWidth(Double.MAX_VALUE);
    topBox.setAlignment(Pos.TOP_CENTER);
    topBox.setSpacing(40);
    topBox.getChildren().addAll(historyLabel, productListings);

    // Upper part of the screen
    HBox topContainer = new HBox();
    topContainer.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(topBox, Priority.ALWAYS);
    topContainer.setAlignment(Pos.CENTER);
    topContainer.getChildren().addAll(topBox);

    // Back button
    // Clicking button means user goes to previous screen
    var backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> {
      primaryStage.setScene(prevScene);
    });

    // Language Button
    // cycles images on click
    var langButton = new LangBtn();

    // Spacer for Bottom Row
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);
    
    // Bottom row of the screen
    HBox bottomContainer = new HBox();
    bottomContainer.setAlignment(Pos.BOTTOM_LEFT);
    bottomContainer.getChildren().addAll(langButton, spacerBottom, backButton);

    // Setting positioning of all the elements
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setTop(topContainer);
    layout.setBottom(bottomContainer);

    return new Scene(layout, 1920, 1080);
  }

  // TODO: will be moved later
  /**
   * This method fetches all necessary product data from the
   * database and returns an array of products that contain
   * all that fetched data.
   * This method is used in the price update section of the
   * admin menu.
   *
   * @param connection database connection
   * @return array containing all products with data from database
   * @throws SQLException database error
   */
  private ArrayList<Product> fetchAllProductData(Connection connection) throws SQLException {
    // ArrayList to store product data
    ArrayList<Product> products = new ArrayList<>();

    // SQL query to fetch needed data from database
    String sql = "SELECT p.product_id, p.`name`, c.`name` AS type, p.is_active, p.price "
        + "FROM product p "
        + "JOIN category c ON p.category_id = c.category_id";

    try (
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
    ) {
      while (rs.next()) {

        // Fetch all product data from database
        int productId = rs.getInt("product_id");
        String name = rs.getString("name");
        Type type = Type.valueOf(rs.getString("type").toUpperCase());
        int isActive = rs.getInt("is_active");
        double price = rs.getDouble("price");
        
        // Make new product with all fetched database data
        Product product = new Product() {};
        product.setId(productId);
        product.setName(name);
        product.setType(type);
        product.setActivity(isActive);
        product.setPrice(price);

        // Add completed product to array
        products.add(product);
      }
    }

    return products;
  }

  // TODO: will be moved later
  /**
   * This method updates the price of a specific product in
   * the database.
   * This method is used in the update price section of the admin menu.
   *
   * @param newPrice int new price of the product
   * @param productId int product id of product that will be updated
   * @param connection database connection
   * @throws SQLException database error
   */
  private void updateProductPrice(
        double newPrice,
        int productId,
        Connection connection
  ) throws SQLException {

    String sql = "UPDATE product "
        + "SET price = ? "
        + "WHERE product_id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setDouble(1, newPrice);
      stmt.setInt(2, productId);
      stmt.executeUpdate();
    }
  }

  /**
   * This is the method to create the scene for deleting
   * products in the admin menu.
   *
   * @param primaryStage the primary stage for the scenes
   * @param prevScene the scene you were previously in
   * @return product deletion menu scene
   */
  private Scene deleteProduct(Stage primaryStage, Scene prevScene) {
    // Label for screen
    Label productDeletionLabel = new Label("Product Deletion:");
    productDeletionLabel.setStyle(
        "-fx-font-size: 45px;"
        + "-fx-font-weight: bold;"
    );


    // Label for system messages
    Label systemMessageLabel = new Label();

    // Button for deletion confirmation
    Button confirmButton = new Button("Yes");

    // Button for deletion cancellation
    Button abbruchButton = new Button("No");

    // Adding buttons in separate box
    HBox buttonBox = new HBox(10, confirmButton, abbruchButton);
    buttonBox.setVisible(false);
    buttonBox.setAlignment(Pos.CENTER);

    // Adding all deletion related action elements in Box
    HBox actionBox = new HBox(10, systemMessageLabel, buttonBox);
    actionBox.setVisible(false);
    actionBox.setAlignment(Pos.CENTER);

    // Table for item lisitngs
    TableView<Product> productTable = getProductTable(false);

    // VBox for the table
    VBox productListings = new VBox(productTable);
    VBox.setVgrow(productListings, Priority.ALWAYS);
    productTable.prefWidthProperty().bind(productListings.widthProperty());
    productListings.setPadding(new Insets(20, 0, 0, 0));

    // VBox to align screen label and table
    VBox topBox = new VBox();
    topBox.setMaxWidth(Double.MAX_VALUE);
    topBox.setAlignment(Pos.TOP_CENTER);
    // TODO optimize code to make shorter/less lines !ALL!
    topBox.setSpacing(40);
    topBox.getChildren().addAll(
        productDeletionLabel,
        productListings,
        systemMessageLabel,
        actionBox
    );

    // Upper part of the screen
    HBox topContainer = new HBox();
    topContainer.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(topBox, Priority.ALWAYS);
    topContainer.setAlignment(Pos.CENTER);
    topContainer.getChildren().addAll(topBox);

    // Back button
    // Clicking button means user goes to previous screen
    var backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> {
      primaryStage.setScene(prevScene);
    });

    // Language Button
    // cycles images on click
    var langButton = new LangBtn();

    // Spacer for Bottom Row
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);
    
    // Bottom row of the screen
    HBox bottomContainer = new HBox();
    bottomContainer.setAlignment(Pos.BOTTOM_LEFT);
    bottomContainer.getChildren().addAll(langButton, spacerBottom, backButton);

    // Setting positioning of all the elements
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setTop(topContainer);
    layout.setBottom(bottomContainer);

    return new Scene(layout, 1920, 1080);
  }
}