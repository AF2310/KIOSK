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
   * @throws SQLException error
   */
  public int placeOrder(Connection conn) throws SQLException {
    Order order = new Order();
    double price = order.calculatePrice();

    String s = "INSERT INTO `order` "
        + "(kiosk_ID, customer_ID, order_date, amount_total, status)"
        + "VALUES (123, 1, NOW(), ?, 'pending')";
    
    PreparedStatement ps = conn.prepareStatement(s);
    ps.setObject(1, price);
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
   * @throws SQLException error
   */
  private int receiveOrderId(Connection conn) throws SQLException {
    int id = -1;
    String s = "SELECT LAST_INSERT_ID()";

    try (PreparedStatement ps = conn.prepareStatement(s)) {
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
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
