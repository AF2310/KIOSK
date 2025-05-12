package org.example.kiosk;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
// import javafx.scene.control.TableCell;
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 * Represents the LanguageSetting for the Self Service Kiosk system.
 */
public class LanguageSetting {
  public List<String> availableLanguages;
  public String selectedLanguage;
  private Dictionary dictionary;

  public LanguageSetting() {
    this.dictionary = new Dictionary();
    this.selectedLanguage = "en";
  }

  /**
   * Changes the current language of the application.
   */
  public void changeLanguage(String newLanguage) {
    if (newLanguage.equals("en") || newLanguage.equals("sv")) {
      this.selectedLanguage = newLanguage;
      dictionary.toggleLanguage(); // Toggle the language in the dictionary
    }
  }

  /**
   * Updates the text of all Label nodes within the given root container.
   * It also translates ListView and TableView items (cell content).
   */
  public void updateAllLabels(Parent root) {
    // Iterate through the children of the Parent container
    for (Node node : root.getChildrenUnmodifiable()) {
      // If the node is a Label, update the text
      if (node instanceof Label) {
        Label label = (Label) node;
        String translatedText = dictionary.translate(label.getText());
        label.setText(translatedText);
      }

      // If the node is a TextField, update the prompt text
      if (node instanceof TextField) {
        TextField textField = (TextField) node;
        String translatedPrompt = dictionary.translate(textField.getPromptText());
        textField.setPromptText(translatedPrompt);
      }

      // If the node is a PasswordField, update the prompt text
      if (node instanceof PasswordField) {
        PasswordField passwordField = (PasswordField) node;
        String translatedPrompt = dictionary.translate(passwordField.getPromptText());
        passwordField.setPromptText(translatedPrompt);
      }

      // If the node is a ListView, update the list cell content
      if (node instanceof ListView<?>) {
        ListView<?> listView = (ListView<?>) node; // Safe cast to ListView<?> first

        // Check if the ListView contains String items
        if (listView.getItems().isEmpty() || listView.getItems().get(0) instanceof String) {
          // Now safely cast to ListView<String> and proceed
          @SuppressWarnings("unchecked")
          ListView<String> stringListView = (ListView<String>) listView;

          // Set a custom cell factory to dynamically update item text
          stringListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
              return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                  super.updateItem(item, empty);
                  if (!empty && item != null) {
                    String translatedItem = dictionary.translate(item);
                    setText(translatedItem);
                  }
                }
              };
            }
          });
        }
      }

      // If the node is a chart, translate its axis labels
      if (node instanceof javafx.scene.chart.Chart) {
        javafx.scene.chart.Chart chart = (javafx.scene.chart.Chart) node;

        if (chart instanceof javafx.scene.chart.XYChart<?, ?>) {
          javafx.scene.chart.XYChart<?, ?> xyChart = (javafx.scene.chart.XYChart<?, ?>) chart;

          if (xyChart.getXAxis() != null) {
            String originalLabel = xyChart.getXAxis().getLabel();
            xyChart.getXAxis().setLabel(dictionary.translate(originalLabel));
          }

          if (xyChart.getYAxis() != null) {
            String originalLabel = xyChart.getYAxis().getLabel();
            xyChart.getYAxis().setLabel(dictionary.translate(originalLabel));
          }
        }
      }

      // If the node is a Button, translate its text
      if (node instanceof javafx.scene.control.Button) {
        javafx.scene.control.Button button = (javafx.scene.control.Button) node;
        String translatedText = dictionary.translate(button.getText());
        button.setText(translatedText);
      }

      // // If the node is a TableView, update the table cell content
      // if (node instanceof TableView) {
      // TableView<?> tableView = (TableView<?>) node; // Use TableView<?> to avoid
      // unchecked cast

      // // Loop through each column and update the cell content
      // for (TableColumn<?, ?> column : tableView.getColumns()) {
      // // Ensure the column is of the expected type TableColumn<String, String>
      // if (column instanceof TableColumn<?, ?>) {
      // // Cast the column safely to TableColumn<String, String>
      // @SuppressWarnings("unchecked")
      // TableColumn<String, String> stringColumn = (TableColumn<String, String>)
      // column;

      // // Set the cell factory for the column
      // stringColumn.setCellFactory(param -> new TableCell<String, String>() {
      // @Override
      // protected void updateItem(String item, boolean empty) {
      // super.updateItem(item, empty);
      // if (!empty && item != null) {
      // String translatedItem = dictionary.translate(item);
      // setText(translatedItem);
      // }
      // }
      // });
      // }
      // }
      // }

      // If the node is a container, recursively update its children
      if (node instanceof Parent) {
        updateAllLabels((Parent) node);
      }
    }
  }

  // Getter for selectedLanguage
  public String getSelectedLanguage() {
    return selectedLanguage;
  }
}
