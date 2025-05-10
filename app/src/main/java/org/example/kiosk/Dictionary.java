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
    swedish.put("Welcome to", "Välkommen till");
    swedish.put("Eat Here", "Ät här");
    swedish.put("Takeaway", "Ta med");
    swedish.put("Driver found and connected", "Drivrutin hittad och ansluten");

    english.put("Välkommen till", "Welcome to");
    english.put("Ät här", "Eat Here");
    english.put("Ta med", "Takeaway");
    english.put("Drivrutin hittad och ansluten", "Driver found and connected");
  }

  /**
   * Translates the given text based on the current language setting.
   */
  public String translate(String text) {
    if (toEnglish) {
      // Try to get the English translation, if not found, return the original text
      return english.containsKey(text) ? english.get(text) : text;
    } else {
      // Try to get the Swedish translation, if not found, return the original text
      return swedish.containsKey(text) ? swedish.get(text) : text;
    }
  }

  public void toggleLanguage() {
    this.toEnglish = !this.toEnglish;
  }

  public boolean isEnglish() {
    return toEnglish;
  }
}
