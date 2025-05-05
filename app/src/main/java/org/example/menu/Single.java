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
public class Single extends Product {
  public List<Ingredient> ingredients;
  public List<Integer> quantity;
  private boolean modified;

  /**
   * This constructor is used to create instances of the Single class with the specified name,
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

  public void setModefied(boolean modified) {
    this.modified = modified;
  }

  public boolean getModified() {
    return modified;
  }

  /**
   * The method calculates the total cost by adding the base price to the cost of ingredients.
   */
  public double recalc() {
    return getPrice() + ingredients.size() * 0.5f;
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
   * The function  retrieves all singles data from a database table and returns a list of objects.
   */
  public List<Single> getAllSingles(Connection conn) throws SQLException {
    List<Single> list = new ArrayList<>();
    String sql = "SELECT id, name, price, image_url FROM product";

    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);

    while (rs.next()) {
      list.add(new Single(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getFloat("price"),
          // TODO: fix path -> fix query by connecting table category and
          //        retrieving type from there
          Type.valueOf(rs.getString("type")),
          rs.getString("image_url")
      ));
    }
    rs.close();
    stmt.close();
    return list;
  }

  /**
   * The method inserts a single a database table and retrieves the generated key value.
   */
  public void saveToDb(Connection conn) throws SQLException {
    // TODO: Type doesnt exist in products. also, i changed singles to products.
    //       type can be replaced with name of the category table, just like i already
    //       did in some other queries. you just need to add it into that other table
    //       while also adding the other stuff in product table and keeping it linked.
    // TODO: So, this whole method here needs fixing to match the database and needs completion.

    String sql = "INSERT INTO products (name, price, type) VALUES (?, ?, ?)";
    PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    stmt.setString(1, getName());
    stmt.setDouble(2, getPrice());
    stmt.executeUpdate();

    ResultSet rs = stmt.getGeneratedKeys();
    if (rs.next()) {
      setId(rs.getInt(1));
    }

    stmt.close();
    rs.close();

  }

