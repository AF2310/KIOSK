package org.example.sql;

import java.time.LocalDate;

/**
 * This is for retrieving order data like order id etc..
 * TODO: add insert and retrieval queries; replace dummy code
 */
public class OrderData {

  private int orderId;
  private Object orderDate;
  private int amountTotal;
  private String status;
  
  /**
   * This is the constructor of the Order data
   * retrvieval class.
   */
  public OrderData() {}

  public void setId(int orderId) {
    this.orderId = orderId;
  }
  
  public int getId() {
    return orderId;
  }

  
  // KioskID is a key from another table
  // -> no setter in this class for it
  public int getKioskId() {
    return 0;
  }


  // CustomerID is a key from another table
  // -> no setter in this class for it
  public int getCustomerId() {
    return 0;
  }


  /**
   * Setter method for current date of registration.
   */
  public void setOrderDate() {
    LocalDate date = LocalDate.now();
    this.orderDate = date;
  }

  public Object getOrderDate() {
    return orderDate;
  }


  public void setTotalAmount(int amountTotal) {
    this.amountTotal = amountTotal;
  }
  
  public int getTotalAmount() {
    return amountTotal;
  }


  public void setStatus(String status) {
    this.status = status.strip();
  }

  public String getStatus() {
    return status;
  }
}
