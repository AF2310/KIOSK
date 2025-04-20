package org.example.menu;

import java.util.List;


/**
 * The interface declares several methods that define the behavior of a menu system.
 *
 */

public interface Imenu {
  List<Single> getMains();

  List<Single> getSides();

  List<Single> getDrinks();

  List<Single> getDesserts();

  List<Single> getExtras();
  
  List<Object> searchProducts(String name);
}