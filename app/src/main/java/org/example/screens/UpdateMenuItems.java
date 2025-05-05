package org.example.screens;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.DropBoxWithLabel;
import org.example.buttons.MidButton;
import org.example.buttons.RectangleTextFieldWithLabel;
import org.example.buttons.SquareButtonWithImg;
import org.example.buttons.TickBoxWithLabel;
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
      // TODO: update product button.
    });

    removeProductButton.setOnAction(e -> {
      // TODO: remove product button.
    });

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

    DropBoxWithLabel productCategoryDropBox = new DropBoxWithLabel("Product Category:");


    Map<String, Integer> categoryMap = new HashMap<>();

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


    Map<String, String> categoryImageMap = new HashMap<>();
    categoryImageMap.put("Burger", "/food/default_burger.png");
    categoryImageMap.put("Side", "/food/default_side.png");
    categoryImageMap.put("Drink", "/food/default_drink.png");
    categoryImageMap.put("Dessert", "/food/default_dessert.png");

    SquareButtonWithImg confirmButton = new SquareButtonWithImg("Confirm",
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

    confirmButton.setOnAction(e -> {
      try {
        SqlConnectionCheck connection = new SqlConnectionCheck();
        connection.getConnection().setAutoCommit(false);



        ObservableList<String> selectedItems = ingredientListView
            .getSelectionModel().getSelectedItems();

        String selectedCategory = productCategoryDropBox.getSelectedItem();
        // Validate inputs first
        if (productName.getText().isEmpty() || productPrice.getText().isEmpty() 
            || selectedCategory == null) {
          showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
          return;
        } 
        if (selectedItems.isEmpty()) {
          showAlert("Info", "No ingredients selected!", Alert.AlertType.INFORMATION);
        }     

        String sqlProduct = "INSERT INTO"
                   + " product (name, description, price, category_id,"
                   + " is_active, is_limited, image_url)"
                   + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        byte isActive = (byte) (productIsActive.isSelected() ? 1 : 0);
        byte isLimited = (byte) (productIsLimited.isSelected() ? 1 : 0);

        PreparedStatement stmtProduct = connection.getConnection()
            .prepareStatement(sqlProduct, PreparedStatement.RETURN_GENERATED_KEYS);

        String imagePath = categoryImageMap.getOrDefault(selectedCategory,
            "/food/default_burger.png");

        stmtProduct.setString(1, productName.getText());
        stmtProduct.setString(2, productDescription.getText());
        // Must parse the string as a double
        stmtProduct.setDouble(3, Double.parseDouble(productPrice.getText()));
        stmtProduct.setInt(4, categoryMap.get(selectedCategory));
        stmtProduct.setByte(5, isActive);
        stmtProduct.setByte(6, isLimited);
        stmtProduct.setString(7, imagePath);

        int affectedRows = stmtProduct.executeUpdate();

        if (affectedRows > 0) {
          showAlert("Success!", "Product Added Successfully!", Alert.AlertType.INFORMATION);
        }

        int productId;
        try (ResultSet generatedKeys = stmtProduct.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            productId = generatedKeys.getInt(1);
          } else {
            throw new SQLException("Creating producted failed, no ID obtained");
          }
        }
        
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

    SquareButtonWithImg backButton = new SquareButtonWithImg("Cancel",
        "cancel.png", "rgb(255, 0, 0)");   

    backButton.setOnAction(e -> {
      primaryStage.setScene(prevScene);
    });

    var menuLabel = new Label("Add A Product to the Menu");
    menuLabel.setStyle("-fx-font-size: 40; -fx-font-weight: bold;");
    HBox menuTitle = new HBox(menuLabel);
    menuTitle.setAlignment(Pos.CENTER);
    menuTitle.setPadding(new Insets(70, 0, 20, 0));

    BorderPane layout = new BorderPane();


    layout.setTop(menuTitle);


    VBox menuLayoutLeft = new VBox(20);

    menuLayoutLeft.getChildren().addAll(productName,
                                    productDescription, 
                                    productPrice);
    menuLayoutLeft.setAlignment(Pos.BASELINE_LEFT);
    layout.setLeft(menuLayoutLeft);

    VBox activeLimitedBox = new VBox(20);
    activeLimitedBox.getChildren().addAll(productIsActive, productIsLimited);

    activeLimitedBox.setAlignment(Pos.CENTER_LEFT);

    VBox categoryIdBox = new VBox(productCategoryDropBox);
    categoryIdBox.setAlignment(Pos.CENTER_RIGHT);
    categoryIdBox.setPadding(new Insets(0, 0, 160, 0));
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

}