package org.example.screens;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.buttons.SearchBar;
import org.example.kiosk.LanguageSetting;
import org.example.menu.Product;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This is the product deletion scene.
 * Used in the admin menu in 'UpdateMenuItems.java'.
 */
public class DeleteProductScene {

  private Stage primaryStage;
  private Scene prevScene;
  private TableView<Product> productTable;
  private SearchBar searchBar;

  /**
   * The product editor scene constructor.
   * ONLY used in the admin menu in 'UpdateMenuItems.java'.
   *
   * @param primaryStage Primary stage for the scenes (stage itself)
   * @param prevScene    Previous scene to return to
   * @param productTable product table listing all needed
   *                     product data fetched from database
   */
  public DeleteProductScene(
      Stage primaryStage,
      Scene prevScene,
      TableView<Product> productTable,
      SearchBar searchBar) {

    this.primaryStage = primaryStage;
    this.prevScene = prevScene;
    this.productTable = productTable;
    this.searchBar = searchBar;

    searchBar.setOnResultsListHandler(filteredProducts -> {
      productTable.getItems().clear();
      productTable.getItems().addAll(filteredProducts);

      searchBar.setOnResultSelectHandler(selected -> {
        if (selected instanceof Product product) {
          /*if (!productTable.getItems().contains(product)) {
            productTable.getItems().add(product);
          }*/
          boolean alreadyExists = productTable.getItems().stream()
            .anyMatch(p -> p.getId() == product.getId());

          if (!alreadyExists) {
              productTable.getItems().add(product);
          }

        }
      }
      );
    });


  }

  /**
   * This is the method to create the scene for deleting
   * products in the admin menu.
   */
  public Scene getProductDeletionScene() {
    // Label for screen
    Label productDeletionLabel = new Label("Product Deletion:");
    productDeletionLabel.setStyle(
        "-fx-font-size: 45px;"
            + "-fx-font-weight: bold;");

    // Label for system messages
    Label systemMessageLabel = new Label();

    // Button for deletion confirmation
    Button confirmButton = new Button("Yes");

    // Button for deletion cancellation
    Button abbruchButton = new Button("No");

    // Adding buttons in separate box
    HBox buttonBox = new HBox(10, confirmButton, abbruchButton);
    buttonBox.setVisible(false);
    buttonBox.setAlignment(Pos.CENTER);

    // Adding all deletion related action elements in Box
    HBox actionBox = new HBox(10, systemMessageLabel, buttonBox);
    actionBox.setVisible(false);
    actionBox.setAlignment(Pos.CENTER);

    // Set action for each row in table
    productTable.setRowFactory(tv -> {
      // Create instance of row
      TableRow<Product> row = new TableRow<>();

      // Admin clicks on a row (product)
      row.setOnMouseClicked(event -> {

        // Row only requires one click to get picked
        if (!row.isEmpty() && event.getClickCount() == 1) {

          // Get clicked product as object and its' name
          Product clickedProduct = row.getItem();
          String productName = clickedProduct.getName();

          // System message printed in the label below table
          systemMessageLabel.setText(
              "Product '" + productName + "' should be deleted?");
          // action box now visible (replacement for popup)
          buttonBox.setVisible(true);
          actionBox.setVisible(true);

          // Confirm deletion
          confirmButton.setOnAction(e -> {

            try {
              // TODO: This will be moved later
              Connection conn = DriverManager.getConnection(
                  "jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
                      + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd"
                      + "?user=u5urh19mtnnlgmog"
                      + "&password=zPgqf8o6na6pv8j8AX8r"
                      + "&useSSL=true"
                      + "&allowPublicKeyRetrieval=true");

              // delete product in database
              removeProductFromDb(clickedProduct.getId(), conn);

              // Update table by removing deleted product
              productTable.getItems().remove(clickedProduct);

              // Database error
            } catch (SQLException er) {
              er.printStackTrace();
            }

            systemMessageLabel.setText(
                "Product '" + productName + "' successfully deleted!");

            // Buttond disappear after clicking one to prevent unwanted double actions
            buttonBox.setVisible(false);
          });

          // Cancel deletion
          abbruchButton.setOnAction(e -> {
            systemMessageLabel.setText(
                "Product '" + productName + "' deletion cancelled!");

            // Buttond disappear after clicking one to prevent unwanted double actions
            buttonBox.setVisible(false);

          });
        }
      });

      return row;
    });

    // VBox for the table
    VBox productListings = new VBox(productTable);
    VBox.setVgrow(productListings, Priority.ALWAYS);
    productTable.prefWidthProperty().bind(productListings.widthProperty());
    productListings.setPadding(new Insets(20, 0, 0, 0));

    // VBox to align screen label and table
    VBox topBox = new VBox(
        40,
        productDeletionLabel,
        searchBar,
        productListings,
        systemMessageLabel,
        actionBox);
    topBox.setMaxWidth(Double.MAX_VALUE);
    topBox.setAlignment(Pos.TOP_CENTER);

    // Upper part of the screen
    HBox topContainer = new HBox();
    topContainer.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(topBox, Priority.ALWAYS);
    topContainer.setAlignment(Pos.CENTER);
    topContainer.getChildren().addAll(topBox);

    // Back button -> user goes to previous screen
    var backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> {
      primaryStage.setScene(prevScene);
    });

    // Language Button -> cycles images on click
    var langButton = new LangBtn();

    // Spacer for Bottom Row
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);

    // Bottom row of the screen
    HBox bottomContainer = new HBox(langButton, spacerBottom, backButton);
    bottomContainer.setAlignment(Pos.BOTTOM_LEFT);

    // Setting positioning of all the elements
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setTop(topContainer);
    layout.setBottom(bottomContainer);
    
    // Translate all the text
    langButton.addAction(event -> {
      LanguageSetting lang = LanguageSetting.getInstance();
      String newLang = lang.getSelectedLanguage().equals("en") ? "sv" : "en";
      lang.changeLanguage(newLang);
      lang.updateAllLabels(layout);
    });

    Scene deleteProdScene = new Scene(layout, 1920, 1080);

    // Update the language for the scene upon creation
    Parent root = deleteProdScene.getRoot();

    LanguageSetting.getInstance().registerRoot(root);
    LanguageSetting.getInstance().updateAllLabels(root);

    return deleteProdScene;
  }

  // TODO will be moved later to Query file
  /**
   * Helper method to delete a product from the database.
   * Used for admin menu in product deletion section.
   *
   * @param productId  int Id of the product that should be deleted
   * @param connection Connection to the database
   * @throws SQLException Database error
   */
  private void removeProductFromDb(int productId, Connection connection) throws SQLException {
    String s = "DELETE FROM product WHERE product_id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(s)) {
      stmt.setInt(1, productId);
      stmt.executeUpdate();
    }
  }
}
