package org.example.screens;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.CancelButtonWithText;
import org.example.buttons.LangBtn;
import org.example.buttons.SqrBtnWithOutline;
import org.example.buttons.TitleLabel;
import org.example.kiosk.LabelManager;
import org.example.kiosk.LanguageSetting;
import org.example.menu.Drink;
import org.example.menu.Meal;
import org.example.menu.Product;
import org.example.menu.Side;
import org.example.menu.Type;
import org.example.orders.Cart;

/**
 * A Class for picking side and drink option for the meal.
 */
public class MealCustomizationScreen {

  private LanguageSetting languageSetting = new LanguageSetting();

  private Connection conn;

  /**
   * Constructor for connecting to the DB.
   */
  public MealCustomizationScreen() {
    try {
      this.conn = DriverManager.getConnection(
          "jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
              + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd"
              + "?user=u5urh19mtnnlgmog"
              + "&password=zPgqf8o6na6pv8j8AX8r"
              + "&useSSL=true"
              + "&allowPublicKeyRetrieval=true");
    } catch (SQLException e) {
      e.printStackTrace();
      System.err.println("Failed to establish DB connection.");
    }
  }

  private List<Product> getSideOptionsForMeal(int mealId) {
    List<Product> sideOptions = new ArrayList<>();
    String sql = """
        SELECT p.product_id, p.name, p.price, p.image_url
        FROM meal_sideoptions mso
        JOIN product p ON mso.product_id = p.product_id
        WHERE mso.meal_id = ?
        """;
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, mealId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Side side = new Side(
              rs.getInt("product_id"),
              rs.getString("name"),
              rs.getFloat("price"),
              Type.SIDES,
              rs.getString("image_url")
          );
          sideOptions.add(side);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return sideOptions;
  }

  /**
   * Constructor for selecting the side option for a meal.
   *
   * @param stage the primary stage
   * @param returnScene the scene to return to in this case if we click cancel
   * @param meal the meal for which this side is picked
   * @return returns a scene for side options.
   */
  public CustomScene createSideSelectionScene(Stage stage, Scene returnScene,
      Meal meal) {
    try {
      meal.setMain(conn);
    } catch (SQLException e) {
      e.printStackTrace();
      System.err.println("Failed to set main for the meal.");
    }
    // Creating the title for the scene
    Label title = new TitleLabel("Pick a Side for your Meal");

    // Centering it on top of the layout
    HBox titleBox = new HBox(title);
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setPadding(new Insets(30, 0, 0, 0));
    BorderPane layout = new BorderPane();
    layout.setTop(titleBox);
    // Making an hbox to separate two things in the center
    HBox centerBox = new HBox(50);
    centerBox.setPadding(new Insets(40));
    centerBox.setAlignment(Pos.CENTER);

    // gridpane is used here for the side options
    GridPane sideOptionsGrid = new GridPane();
    sideOptionsGrid.setHgap(20);
    sideOptionsGrid.setVgap(20);
    sideOptionsGrid.setAlignment(Pos.CENTER_LEFT);
    sideOptionsGrid.setPadding(new Insets(10));
    
    DropShadow glowEffect = new DropShadow();
    glowEffect.setColor(Color.CORNFLOWERBLUE);
    glowEffect.setRadius(20);
    glowEffect.setSpread(0.6);
    
    List<Product> sideOptions = getSideOptionsForMeal(meal.getId());
    final ImageView[] selectedImage = {null};

    for (int i = 0; i < sideOptions.size(); i++) {
      Product side = sideOptions.get(i);
      
      VBox sideBox = new VBox(5);
      sideBox.setAlignment(Pos.CENTER);
      sideBox.setPadding(new Insets(10));

      ImageView sideImage;
      InputStream imgStream = getClass().getResourceAsStream(side.getImagePath());

      if (imgStream != null) {
        sideImage = new ImageView(new Image(imgStream));
      } else {
        System.err.println("Could not load image; " + side.getImagePath());
        sideImage = new ImageView();
      }
      sideImage.setFitHeight(100);
      sideImage.setPreserveRatio(true);

      Label sideLabel = new Label(side.getName());
      sideLabel.setStyle("-fx-font-size: 14px;");
      LabelManager.register(sideLabel);

      sideBox.getChildren().addAll(sideImage, sideLabel);

      sideBox.setOnMouseClicked(e -> {
        if (selectedImage[0] != null) {
          selectedImage[0].setEffect(null);
        }
        sideImage.setEffect(glowEffect);
        selectedImage[0] = sideImage;
        meal.setSide(side);
      });

      sideOptionsGrid.add(sideBox, i % 2, i / 2);
    }
    // Inputing the image of the selected meal and the name so
    // the user is aware what burger/meal they picked
    VBox mealDisplay = new VBox(10);
    mealDisplay.setAlignment(Pos.CENTER);
    ImageView mealImage;
    InputStream imageStream = getClass().getResourceAsStream(meal.getImagePath());
    if (imageStream != null) {
      mealImage = new ImageView(new Image(imageStream));
      
    } else {
      System.err.println("Could not load image: " + meal.getImagePath());
      mealImage = new ImageView();
    }
    mealImage.setFitHeight(300);
    mealImage.setPreserveRatio(true);

    Label mealLabel = new Label(meal.getName());
    mealLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    LabelManager.register(mealLabel);
    mealDisplay.getChildren().addAll(mealImage, mealLabel);
    centerBox.getChildren().addAll(sideOptionsGrid, mealDisplay);
    layout.setCenter(centerBox);

    // Implementing a lot of the same stuff we implemented in other scenes,
    // the bottom bar with the language button and confirm and canccel
    var languageBtn = new LangBtn();
    HBox bottomLeftBox = new HBox(languageBtn);
    bottomLeftBox.setAlignment(Pos.BOTTOM_LEFT);
    Region leftSpacer = new Region();
    Region rightSpacer = new Region();
    HBox.setHgrow(leftSpacer, Priority.ALWAYS);
    HBox.setHgrow(rightSpacer, Priority.ALWAYS);
  
    SqrBtnWithOutline confirmButton = new SqrBtnWithOutline("Confirm",
        "green_tick.png", "rgb(81, 173, 86)");
    CancelButtonWithText cancelBtn = new CancelButtonWithText();
    HBox confirmBox = new HBox(confirmButton);
    confirmBox.setAlignment(Pos.BOTTOM_CENTER);
    HBox cancelBox = new HBox(cancelBtn);
    cancelBox.setAlignment(Pos.BOTTOM_RIGHT);

    HBox bottomLayout = new HBox();
    bottomLayout.setPadding(new Insets(20));
    bottomLayout.getChildren().addAll(languageBtn, leftSpacer, confirmBox, rightSpacer, cancelBox);
    layout.setBottom(bottomLayout);

    // goes back to the main screen when the user clicks cancel
    cancelBtn.setOnMouseClicked(e -> {
      stage.setScene(returnScene);
    });

    // Goes to the next scene which is the drink options scene
    confirmButton.setOnMouseClicked(e -> {
      Scene drinkScene = createDrinkSelectionScene(stage, returnScene, meal,
          stage.getScene());
      stage.setScene(drinkScene);
    });

    CustomScene scene = new CustomScene(layout, 1920, 1080);

    // Reads and applies the customized background color
    Color bgColor = BackgroundColorStore.getCurrentBackgroundColor();

    if (bgColor != null) {

      scene.setBackgroundColor(bgColor);

    }

    return scene;
  }

