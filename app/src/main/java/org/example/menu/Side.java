package org.example.menu;

/**
 * The Side class extends the Single class and represents a side item with a name and price.
 */
public class Side extends Single {
  // added id to avoid error
  public Side(int id, String name, float price,SingleType type) {
    super(id, name, price,type);
  }
}

