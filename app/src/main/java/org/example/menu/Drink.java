package org.example.menu;

/**
 * The Drink class extends the Single class and represents a drink with a name and price.
 */
public class Drink extends Single {
  // id added to avoid error
  public Drink(int id, String name, float price,SingleType type) {
    super(id, name, price,type);
  }
}