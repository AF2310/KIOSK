package org.example.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.example.sql.SqlQueries;

/**
 * The class represents an ingredient with properties like id and name,
 * and provides methods to save ingredients to a database and
 * retrieve all ingredients from the database.
 */
public class Ingredient {
  private int id;
  private String name;

  /**
   * constructor class is initializing an ingredient
   * object with the provided id and name values.
   *
   * @param id   ingredient id
   * @param name ingredient name
   */
  public Ingredient(int id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * returns the value of the id.
   */
  public int getId() {
    return id;
  }

  /**
   * the value of the name.
   */
  public String getName() {
    return name;
  }

  /**
   * To string method for the name of the ingredient.
   */
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Ingredient ingredient = (Ingredient) obj;
    return getName() != null
        ? getName().equals(ingredient.getName())
        : ingredient.getName() == null;
  }

  @Override
  public int hashCode() {
    return getName() != null ? getName().hashCode() : 0;
  }

  /**
   * The function inserts an ingredient name
   * into a database table and retrieves the generated ID from the database.
   */
  public void saveToDb(Connection conn) throws SQLException {
    String sql = "INSERT INTO ingredients (name) VALUES (?)";
    PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    pstmt.setString(1, getName());
    pstmt.executeUpdate();

    try (ResultSet rs = pstmt.getGeneratedKeys()) {
      if (rs.next()) {
        this.id = rs.getInt(1); // setting ID from DB
      }
    }
  }

  /**
   * The method is responsible for retrieving all ingredients from the database
   * table in a list.
   */
  public List<Ingredient> getAllIngredients() throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.getAllIngredients();

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * Searching ingredients by name.
   *
   * @param name name of the desired ingredient
   * @param conn database connection
   * @return List containing all ingredients that match the input string name
   * @throws SQLException SQL errors
   */
  public List<Ingredient> searchIngredientsByName(
      String name, Connection conn) throws SQLException {

    List<Ingredient> list = new ArrayList<>();
    String sql = "SELECT ingredient_id AS id, ingredient_name AS name "
        + "FROM ingredient "
        + "WHERE LOWER(ingredient_name) LIKE ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, "%" + name.toLowerCase() + "%");
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        list.add(new Ingredient(rs.getInt("id"), rs.getString("name")));
      }
    }
    return list;

  }

  /**
   * Serching ingredients by price.
   *
   * @param maxPrice total maximum price that is searched for
   * @param conn     database connection
   * @return List containing all ingredients that are below input price
   * @throws SQLException SQL error
   */
  public List<Ingredient> searchIngredientsByPrice(
      float maxPrice, Connection conn) throws SQLException {

    List<Ingredient> list = new ArrayList<>();
    String sql = "SELECT DISTINCT i.ingredient_id AS id, i.ingredient_name AS name "
        + "FROM ingredient i "
        + "JOIN productingredients pi ON i.ingredient_id = pi.ingredient_id "
        + "JOIN product p ON pi.product_id = p.product_id "
        + "WHERE p.price < ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setFloat(1, maxPrice);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        list.add(new Ingredient(rs.getInt("id"), rs.getString("name")));
      }
    }
    return list;
  }

  /**
   * Searching ingredients by name and price.
   *
   * @param conn     database connection
   * @param name     string name of the desired ingredient
   * @param maxPrice maximum price value that is searched for
   * @return List containing all ingredients that fall below input max price and
   *         match input name
   * @throws SQLException SQL error
   */
  public List<Ingredient> searchIngredientByNameAndPrice(
      Connection conn, String name, float maxPrice)

      throws SQLException {
    List<Ingredient> list = new ArrayList<>();
    String sql = "SELECT DISTINCT i.ingredient_id AS id, i.ingredient_name AS name "
        + "FROM ingredient i "
        + "JOIN productingredients pi ON i.ingredient_id = pi.ingredient_id "
        + "JOIN product p ON pi.product_id = p.product_id "
        + "WHERE LOWER(i.ingredient_name) LIKE ? AND p.price <= ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, "%" + name.toLowerCase() + "%");
      stmt.setFloat(2, maxPrice);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        list.add(new Ingredient(rs.getInt("id"), rs.getString("name")));
      }
    }
    return list;
  }
}