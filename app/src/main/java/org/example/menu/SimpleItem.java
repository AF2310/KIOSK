package org.example.menu;

/**
 * This is a simple item class.
 */
public class SimpleItem {
  private String name;
  private String imagePath;
  private double price;

  /**
   * This is the constructor of the simple item class.
   *
   * @param name name of the item
   * @param imagePath the path of the image
   * @param price the price of this item
   */
  public SimpleItem(String name, String imagePath, double price) {
    this.name = name;
    this.imagePath = imagePath;
    this.price = price;
  }

  /**
   * This is the name getter.
   *
   * @return String representation of the name
   */
  public String name() {
    return name;
  }

  /**
   * This is the image path getter.
   *
   * @return String representation of path of the image
   */
  public String imagePath() {
    return imagePath;
  }

  /**
   * This is the price getter.
   *
   * @return double variable of the price
   */
  public double price() { 
    return price;
  }
}

