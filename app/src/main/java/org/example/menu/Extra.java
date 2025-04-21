package org.example.menu;


/**
 * The class Extra extends the class Single and
 *  has a constructor that takes a name and price as parameters.
 */
public class Extra extends Single {
  // id added to avoid error
  public Extra(int id, String name, float price,SingleType type) {
    super(id, name, price,type);
  }
}