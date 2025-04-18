package org.example.menu;

import java.util.List;


/**
 * The interface declares several methods that define the behavior of a menu system.
 *
 */

public interface Imenu {
  List<MainDish> getMainDishes();

  List<Side> getSides();

  List<Drink> getDrinks();

  List<Dessert> getDesserts();
  
  List<Object> searchProducts(String name);
}