package org.example.orders;

// import java.util.ArrayList;
// import org.example.menu.Meal;
// import org.example.menu.Single;

/**
 * Will handle Takeaway orders.
 */
public class Takeaway extends Order {

  public int vat = 12;

  /**
   * Empty constructor, reason -> see Order.java.
   */
  public Takeaway() {}

  /**
   * Overrides Order.java method as to include the vat in the calculation.
   */
  public double calculatePrize(int vat) {

    return 0.00;

  }

}
