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

    // TODO: Move the translations to a separate file
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
    // addTranslation("Cancel", "Avbryt");

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
    // addTranslation("Cancel", "Avbryt");

    // Item Details vocabulary
    addTranslation("Add To Cart", "Lägg till");
    addTranslation("Back", "Tillbaks");
    addTranslation("Chicken Burger", "Kycklingburgare");
    addTranslation("Beef Burger", "Nötköttsburgare");
    addTranslation("Beer Burger", "Ölburgare");
    addTranslation("Onion Rings", "Lökringar");
    addTranslation("Fries", "Pommes frites");
    addTranslation("Ice Cream", "Glass");

    // Item Descriptions
    addTranslation("This is a yummy chicken burger", "Det här är en god kycklingburgare");
    addTranslation("This is a yummy beef burger", "Det här är en god nötköttsburgare");
    addTranslation("This is a yummy beer burger", "Det här är en god ölburgare");
    addTranslation("This is a yummy onion rings", "Det här är goda lökringar");
    addTranslation("This is a yummy fries", "Det här är goda pommes frites");
    addTranslation("This is a yummy ice cream", "Det här är en god glass");
    
    // Ingredients
    addTranslation("Beef Patty", "Nötköttspuck");
    addTranslation("Bun", "Bröd");
    addTranslation("Swiss Cheese", "Schweizerost");
    addTranslation("Pickles", "Picklad gurka");
    addTranslation("Onion", "Lök");
    // addTranslation("Ketchup", "Ketchup");
    addTranslation("Mustard", "Senap");
    addTranslation("Cream", "Grädde");
    addTranslation("Chocolate Bits", "Chokladbitar");
    addTranslation("Rainbow Sprinkles", "Strössel");
    addTranslation("Whipped Cream", "Vispad grädde");
    addTranslation("Sugar", "Socker");
    addTranslation("Lemon", "Citron");
    // addTranslation("Lime", "Lime");
    addTranslation("Mint Leaves", "Myntablad");
    addTranslation("Ice", "Is");
    addTranslation("Chili Flakes", "Chiliflakes");
    addTranslation("Black Pepper", "Svartpeppar");
    addTranslation("Sea Salt", "Havssalt");
    addTranslation("Chicken Patty", "Kycklingpuck");
    addTranslation("Lettuce", "Sallad");
    addTranslation("Tomato", "Tomat");
    addTranslation("Cheddar Cheese", "Cheddarost");
    addTranslation("Mayonnaise", "Majonnäs");

    // Checkout screen vocabulary
    addTranslation("Checkout", "Kassan");
    addTranslation("Confirm Order", "Bekräfta");
    addTranslation("Total", "Totalt");
    addTranslation("Enter Promo Code", "Ange Kampanjkod");
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