  /**
   * Retrieving all options by type.
   *
   * @param conn database connection
   * @param type type of single food item
   * @return options by type
   * @throws SQLException error with sql
   */
  public List<Single> getOptionsByType(Connection conn, Type type) throws SQLException {
    List<Single> options = new ArrayList<>();
    //String sql = "SELECT id, name, price, type FROM singles WHERE type = ?";
    String sql = "SELECT p.product_id AS id, p.name, p.price, p.image_url, c.name AS type "
        + "FROM product p "
        + "JOIN category c ON p.category_id = c.category_id "
        + "WHERE c.name = ?";

    // Trying with ressources colses statements and sets autmatically
    
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, type.name());

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          options.add(new Single(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getFloat("price"),
              // Just to be sure, use uppercase since enum uses uppercase
              Type.valueOf(rs.getString("type").toUpperCase()),
              rs.getString("image_url")
          ));
        }
      }
    }

    return options;
  }

  /**
   * Retrieves a list of Singles filtered by type (string input).
   * Converts string input to a SingleType enum and hands it down to the overloaded method.
   *
   * @param conn database connection
   * @param type name of the SingleType
   * @return list of Singles matching the input type
   * @throws SQLException if database access error occurs
   * @throws IllegalArgumentException if type string doesn't match any SingleType
   */
  public List<Single> getOptionsByType(Connection conn, String type) throws SQLException {
    // Convert input string to uppercase and map it to the enum and hand it down
    return getOptionsByType(conn, Type.valueOf(type.toUpperCase()));
  }

  /**
   * Retrieves list of Single food items from the database 
   * that are priced under a specific price limit.
   *
   * @param priceLimit  maximum price to filter the food items
   * @param conn database connection to use for the query
   * @return list of Singles that are under the specified price
   * @throws SQLException if database access error occurs
   */
  public List<Single> getSinglesUnder(float priceLimit, Connection conn) throws SQLException {

    // list to store resulting Singles in
    List<Single> list = new ArrayList<>();

    // SQL query to select all specified singles
    String sql = "SELECT p.id, p.name, p.price, p.image_url, c.name AS type"
        + "FROM products"
        + "JOIN category c ON p.category_id = c.category_id"
        + "WHERE price < ?";

    // Prepare SQL statement with current connection
    // Try with this statement ensures the statement is closed automatically
    try (PreparedStatement ps = conn.prepareStatement(sql)) {

      // Bind priceLimit to SQL query
      ps.setFloat(1, priceLimit);

      // Execute query to retrieve wanted singles
      try (ResultSet rs = ps.executeQuery()) {
        // Iterate over result set and construct Single objects from each row
        while (rs.next()) {
          list.add(new Single(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getFloat("price"),
              // Just to be sure, use uppercase since enum uses uppercase
              Type.valueOf(rs.getString("type").toLowerCase()),
              rs.getString("image_url")
              ));

        }
      }
    }

    return list;
  }

  /**
   * Lets you delete a single item from the database
   * by using the single's id.
   *
   * @param conn remote server connection
   * @param id id of the product (single)
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
   * @param conn remote server connection
   * @param id id of the product (single)
   * @param amount new amount of this product
   * @throws SQLException if server issues arise
   */
  public void reduceProductQuantity(Connection conn, int id, int amount) throws SQLException {
    String sql = "UPDATE product SET quantity"
        + "= quantity - ? WHERE product_id = ? AND quantity >= ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, amount);
      stmt.setInt(2, id);
      stmt.setInt(3, amount);
      stmt.executeUpdate();
    }
  }

  /**
   * Retrieves a list of Singles that belong to a specific category ID.
   * If the category name does not map to a valid SingleType enum, defaults to SingleType.EXTRA.
   *
   * @param conn database connection
   * @param categoryId id of the category to filter by
   * @return list of Singles belonging to the filtered category
   * @throws SQLException if database access error occurs
   */
  public List<Single> getOptionsByCategoryId(Connection conn, int categoryId) throws SQLException {
    List<Single> options = new ArrayList<>();

    // SQL query to retrieve items + their category names

    // TODO: fix wrong name "item". i assume you meant product

    String sql = "SELECT i.id, i.name, i.price, i.image_url, c.name AS category_name "
        + "FROM item i "
        + "JOIN category c ON i.category_id = c.category_id "
        + "WHERE i.category_id = ?";

    // Try with this statement ensures the statement is closed automatically
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      // Binding category it to query
      stmt.setInt(1, categoryId);

      ResultSet rs = stmt.executeQuery();

      // Clean and fix category name
      while (rs.next()) {
        String categoryName = rs.getString("category_name").toUpperCase().trim();

        // Quickfix; TODO: deal with later
        @SuppressWarnings("unused")
        Type type;

        // Trying to convert category name to valid enum value
        try {
          type = Type.valueOf(categoryName);

        // going back to to EXTRA if no matches found
        } catch (IllegalArgumentException e) {
          type = Type.EXTRA;
        }

        // Create and add new Single to the list
        options.add(new Single(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getFloat("price"),

            // Just to be sure, use uppercase since enum uses uppercase
            Type.valueOf(rs.getString("type").toLowerCase()),
            rs.getString("image_url")
            // TODO: fix this quickfix
            //type;           
            ));
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
            rs.getInt("ingredient_id"), rs.getString("ingredient_name")));
        quantity.add(rs.getInt("ingredientCount"));
      }
      rs.close();
    }
  }


  /*public List<Single> getOptionsByCategoryName(Connection conn,
                          String categoryName) throws SQLException {
      List<Single> options = new ArrayList<>();
      String sql = "SELECT i.id, i.name, i.price, c.name AS category_name " +
      "FROM item i " +
      "JOIN category c ON i.category_id = c.category_id " +
      "WHERE c.name = ?";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, categoryName);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
          options.add(new Single(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getFloat("price"),
                rs.getString("category_name")
            ));
        }
        rs.close();
      }
      return options;
    }*/
}

  
