package org.example.kiosk;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a Dictionary.
 */
public class Dictionary {
  private Map<String, String> englishToSwedish = new HashMap<>();
  private Map<String, String> swedishToEnglish = new HashMap<>();
  private boolean toEnglish;

  /**
   * Constructs a Dictionary object.
   */
  public Dictionary() {

    // Translations (these can be moved to a file later)
    // Welcome screen vocabulary
    addTranslation("Welcome to", "Välkommen till");
    addTranslation("Eat Here", "Ät här");
    addTranslation("Takeaway", "Ta med");
    addTranslation("Driver found and connected", "Drivrutin hittad och ansluten");

    // Main menu vocabulary
    addTranslation("Burgers", "Burgare");
    addTranslation("Sides", "Tillbehör");
    addTranslation("Drinks", "Drycker");
    addTranslation("Desserts", "Desserter");
    addTranslation("Meals", "Kombomenyer");
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
    addTranslation("This is tasty fanta", "Det här är en god fanta");
    addTranslation("This is tasty sprite", "Det här är en god sprite");
    addTranslation("This is tasty coke zero", "Det här är en god coke zero");
    addTranslation("This is tasty pepsi", "Det här är en god pepsi");
    addTranslation("This is tasty dr pepper", "Det här är en god Dr pepper");

    // Ingredients
    addTranslation("Beef Patty", "Nötköttspuck");
    addTranslation("Bun", "Bröd");
    addTranslation("Swiss Cheese", "Schweizerost");
    addTranslation("Pickles", "Picklad gurka");
    addTranslation("Onion", "Lök");
    addTranslation("Mustard", "Senap");
    addTranslation("Cream", "Grädde");
    addTranslation("Chocolate Bits", "Chokladbitar");
    addTranslation("Rainbow Sprinkles", "Strössel");
    addTranslation("Whipped Cream", "Vispad grädde");
    addTranslation("Sugar", "Socker");
    addTranslation("Lemon", "Citron");
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
    addTranslation("Confirm Order", "Bekräfta ");
    addTranslation("Total", "Totalt");
    addTranslation("Enter Promo Code", "Ange Kampanjkod");
    addTranslation("Total", "Totalt");
    // TODO: Fix the page counter label
    addTranslation("Page 0 of 0", "Sida 0 av 0");
    addTranslation("Page 1 of 1", "Sida 1 av 1");
    addTranslation("Page 1 of 2", "Sida 1 av 2");
    addTranslation("Page 2 of 2", "Sida 2 av 2");

    // Sales statistics vocabulary
    addTranslation("Sales Statistics:", "Försäljningsstatistik:");
    addTranslation("Sold Products", "Sålda Produkter");
    addTranslation("Orders per Day", "Beställningar per dag");
    addTranslation("Product Sales", "Produktsförsäljning");
    addTranslation("Sales Data", "Försäljningsdata");
    addTranslation("Product", "Produkt");
    addTranslation("Quantity Sold", "Antal sålda");

    addTranslation("Orders per Weekday", "Beställningar per veckodag");
    addTranslation("Orders", "Beställningar");
    addTranslation("Number of Orders", "Antal beställningar");
    addTranslation("Weekday", "Veckodag");

    // Order history vocabulary
    addTranslation("Order History:", "Beställningshistorik:");
    addTranslation("Order ID", "Beställnings-ID");
    addTranslation("Kiosk ID", "Kiosk-ID");
    addTranslation("Products", "Produkter");
    addTranslation("Amount Total", "Totalt belopp");
    addTranslation("Order Date", "Beställningsdatum");
    addTranslation("pending", "väntande");
    addTranslation("PAID", "Betald");
  }

  // Adds translations to both the English to Swedish and Swedish to English maps
  private void addTranslation(String english, String swedish) {
    englishToSwedish.put(english, swedish);
    swedishToEnglish.put(swedish, english);
  }

  // Toggle between English and Swedish
  public void toggleLanguage() {
    toEnglish = !toEnglish;
  }

  /**
   * Translates a given word based on the current language setting.
   */
  public String translate(String word) {
    if (toEnglish) {
      return englishToSwedish.getOrDefault(word, word);
    } else {
      return swedishToEnglish.getOrDefault(word, word);
    }
  }
}
