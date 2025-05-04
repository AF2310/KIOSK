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

    SquareButtonWithImg confirmButton = new SquareButtonWithImg("Next",
        "green_right_arrow.png", "rgb(81, 173, 86)");
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
    RectangleTextFieldWithLabel productIsLimited = new RectangleTextFieldWithLabel("Is limited?",
        "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel productIsActive = new RectangleTextFieldWithLabel("Is active?",
        "rgb(255, 255, 255)");

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

        byte isActive = 0;
        byte isLimited = 0;

        
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

    // Bottom container for add and back button
    HBox bottomContainer = new HBox(20);
    bottomContainer.setAlignment(Pos.CENTER);
    bottomContainer.getChildren().addAll(confirmButton, backButton);
    productName.setPrefWidth(300);

    VBox menuLayoutRight = new VBox(20);
    VBox menuLayoutLeft = new VBox(20);
    var menuLabel = new Label("Add A Product to the Menu");
    menuLabel.setStyle("-fx-font-size: 40");

    
    menuLayoutLeft.getChildren().addAll(menuLabel, productName, productPrice, 
                                    productDescription, productCategoryid,
                                    bottomContainer);

    menuLayoutRight.getChildren().addAll(productIsActive, productIsLimited);
    menuLayoutLeft.setAlignment(Pos.CENTER_LEFT);
    menuLayoutRight.setAlignment(Pos.CENTER_RIGHT);

    HBox menuLayout = new HBox(15);
    menuLayout.getChildren().addAll(menuLayoutLeft, menuLayoutRight);
    BorderPane layout = new BorderPane();
    layout.setCenter(menuLayout);

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