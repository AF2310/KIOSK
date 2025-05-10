package org.example.kiosk;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a Dictionary.
 */
public class Dictionary {
  private Map<String, Map<String, String>> dictionary = new HashMap<>();
  private boolean toEnglish;

  /**
   * Constructs a Dictionary object.
   */
  public Dictionary(boolean startInEnglish) {
    this.toEnglish = startInEnglish;

    // Translations, will move to a separate file later
    // Welcome screen vocabulary
    addTranslation("Welcome to", "Välkommen till");
    addTranslation("Eat Here", "Ät här");
    addTranslation("Takeaway", "Ta med");
    addTranslation("Driver found and connected", "Drivrutin hittad och ansluten");

    // Main menu vocabulary
    // Didn't implement the translation for it yet
    addTranslation("Burgers", "Burgare");
    addTranslation("Sides", "Tillbehör");
    addTranslation("Drinks", "Drycker");
    addTranslation("Desserts", "Desserter");
    addTranslation("Special Offers", "Specialerbjudanden");
    addTranslation("Cancel", "Avbryt");

    // Admin login vocabulary
    addTranslation("Admin Menu", "Adminmeny");
    addTranslation("Login", "Logga in");
    addTranslation("Back to Menu", "Gå tillbaks");
    addTranslation("Password", "Lösenord");
    addTranslation("Username", "Användarnamn");
    addTranslation("Invalid login details", "Ogiltig inloggning");

    // Admin Main Menu vocabulary
    addTranslation("Welcome, Admin!", "Välkommen, Admin!");
    addTranslation("Update Menu Items", "Uppdatera menyartiklar");
    addTranslation("Order History", "Beställningshistorik");
    addTranslation("Change Timer Setting", "Ändra Timerinställningar");
    addTranslation("See Sales Summary", "Försäljningsöversikt");
    addTranslation("Set Special Offers", "Sätt Specialerbjudanden");
    addTranslation("Cancel", "Avbryt");

    // Admin Item Screen vocabulary
    addTranslation("Add Product to Menu", "Lägg till produkten");
    addTranslation("Change Prices", "Ändra priser");
    addTranslation("Remove Product from Menu", "Ta bort produkten");

    addTranslation("Add A Product to the Menu", "Lägg till en Produkt i Menyn");
    addTranslation("Product Name:", "Produktnamn:");
    addTranslation("Product Description:", "Produktbeskrivning:");
    addTranslation("Product Price:", "Produktpris:");
    addTranslation("Is active?", "Är aktiv?");
    addTranslation("Is limited?", "Begränsad?");
    addTranslation("Product Category:", "Produktkategori:");
    addTranslation("Ingredient List", "Ingredienslista");
    addTranslation("Confirm", "Bekräfta");
    addTranslation("Cancel", "Avbryt");
  }

  private void addTranslation(String english, String swedish) {
    dictionary.put(english, Map.of("sv", swedish));
    dictionary.put(swedish, Map.of("en", english));
  }

  public void toggleLanguage() {
    toEnglish = !toEnglish;
  }

  /**
   * Translates a given word based on the current language setting.
   */
  public String translate(String word) {
    if (toEnglish) {
      Map<String, String> result = dictionary.get(word);
      return result != null ? result.get("en") : word;
    } else {
      Map<String, String> result = dictionary.get(word);
      return result != null ? result.get("sv") : word;
    }
  }
}
