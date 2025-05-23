package org.example.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.menu.Ingredient;
import org.example.menu.OrderItem;
import org.example.menu.Product;
import org.example.menu.Single;
import org.example.menu.Type;
import org.example.orders.Order;


/**
 * Class for all queries related to the DB.
 */
public class SqlQueries {
  /**
   * Order query method.
   *
   * @return history arraylist.
   * @throws SQLException error handling.
   */
  public ArrayList<Order> queryOrders() throws SQLException {

    // ArrayList to hold all orders queried from the db
    ArrayList<Order> history = new ArrayList<>();

    String querySql = "SELECT order_ID, kiosk_ID, customer_ID, order_date, amount_total, status "
        + "FROM `order`";

    try (Connection connection = DatabaseManager.getConnection()) {
      PreparedStatement stmt = connection.prepareStatement(querySql);
      ResultSet results = stmt.executeQuery();
    
      // Creates Orders from queried data
      while (results.next()) {
        int orderId = results.getInt("order_ID");
        int kioskId = results.getInt("kiosk_ID");
        int customerId = results.getInt("customer_ID");
        Timestamp orderDate = results.getTimestamp("order_date");
        double amountTotal = results.getDouble("amount_total");
        String status = results.getString("status");

        Order order = new Order(orderId, kioskId, customerId, orderDate, amountTotal, status);
        history.add(order);
      }
    }
    return history;
  }

  /**
   * Query for each product belonging to which order.
   *
   * @param orders orders.
   * @throws SQLException error handling.
   */
  public void queryOrderItemsFor(ArrayList<Order> orders) throws SQLException {

    String itemQuery = "SELECT oi.order_id, oi.product_id, p.name, p.price, oi.quantity "
          + "FROM order_item oi "
          + "JOIN product p ON oi.product_id = p.product_id";

    try (Connection connection = DatabaseManager.getConnection()) {
      PreparedStatement stmt = connection.prepareStatement(itemQuery);
      ResultSet rs = stmt.executeQuery();
     
      while (rs.next()) {
        int orderId = rs.getInt("order_id");
        int productId = rs.getInt("product_id");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        int quantity = rs.getInt("quantity");

        for (Order order : orders) {

          if (order.getOrderId() == orderId) {
            Product product = new Product() {};
            product.setId(productId);
            product.setName(name);
            product.setPrice(price);

            OrderItem orderItem = new OrderItem(product, quantity, price);

            order.getProducts().add(orderItem);

            break;
          }
        }
      }
    }
  }

  /**
   * Query for getting all categories.
   *
   * @return The categories.
   * @throws SQLException error handling.
   */

