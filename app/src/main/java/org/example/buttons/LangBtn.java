package org.example.buttons;

import java.util.List;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
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
    this.isEnglish = !this.isEnglish;
  }

  /**
   * Updates the language of both labeled components and text input prompts.
   */
  public void updateLanguage(List<Labeled> labels, List<TextInputControl> inputs) {
    dictionary.toggleLanguage();

    // Update labels
    for (Labeled component : labels) {
      String currentText = component.getText();
      String translated = dictionary.translate(currentText);
      component.setText(translated);
    }

    // Update text field prompt texts
    for (TextInputControl input : inputs) {
      String prompt = input.getPromptText();
      String translated = dictionary.translate(prompt);
      input.setPromptText(translated);
    }
    this.isEnglish = !this.isEnglish;
  }
}
