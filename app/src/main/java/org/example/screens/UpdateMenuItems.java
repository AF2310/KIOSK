package org.example.screens;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
import org.example.buttons.SqrBtnWithOutline;
import org.example.buttons.TickBoxWithLabel;
import org.example.sql.SqlQueries;

/**
 * Updating menu class.
 */
public class UpdateMenuItems {
  private SqlQueries queries = new SqlQueries();
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

    try {
      // Get categories from SqlQueries class
      Map<String, Integer> categoryMap = queries.getAllCategories();
      productCategoryDropBox.getComboBox().getItems().addAll(categoryMap.keySet());

      productCategoryDropBox.getComboBox().setOnAction(e -> {
        // Gets selected category
        String selectedCategory = productCategoryDropBox.getSelectedItem();
        if (selectedCategory != null) {
          try {
            List<String> ingredients = queries.getIngredientsByCategory(selectedCategory);
            ingredientListView.setItems(FXCollections.observableArrayList(ingredients));


          } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Database error", "Failed to load categories", Alert.AlertType.ERROR);
          }
        }
      });
    } catch (SQLException ex) {
      ex.printStackTrace();
      showAlert("Database error", "Failed to load categories", Alert.AlertType.ERROR);
    } 

    //Maps categories to default image paths for now
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

    // Tick boxes for Is_active or Is_limited
    TickBoxWithLabel productIsActive = new TickBoxWithLabel("Is active?");
    TickBoxWithLabel productIsLimited = new TickBoxWithLabel("Is limited?");

    // Handler for when the confirm button is clicked, it adds that new product
    confirmButton.setOnAction(e -> {
      try {
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

        Map<String, Integer> categoryMap = queries.getAllCategories();
        int categoryId = categoryMap.get(selectedCategory);

        String imageFileName;
        // And constructs the image file path based on the selected category
        if (selectedCategory.equalsIgnoreCase("burgers")) {
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

        int productId = queries.addProduct(
            productName.getText(),
            productDescription.getText(),
            Double.parseDouble(productPrice.getText()),
            categoryId,
            isActive,
            isLimited,
            imageUrl
        );

        Map<String, Integer> ingredientsMap = new HashMap<>();
        int ingredientCount = 1;
        for (String ingredientName : selectedItems) {
          ingredientsMap.put(ingredientName, ingredientCount++);
        }
        
        queries.addProductIngredients(productId, ingredientsMap);

        // Notifying the admin if the adding proccess was successful or not
        showAlert("Success!", "Product Added Successfully!", Alert.AlertType.INFORMATION);
    
                
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

}