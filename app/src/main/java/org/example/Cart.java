package org.example;

import java.util.ArrayList;
import org.example.menu.*;

/**
 * The cart class.
 */
public class Cart {
  ArrayList<SimpleItem> items;
  ArrayList<Integer> quantity;

  public Cart() {
    items = new ArrayList<>();
    quantity = new ArrayList<>();
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
}
