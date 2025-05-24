package org.example.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The class represents a meal with a name, a list of items, and methods to add items,
 *  calculate total price, and retrieve contents.
 */
public class Meal extends Product {
  private Single main;
  private Product side;
  private Product drink;
  private Connection connection;

  /**
   * Constructs a meal with a given name.
   *
   * @param name the name of the meal
   */
  public Meal(String name, Connection conn) {
    setName(name);
    this.connection = conn;
  }

  public void setSide(Product side) {
    this.side = side;
  }

  public Product getSide() {
    return side;
  }

  public void setDrink(Product drink) {
    this.drink = drink;
  }

  public Product getDrink() {
    return drink;
  }

  public Single getMain() {
    return main;
  }
    
  /**
   * The method is responsible for retrieving all meals from a database.
   */
  public List<Meal> getAllMeals(Connection conn) throws SQLException {

    List<Meal> list = new ArrayList<>();

    // TODO: how change meals to match database???
    String sql = "SELECT id, name FROM meals";
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);

    while (rs.next()) {
      Meal meal = new Meal(rs.getString("name"), connection);
      meal.setId(rs.getInt("id"));
      list.add(meal);
    }

    rs.close();
    stmt.close();

    return list;
    
  }

  /**
   * Method to set the main of the Meal.
   *
   * @param conn the connection
   * @throws SQLException if database fails
   */
  public void setMain(Connection conn) throws SQLException {
    String sql = "SELECT product_id FROM meal WHERE meal_id = ?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setInt(1, getId());
    ResultSet rs = ps.executeQuery();
    int productId = 0;
    while (rs.next()) {
      productId = rs.getInt("product_id");
    }
    ps.close();
    rs.close();

    String sql2 = "SELECT name, price, image_url FROM product WHERE product_id = ?";

    PreparedStatement stmt = conn.prepareStatement(sql2);
    stmt.setInt(1, productId);
    ResultSet rs2 = stmt.executeQuery();

    while (rs2.next()) {
      this.main = new Single(
          productId,
          rs2.getString("name"),
          rs2.getFloat("price"),
          Type.valueOf("MEAL"),
          rs2.getString("image_url")
      );
    }
    rs2.close();
    stmt.close();
    main.setIngredients(conn);
    System.out.println(this.main);
  }

  /**
   * Retrieves a list of meals from the database that are
   * under the input price limit.
   *
   * @param conn database connection
   * @param priceLimit maximum price for filtering meals
   * @return list of meals priced under the input limit
   * @throws SQLException if a database access error occurs
   */
  public List<Meal> getMealsUnder(Connection conn, float priceLimit) throws SQLException {
    List<Meal> list = new ArrayList<>();
    String sql = "SELECT id, name, total_price FROM meals WHERE total_price < ?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setFloat(1, priceLimit);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      Meal m = new Meal(rs.getString("name"), connection);
      m.setId(rs.getInt("id"));
      list.add(m);
    }
    ps.close();
    rs.close();
    return list;
  }

  /**
   * Retrieves a list of meals from the database.
   * The meals are filerted by the input meal type.
   *
   * @param conn database connection
   * @param type type of meal that is requested
   * @return list of meals of the given type
   * @throws SQLException if a database access error occurs
   */
  public List<Meal> getMealsbyType(Connection conn, String type) throws SQLException {

    // list to store resulting Meals in
    List<Meal> list = new ArrayList<>();

    // SQL query to select all specified Meals
    String sql = "SELECT id, name, total_price FROM meals WHERE type = ?";

    // Prepare SQL statement with current connection
    PreparedStatement ps = conn.prepareStatement(sql);

    // Bind type to SQL query
    ps.setString(1, type);

    // Execute query to retrieve wanted Meals
    ResultSet rs = ps.executeQuery();

    // Iterate over result set and construct Meal objects from each row
    while (rs.next()) {
      Meal m = new Meal(rs.getString("name"), connection);
      m.setId(rs.getInt("id"));
      list.add(m);
    }

    // Close result set and statement and return build list
    ps.close();
    rs.close();
    return list;
  }

  /**
   * The toString method.
   */
  public String toString() {
    String output = getName() + "    " + getPrice();
    output = output + "\n    " + main.getName();
    output = output + "\n    " + side.getName();
    output = output + "\n    " + drink.getName();
    return output;
  }
}
