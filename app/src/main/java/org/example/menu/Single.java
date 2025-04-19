

package org.example.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * Abstract base class for all single items on the menu.
 */
public class Single {
  protected String name;
  protected float price;
  protected List<Ingredient> ingredients;
  public int id;
  private SingleType type;

  /**
   *.
   * This constructor is used to create instances of the Single class with the specified name,
   * price, and an empty list of ingredients.
   *
   *
   */
  public Single(int id, String name, float price ,SingleType type) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.ingredients = new ArrayList<>();
    this.type = type;
  }

  /**
   * The `getName` function in Java returns the value of the `name` variable.
   *.
   *.
   */
  public String getName() {
    return name;
  }

  /**
   * The method the price as a float.
   *.
   *.
   */
  public float getPrice() {
    return price;
  }

  /**
   * The method returns a list of ingredient objects.
   *.
   *
   */
  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  /**
   * The method calculates the total cost by adding the base price to the cost of ingredients.
   *.
   *
   */
  public float recalc() {
    
    return price + ingredients.size() * 0.5f;
  }

  /**
   * The function  returns the id value.
   * .
   */
  public int getId() {
    return id;
  }

  public SingleType getType() {
    return type;
  }

  /**
   * The method adds the current item to a specified object.
   *
   * @param meal Meal object that the current item will be add
   */
  public void convertToMeal(Meal meal) {
    meal.addItem(this);
  }

  /**
   * adds an Ingredient to a list of ingredients.
   *.
   *
   */
  public void addIngredient(Ingredient ingredient) {
    ingredients.add(ingredient);
  }

  /**
   * removes an ingredient from a list based on its ID.
   *.
   *
   */
  public void removeIngredient(Ingredient ingredient) {
    ingredients.removeIf(i -> i.getId() == ingredient.getId());
  }

  /**
   * The function  retrieves all singles data from a database table and returns a list of objects.
   *
   *
   *
   */
  public List<Single> getAllSingles(Connection conn) throws SQLException {
    List<Single> list = new ArrayList<>();
    String sql = "SELECT id, name, price FROM singles";

    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);

    while (rs.next()) {
      list.add(new Single(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getFloat("price"),
          SingleType.valueOf(rs.getString("type"))
      ));
    }
    rs.close();
    stmt.close();
    return list;
  }

  /**
   * The method inserts a single a database table and retrieves the generated key value.
   *.
   *.
   */
  public void saveToDb(Connection conn) throws SQLException {
    String sql = "INSERT INTO singles (name, price, type) VALUES (?, ?, ?)";
    PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    stmt.setString(1, name);
    stmt.setFloat(2, price);
    stmt.executeUpdate();

    ResultSet rs = stmt.getGeneratedKeys();
    if (rs.next()) {
      id = rs.getInt(1);
    }

    stmt.close();
    rs.close();

  }

  public List<Single> getOptionsByType(Connection conn, SingleType type) throws SQLException {
    List<Single> options = new ArrayList<>();
    String sql = "SELECT id, name, price, type FROM singles WHERE type = ?";

    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setString(1, type.name());
    ResultSet rs = stmt.executeQuery();

    while (rs.next()) {
        options.add(new Single(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getFloat("price"),
            SingleType.valueOf(rs.getString("type"))
        ));
    }

    rs.close();
    stmt.close();
    return options;
  }

  public List<Single> getOptionsByType(Connection conn, String type) throws SQLException {
    return getOptionsByType(conn, SingleType.valueOf(type.toUpperCase()));
  }

  
}