  public Map<String, Integer> getAllCategories() throws SQLException {
    Map<String, Integer> categoryMap = new HashMap<>();
    String sql = "SELECT category_id, name FROM category";
        
    try (Connection connection = DatabaseManager.getConnection()) {
      PreparedStatement stmt = connection.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("category_id");
        String name = rs.getString("name");
        categoryMap.put(name, id);
      }
    }
    return categoryMap;
  }

  /**
   * Query for getting all ingredients by category.
   *
   * @param categoryName name of the category.
   * @return a result set for all ingredients.
   * @throws SQLException error handling.
   */
  public List<String> getIngredientsByCategory(String categoryName) throws SQLException {
    List<String> ingredients = new ArrayList<>();
    String sql = "SELECT i.ingredient_id, i.ingredient_name " 
                    + "FROM ingredient i "
                    + "JOIN categoryingredients ci ON i.ingredient_id = ci.ingredient_id " 
                    + "JOIN category c ON ci.category_id = c.category_id " 
                    + "WHERE c.name = ?";
    try (Connection connection = DatabaseManager.getConnection()) {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, categoryName);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          ingredients.add(rs.getString("ingredient_name"));
        }
      }
    }
    return ingredients;
  }

  /**
   * Query for adding product into database.
   *
   * @param name name of product
   * @param description description
   * @param price price
   * @param categoryId category ID
   * @param isActive boolean
   * @param isLimited boolean
   * @param imageUrl image url.
   * @throws SQLException error handling.
   */
  public int addProduct(String name, String description, double price, int categoryId, 
                         byte isActive, byte isLimited, String imageUrl) throws SQLException {

    String sql = "INSERT INTO product (name, description, price, category_id, "
                   + "is_active, is_limited, image_url) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
    try (Connection connection = DatabaseManager.getConnection();
        PreparedStatement stmt = connection
            .prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

      stmt.setString(1, name);
      stmt.setString(2, description);
      stmt.setDouble(3, price);
      stmt.setInt(4, categoryId);
      stmt.setByte(5, isActive);
      stmt.setByte(6, isLimited);
      stmt.setString(7, imageUrl);
        
            
      int affectedRows = stmt.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Creating product failed, no rows affected.");
      }
            
      try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getInt(1);
        } else {
          throw new SQLException("Creating product failed, no ID obtained.");
        }
      }
    }
  }

  /**
  * Query for adding products ingredients.
   *
   * @param productId product ID
   * @param ingredients ingredients of the product
   * @throws SQLException error handling.
   */
  public void addProductIngredients(int productId, Map<String, Integer> ingredients) 
       throws SQLException {
    String sql = "INSERT INTO productingredients (product_id, ingredient_id, ingredientCount) "
                + "VALUES (?, (SELECT ingredient_id FROM ingredient WHERE ingredient_name = ?), ?)";
    
    Connection connection = null;
    try {
      connection = DatabaseManager.getConnection();
      connection.setAutoCommit(false);

      try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
          stmt.setInt(1, productId);
          stmt.setString(2, entry.getKey());
          stmt.setInt(3, entry.getValue());
          stmt.addBatch();
        }

        stmt.executeBatch();
      }

      connection.commit();
    } catch (SQLException e) {
      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException rollbackEx) {
          // log rollback failure
          rollbackEx.printStackTrace();
        }
      }
      throw e;
    } finally {
      if (connection != null) {
        try {
          connection.setAutoCommit(true);
          connection.close();
        } catch (SQLException ex) {
          // log closing failure
          ex.printStackTrace();
        }
      }
    }
  }
  /**
   * Retrieves all Single products from the database.
   *
   * @param conn database connection
   * @return list of Single products
   * @throws SQLException if database access error occurs
   */

  public List<Single> getAllSingles(Connection conn) throws SQLException {
    List<Single> list = new ArrayList<>();
    String sql = "SELECT product_id, name, price, image_url FROM product";

    try (PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        list.add(new Single(
            rs.getInt("product_id"),
            rs.getString("name"),
            rs.getDouble("price"),
            Type.EXTRA, // Default type, can be modified as needed
            rs.getString("image_url")
        ));
      }
    }
    return list;
  }

  /**
   * Saves a Single product to the database.
   *
   * @param conn database connection
   * @param single the Single product to save
   * @throws SQLException if database access error occurs
   */
  public void saveSingleToDb(Connection conn, Single single) throws SQLException {
    String sql = "INSERT INTO product "
        + "(name, price, category_id, image_url) VALUES (?, ?, ?, ?)";

    try (PreparedStatement stmt = conn.prepareStatement(sql,
         PreparedStatement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, single.getName());
      stmt.setDouble(2, single.getPrice());
      stmt.setInt(3, getCategoryIdForType(single.getType())); // Helper method needed
      stmt.setString(4, single.getImagePath());
      stmt.executeUpdate();

      try (ResultSet rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          single.setId(rs.getInt(1));
        }
      }
    }
  }

  /**
   * Retrieves Single products by their type.
   *
   * @param conn database connection
   * @param type the type of Single products to retrieve
   * @return list of Single products matching the type
   * @throws SQLException if database access error occurs
   */
  public List<Single> getOptionsByType(Connection conn, Type type) throws SQLException {
    List<Single> options = new ArrayList<>();
    String sql = "SELECT p.product_id AS id, p.name, p.price, p.image_url, c.name AS type "
        + "FROM product p "
        + "JOIN category c ON p.category_id = c.category_id "
        + "WHERE c.name = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, type.name());
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          options.add(new Single(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getDouble("price"),
              Type.valueOf(rs.getString("type").toUpperCase()),
              rs.getString("image_url")
          ));
        }
      }
    }
    return options;
  }

  /**
   * Retrieves Single products by type name.
   *
   * @param conn database connection
   * @param typeName the name of the type
   * @return list of Single products matching the type
   * @throws SQLException if database access error occurs
   */
  public List<Single> getOptionsByTypeName(Connection conn, String typeName) throws SQLException {
    return getOptionsByType(conn, Type.valueOf(typeName.toUpperCase()));
  }

  /**
   * Retrieves Single products under a specified price.
   *
   * @param priceLimit the maximum price
   * @param conn database connection
   * @return list of Single products under the price limit
   * @throws SQLException if database access error occurs
   */
  public List<Single> getSinglesUnder(double priceLimit, Connection conn) throws SQLException {
    List<Single> list = new ArrayList<>();
    String sql = "SELECT p.product_id AS id, p.name, p.price, p.image_url, c.name AS type "
        + "FROM product p "
        + "JOIN category c ON p.category_id = c.category_id "
        + "WHERE price < ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setDouble(1, priceLimit);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          list.add(new Single(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getDouble("price"),
              Type.valueOf(rs.getString("type").toUpperCase()),
              rs.getString("image_url")
          ));
        }
      }
    }
    return list;
  }

  /**
   * Deletes a Single product by its ID.
   *
   * @param conn database connection
   * @param id the ID of the product to delete
   * @throws SQLException if database access error occurs
   */
  public void deleteSingleById(Connection conn, int id) throws SQLException {
    String sql = "DELETE FROM product WHERE product_id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }

  /**
   * Reduces the quantity of a product.
   *
   * @param conn database connection
   * @param id the product ID
   * @param amount the amount to reduce
   * @throws SQLException if database access error occurs
   */
  public void reduceProductQuantity(Connection conn, int id, int amount) throws SQLException {
    String sql = "UPDATE product SET quantity = quantity - ? "
        + "WHERE product_id = ? AND quantity >= ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, amount);
      stmt.setInt(2, id);
      stmt.setInt(3, amount);
      stmt.executeUpdate();
    }
  }

  /**
   * Retrieves Single products by category ID.
   *
   * @param conn database connection
   * @param categoryId the category ID
   * @return list of Single products in the category
   * @throws SQLException if database access error occurs
   */
  public List<Single> getOptionsByCategoryId(Connection conn, int categoryId) throws SQLException {
    List<Single> options = new ArrayList<>();
    String sql = "SELECT p.product_id AS id, p.name, p.price, p.image_url, c.name AS type "
        + "FROM product p "
        + "JOIN category c ON p.category_id = c.category_id "
        + "WHERE p.category_id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, categoryId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          options.add(new Single(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getDouble("price"),
              Type.valueOf(rs.getString("type").toUpperCase()),
              rs.getString("image_url")
          ));
        }
      }
    }
    return options;
  }

  /**
   * Sets the ingredients for a Single product.
   *
   * @param conn database connection
   * @param single the Single product to set ingredients for
   * @throws SQLException if database access error occurs
   */
  public void setIngredientsForSingle(Connection conn, Single single) throws SQLException {
    String sql = "SELECT pi.ingredient_id, pi.ingredientCount, i.ingredient_name "
        + "FROM productingredients pi "
        + "JOIN ingredient i ON pi.ingredient_id = i.ingredient_id "
        + "WHERE product_id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, single.getId());
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          single.addIngredient(new Ingredient(
              rs.getInt("ingredient_id"), 
              rs.getString("ingredient_name")));
          single.quantity.add(rs.getInt("ingredientCount"));
        }
      }
    }
  }

  /**
   * Helper method to get category ID for a given type.
   *
   * @param type the product type
   * @return the category ID
   * @throws SQLException if database access error occurs
   */
  private int getCategoryIdForType(Type type) throws SQLException {
    String sql = "SELECT category_id FROM category WHERE name = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, type.name());
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1);
        }
      }
    }
    return -1; // or throw an exception if not found
  }

  /**
   * Query method to change the name of a product.
   * Used in product table getter method.
   *
   * @param newName    String new name of product
   * @param productId  int product id that gets name-change
   * @throws SQLException Database error
   */
  public void updateProductName(
      String newName,
      int productId) throws SQLException {

    String sql = "UPDATE product "
        + "SET name = ? "
        + "WHERE product_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, newName);
      stmt.setInt(2, productId);
      stmt.executeUpdate();
    }
  }

  /**
   * Query method to change the description of a product.
   * Used in product table getter method.
   *
   * @param newDescription String new description of product
   * @param productId      int product id that gets new description
   * @throws SQLException Database error
   */
  public void updateProductDescription(
      String newDescription,
      int productId) throws SQLException {

    String sql = "UPDATE product "
        + "SET description = ? "
        + "WHERE product_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, newDescription);
      stmt.setInt(2, productId);
      stmt.executeUpdate();
    }
  }

  /**
   * Query method to update is_active value of a product.
   * Used in product table getter method.
   *
   * @param newActivityValue int new is_active value (1 or 0)
   * @param productId        int id of product that will be changed
   * @throws SQLException Database error
   */
  public void updateActivityValue(
      int newActivityValue,
      int productId) throws SQLException {

    String sql = "UPDATE product "
        + "SET is_active = ? "
        + "WHERE product_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, newActivityValue);
      stmt.setInt(2, productId);
      stmt.executeUpdate();
    }
  }

  /**
   * This method updates the price of a specific product in
   * the database.
   * This method is used in the update price section of the admin menu.
   *
   * @param newPrice   int new price of the product
   * @param productId  int product id of product that will be updated
   * @throws SQLException database error
   */
  public void updateProductPrice(
      double newPrice,
      int productId) throws SQLException {

    String sql = "UPDATE product "
        + "SET price = ? "
        + "WHERE product_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setDouble(1, newPrice);
      stmt.setInt(2, productId);
      stmt.executeUpdate();
    }
  }

  /**
   * This method updates the preparation time of a specific product in
   * the database.
   * This method is used in the update price section of the admin menu.
   *
   * @param newTime    int new preparation time of the product
   * @param productId  int product id of product that will be updated
   * @throws SQLException database error
   */
  public void updateProductPreptime(
      int newTime,
      int productId) throws SQLException {

    String sql = "UPDATE product "
        + "SET preparation_time = ? "
        + "WHERE product_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setDouble(1, newTime);
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
   * @return array containing all products with data from database
   * @throws SQLException database error
   */
  public ArrayList<Product> fetchAllProductData() throws SQLException {

    // ArrayList to store product data
    ArrayList<Product> products = new ArrayList<>();

    // SQL query to fetch needed data from database
    String sql = "SELECT p.product_id, p.`name`, p.description, "
        + "c.`name` AS type, p.is_active, p.price, p.preparation_time "
        + "FROM product p "
        + "JOIN category c ON p.category_id = c.category_id";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
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
} 

