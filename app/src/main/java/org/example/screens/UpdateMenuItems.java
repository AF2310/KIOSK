package org.example.screens;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    MidButton addProductButton = new MidButton("Add product to menu", "rgb(255, 255, 255)", 30);
    MidButton changePriceButton = new MidButton("Change prices", "rgb(255, 255, 255)", 30);
    MidButton removeProductButton = new MidButton("Remove product from menu", 
                                                  "rgb(255, 255, 255)", 30);

    this.primaryStage = primaryStage;

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
    VBox menuLayout = new VBox(20);
    var menuLabel = new Label("Add A Product to the Menu");
    menuLabel.setStyle("-fx-font-size: 40");

    SquareButtonWithImg confirmButton = new SquareButtonWithImg("Add", "green_tick.png","rgb(81, 173, 86)");
    SquareButtonWithImg backButton = new SquareButtonWithImg("Cancel", "cancel.png", "rgb(255, 0, 0)");

    // Textfields for the information to put into the SQL query
    RectangleTextFieldWithLabel productName = new RectangleTextFieldWithLabel("Product Name:",
        "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel productPrice = new RectangleTextFieldWithLabel("Product Price:",
        "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel productDescription = new RectangleTextFieldWithLabel(
        "Product Description:", "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel productCategoryID = new RectangleTextFieldWithLabel(
        "Product Category ID:", "rgb(255, 255, 255)");

    // Bottom container for add and back button
    HBox bottomContainer = new HBox(20);
    bottomContainer.setAlignment(Pos.CENTER);
    bottomContainer.getChildren().addAll(confirmButton, backButton);
    productName.setPrefWidth(300);

    confirmButton.setOnAction(e -> {
      SqlConnectionCheck connection = new SqlConnectionCheck(); // TODO: fix sql statement

      String sql = "INSERT INTO product (name, price, category, stock_quantity) VALUES (?, ?, ?, ?)";
      PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
      stmt.setString(1, productName.getText());
      stmt.setDouble(2, productPrice.getText());
      stmt.setString(3, "Coffee");
      stmt.setInt(4, 100);
      stmt.executeUpdate();

      try {
        
      } catch (SQLException error) {
        // TODO: handle exception
      }
    });


    menuLayout.getChildren().addAll(menuLabel, productName, productPrice, productDescription, productCategoryID, bottomContainer);
    menuLayout.setAlignment(Pos.CENTER);
    BorderPane layout = new BorderPane();
    layout.setCenter(menuLayout);

    Scene addProductScene = new Scene(layout, 1920, 1080);
    return addProductScene;
  }
}
