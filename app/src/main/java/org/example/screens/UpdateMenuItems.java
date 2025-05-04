package org.example.screens;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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

    });

    removeProductButton.setOnAction(e -> {
        
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

    SquareButtonWithImg confirmButton = new SquareButtonWithImg("Confirm",
        "green_tick.png", "rgb(81, 173, 86)");
    SquareButtonWithImg backButton = new SquareButtonWithImg("Cancel",
        "cancel.png", "rgb(255, 0, 0)");

    // Textfields for the information to put into the SQL query
    RectangleTextFieldWithLabel productName = new RectangleTextFieldWithLabel("Product Name:",
        "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel productPrice = new RectangleTextFieldWithLabel("Product Price:",
        "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel productDescription = new RectangleTextFieldWithLabel(
        "Product Description:", "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel productCategoryid = new RectangleTextFieldWithLabel(
        "Product Category ID:", "rgb(255, 255, 255)");
    TickBoxWithLabel productIsActive = new TickBoxWithLabel("Is active?");
    TickBoxWithLabel productIsLimited = new TickBoxWithLabel("Is limited?");

    confirmButton.setOnAction(e -> {
      try {
        SqlConnectionCheck connection = new SqlConnectionCheck();
        connection.getConnection().setAutoCommit(true);

        // Validate inputs first
        if (productName.getText().isEmpty() || productPrice.getText().isEmpty() 
            || productDescription.getText().isEmpty() || productCategoryid.getText().isEmpty()) {
          showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
          return;
        }        

        String sql = "INSERT INTO"
                   + " product (name, description, price, category_id, is_active, is_limited)"
                   + " VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
        
        stmt.setString(1, productName.getText());
        // Must parse the string as a double
        stmt.setString(2, productDescription.getText());
        stmt.setDouble(3, Double.parseDouble(productPrice.getText()));
        stmt.setInt(4, Integer.parseInt(productCategoryid.getText()));

        byte isActive = (byte) (productIsActive.isSelected() ? 1 : 0);
        byte isLimited = (byte) (productIsLimited.isSelected() ? 1 : 0);

        
        stmt.setByte(5, isActive);
        stmt.setByte(6, isLimited);
        int affectedRows = stmt.executeUpdate();

        if (affectedRows > 0) {
          showAlert("Success!", "Product Added Successfully!", Alert.AlertType.INFORMATION);
        }
        
      } catch (NumberFormatException ex) {
        showAlert("Input error", "Enter valid numbers for price and category ID",
                  Alert.AlertType.ERROR);
      } catch (SQLException ex) {
        // Full error for debugging
        ex.printStackTrace();
        showAlert("Database Error", "Failed to save product", Alert.AlertType.ERROR);
      }
    });

    BorderPane layout = new BorderPane();
    
    var menuLabel = new Label("Add A Product to the Menu");
    menuLabel.setStyle("-fx-font-size: 40");
    HBox menuTitle = new HBox(menuLabel);
    menuTitle.setAlignment(Pos.CENTER);
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

    VBox categoryIdBox = new VBox(productCategoryid);
    categoryIdBox.setAlignment(Pos.TOP_CENTER);

    HBox menuLayoutCenter = new HBox();
    menuLayoutCenter.getChildren().addAll(activeLimitedBox, categoryIdBox);
    layout.setCenter(menuLayoutCenter);

    var ingredientList = new Label("Ingredient List");
    ingredientList.setStyle("-fx-font-size: 30");
    HBox ingredientBox = new HBox(ingredientList);
    ingredientBox.setAlignment(Pos.CENTER);

    layout.setRight(ingredientList);
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