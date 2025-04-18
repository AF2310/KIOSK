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
public class Meal {
  private int id;
  private String name;
  private List<Single> contents;

  /**
   * Constructs a meal with a given name.
   *
   * @param name the name of the meal
   */
  public Meal(String name) {
    this.name = name;
    this.contents = new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  /**
   * Adds an item to the meal.
   *
   * @param item the item to add
   */
  public void addItem(Single item) {
    contents.add(item);
  }

  /**
   * Returns the list of items in the meal.
   *
   * @return list of items
   */
  public List<Single> getContents() {
    return contents;
  }

  public String getName() {
    return name;
  }

  /**
   * Calculates total price of all items in the meal.
   *
   * @return total price
   */
  public float getTotalPrice() {
    float total = 0;
    for (Single item : contents) {
      total += item.recalc();
    }
    return total;
  }


  public void chooseMain(Single main) {
    contents.add(main);
  }

  public void chooseSide(Single side) {
    contents.add(side);
}

  public void chooseDrink(Single drink) {
    contents.add(drink);
  }

  public void addExtra(Single extra) {
    contents.add(extra);
  }


  
  /**
   *.
   *.
   * The  method  is responsible for saving the meal data to a database.
   *
   */
  public void saveToDb(Connection conn) throws SQLException {
    String sql = "INSERT INTO meals (name, total_price) VALUES (?, ?)";

    PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    pstmt.setString(1, getName());
    pstmt.setFloat(2, getTotalPrice());
    pstmt.executeUpdate();

    try (ResultSet rs = pstmt.getGeneratedKeys()) {
      if (rs.next()) {
        this.id = rs.getInt(1);
      }
    }
  }
    
  /**
   * The method is responsible for retrieving all meals from a database.
   *
   *
   */
  public List<Meal> getAllMeals(Connection conn) throws SQLException{
    List<Meal> list = new ArrayList<>();
    String sql = "SELECT id, name FROM meals";
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);

    while (rs.next()) {
      Meal meal = new Meal(rs.getString("name"));
      meal.id = rs.getInt("id");
      list.add(meal);
    }

    rs.close();
    stmt.close();

    return list;
    
  }


}
