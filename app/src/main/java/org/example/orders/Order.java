package org.example.orders;

import java.sql.Timestamp;
import java.util.ArrayList;
import org.example.menu.Product;

/**
 * Handles the ordering process and holds the carted items.
 */
public class Order {
  
  private int orderId;
  private int kioskId;
  private int customerId;
  private Timestamp orderDate;
  private double amountTotal;
  private String status;
  private ArrayList<Product> products = new ArrayList<>();

  /**
   * Constructor for orderes queried from the db.
   */
  public Order(int orderId, int kioskId, int customerId,
      Timestamp orderDate, double amountTotal, String status) {

    this.orderId = orderId;
    this.kioskId = kioskId;
    this.customerId = customerId;
    this.orderDate = orderDate;
    this.amountTotal = amountTotal;
    this.status = status;

  }

  /**
   * Will calculate the overall cost of the order
   * by iterating thru the ArrayLists.
   */
  public double calculatePrize() {

    /*
     * Default return value for now.
     */
    return 0.00;

  }

  /**
   * Will add a Meal to the corresponding ArrayList
   * Must call on calculatePrize() to update orders cost.
   */
  public void addMeal() {}

  /**
   * Will add a Single to the corresponding ArrayList
   * Must call on calculatePrize() to update orders cost.
   */
  public void addSingle() {}

  /**
   * Will remove a selected Item from its ArrayList
   * Must call on calculatePrize() to update orders cost.
   */
  public void removeItem() {}

  /**
   * Getter for the orders ID.
   */
  public int getOrderId() {

    return orderId;

  }

  /**
   * Getter for the kiosk ID.
   */
  public int getKioskId() {

    return kioskId;

  }

  /**
   * Getter for the customer ID.
   */
  public int getCustomerId() {

    return customerId;

  }

  /**
   * Getter for date of order.
   */
  public Timestamp getOrderDate() {

    return orderDate;

  }

  /**
   * Getter for the cost of the order.
   */
  public double getAmountTotal() {

    return amountTotal;

  }

  /**
   * Getter for the orders status.
   */
  public String getStatus() {

    return status;

  }

  /**
   * Getter for the products of the order.
   */
  public ArrayList<Product> getProducts() {

    return products;

  }
}
