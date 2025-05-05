package org.example.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.orders.Order;
//import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

/**
 * Represents a customer using the Self Service Kiosk.
 */
public class Customer implements User {

  /**
   * Creates a new order.
   *
   * @throws SQLException SQL Database errors
   */
  public int placeOrder(Connection conn) throws SQLException {
    // Calculate total order price
    Order order = new Order();
    double price = order.calculatePrice();

    // Customer id is NOT nullable for now TODO make nullable
    // SQL Query as string statement
    String s = "INSERT INTO `order` "
        + "(kiosk_ID, customer_ID, order_date, amount_total, status)"
        + "VALUES (123, 1, NOW(), ?, 'pending')";
    
    // Prepare statement to be actual query
    PreparedStatement ps = conn.prepareStatement(s);

    // Insert price variable
    ps.setObject(1, price);

    // Execute query
    ps.executeUpdate();

    // immediately fetch the auto generated order id
    return receiveOrderId(conn);
  }

  /**
   * Chooses between eat here and takeaway.
   *
   * @param newOrder the order that is being placed.
   */
  public void selectOrderOption(Order newOrder) {

  }

  /**
   * Shows the summary of the order.
   */
  public void viewOrderSummary() {

  }

  /**
   * Generates or shows the order ID.
   * This has to be used right after order creation.
   * To not get false IDs on wrong usage, this is private
   * and used in the place order method only.
   *
   * @throws SQLException SQL Database errors
   */
  private int receiveOrderId(Connection conn) throws SQLException {
    // Set default order id
    int id = -1;

    // SQL Query as string statement
    String s = "SELECT LAST_INSERT_ID()";

    // Prepare statement to be actual query
    // Using try to save ressources and close process automatically
    try (PreparedStatement ps = conn.prepareStatement(s)) {
      // Open result set to fetch order id (execute query)
      ResultSet rs = ps.executeQuery();

      // if there is a result
      if (rs.next()) {
        // store result as order id integer
        id = rs.getInt(1);
      }
    }

    return id;
  }

  /**
   * Cancels the current order.
   */
  public void abortOrder() {

  }

  /**
   * Proceeds to payment.
   */
  public void selectPayment() {

  }

  /**
   * Optionally link the order to a user account.
   */
  public void linkToAccount() {

  }

  /**
   * Browses the menu implementation.
   */
  @Override
   public void browseMenu() {

  }

  /**
   * Searches a product by name.
   */
  @Override
  public void searchProduct(String name) {

  }    
    
}
