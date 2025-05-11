package org.example.screens;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.example.buttons.MidButton;
import org.example.menu.Product;
import org.example.menu.Type;

/**
 * Updating menu class.
 */
public class UpdateMenuItems {

  /**
   * Scene for adding/removing items on the menu.
   *
   * @param primaryStage Window itself.
   * @param prevScene Previous scene to return to.
   * @return The scene itself.
   */

  public Scene adminUpdateMenuItems(Stage primaryStage, Scene prevScene) {

    // All the buttons for updating menu items
    MidButton addProductButton = makeMidButton("Add Product to Menu");
    MidButton editProductButton = makeMidButton("Edit Product Data");
    MidButton removeProductButton = makeMidButton("Remove Product from Menu");

    // Actions for all mid buttons

    // Action: Adding product
    addProductButton.setOnAction(e -> {

      // Get add product scene
      AddProductScene addProductScene = new AddProductScene(
          primaryStage,
          prevScene
        );

      primaryStage.setScene(addProductScene.getProductScene());
    });

    // Action: editing product
    editProductButton.setOnAction(e -> {

      // Get product editor scene
      ProductEditorScene productEditor = new ProductEditorScene(
          primaryStage,
          prevScene,
          getProductTable(true, true, true)
      );

      primaryStage.setScene(productEditor.getProductEditorScene());
    });

    // Action: deleting product
    removeProductButton.setOnAction(e -> {

      // Get product deletion scene
      DeleteProductScene productDeletionScene = new DeleteProductScene(
          primaryStage,
          prevScene,
          getProductTable(false, false, false)
      );

      // Set the new (current) scene
      primaryStage.setScene(productDeletionScene.getProductDeletionScene());

    });

    // Layout for arranging buttons in a grid
    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(20);
    gridPane.setVgap(20);
    gridPane.add(addProductButton, 0, 0);
    gridPane.add(editProductButton, 0, 1);
    gridPane.add(removeProductButton, 0, 2);

    // Layout borders            
    BorderPane layout = new BorderPane();
    layout.setCenter(gridPane);

    // Final scene design
    Scene updateItemScene = new Scene(layout, 1920, 1080);

    return updateItemScene;
  }

  /**
   * Helper method to make the 3 update menu items
   * scene buttons.
   *
   * @param labelName label name of mid button
   * @return mid button
   */
  private MidButton makeMidButton(String labelName) {
    MidButton thisMidButton = new MidButton(
        labelName,                                             
        "rgb(255, 255, 255)", 
        30
    );
    return thisMidButton;
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
  private TableView<Product> getProductTable(
      boolean priceEditable, 
      boolean activityEditable, 
      boolean nameEditable) {

    // Product ID column
    TableColumn<Product, Integer> idColumn = new TableColumn<>("Product ID");
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);

    // Product name column
    TableColumn<Product, String> nameColumn = new TableColumn<>("Product Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    // If product name is editable
    if (nameEditable) {
      nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

      // Name column of a product is clicked
      nameColumn.setOnEditCommit(event -> {

        // Get current value of clicked field and get newly entered value
        Product product = event.getRowValue();
        String newName = event.getNewValue();

        // NO empty string was entered
        if (!(newName.strip().isEmpty())) {
          
          // Get id of local product
          int productId = product.getId();

          // Update product name locally
          product.setName(newName);
          
          // TODO: This will be moved later
          try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
                + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd"
                + "?user=u5urh19mtnnlgmog"
                + "&password=zPgqf8o6na6pv8j8AX8r"
                + "&useSSL=true"
                + "&allowPublicKeyRetrieval=true"
              );
            
            // Update newly inserted activity value of product in database
            updateProductName(newName, productId, conn);
            
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
        // ELSE: empty string was entered -> do nothing
      });
    }

    // Product category column
    TableColumn<Product, String> categoryColumn = new TableColumn<>("Product Category");
    categoryColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

    // Product activity column
    TableColumn<Product, Integer> activityColumn = new TableColumn<>("Product Active");
    activityColumn.setCellValueFactory(new PropertyValueFactory<>("activity"));
    activityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    
    // If activity value is editable
    if (activityEditable) {
      activityColumn.setOnEditCommit(event -> {
        Product product = event.getRowValue();
        int newActivityValue = event.getNewValue();
  
        // NO invalid int value was entered
        if (newActivityValue == 1 || newActivityValue == 0) {
          
          // getting local product id
          int productId = product.getId();

          // Updating value locally
          product.setActivity(newActivityValue);
          
          // TODO: This will be moved later
          try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
                + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd"
                + "?user=u5urh19mtnnlgmog"
                + "&password=zPgqf8o6na6pv8j8AX8r"
                + "&useSSL=true"
                + "&allowPublicKeyRetrieval=true"
              );
            
            // Update newly inserted activity value in database
            updateActivityValue(newActivityValue, productId, conn);
            
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
        // ELSE: invalid value was entered -> do nothing
      });
    }
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
        try {
          Connection conn = DriverManager.getConnection(
                  "jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
                  + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd"
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
                  "jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
                  + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd"
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
  

  // TODO DATABASE/QUERY METHODS BELOW


  /**
   * Query method to change the name of a product.
   * Used in product table getter method.
   *
   * @param newName String new name of product
   * @param productId int product id that gets name-change
   * @param connection Database connection
   * @throws SQLException Database error
   */
  private void updateProductName(
      String newName,
      int productId,
      Connection connection
  ) throws SQLException {

    String sql = "UPDATE product "
        + "SET name = ? "
        + "WHERE product_id = ?";
    
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, newName);
      stmt.setInt(2, productId);
      stmt.executeUpdate();
    }
  }

  /**
   * Query method to update is_active value of a product.
   * Used in product table getter method.
   *
   * @param newActivityValue int new is_active value (1 or 0)
   * @param productId int id of product that will be changed
   * @param connection Connection to database
   * @throws SQLException Database error
   */
  private void updateActivityValue(
      int newActivityValue,
      int productId,
      Connection connection
  ) throws SQLException {

    String sql = "UPDATE product "
        + "SET is_active = ? "
        + "WHERE product_id = ?";
    
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, newActivityValue);
      stmt.setInt(2, productId);
      stmt.executeUpdate();
    }
  }

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
}