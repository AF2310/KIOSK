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
  private ArrayList<Runnable> allListeners;

  // Private constructor to prevent instantiation from outside
  private Cart() {
    items = new ArrayList<>();
    quantity = new ArrayList<>();
    allListeners = new ArrayList<>();
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
    notifyAllListeners();
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
      notifyAllListeners();
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
    //TODO: uncomment one by one to test functionality
    // notifyAllListeners();
  }

  /**
   * Add a listener for later notifications when the
   * cart updates.
   * Since cart is a singleton and other classes aren't or 
   * things like scene components, that cannot be made into
   * a singleton at all, it is simpler to notify all other
   * listeners by adding them here.
   * This way, you can use the cart immediately to notify
   * other liseners, instead of using/making extra instances
   * to notify other liseners separately, and prevent
   * possible complications with those instances.
   *
   * @param listener operation from another class that should
   *                 listen to changes in cart
   */
  public void addListener(Runnable listener) {
    allListeners.add(listener);
  }

  /**
   * Notifying all listeners in the private array when
   * a change in the cart occurs.
   * For now, only used inside the class but left public
   * for possible later improvements.
   */
  public void notifyAllListeners() {
    for (Runnable eachListener : allListeners) {
      eachListener.run();
    }
  }
}
