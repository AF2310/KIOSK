package org.example.orders;

import java.util.ArrayList;
import org.example.menu.Meal;
import org.example.menu.Product;
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
  public double calculatePrice() {

    Cart theCart = Cart.getInstance();
    
    // Get items and their quantities
    Product[] theItems = theCart.getItems();
    int[] theQuantities = theCart.getQuantity();

    // Get total price
    double total = 0.0;

    // Iterate through the lists to get item and corresponding quantity
    for (int index = 0; index < theItems.length; index++) {

      // Multiply price of current item by its' quantity
      // and add calculated price to total
      total += 
      theItems[index].getPrice() * theQuantities[index];
    }

    return total;

  }

  /**
   * Will add a Meal to the corresponding ArrayList
   * Must call on calculatePrice() to update orders cost.
   */
  public void addMeal() {}

  /**
   * Will add a Single to the corresponding ArrayList
   * Must call on calculatePrice() to update orders cost.
   */
  public void addSingle() {}

  /**
   * Will remove a selected Item from its ArrayList
   * Must call on calculatePrice() to update orders cost.
   */
  public void removeItem() {}

}
