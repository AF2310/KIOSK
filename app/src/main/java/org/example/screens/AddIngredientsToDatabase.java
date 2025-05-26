package org.example.screens;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.buttons.RectangleTextFieldWithLabel;
import org.example.buttons.SqrBtnWithOutline;
import org.example.kiosk.LanguageSetting;
import org.example.sql.DatabaseManager;

/**
 * Scene for adding new ingredients to the database.
 */
public class AddIngredientsToDatabase {
  private Stage primaryStage;
  private Scene prevScene;
  private ListView<HBox> categoryListView;
  private ObservableList<HBox> categories = FXCollections.observableArrayList();

  /**
   * Constructor for the AddIngredientsToDatabase scene.
   *
   * @param primaryStage the primary stage of the application
   * @param prevScene    the previous scene to return to
   */
  public AddIngredientsToDatabase(Stage primaryStage, Scene prevScene) {
    this.primaryStage = primaryStage;
    this.prevScene = prevScene;
  }

  /**
   * Creates and returns the scene for adding ingredients to the database.
   *
   * @return the configured Scene object
   */
  public Scene addIngredientToDb() {

    Label titleLabel = new Label("Add New Ingredient");
    titleLabel.setStyle("-fx-font-size: 40; -fx-font-weight: bold;");

    HBox titleBox = new HBox(titleLabel);
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setPadding(new Insets(30));

    // Input fields
    RectangleTextFieldWithLabel nameField = new RectangleTextFieldWithLabel(
        "Ingredient Name:", "rgb(255, 255, 255)");

    VBox categoryBox = new VBox(5); 
    categoryBox.setPrefHeight(500);
    categoryBox.setPrefWidth(250);
    loadCategories(categoryBox); 

    categoryBox.setAlignment(Pos.CENTER);

    VBox inputFields = new VBox(20, nameField, 
        new Label("Select Categories which will have this ingredient:"), categoryBox);
    inputFields.setAlignment(Pos.CENTER);
    inputFields.setPadding(new Insets(20));
    // Buttons
    SqrBtnWithOutline confirmButton = new SqrBtnWithOutline(
        "Add Ingredient", "green_tick.png", "rgb(81, 173, 86)");
    confirmButton.setOnAction(e -> addIngredientToDatabase(
        nameField.getText(),
        getSelectedCategories(categoryBox)));

    BackBtnWithTxt backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> primaryStage.setScene(prevScene));

    HBox buttonBox = new HBox(20, confirmButton, backButton);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.setPadding(new Insets(20));

    // Language button
    LangBtn langButton = new LangBtn();

    // Spacer for bottom layout
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    // Bottom layout
    HBox bottomBox = new HBox(langButton, spacer, buttonBox);
    bottomBox.setPadding(new Insets(20));

    // Main layout
    BorderPane mainLayout = new BorderPane();
    mainLayout.setTop(titleBox);
    mainLayout.setCenter(inputFields);
    mainLayout.setBottom(bottomBox);

    // Translate button action
    langButton.addAction(event -> {
      LanguageSetting lang = LanguageSetting.getInstance();
      String newLang = lang.getSelectedLanguage().equals("en") ? "sv" : "en";
      lang.changeLanguage(newLang);
      lang.smartTranslate(mainLayout);
    });

    // Initial translation
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(mainLayout);
    lang.smartTranslate(mainLayout);

    return new Scene(mainLayout, 1920, 1080);
  }

  private void loadCategories(VBox categoryBox) {
    try (Connection connection = DatabaseManager.getConnection()) {
      String sql = "SELECT category_id, name FROM category";
      PreparedStatement stmt = connection.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        CheckBox checkBox = new CheckBox(rs.getString("name"));
        checkBox.setUserData(rs.getInt("category_id"));
        checkBox.setStyle("-fx-font-size: 16px;"
            + "-fx-padding: 8px;"   
            + "-fx-scale-x: 1.2;"     
            + "-fx-scale-y: 1.2;"
        );

        // Add directly to the VBox instead of creating HBoxes
        categoryBox.getChildren().add(checkBox);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private List<Integer> getSelectedCategories(VBox categoryBox) {
    List<Integer> selected = new ArrayList<>();
    for (Node node : categoryBox.getChildren()) {
      if (node instanceof CheckBox) {
        CheckBox checkBox = (CheckBox) node;
        if (checkBox.isSelected()) {
          selected.add((Integer) checkBox.getUserData());
        }
      }
    }
    return selected;
  }

  private void addIngredientToDatabase(String name, List<Integer> categoryIds) {
    if (name.isEmpty()) {
      return;
    }

    Connection connection = null;
    try {
      connection = DatabaseManager.getConnection();
      connection.setAutoCommit(false);

      // Insert ingredient
      String ingredientSql = "INSERT INTO ingredient (ingredient_name) VALUES (?)";
      PreparedStatement ingredientStmt = connection.prepareStatement(
          ingredientSql, PreparedStatement.RETURN_GENERATED_KEYS);
      ingredientStmt.setString(1, name);
      ingredientStmt.executeUpdate();

      // Get generated ingredient ID
      int ingredientId;
      try (ResultSet generatedKeys = ingredientStmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          ingredientId = generatedKeys.getInt(1);
        } else {
          throw new SQLException("Failed to get ingredient ID");
        }
      }

      // Insert category associations
      if (!categoryIds.isEmpty()) {
        String categorySql = "INSERT INTO ingredient_category"
            + "(ingredient_id, category_id) VALUES (?, ?)";

        PreparedStatement categoryStmt = connection.prepareStatement(categorySql);

        for (int categoryId : categoryIds) {
          categoryStmt.setInt(1, ingredientId);
          categoryStmt.setInt(2, categoryId);
          categoryStmt.addBatch();
        }
        categoryStmt.executeBatch();
      }

      connection.commit();
    } catch (SQLException e) {
      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
      e.printStackTrace();
    } finally {
      if (connection != null) {
        try {
          connection.setAutoCommit(true);
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void showError(String errorText, Label label) {
    label.setStyle("-fx-text-fill: red; -fx-font-size: 14;");
    label.setVisible(true);
  }
}