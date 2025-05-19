package org.example.screens;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.RectangleTextFieldWithLabel;
import org.example.buttons.SqrBtnWithOutline;
import org.example.sql.SqlConnectionCheck;


/**
 * The Scene to add a Meal to the menu.
 */
public class AddMealScene {
  private Stage primaryStage;
  private Scene prevScene;

  public AddMealScene(Stage primaryStage, Scene prevScene) {
    this.primaryStage = primaryStage;
    this.prevScene = prevScene;
  }

  /**
   * Method for getting the meal scene.
   *
   * @return the meal scene
   */
  public Scene getAddMealScene() {

    RectangleTextFieldWithLabel mealName = new RectangleTextFieldWithLabel("Meal Name:",
        "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel mealPrice = new RectangleTextFieldWithLabel("Price",
        "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel mealDescription = new RectangleTextFieldWithLabel(
          "Meal Description:",
        "rgb(255, 255, 255)");

    ObservableList<CheckBox> products = FXCollections.observableArrayList();
    ObservableList<CheckBox> sides = FXCollections.observableArrayList();
    ObservableList<CheckBox> drinks = FXCollections.observableArrayList();

    try {
      SqlConnectionCheck connection = new SqlConnectionCheck();
      String sql = "SELECT name FROM product WHERE category_id = 1";
      PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        CheckBox checkBox = new CheckBox(rs.getString("name"));
        products.add(checkBox);
      }

      String sql2 = "SELECT name FROM product WHERE category_id = 2";
      PreparedStatement stmt2 = connection.getConnection().prepareStatement(sql2);
      ResultSet rs2 = stmt2.executeQuery();

      while (rs2.next()) {
        CheckBox checkBox = new CheckBox(rs2.getString("name"));
        sides.add(checkBox);
      }

      String sql3 = "SELECT name FROM product WHERE category_id = 3";
      PreparedStatement stmt3 = connection.getConnection().prepareStatement(sql3);
      ResultSet rs3 = stmt3.executeQuery();

      while (rs3.next()) {
        CheckBox checkBox = new CheckBox(rs3.getString("name"));
        drinks.add(checkBox);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    ListView<CheckBox> pickSide = new ListView<>(sides);
    ListView<CheckBox> pickDrink = new ListView<>(drinks);
    ListView<CheckBox> chooseProduct = new ListView<>(products);

    VBox pickOptionsBox = new VBox(pickSide, pickDrink);

    var productLabel = new Label("Choose Main");
    productLabel.setStyle("-fx-font-size: 30");

    VBox chooseProductBox = new VBox(productLabel, chooseProduct);
    chooseProductBox.setAlignment(Pos.CENTER);
    chooseProductBox.setPadding(new Insets(0, 150, 50, 0));
    
    VBox leftSide = new VBox();
    leftSide.getChildren().addAll(mealName, mealPrice, mealDescription);

    var menuLabel = new Label("Add a meal to the menu");
    menuLabel.setStyle("-fx-font-size: 40; -fx-font-weight: bold;");
  
    SqrBtnWithOutline confirmButton = new SqrBtnWithOutline("Confirm",
        "green_tick.png", "rgb(81, 173, 86)");

    confirmButton.setOnAction(e -> {
      ArrayList<String> selectedProducts = getSelected(products);
      if (selectedProducts.size() == 1) {
        ArrayList<String> selectedDrinks = getSelected(drinks);
        ArrayList<String> selectSides = getSelected(sides);

        try {
          SqlConnectionCheck connection = new SqlConnectionCheck();
          String sql = "SELECT product_id FROM product WHERE name = ?";
          PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
          stmt.setString(1, selectedProducts.get(0));
          ResultSet rs = stmt.executeQuery();

          rs.next();
          int productId = rs.getInt("product_id");
          rs.close();
          stmt.close();

          String sql2 = "INSERT INTO meal (name, description, price, image_url, product_id) VALUES (?, ?, ?, ?, ?)";
          PreparedStatement stmt2 = connection.getConnection().prepareStatement(sql2);
          stmt2.setString(1, mealName.getText());
          stmt2.setString(2, mealDescription.getText());
          stmt2.setDouble(3, Double.parseDouble(mealPrice.getText()));
          stmt2.setString(4, "/food/default_meal.png");
          stmt2.setInt(5, productId);
          stmt2.executeUpdate();
          stmt2.close();
          System.out.println("Ended");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      } else {
        System.out.println("Choose only one Product");
      }
    });

    BorderPane layout = new BorderPane();
    layout.setTop(menuLabel);
    layout.setLeft(leftSide);
    layout.setCenter(chooseProductBox);
    layout.setRight(pickOptionsBox);
    layout.setBottom(confirmButton);

    
    Scene addMealScene = new Scene(layout, 1920, 1080);
    return addMealScene;
  }

  private ArrayList<String> getSelected(ObservableList<CheckBox> list) {
    ArrayList<String> select = new ArrayList<>();
    for (CheckBox cb : list) {
      if (cb.isSelected()) {
        select.add(cb.getText());
      }
    }
    return select;
  }
}
