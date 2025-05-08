package org.example.buttons;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.example.menu.Ingredient;
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

public class SearchBar extends VBox {
  private final TextField nameField;
  private final TextField priceField;
  private final CheckBox ingredientCheckbox;
  private final Button searchButton;
  private final ListView<String> resultList;
  private final ComboBox<String> categoryCombo;

  public SearchBar(Connection conn) {
    this.setPadding(new Insets(10));
    this.setSpacing(10);

    nameField = new TextField();
    nameField.setPromptText("Search by name...");

    priceField = new TextField();
    priceField.setPromptText("Search by max price...");

    ingredientCheckbox = new CheckBox("Search Ingredients");

    categoryCombo = new ComboBox<>();
    categoryCombo.getItems().addAll("MAIN", "DRINK", "DESSERT", "EXTRA"); // Populate as per your Type enum
    categoryCombo.setPromptText("Search by category...");

    searchButton = new Button("Search");

    resultList = new ListView<>();
    resultList.setPrefHeight(200);

    HBox inputFields = new HBox(10, nameField, priceField, ingredientCheckbox, searchButton);
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
          if (!name.isEmpty()) {
            List<Ingredient> ingredients = ingredientDummy.searchIngredientsByName(name, conn);
            for (Ingredient ing : ingredients) {
              resultList.getItems().add("Ingredient: " + ing.getName());
            }

          }  else if (!priceText.isEmpty()) {
            float price = Float.parseFloat(priceText);
            List<Ingredient> ingredients = ingredientDummy.searchIngredientsByPrice(price, conn);
            for (Ingredient ing : ingredients) {
              resultList.getItems().add("Ingredient: " + ing.getName());
            }

          } else {
            resultList.getItems().add("Please enter name or price.");
          }
        } else {
          Single dummy = new Single(0, "", 0.0, null, "");
          //if (selectedCategory != null && !selectedCategory.isEmpty())
          if (selectedCategory != null && !selectedCategory.isEmpty()) {
            List<Single> byCategory = dummy.getOptionsByType(conn, selectedCategory);
            for (Single s : byCategory) {
              resultList.getItems().add("Single: " + s.getName() + " - $" + s.getPrice());

            }

          } else if (!name.isEmpty()) {
            List<Single> singles = dummy.searchByName(conn, name);
            for (Single s : singles) {
              resultList.getItems().add("Single: " + s.getName() + " - $" + s.getPrice());

            }
          } else if (!priceText.isEmpty()) {
            float price = Float.parseFloat(priceText);
            List<Single> singles = dummy.getSinglesUnder(price, conn);
            for (Single s : singles) {
              resultList.getItems().add("Single: " + s.getName() + " - $" + s.getPrice());
            }
          } else {
            resultList.getItems().add("Please enter name or price.");
          }
        }
      } catch (SQLException ex) {
        resultList.getItems().add("SQL Error: " + ex.getMessage());
      } catch (NumberFormatException ex) {
        resultList.getItems().add("Invalid price input.");
      }


    });

  }
  
  

  
}
