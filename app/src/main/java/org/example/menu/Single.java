package org.example.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.example.sql.SqlQueries;


/**
 * Abstract base class for all single items on the menu.
 */
public class Single extends Product {
  public List<Ingredient> ingredients;
  public List<Integer> quantity;
  private boolean modified;
  private boolean inMeal;

  /**
   * This constructor is used to create instances of the Single class with the
   * specified name,
   * price, and an empty list of ingredients.
   */
  public Single(int id, String name, double price, Type type, String imgPath) {
    setId(id);
    setName(name);
    setPrice(price);
    this.ingredients = new ArrayList<>();
    this.quantity = new ArrayList<>();
    setType(type);
    setImagePath(imgPath);
  }

  /**
   * This constructor initializes a Single object with the specified parameters.
   */
  public Single(
      int id,
      String name,
      double price,
      Type type,
      String imgPath,
      List<Ingredient> ingredients) {
    setId(id);
    setName(name);
    setPrice(price);
    this.ingredients = new ArrayList<>();
    this.quantity = new ArrayList<>();
    setType(type);
    setImagePath(imgPath);
    this.ingredients = ingredients;
  }

  public void setModefied(boolean modified) {
    this.modified = modified;
  }

  public boolean getModified() {
    return modified;
  }

  /**
   * The method calculates the total cost by adding the base price to the cost of
   * ingredients.
   */
  public double recalc() {
    return getPrice() + ingredients.size() * 0.5f;
  }

  /**
   * adds an Ingredient to a list of ingredients.
   */
  public void addIngredient(Ingredient ingredient) {
    ingredients.add(ingredient);
  }

  /**
   * removes an ingredient from a list based on its ID.
   */
  public void removeIngredient(Ingredient ingredient) {
    ingredients.removeIf(i -> i.getId() == ingredient.getId());
  }

