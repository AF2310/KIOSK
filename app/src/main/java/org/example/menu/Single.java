package org.example.menu;

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
  
  public Single(int id, String name, double price, Type type,
       String imgPath, List<Ingredient> ingredients) {
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
}