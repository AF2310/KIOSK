package org.example.orders;

import java.util.ArrayList;
import org.example.menu.Product;

/**
 * The cart class implemented as a singleton.
 */
public class Cart {
  // Changed the cart into a singleton so
  // that it can be accessed anywhere easier
  private static Cart instance;
  private ArrayList<Product> items;
  private ArrayList<Integer> quantity;

  // Private constructor to prevent instantiation from outside
  private Cart() {
    items = new ArrayList<>();
    quantity = new ArrayList<>();
  }

  /**
   * Method to get the single instance of the Cart.
   *
   * @return the Cart instance
   */
  public static Cart getInstance() {
    if (instance == null) {
      instance = new Cart();
    }
    return instance;
  }

  /**
   * Method to get an array of the items.
   *
   * @return the array of the items
   */
  public Product[] getItems() {
    Product[] newItems = new Product[items.size()];
    for (int i = 0; i < items.size(); i++) {
      newItems[i] = items.get(i);
    }
    return newItems;
  }

  /**
   * Method to get an array of the quantitys.
   *
   * @return array of quantitys
   */
  public int[] getQuantity() {
    int[] newQuantity = new int[quantity.size()];
    for (int i = 0; i < quantity.size(); i++) {
      newQuantity[i] = quantity.get(i);
    }
    return newQuantity;
    }

  /**
   * Method to add a product to the cart.
   *
   * @param product the product to add
   */
  public void addProduct(Product product) {
    if (items.contains(product)) {
      int index = items.indexOf(product);
      quantity.set(index, (quantity.get(index) + 1));
    } else {
      items.add(product);
      quantity.add(1);
    }
  }

  /**
   * Method to remove a product from the cart.
   *
   * @param product the product to remove
   */
  public void removeProduct(Product product) {
    if (items.contains(product)) {
      int index = items.indexOf(product);
      int currentQuantity = quantity.get(index);

      if (currentQuantity > 1) {

        quantity.set(index, currentQuantity - 1);
      } else {
        items.remove(index);
        quantity.remove(index);
      }
    }
  }

  /**
   * Turning cart items into string.
   */
  public String toString() {
    return items.toString() + quantity.toString();
  }

  /**
   * Empties all items and quantities from the cart.
   */
  public void clearCart() {
    items.clear();
    quantity.clear();
  }
}