  /**
   * The function retrieves all singles data from a database table and returns a
   * list of objects.
   *
   * @throws SQLException if database error occurs
   */
  public List<Single> getAllSingles() throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.getAllSingles();

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * Inserts this product into the database and sets the generated product ID.
   * Assumes category name maps to valid category in DB.
   */
  public void saveToDb(Connection conn) throws SQLException {
    /* // Get the category_id from the category name (enum)
    String categoryQuery = "SELECT category_id FROM category WHERE name = ?";
    int categoryId;
    try (PreparedStatement categoryStmt = conn.prepareStatement(categoryQuery)) {
      // assumes enum name matches DB name
      categoryStmt.setString(1, getType().name());

      try (ResultSet rs = categoryStmt.executeQuery()) {
        if (rs.next()) {
          categoryId = rs.getInt("category_id");

          // For debugging
        } else {
          throw new SQLException("ERROR no type in DB found named: " + getType().name());
        }
      }
    }
    // Insert product in database with query and needed data
    String insertSql = "INSERT INTO product "
        + "(name, description, price, category_id, is_active) "
        + "VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement stmt = conn.prepareStatement(
        insertSql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, getName());
      stmt.setString(2, getDescription());
      stmt.setDouble(3, getPrice());
      stmt.setInt(4, categoryId);
      stmt.setInt(5, getActivity()); // stored as Tinyint (1 or 0)
      stmt.executeUpdate();

      try (ResultSet rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          setId(rs.getInt(1));
        }
      }
    } */
    try {
      SqlQueries pool = new SqlQueries();
      pool.saveSingleToDb(this);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieving all options by type.
   *
   * @param type type of single food item
   * @return options by type
   * @throws SQLException error with sql
   */
  public List<Single> getOptionsByType(Type type) throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.getOptionsByType(type);

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * Retrieves a list of Singles filtered by type (string input).
   * Converts string input to a SingleType enum and hands it down to the
   * overloaded method.
   *
   * @param type name of the SingleType
   * @return list of Singles matching the input type
   * @throws SQLException             if database access error occurs
   * @throws IllegalArgumentException if type string doesn't match any SingleType
   */
  public List<Single> getOptionsByType(String type) throws SQLException {
    // Convert input string to uppercase and map it to the enum and hand it down
    return getOptionsByType(Type.valueOf(type.toUpperCase()));
  }

  /**
   * Retrieves list of Single food items from the database
   * that are priced under a specific price limit.
   *
   * @param priceLimit maximum price to filter the food items
   * @param conn       database connection to use for the query
   * @return list of Singles that are under the specified price
   * @throws SQLException if database access error occurs
   */
  public List<Single> getSinglesUnder(double priceLimit, Connection conn) throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.getSinglesUnder(priceLimit);

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * Lets you delete a single item from the database
   * by using the single's id.
   *
   * @param conn remote server connection
   * @param id   id of the product (single)
   * @throws SQLException if server issues arise
   */
  public void deleteSingleById(Connection conn, int id) throws SQLException {
    String sql = "DELETE FROM product WHERE product_id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }

  /**
   * Lets you reduce the product quantity inside
   * the database.
   *
   * @param conn   remote server connection
   * @param id     id of the product (single)
   * @param amount new amount of this product
   * @throws SQLException if server issues arise
   */
  public void reduceProductQuantity(Connection conn, int id, int amount) throws SQLException {

    String sql = "UPDATE product SET quantity = quantity - ? "
        + "WHERE product_id = ? AND quantity >= ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, amount);
      stmt.setInt(2, id);
      stmt.setInt(3, amount);
      stmt.executeUpdate();
    }
  }

  /**
   * Retrieves a list of Singles that belong to a specific category ID.
   * If the category name does not map to a valid SingleType enum, defaults to
   * SingleType.EXTRA.
   *
   * @param conn       database connection
   * @param categoryId id of the category to filter by
   * @return list of Singles belonging to the filtered category
   * @throws SQLException if database access error occurs
   */
  public List<Single> getOptionsByCategoryId(Connection conn, int categoryId) throws SQLException {
    List<Single> options = new ArrayList<>();

    // SQL query to retrieve items + their category names
    String sql = "SELECT p.product_id, p.name, p.price, p.image_url, c.name AS type "
        + "FROM product p "
        + "JOIN category c ON p.category_id = c.category_id "
        + "WHERE p.category_id = ?";

    try (
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();) {

      while (rs.next()) {

        // Create and add new Single to the list
        options.add(new Single(
            rs.getInt("product_id"),
            rs.getString("name"),
            rs.getDouble("price"),
            Type.valueOf(rs.getString("type").toUpperCase()),
            rs.getString("image_url")));
      }
      // Close result set
      rs.close();
    }
    return options;
  }

  /**
   * Method to set the ingredients to them in the database.
   *
   * @param conn database connection
   */
  public void setIngredients(Connection conn) throws SQLException {

    String sql = "SELECT pi.ingredient_id, pi.ingredientCount, i.ingredient_name "
        + "FROM productingredients pi "
        + "JOIN ingredient i ON pi.ingredient_id = i.ingredient_id "
        + "WHERE product_id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, getId());

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        ingredients.add(new Ingredient(
            rs.getInt("ingredient_id"),
            rs.getString("ingredient_name")));
        quantity.add(rs.getInt("ingredientCount"));
      }
      rs.close();
    }
  }

  /**
   * Overloading the equality operator.
   *
   * @param obj the other single
   * @return the equality value
   */
  @Override
  public boolean equals(Object obj) {
    //Single other = (Single) obj;
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Single other = (Single) obj;
    
    if (this.getId() != other.getId()) {
      return false;
    }
    if (this.quantity.size() != other.quantity.size()) {
      return false;
    }

    for (int i = 0; i < other.quantity.size(); i++) {
      if (!Objects.equals(this.quantity.get(i), other.quantity.get(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Method that checks if the product is in a meal.
   *
   * @param conn the connection to the databse
   * @return if the product is in the meal
   * @throws SQLException if databse error
   */
  public boolean isInMeal(Connection conn) throws SQLException {
    String sql = "SELECT meal_id FROM meal WHERE product_id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, getId());

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          this.inMeal = true;
          return this.inMeal;
        } else {
          this.inMeal = false;
          return this.inMeal;
        }
      }
    }
  }

  /*
   * public List<Single> getOptionsByCategoryName(Connection conn,
   * String categoryName) throws SQLException {
   * List<Single> options = new ArrayList<>();
   * String sql = "SELECT i.id, i.name, i.price, c.name AS category_name " +
   * "FROM item i " +
   * "JOIN category c ON i.category_id = c.category_id " +
   * "WHERE c.name = ?";
   * try (PreparedStatement stmt = conn.prepareStatement(sql)) {
   * stmt.setString(1, categoryName);
   * ResultSet rs = stmt.executeQuery();
   * while (rs.next()) {
   * options.add(new Single(
   * rs.getInt("id"),
   * rs.getString("name"),
   * rs.getFloat("price"),
   * rs.getString("category_name")
   * ));
   * }
   * rs.close();
   * }
   * return options;
   * }
   */

}
