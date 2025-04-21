package org.example.menu;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
//import org.example.menu.Single;
//import org.example.menu.Meal;
public class Menu implements Imenu {
  private final List<Single> mains;
  private final List<Single> sides;
  private final List<Single> drinks;
  private final List<Single> extras;
  private final List<Single> Desserts;
  Single singleHelper = new Single(0, "", 0.0f, SingleType.EXTRA);
  

  public Menu(Connection conn) throws SQLException {
    
    this.mains = singleHelper.getOptionsByType(conn, SingleType.MAIN);
    this.sides = singleHelper.getOptionsByType(conn, SingleType.SIDE);
    this.drinks = singleHelper.getOptionsByType(conn, SingleType.DRINK);
    this.extras = singleHelper.getOptionsByType(conn, SingleType.EXTRA);
    this.Desserts = singleHelper.getOptionsByType(conn, SingleType.DESSERT);
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
      return Desserts;
  }

  @Override
  public List<Object> searchProducts(String name) {
      throw new UnsupportedOperationException("Not supported yet.");
  }

}