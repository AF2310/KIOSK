package org.example.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The class represents an ingredient with properties like id and name,
 *  and provides methods to save ingredients to a database and
 * retrieve all ingredients from the database.
 */
public class Ingredient {
  private int id;
  private String name;

    

  // constructor  class is initializing an ingredient object with the provided id and name values.
  public Ingredient(int id, String name) {
    this.id = id;
    this.name = name;
  }

    
  /**
   *  returns the value of the id.
   *
   *
   */
  public int getId() {
    return id;
  }

  /**
   *  the value of the name.
   *
   *
   */
  public String getName() {
    return name;
  }

    

  /**
   * The  function inserts an ingredient name
   * into a database table and retrieves the generated ID from the database.
   *
   *
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
   * The  method is responsible for retrieving all ingredients from the database table in a list.
   *
   *
   */
  public List<Ingredient> getAllIngredients(Connection conn) throws SQLException {
    List<Ingredient> list = new ArrayList<>();
    String sql = "SELECT id, name FROM ingredients";

    try (Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        list.add(new Ingredient(rs.getInt("id"), rs.getString("name")));
      }
    }

    return list;
  }
}