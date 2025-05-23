package org.example.screens;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButton;
import org.example.buttons.SearchBar;
import org.example.kiosk.LanguageSetting;
import org.example.menu.Product;
import org.example.menu.Type;
import org.example.sql.DatabaseManager;
import org.example.sql.SqlQueries;


/**
 * Updating menu class.
 */
public class UpdateMenuItems {
  private Connection con;
  private SqlQueries pool = new SqlQueries();

  /**
   * Creates a scene for updating menu items in the admin interface.
   *
   * @param primaryStage The primary stage of the application.
   * @param prevScene    The previous scene to return to.
   * @return The scene for updating menu items.
   */
  public Scene adminUpdateMenuItems(Stage primaryStage, Scene prevScene) {


    try {
      this.con = DriverManager.getConnection(
          "jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
              + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd"
              + "?user=u5urh19mtnnlgmog"
              + "&password=zPgqf8o6na6pv8j8AX8r"
              + "&useSSL=true"
              + "&allowPublicKeyRetrieval=true");

    } catch (SQLException e) {
      e.printStackTrace();
    }
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
          prevScene);
      // Set the new (current) scene
      primaryStage.setScene(addProductScene.getProductScene());
    });
    // Action: editing product
    editProductButton.setOnAction(e -> {

      // Get product editor scene
      SearchBar searchBar = new SearchBar(con);
      ProductEditorScene productEditor = new ProductEditorScene(
          primaryStage,
          prevScene,
          getProductTable(true, true, true, true, true),
          searchBar);

      // Set the new (current) scene
      primaryStage.setScene(productEditor.getProductEditorScene());
    });

    // Action: deleting product
    removeProductButton.setOnAction(e -> {

      // Get product deletion scene
      SearchBar searchBar = new SearchBar(con);
      DeleteProductScene productDeletionScene = new DeleteProductScene(
          primaryStage,
          prevScene,
          getProductTable(false, false, false, false, false),
          searchBar);

      // Set the new (current) scene
      primaryStage.setScene(productDeletionScene.getProductDeletionScene());
    });

    MidButton addMealButton = makeMidButton("Add a Meal");

    addMealButton.setOnAction(e -> {
      AddMealScene addMealScene = new AddMealScene(primaryStage, prevScene);
      
      primaryStage.setScene(addMealScene.getAddMealScene());
    });

    MidButton globalDiscountButton = makeMidButton("Apply Global Discounts");

    globalDiscountButton.setOnAction(e -> {
      GlobalDiscountScreen globalDiscountScreen = new GlobalDiscountScreen(
          primaryStage,
          prevScene);
      
      primaryStage.setScene(globalDiscountScreen.getGlobalDiscountScreen());
    });

    // MidButton globalDiscountButton = makeMidButton("Apply Global Discounts");

    globalDiscountButton.setOnAction(e -> {
      GlobalDiscountScreen globalDiscountScreen = new GlobalDiscountScreen(
          primaryStage,
          prevScene);
      
      primaryStage.setScene(globalDiscountScreen.getGlobalDiscountScreen());
    });

    // Back button -> user goes to previous screen
    var backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> {
      primaryStage.setScene(prevScene);
    });

    // Language Button -> cycles images on click
    var langButton = new LangBtn();

    // Spacer for Bottom Row
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);

    // Bottom row of the screen
    HBox bottomContainer = new HBox(langButton, spacerBottom, backButton);
    bottomContainer.setAlignment(Pos.BOTTOM_LEFT);

    // Layout for arranging buttons in a grid
    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(20);
    gridPane.setVgap(20);
    gridPane.add(addProductButton, 0, 0);
    gridPane.add(editProductButton, 0, 1);
    gridPane.add(removeProductButton, 0, 2);
    gridPane.add(globalDiscountButton, 0, 3);

    // Position the language button in the bottom-left corner
    StackPane.setAlignment(langButton, Pos.BOTTOM_LEFT);
    StackPane.setMargin(langButton, new Insets(0, 0, 30, 30));

    // Layout borders
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setCenter(gridPane);
    layout.setBottom(bottomContainer);
    
    // Translate button action
    langButton.addAction(event -> {
      LanguageSetting lang = LanguageSetting.getInstance();
      String newLang;
      if (lang.getSelectedLanguage().equals("en")) {
        newLang = "sv";
      } else {
        newLang = "en";
      }
      lang.changeLanguage(newLang);
      lang.smartTranslate(layout);
    });

    // Translate the whole layout before rendering
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(layout);
    lang.smartTranslate(layout);

    // put everything into a stackpane
    StackPane layoutWithLangButton = new StackPane(layout, langButton);

    Scene updateItemScene = new Scene(layoutWithLangButton, 1920, 1080);

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
        30);
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
      boolean descriptionEditable,
      boolean activityEditable,
      boolean nameEditable,
      boolean timeEditable) {

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
        String newName = event.getNewValue().strip();

        // NO empty string was entered
        if (!(newName.strip().isEmpty())) {

          // Get id of local product
          int productId = product.getId();

          // Update product name locally
          product.setName(newName);

          try {
            // Update newly inserted activity value of product in database
            pool.updateProductName(newName, productId);

          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
        // ELSE: empty string was entered -> do nothing
      });
    }

    // Product description column
    TableColumn<Product, String> descriptionColumn = new TableColumn<>("Description");
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

    // If product description is editable
    if (descriptionEditable) {
      descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

      // Description column of a product is clicked
      descriptionColumn.setOnEditCommit(event -> {

        // Get current value of clicked field and get newly entered value
        Product product = event.getRowValue();
        String newDescription = event.getNewValue().strip();

        // Get id of local product
        int productId = product.getId();

        // Update product name locally
        product.setDescription(newDescription);

        try {
          // Update newly inserted activity value of product in database
          pool.updateProductDescription(newDescription, productId);

        } catch (SQLException e) {
          e.printStackTrace();
        }
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

          try {
            // Update newly inserted activity value in database
            pool.updateActivityValue(newActivityValue, productId);

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

        try {
          // update the newly inserted price in database
          pool.updateProductPrice(newPrice, productId);

        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
    }
    priceColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);

    // Product preparation time column
    TableColumn<Product, Integer> timeColumn = new TableColumn<>("Preparation Time");
    timeColumn.setCellValueFactory(new PropertyValueFactory<>("preparationTime"));

    // If product preparation time is editable
    if (timeEditable) {
      timeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
      timeColumn.setOnEditCommit(event -> {
        Product product = event.getRowValue();
        int newTime = event.getNewValue();
        int productId = product.getId();
        product.setPreparationTime(newTime);

        // TODO: This will be moved later
        try {
          Connection connection = DriverManager.getConnection(
              "jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
                  + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd"
                  + "?user=u5urh19mtnnlgmog"
                  + "&password=zPgqf8o6na6pv8j8AX8r"
                  + "&useSSL=true"
                  + "&allowPublicKeyRetrieval=true");

          // update the newly inserted price in database
          updateProductPreptime(newTime, productId, connection);

        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
    }
    timeColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);

    // Creating Table
    TableView<Product> productTable = new TableView<>();
    productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    productTable.setMaxWidth(Double.MAX_VALUE);
    productTable.setPrefWidth(Region.USE_COMPUTED_SIZE);
    productTable.setEditable(true);

    // Combining columns in table
    productTable.getColumns().add(idColumn);
    productTable.getColumns().add(nameColumn);
    productTable.getColumns().add(descriptionColumn);
    productTable.getColumns().add(categoryColumn);
    productTable.getColumns().add(activityColumn);
    productTable.getColumns().add(priceColumn);
    productTable.getColumns().add(timeColumn);

    // Querys data into the table
    try {
      // TODO: This will be moved later
      Connection conn = DriverManager.getConnection(
          "jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
              + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd"
              + "?user=u5urh19mtnnlgmog"
              + "&password=zPgqf8o6na6pv8j8AX8r"
              + "&useSSL=true"
              + "&allowPublicKeyRetrieval=true");

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
    String sql = "SELECT p.product_id, p.`name`, p.description, "
        + "c.`name` AS type, p.is_active, p.price, p.preparation_time "
        + "FROM product p "
        + "JOIN category c ON p.category_id = c.category_id";

    try (
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();) {
      while (rs.next()) {

        // Fetch all product data from database
        int productId = rs.getInt("product_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        Type type = Type.valueOf(rs.getString("type").toUpperCase());
        int isActive = rs.getInt("is_active");
        double price = rs.getDouble("price");
        int prepTime = rs.getInt("preparation_time");

        // Make new product with all fetched database data
        Product product = new Product() {
        };
        product.setId(productId);
        product.setName(name);
        product.setType(type);
        product.setDescription(description);
        product.setActivity(isActive);
        product.setPrice(price);
        product.setPreparationTime(prepTime);

        // Add completed product to array
        products.add(product);
      }
    }

    return products;
  }

  /**
   * This method updates the preparation time of a specific product in
   * the database.
   * This method is used in the update price section of the admin menu.
   *
   * @param newTime    int new preparation time of the product
   * @param productId  int product id of product that will be updated
   * @param connection database connection
   * @throws SQLException database error
   */
  private void updateProductPreptime(
      int newTime,
      int productId,
      Connection connection) throws SQLException {

    String sql = "UPDATE product "
        + "SET preparation_time = ? "
        + "WHERE product_id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setDouble(1, newTime);
      stmt.setInt(2, productId);
      stmt.executeUpdate();
    }
  }
}