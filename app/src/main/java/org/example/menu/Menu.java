package org.example.menu;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.example.sql.SqlQueries;

/**
 * Menu class that contains/returns all Singles when requested.
 */
public class Menu implements Imenu {
  SqlQueries queries = new SqlQueries();
  private final List<Single> mains;
  private final List<Single> sides;
  private final List<Single> drinks;
  private final List<Single> extras;
  private final List<Single> desserts;

  // TODO: needs fixing do add actual image path
  Single singleHelper = new Single(0, "", 0.0f, Type.EXTRA, "dummy_path");

  /**
   * Constructor of Meal class.
   *
   * @param conn server connection
   * @throws SQLException sql connection issues
   */
  public Menu(Connection conn) throws SQLException {

    this.mains = queries.getOptionsByType(conn, Type.BURGERS);
    this.sides = queries.getOptionsByType(conn, Type.SIDES);
    this.drinks = queries.getOptionsByType(conn, Type.DRINKS);
    this.extras = queries.getOptionsByType(conn, Type.EXTRA);
    this.desserts = queries.getOptionsByType(conn, Type.DESSERTS);
  }

  @Override
  public List<Single> getMains() {
    return mains;
  }

  @Override
  public List<Single> getSides() {
    return sides;
  }

  @Override
  public List<Single> getDrinks() {
    return drinks;
  }

  @Override
  public List<Single> getExtras() {
    return extras;
  }

  @Override
  public List<Single> getDesserts() {
    return desserts;
  }

  @Override
  public List<Object> searchProducts(String name) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}