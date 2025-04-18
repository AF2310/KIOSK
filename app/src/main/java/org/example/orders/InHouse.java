package org.example.orders;

// import java.util.ArrayList;
// import org.example.menu.Meal;
// import org.example.menu.Single;

/**
 * Class for inhouse orders.
 */
public class InHouse extends Order {

  public int vat = 25;

  /**
   * Empty constructor, reason -> see Order.java.
   */
  public InHouse() {}
  
  /**
   * Overrides Order.java method as to include the vat in the calculation.
   */
  public double calculatePrize(int vat) {
  
    return 0.00;
  
  }

}
