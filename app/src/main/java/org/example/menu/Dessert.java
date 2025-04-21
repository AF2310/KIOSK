package org.example.menu;

/**
 * The Dessert class extends the Single class and represents a dessert item with a name and price.
 */
public class Dessert extends Single {
  // id added to avoid error

  /**
   * Dessert constructor.
   *
   * @param id id int dessert
   * @param name name string dessert
   * @param price price dessert
   * @param type type of single
   */
  public Dessert(int id, String name, float price, SingleType type) {
    super(id, name, price, type);
  }
}
