package org.example.orders;

import java.util.ArrayList;
import org.example.menu.Meal;
import org.example.menu.Single;

/**
 * Handles the ordering process and holds the carted items.
 */
public class Order {
  
  /*
   * Attributes should be private
   */
  public double cost;
  public ArrayList<Meal> meals = new ArrayList<>();
  public ArrayList<Single> singles = new ArrayList<>();

  /**
   * Empty constructor because:
   * Cost will be calculated by method
   * The order is empty before Items are added to cart.
   */
  public Order() {}

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

}
