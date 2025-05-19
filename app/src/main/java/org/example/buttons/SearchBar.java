package org.example.buttons;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.example.menu.Ingredient;
import org.example.menu.Menu;
import org.example.menu.Product;
import org.example.menu.Single;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The SearchBar class provides a user interface component for searching items
 * by name, price, ingredients, and category. It displays the search results
 * in a list view and interacts with a database connection to fetch data.
 */
public class SearchBar extends VBox {
  private final TextField nameField;
  private final TextField priceField;
  private final CheckBox ingredientCheckbox;
  private final Button searchButton;
  private final ListView<String> resultList;
  private final ComboBox<String> categoryCombo;
  private java.util.function.Consumer<Product> resultSelectHandler;
  private java.util.function.Consumer<List<Product>> resultsListHandler;
  private List<Single> filteredSingles = new ArrayList<>();

  /**
   * Constructs a SearchBar instance with the specified database connection.
   *
   * @param conn the database connection used for fetching data
   */
  public SearchBar(Connection conn) {

    this.setPadding(new Insets(10));
    this.setSpacing(10);

    nameField = new TextField();
    nameField.setPromptText("Search by name...");

    String textFieldStyle = ("-fx-text-fill: #333333;"
        + "-fx-prompt-text-fill: #666666;"
        + "-fx-background-color: #f0f0f0;"
        + "-fx-border-color: #999999;"
        + "-fx-border-width: 1px;"
        + "-fx-border-radius: 4px;"
        + "-fx-padding: 6px 8px;"
        + "-fx-font-size: 14px;"
        + "-fx-pref-width: 180px;"
        + "-fx-pref-height: 32px;");

    priceField = new TextField();
    priceField.setPromptText("Search by max price...");
    nameField.setStyle(textFieldStyle);
    priceField.setStyle(textFieldStyle);

    // Set field sizes
    nameField.setPrefWidth(200);
    priceField.setPrefWidth(150);

    ingredientCheckbox = new CheckBox("Search Ingredients");

    categoryCombo = new ComboBox<>();
    categoryCombo.getItems().addAll("-- Any --", "MAIN", "DRINK", "DESSERT", "EXTRA");
    categoryCombo.setPromptText("Search by category...");
    categoryCombo.getSelectionModel().selectFirst();

    searchButton = new Button("Search");
    searchButton.setStyle(
        "-fx-background-color: #4285f4;"
        + "-fx-text-fill: white;"
        + "-fx-font-weight: bold;"
        + "-fx-padding: 4px 12px;"
        + "-fx-background-radius: 4px;"
    );

    resultList = new ListView<>();
    resultList.setPrefHeight(100);
    resultList.setStyle(
        "-fx-font-size: 12px;"
        + "-fx-padding: 2px;"
        + "-fx-cell-size: 25px;"
    );

    HBox inputFields = new HBox(
        10, nameField, priceField, ingredientCheckbox, categoryCombo, searchButton);
    this.getChildren().addAll(inputFields, new Label("Search Results:"), resultList);
    searchButton.setOnAction(e -> {
      resultList.getItems().clear();
      String name = nameField.getText().trim();
      String priceText = priceField.getText().trim();
      boolean isIngredientSearch = ingredientCheckbox.isSelected();
      String selectedCategory = categoryCombo.getValue();

      try {
        if (isIngredientSearch) {
          Ingredient ingredientDummy = new Ingredient(0, "");
          boolean hasName = !name.isEmpty();
          boolean hasPrice = !priceText.isEmpty();

          List<Ingredient> ingredients;
          if (!hasName && !hasPrice) {
            // resultList.getItems().add("Please enter name or price.");
            ingredients = ingredientDummy.getAllIngredients(conn);
          } else if (hasName && hasPrice) {
            float price = Float.parseFloat(priceText);
            ingredients = ingredientDummy.searchIngredientByNameAndPrice(conn, name, price);
          } else if (!name.isEmpty()) {
            ingredients = ingredientDummy.searchIngredientsByName(name, conn);
            /*
             * for (Ingredient ing : ingredients) {
             * resultList.getItems().add("Ingredient: " + ing.getName());
             * }
             */

          } else {
            float price = Float.parseFloat(priceText);
            ingredients = ingredientDummy.searchIngredientsByPrice(price, conn);
          }
          if (ingredients.isEmpty()) {
            resultList.getItems().add("No ingredients found.");
          } else {
            for (Ingredient ing : ingredients) {
              resultList.getItems().add("Ingredient: " + ing.getName());
            }
          }
          return;
        }
        /*
         * boolean hasName = !name.isEmpty();
         * boolean hasPrice = !priceText.isEmpty();
         * boolean hasCategory = selectedCategory != null &&
         * !selectedCategory.equals("-- Any --");
         * 
         * Float priceLimit = null;
         * if (hasPrice) {
         * try {
         * priceLimit = Float.parseFloat(priceText);
         * } catch (NumberFormatException ex) {
         * resultList.getItems().add("Invalid price input.");
         * return;
         * }
         * }
         * Single dummy = new Single(0, "", 0.0, null, "");
         * List<Single> singles;
         * if (hasCategory && hasName && hasPrice) {
         * singles = dummy.searchByAllFilters(conn, normalizeCategory(selectedCategory),
         * name, priceLimit);
         * } else if (hasCategory && hasName) {
         * singles = dummy.searchByCategoryAndName(conn,
         * normalizeCategory(selectedCategory), name);
         * } else if (hasCategory && hasPrice) {
         * singles = dummy.searchByCategoryAndPrice(conn,
         * normalizeCategory(selectedCategory), priceLimit);
         * } else if (hasName && hasPrice) {
         * singles = dummy.searchByNameAndPrice(conn, name, priceLimit);
         * } else if (hasCategory) {
         * singles = dummy.searchByCategory(conn, normalizeCategory(selectedCategory));
         * } else if (hasName) {
         * singles = dummy.searchByName(conn, name);
         * } else if (hasPrice) {
         * singles = dummy.getSinglesUnder(priceLimit, conn);
         * } else {
         * //resultList.getItems().add("Please enter name, price or category.");
         * singles = dummy.getAllSingles(conn);
         * }
         * 
         * if (singles.isEmpty()) {
         * resultList.getItems().add("No items found.");
         * } else {
         * for (Single s : singles) {
         * resultList.getItems().add("Single: " + s.getName() + " - $" + s.getPrice());
         * }
         * }
         */
        // boolean hasName = !name.isEmpty();
        boolean hasPrice = !priceText.isEmpty();
        // boolean hasCategory = selectedCategory != null &&
        // !selectedCategory.equals("-- Any --");
        Float priceLimit = Float.MAX_VALUE;
        if (hasPrice) {
          try {
            priceLimit = Float.parseFloat(priceText);
          } catch (NumberFormatException ex) {
            resultList.getItems().add("Invalid price input.");
            return;
          }
        }

        Menu menu = new Menu(conn);
        List<Single> allSingles = new ArrayList<>();
        if (selectedCategory.equals("MAIN") || selectedCategory.equals("BURGERS")) {
          allSingles.addAll(menu.getMains());
        } else if (selectedCategory.equals("SIDES")) {
          allSingles.addAll(menu.getSides());
        } else if (selectedCategory.equals("DRINK") || selectedCategory.equals("DRINKS")) {
          allSingles.addAll(menu.getDrinks());
        } else if (selectedCategory.equals("DESSERT") || selectedCategory.equals("DESSERTS")) {
          allSingles.addAll(menu.getDesserts());
        } else if (selectedCategory.equals("EXTRA") || selectedCategory.equals("EXTRAS")) {
          allSingles.addAll(menu.getExtras());
        } else {
          // "-- Any --" or unrecognized value
          allSingles.addAll(menu.getMains());
          allSingles.addAll(menu.getSides());
          allSingles.addAll(menu.getDrinks());
          allSingles.addAll(menu.getDesserts());
          allSingles.addAll(menu.getExtras());
        }

        List<Single> filtered = new ArrayList<>();
        /*
         * for (Single single : allSingles) {
         * boolean matchesName = true;
         * boolean matchesPrice = true;
         * boolean matchesCategory = true;
         * if (hasName) {
         * String lowerCaseName = single.getName().toLowerCase();
         * matchesName = lowerCaseName.contains(name.toLowerCase());
         * }
         * 
         * if (hasPrice) {
         * matchesPrice = single.getPrice() <= priceLimit;
         * }
         * 
         * if (hasCategory) {
         * matchesCategory =
         * single.getType().name().equalsIgnoreCase(normalizedCategory);
         * }
         * 
         * if (matchesName && matchesPrice && matchesCategory) {
         * filtered.add(single);
         * }
         * }
         */
        for (Single single : allSingles) {
          String singleName = single.getName().toLowerCase();
          double singlePrice = single.getPrice();

          boolean nameMatches = true;
          boolean priceMatches = true;
          if (!name.isEmpty()) {
            String searchTerm = name.toLowerCase();
            nameMatches = singleName.contains(searchTerm);
          }

          if (singlePrice > priceLimit) {
            priceMatches = false;
          } else {
            priceMatches = true;
          }

          if (nameMatches && priceMatches) {
            filtered.add(single);
          }
        }
        boolean nameFilterActive = !name.isEmpty();
        boolean priceFilterActive = !priceText.isEmpty();

        Comparator<Single> nameComparator = Comparator.comparing(Single::getName,String.CASE_INSENSITIVE_ORDER);
        Comparator<Single> priceComparator = Comparator.comparingDouble(Single::getPrice);

        if (nameFilterActive && !priceFilterActive) {
          filtered.sort(nameComparator);

        } else if  (!nameFilterActive && priceFilterActive) {
          filtered.sort(priceComparator.reversed());

        } else if (nameFilterActive && priceFilterActive) {
          filtered.sort(nameComparator.thenComparing(priceComparator));


        }





        if (filtered.isEmpty()) {
          resultList.getItems().add("No items found.");
        } else {
          for (Single s : filtered) {
            resultList.getItems().add("Single: " + s.getName() + " - " + s.getPrice() + " SEK");
          }
        }
        filteredSingles = filtered;


      } catch (SQLException ex) {
        resultList.getItems().add("SQL Error: " + ex.getMessage());
      } catch (NumberFormatException ex) {
        resultList.getItems().add("Invalid price input.");
      }

      if (resultsListHandler != null) {
        List<Product> productList = new ArrayList<>(filteredSingles);
        resultsListHandler.accept(productList);
      }

    });

    resultList.setOnMouseClicked(e -> {
      String selectedText = resultList.getSelectionModel().getSelectedItem();
      if (selectedText == null || resultSelectHandler == null) {
        return;
      }
      try {
        Menu menu = new Menu(conn);
        List<Single> allSingles = new ArrayList<>();
        allSingles.addAll(menu.getMains());
        allSingles.addAll(menu.getSides());
        allSingles.addAll(menu.getDrinks());
        allSingles.addAll(menu.getDesserts());
        allSingles.addAll(menu.getExtras());

        String clickedItemName = selectedText.split(" - ")[0].replace("Single: ", "").trim();

        for (Single s : allSingles) {
          if (s.getName().equalsIgnoreCase(clickedItemName)) {
            resultSelectHandler.accept(s);
            break;
          }
        }

      } catch (SQLException ex) {
        ex.printStackTrace();

      }
    });

  }

