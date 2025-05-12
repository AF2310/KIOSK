package org.example.orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.menu.Ingredient;
import org.example.menu.Meal;
import org.example.menu.Product;
import org.example.menu.Single;

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
   * to save quantity to database.
   *
   * @param conn database connection
   * @param orderId order id from database
   * @throws SQLException database error
   */
  public void saveQuantityToDb(Connection conn, int orderId) throws SQLException {
    for (int i = 0; i < items.size(); i++) {

      String s = "INSERT INTO order_item "
          + "(order_id, product_id, quantity)"
          + "VALUES (?, ?, ?)";

      // Prepare statement to be actual query
      PreparedStatement ps = conn.prepareStatement(s);

      // Get product ID from the item
      int productId = items.get(i).getId();

      // Insert values into prepared statement
      ps.setInt(1, orderId); 
      ps.setInt(2, productId);
      ps.setInt(3, quantity.get(i));
      
      // Execute query
      ps.executeUpdate();
      int orderItemid = receiveOrderId(conn);

      if (items.get(i) instanceof Single) {
        System.out.println("Hallo");
        List<Ingredient> ingrediets = ((Single) items.get(i)).ingredients;
        List<Integer> quantitys = ((Single) items.get(i)).quantity;
        for (int j = 0; j < ingrediets.size(); j++) {
          String query = "INSERT INTO orderitemingredients "
                + "(order_item_id, ingredient_id, ingredientCount)"
                + "VALUES (?, ?, ?)";
            
          PreparedStatement ps2 = conn.prepareStatement(query);
          ps2.setInt(1, orderItemid);
          ps2.setInt(2, ingrediets.get(j).getId());
          ps2.setInt(3, quantitys.get(j));
            
          ps2.executeUpdate();
        }
      } else {
        System.out.println("Item is not an instance of Single: " + items.get(i));
      }
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
    notifyAllListeners();
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

  private int receiveOrderId(Connection conn) throws SQLException {
    // Set default order id
    int id = -1;

    // SQL Query as string statement
    String s = "SELECT LAST_INSERT_ID()";

    // Prepare statement to be actual query
    // Using try to save ressources and close process automatically
    try (PreparedStatement ps = conn.prepareStatement(s)) {
      // Open result set to fetch order id (execute query)
      ResultSet rs = ps.executeQuery();

      // if there is a result
      if (rs.next()) {
        // store result as order id integer
        id = rs.getInt(1);
      }
    }

    return id;
  }

  /**
   * Converting meals into singles for storing them in the database.
   */
  public void convertMealsIntoSingles() {
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i) instanceof Meal) {
        Meal item = (Meal) items.get(i);
        int quant = quantity.get(i);
        items.remove(i);
        quantity.remove(i);
        items.add(item.getSide());
        quantity.add(quant);
      }
    }
  }
}
