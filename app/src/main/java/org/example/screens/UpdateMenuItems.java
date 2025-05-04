package org.example.screens;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.MidButton;
import org.example.buttons.RectangleTextFieldWithLabel;

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
    HBox menuLayout = new HBox(20);
    
    RectangleTextFieldWithLabel productName = new RectangleTextFieldWithLabel("Product Name:",
                                                   "rgb(255, 255, 255)");
    menuLayout.getChildren().addAll(productName);
    menuLayout.setAlignment(Pos.CENTER);
    BorderPane layout = new BorderPane();
    layout.setCenter(menuLayout);

    Scene addProductScene = new Scene(layout, 1920, 1080);
    return addProductScene;
  }
}
