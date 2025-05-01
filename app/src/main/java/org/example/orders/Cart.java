package org.example.orders;

import java.util.ArrayList;
import org.example.menu.SimpleItem;


/**
 * The cart class.
 */
public class Cart {
  private ArrayList<SimpleItem> items;
  private ArrayList<Integer> quantity;

  /**
   * Cart constructor.
   */
  public Cart() {
    items = new ArrayList<>();
    quantity = new ArrayList<>();
  }

  /**
   * Method to get an array of the items.
   *
   * @return the array of the items
   */
  public SimpleItem[] getItems() {
    SimpleItem[] newItems =  new SimpleItem[items.size()];
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
    for (int i = 0; i < items.size(); i++) {
      newQuantity[i] = quantity.get(i);
    }

    return newQuantity;
  }

  /**
   * Method to add a product to the cart.
   *
   * @param product the product to add
   */
  public void addProduct(SimpleItem product) {
    if (items.contains(product)) {
      int index = items.indexOf(product);
      quantity.set(index, (quantity.get(index) + 1));
    } else {
      items.add(product);
      quantity.add(1);
    }
  }

  /**
   * Turning cart items into string.
   */
  public String toString() {
    return items.toString() + quantity.toString();
  }
}
