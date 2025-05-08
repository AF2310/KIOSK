package org.example.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.example.menu.OrderItem;
import org.example.menu.Product;
import org.example.orders.Order;

/**
 * Class for all queries related to the DB.
 */
public class SqlQueries {
  public SqlConnectionCheck connectionCheck = new SqlConnectionCheck();
  public Connection connection = connectionCheck.getConnection();

  public Connection getConnection() {
    return connection;
  }

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

    try (
        PreparedStatement stmt = connection.prepareStatement(querySql);
        ResultSet results = stmt.executeQuery()
    ) {
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

    try (
        PreparedStatement stmt = connection.prepareStatement(itemQuery);
        ResultSet rs = stmt.executeQuery()
    ) {

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
        
    try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
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
  public ResultSet getIngredientsByCategory(String categoryName) throws SQLException {
    String sql = "SELECT i.ingredient_id, i.ingredient_name " 
                + "FROM ingredient i "
                + "JOIN categoryingredients ci ON i.ingredient_id = ci.ingredient_id " 
                + "JOIN category c ON ci.category_id = c.category_id " 
                + "WHERE c.name = ?";
        
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, categoryName);
    return stmt.executeQuery();
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
        
    try (PreparedStatement stmt = connection
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
        
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      connection.setAutoCommit(false);
            
      for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
        stmt.setInt(1, productId);
        stmt.setString(2, entry.getKey());
        stmt.setInt(3, entry.getValue());
        stmt.addBatch();
      }
            
      stmt.executeBatch();
      connection.commit();
    } catch (SQLException e) {
      connection.rollback();
      throw e;
    } finally {
      connection.setAutoCommit(true);
    }
  }
}