  public String getText() {
    String name = nameField.getText();
    return name.trim();
  }

  /**
   * Returns the currently selected category from the category combo box.
   */
  public String getSelectedCategory() {
    String selected = categoryCombo.getValue();
    if (selected == null || selected.equals("-- Any --")) {
      return "";
    } else {
      return selected;
    }
  }

  /**
   * Returns the price filter entered by the user, or -1 if the field is empty.
   */
  public double getPriceFilter() throws NumberFormatException {
    String input = priceField.getText().trim();
    if (input.isEmpty()) {
      return -1;
    } else {
      return Double.parseDouble(input);
    }
  }

  public void setOnResultSelectHandler(java.util.function.Consumer<Product> handler) {
    this.resultSelectHandler = handler;
  }

  public void setOnResultsListHandler(java.util.function.Consumer<List<Product>> handler) {
    this.resultsListHandler = handler;
  }

  // private String normalizeCategory(String categoryName) {
  // if (categoryName == null) {
  // return "EXTRA";
  // }

  // switch (categoryName.trim().toLowerCase()) {
  // case "main":
  // case "burgers":
  // return "BURGERS";
  // case "sides":
  // return "SIDES";
  // case "drinks":
  // return "DRINKS"; // <-- FIXED HERE
  // case "desserts":
  // return "DESSERTS";
  // case "extras":
  // case "special offers":
  // return "EXTRA";
  // default:
  // return "EXTRA";
  // }
  // }

}
