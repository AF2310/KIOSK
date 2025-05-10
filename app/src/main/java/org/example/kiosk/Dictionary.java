package org.example.kiosk;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a Dictionary.
 */
public class Dictionary {
  private Map<String, String> swedish = new HashMap<>();
  private Map<String, String> english = new HashMap<>();

  private boolean toEnglish;

  /**
   * Constructs a Dictionary object.
   */
  public Dictionary(boolean startInEnglish) {
    this.toEnglish = startInEnglish;

    // Translations, will move to a separate file later
    // Welcome screen vocabulary
    swedish.put("Welcome to", "Välkommen till");
    swedish.put("Eat Here", "Ät här");
    swedish.put("Takeaway", "Ta med");
    swedish.put("Driver found and connected", "Drivrutin hittad och ansluten");

    english.put("Välkommen till", "Welcome to");
    english.put("Ät här", "Eat Here");
    english.put("Ta med", "Takeaway");
    english.put("Drivrutin hittad och ansluten", "Driver found and connected");

    // Main menu vocabulary
    swedish.put("Burgers", "Burgare");
    swedish.put("Sides", "Tillbehör");
    swedish.put("Drinks", "Drycker");
    swedish.put("Desserts", "Desserter");
    swedish.put("Special Offers", "Specialerbjudanden");
    swedish.put("Cancel", "Avbryt");

    english.put("Burgare", "Burgers");
    english.put("Tillbehör", "Sides");
    english.put("Drycker", "Drinks");
    english.put("Desserter", "Desserts");
    english.put("Specialerbjudanden", "Special Offers");
    english.put("Avbryt", "Cancel");

    // Admin login vocabulary
    swedish.put("Admin Menu", "Adminmeny");
    swedish.put("Login", "Logga in");
    swedish.put("Back to Menu", "Gå tillbaks");
    swedish.put("Password", "Lösenord");
    swedish.put("Username", "Användarnamn");

    english.put("Adminmeny", "Admin Menu");
    english.put("Logga in", "Login");
    english.put("Gå tillbaks", "Back to Menu");
    english.put("Lösenord", "Password");
    english.put("Användarnamn", "Username");

    // Admin Main Menu vocabulary
    swedish.put("Welcome, Admin!", "Välkommen, Admin!");
    swedish.put("Update Menu Items", "Uppdatera menyartiklar");
    swedish.put("Order History", "Beställningshistorik");
    swedish.put("Change Timer Setting", "Ändra Timerinställningar");
    swedish.put("See Sales Summary", "Försäljningsöversikt");
    swedish.put("Set Special Offers", "Sätt Specialerbjudanden");
    swedish.put("Cancel", "Avbryt");

    english.put("Välkommen, Admin!", "Welcome, Admin!");
    english.put("Uppdatera menyartiklar", "Update Menu Items");
    english.put("Beställningshistorik", "Order History");
    english.put("Ändra Timerinställningar", "Change Timer Setting");
    english.put("Försäljningsöversikt", "See Sales Summary");
    english.put("Sätt Specialerbjudanden", "Set Special Offers");
    english.put("Avbryt", "Cancel");
  }

  /**
   * Translates the given text based on the current language setting.
   */
  public String translate(String text) {
    if (toEnglish) {
      // Try to get the English translation, if not found, return the original text
      if (english.containsKey(text)) {
        return english.get(text);
      } else {
        return text;
      }
    } else {
      // Try to get the Swedish translation, if not found, return the original text
      if (swedish.containsKey(text)) {
        return swedish.get(text);
      } else {
        return text;
      }
    }
  }

  public void toggleLanguage() {
    this.toEnglish = !this.toEnglish;
  }

  public boolean isEnglish() {
    return toEnglish;
  }
}
