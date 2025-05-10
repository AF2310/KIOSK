package org.example.buttons;

import java.util.List;
import javafx.scene.control.Labeled;
import org.example.kiosk.Dictionary;

/**
 * LangBtn is a button that represents a language selection option.
 * It extends the RoundButton class and initializes with specific parameters.
 */
public class LangBtn extends RoundButton {

  private boolean isEnglish = true;
  private Dictionary dictionary;

  public LangBtn() {
    super("languages", 140);
    this.dictionary = new Dictionary(true);
  }

  public boolean isEnglish() {
    return isEnglish;
  }

  /**
   * Updates the language of the provided UI components.
   * Toggles the language in the dictionary and translates the text of each component.
   */
  public void updateLanguage(List<Labeled> components) {
    dictionary.toggleLanguage();
    for (Labeled component : components) {
      String currentText = component.getText();
      String translated = dictionary.translate(currentText);
      component.setText(translated);
    }
  }
}