  private List<Product> getDrinkOptionsForMeal(int mealId) {
    List<Product> drinkOptions = new ArrayList<>();
    String sql = """
        SELECT p.product_id, p.name, p.price, p.image_url
        FROM meal_drinkoptions mdo
        JOIN product p ON mdo.product_id = p.product_id
        WHERE mdo.meal_id = ?
        """;
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, mealId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Drink drink = new Drink(
              rs.getInt("product_id"),
              rs.getString("name"),
              rs.getFloat("price"),
              Type.DRINKS,
              rs.getString("image_url")
          );
          drinkOptions.add(drink);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return drinkOptions;
  }

  /**
   * This constructor creates the scene for selecting a drink for a meal.
   *
   * @param stage primary stage
   * @param mainScene goes back to the mainscreen
   * @param meal the meal for which this drink goes to.
   * @param sideScene goesback to the side scene
   * @return it returns the scene for drink options.
   */
  public CustomScene createDrinkSelectionScene(Stage stage, Scene mainScene,
      Meal meal, Scene sideScene) {

    Label title = new TitleLabel("Pick a Drink for your Meal");

    // The drink options scene is for now almost exact same as the side scene one,
    // It just has a different label, so no need for commenting this part
    HBox titleBox = new HBox(title);
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setPadding(new Insets(30, 0, 0, 0));
    BorderPane layout = new BorderPane();
    layout.setTop(titleBox);

    HBox centerBox = new HBox(50);
    centerBox.setPadding(new Insets(40));
    centerBox.setAlignment(Pos.CENTER);

    GridPane drinkOptionsGrid = new GridPane();
    drinkOptionsGrid.setHgap(20);
    drinkOptionsGrid.setVgap(20);
    drinkOptionsGrid.setAlignment(Pos.CENTER_LEFT);
    drinkOptionsGrid.setPadding(new Insets(10));
    
    DropShadow glowEffect = new DropShadow();
    glowEffect.setColor(Color.CORNFLOWERBLUE);
    glowEffect.setRadius(20);
    glowEffect.setSpread(0.6);

    List<Product> drinkOptions = getDrinkOptionsForMeal(meal.getId());
    final ImageView[] selectedImage = {null};

    for (int i = 0; i < drinkOptions.size(); i++) {
      Product drink = drinkOptions.get(i);
      
      VBox drinkBox = new VBox(5);
      drinkBox.setAlignment(Pos.CENTER);

      ImageView drinkImage;
      InputStream imgStream = getClass().getResourceAsStream(drink.getImagePath());

      if (imgStream != null) {
        drinkImage = new ImageView(new Image(imgStream));
      } else {
        System.err.println("Could not load image; " + drink.getImagePath());
        drinkImage = new ImageView();
      }
      drinkImage.setFitHeight(100);
      drinkImage.setPreserveRatio(true);

      Label drinkLabel = new Label(drink.getName());
      drinkLabel.setStyle("-fx-font-size: 14px;");
      LabelManager.register(drinkLabel);

      drinkBox.getChildren().addAll(drinkImage, drinkLabel);
      drinkBox.setOnMouseClicked(e -> {
        if (selectedImage[0] != null) {
          selectedImage[0].setEffect(null);
        }
        drinkImage.setEffect(glowEffect);
        selectedImage[0] = drinkImage;
        meal.setDrink(drink);
      });
      drinkOptionsGrid.add(drinkBox, i % 2, i / 2);
    }
    
    VBox mealDisplay = new VBox(10);
    mealDisplay.setAlignment(Pos.CENTER);
    InputStream imageStream = getClass().getResourceAsStream(meal.getImagePath());
    ImageView mealImage;

    mealImage = new ImageView(new Image(imageStream));
    mealImage.setFitHeight(300);
    mealImage.setPreserveRatio(true);

    Label mealLabel = new Label(meal.getName());
    LabelManager.register(mealLabel);
    mealLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    mealDisplay.getChildren().addAll(mealImage, mealLabel);
    centerBox.getChildren().addAll(drinkOptionsGrid, mealDisplay);
    layout.setCenter(centerBox);



    var langButton = new LangBtn();
    var confirmBtn = new SqrBtnWithOutline("Confirm", "green_tick.png", "rgb(81, 173, 86)");
    var cancelBtn = new CancelButtonWithText();
    var backButton = new BackBtnWithTxt();

    Region spacer1 = new Region();
    Region spacer2 = new Region();
    HBox.setHgrow(spacer1, Priority.ALWAYS);
    HBox.setHgrow(spacer2, Priority.ALWAYS);

    HBox bottomBar = new HBox(20, langButton, spacer1, confirmBtn, spacer2, backButton, cancelBtn);
    bottomBar.setPadding(new Insets(20));
    layout.setBottom(bottomBar);

    //Setting the onclick for the backbutton (going back to the side options)
    backButton.setOnMouseClicked(e -> {
      stage.setScene(sideScene);
    });

    // Here we go back to the side options scene when we click cancel
    cancelBtn.setOnMouseClicked(e -> {
      stage.setScene(mainScene);
    });

    //Onclick for the confirm button (loading meal confirmation scene)
    confirmBtn.setOnMouseClicked(e -> {
      Cart cart = Cart.getInstance();
      cart.addProduct(meal);
      stage.setScene(mainScene);
    });

    // Translate all the text
    langButton.addAction(event -> {
      // Toggle the language in LanguageSetting
      languageSetting.changeLanguage(
          languageSetting.getSelectedLanguage().equals("en") ? "sv" : "en");
      languageSetting.updateAllLabels(layout);
    });

    CustomScene scene = new CustomScene(layout, 1920, 1080);

    // Reads and applies the customized background color
    Color bgColor = BackgroundColorStore.getCurrentBackgroundColor();

    if (bgColor != null) {

      scene.setBackgroundColor(bgColor);

    }

    return scene;
  }

  // TODO create this screen and link it after the drink selection.
  /**
   * Creates the meal confirmation scene after drink selection.
   *
   * @param stage the primary stage
   * @param drinkSelectionScene the previous scene for drink selection
   * @return the meal confirmation CustomScene
   */
  public CustomScene createMealConfirmationScene(Stage stage, Scene drinkSelectionScene) {
    BorderPane layout = new BorderPane();

    CustomScene scene = new CustomScene(layout, 1920, 1080);

    // Reads and applies the customized background color
    Color bgColor = BackgroundColorStore.getCurrentBackgroundColor();

    if (bgColor != null) {

      scene.setBackgroundColor(bgColor);

    }

    return scene;
  }
}


