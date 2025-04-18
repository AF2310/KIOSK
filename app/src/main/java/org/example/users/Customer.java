package org.example.users;

import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

/**
 * Represents a customer using the Self Service Kiosk.
 */
public class Customer implements User {

  /**
    * Creates a new order.
    */
  public void placeOrder() {

  }

  /**
   * Chooses between eat here and takeaway.
   * 
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
   */
  public void receiveOrderId() {

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
