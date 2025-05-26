package org.example.screens;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.boxes.CheckoutGridWithButtons;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.ColorSquareButtonWithImage;
import org.example.buttons.ConfirmOrderButton;
import org.example.buttons.EatHereButton;
import org.example.buttons.LangBtn;
import org.example.buttons.RectangleTextFieldWithLabel;
import org.example.buttons.SqrBtnWithOutline;
import org.example.buttons.TakeAwayButton;
import org.example.buttons.TitleLabel;
import org.example.kiosk.InactivityTimer;
import org.example.kiosk.LanguageSetting;
import org.example.menu.Product;
import org.example.orders.Cart;
import org.example.orders.Order;
import org.example.sql.DatabaseManager;
import org.example.users.Customer;

/**
 * This is the Screen that displays the order
 * of the customer. Here, the customer can check,
 * edit and confirm his order. It also displays the
 * final price of the order.
 */
public class CheckoutScreen {

  private Stage primaryStage;
  private Boolean discountApplied = false; //TODO Temporary quickfix; remove later!!
  private int discountFactor;
  private Order order = new Order();

  /**
   * Creating a scene for the checkout menu.
   * Most likely not all the variables that are needed.
   *
   * @param primaryStage    the primary stage of this scene
   * @param windowWidth     width of window
   * @param windowHeight    height of window
   * @param mainMenuScreen  the previous scene of this scene
   * @param welcomeScrScene the welcome screen (for cancel order button)
   * @param conn            the database connection
   * @return scene containing all the order details
   */

  public CustomScene createCheckoutScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene mainMenuScreen,
      Scene welcomeScrScene,
      String mode,
      Cart cart,
      Connection conn) {

    // Setting primary stage and welcome screen
    this.primaryStage = primaryStage;

    // Spacer to elements away from each other
    Region topspacer = new Region();
    HBox.setHgrow(topspacer, Priority.ALWAYS);
    // You cannot use one spacer twice
    Region topLeftSpacer = new Region();
    HBox.setHgrow(topLeftSpacer, Priority.ALWAYS);

    HBox modeIndicatorBox = new HBox();
    modeIndicatorBox.setAlignment(Pos.TOP_RIGHT);

    if ("takeaway".equalsIgnoreCase(mode)) {
      TakeAwayButton takeawayButton = new TakeAwayButton();
      modeIndicatorBox.getChildren().add(takeawayButton);
    } else {
      EatHereButton eatHereButton = new EatHereButton();
      modeIndicatorBox.getChildren().add(eatHereButton);
    }

    // Top of layout - creating elements

    // Title of the checkout screen
    var checkoutLabel = new TitleLabel("Checkout");
    // Alignment of label
    checkoutLabel.setAlignment(Pos.TOP_LEFT);
    checkoutLabel.setPadding(new Insets(50, 100, 50, 50));
    checkoutLabel.setMinWidth(500); // Gives label space to breathe

    var promoCodeLabel = new Label();
    promoCodeLabel.setStyle(
        "-fx-text-fill: black;"
            + "-fx-font-weight: lighter;"
            + "-fx-font-size: 15;"
            + "-fx-background-radius: 10;");

    // Initially hidden
    promoCodeLabel.setVisible(false);

    // Promo code section
    RectangleTextFieldWithLabel promoField = new RectangleTextFieldWithLabel("Enter Code:",
        "rgb(255, 255, 255)");

    SqrBtnWithOutline applyPromoCode = new SqrBtnWithOutline("Apply",
        "green_tick.png", "rgb(81, 173, 86)");

    promoField.setPadding(new Insets(0, 0, 30, 0));

    applyPromoCode.setPrefWidth(80);
    applyPromoCode.setPrefHeight(50);
    HBox topRightPromoBox = new HBox(20);
    topRightPromoBox.getChildren().addAll(promoField, applyPromoCode);

    VBox topRightBox = new VBox(topRightPromoBox, promoCodeLabel);

    applyPromoCode.setOnAction(e -> {
      try (Connection connection = DatabaseManager.getConnection()) {
        String userPromoCode = promoField.getText();
        if (userPromoCode.isEmpty()) {
          // Creates an image icon for an incorrect login so that the image changes
          // upon correct or incorrect promo code.
          Image errorIcon = new Image(getClass().getResourceAsStream("/errorLogin.png"));
          ImageView errorIconView = new ImageView(errorIcon);
          errorIconView.setFitWidth(40);
          errorIconView.setFitHeight(40);
          errorIconView.setPreserveRatio(true);

          promoCodeLabel.setGraphic(errorIconView);
          promoCodeLabel.setGraphicTextGap(10);

          promoCodeLabel.setText("Invalid promo code entered!");
          promoCodeLabel.setVisible(true);
          // Using the class pause transition so the user can temp. see the
          // error message and its then removed and set to null.
          PauseTransition pause = new PauseTransition(Duration.seconds(2));
          pause.setOnFinished(event -> {
            promoCodeLabel.setVisible(false);
            promoCodeLabel.setText(null);
            promoCodeLabel.setGraphic(null); // removes all fields of the label
          });
          pause.play();
        }

        String promoCodeSql = "SELECT name, discount_type, discount_value, promo_code "
            + "FROM promotion";

        PreparedStatement promoStmt = connection.prepareStatement(promoCodeSql);
        ResultSet promoCodeResults = promoStmt.executeQuery();

        boolean valid = false;
        while (promoCodeResults.next()) {
          String currentCode = promoCodeResults.getString("promo_code");
          if (userPromoCode.equals(currentCode)) {
            int discountFactor = promoCodeResults.getInt("discount_value");
            order.applyDiscount(discountFactor);
            promoCodeLabel.setText("Promo code applied " + discountFactor + "% off");
            promoCodeLabel.setVisible(true);
            valid = true;
            break;
          }
        }

        if (!valid) {
          promoCodeLabel.setText("Invalid promo code");
          promoCodeLabel.setVisible(true);
        }

      } catch (SQLException e1) {
        e1.printStackTrace();
      }

      for (ConfirmOrderButton button : ConfirmOrderButton.getInstances()) {
        button.updatePriceLabel();
      }
    });

    HBox topBox = new HBox();
    topBox.setAlignment(Pos.TOP_LEFT);
    topBox.getChildren().addAll(
        checkoutLabel,
        modeIndicatorBox,
        topspacer,
        topRightBox);

    // Bottom buttons

    HBox bottomButtons = new HBox();
    bottomButtons.setPadding(new Insets(10));

    // Create confirm order button instance
    ConfirmOrderButton confirmOrderButton = new ConfirmOrderButton(order);

    // Add confirmation button to listeners of cart changes
    // -> so price label of button updates when cart changes
    Cart.getInstance().addListener(() -> confirmOrderButton.updatePriceLabel());

    // User confirms order
    confirmOrderButton.setOnAction(e -> {
      int orderId = -1;
      Customer customer = new Customer();
      try {
        orderId = customer.placeOrder(discountApplied, discountFactor);
      } catch (SQLException err) {
        err.printStackTrace();
      }
      String subject = "Reciept for order: " + orderId;
      Cart.getInstance().convertMealsIntoSingles();
      try {
        Cart.getInstance().saveQuantityToDb(orderId);
      } catch (SQLException e1) {
        e1.printStackTrace();
      }

      // // Create order confirmation screen
      // OrderConfirmationScreen ordConfirmation = new OrderConfirmationScreen();
      var recieptScreen = new SendReceiptScreen();

      Scene recieptScene = recieptScreen.createSendReceiptScreen(
          this.primaryStage,
          windowWidth,
          windowHeight,
          welcomeScrScene,
          orderId,
          subject,
          Cart.getInstance().printCart(orderId)
      );
      this.primaryStage.setScene(recieptScene);
    });

    // Back button
    var backButton = new BackBtnWithTxt();
    // clicking button means user goes to previous screen
    backButton.setOnAction(e -> primaryStage.setScene(mainMenuScreen));

    // Cancel button
    var cancelButton = new ColorSquareButtonWithImage("Cancel", "/cancel.png");
    // clicking button means cancellation of order
    // and user gets send back to welcome screen
    cancelButton.setOnAction(e -> {
      Cart.getInstance().clearCart();
      InactivityTimer.getInstance().stopTimer();
      System.out.println("Order canceled!");
      primaryStage.setScene(welcomeScrScene);
    });

    // Bottom part - adding all elements together

    // Box for cancel order and back button
    HBox backAndCancel = new HBox(30);
    backAndCancel.setAlignment(Pos.BOTTOM_RIGHT);
    backAndCancel.getChildren().addAll(backButton, cancelButton);

    // Spacer to elements away from each other
    // You cannot use the same spacer twice, so a second one is needed
    Region spacer = new Region();
    HBox.setHgrow(topspacer, Priority.ALWAYS);

    // Language button
    var langButton = new LangBtn();

    // Combine all; 300px spacing between each child
    HBox bottomPart = new HBox(250);
    // Top, Right, Bottom, Left padding
    bottomPart.setPadding(new Insets(30, 75, 10, 5));
    bottomPart.getChildren().addAll(
        langButton,
        spacer,
        confirmOrderButton,
        backAndCancel);

    VBox layout = new VBox(100);
    layout.setAlignment(Pos.TOP_LEFT);
    layout.setPadding(new Insets(30));

    // Get items and their quantities
    Product[] items = cart.getItems();
    int[] quantitys = cart.getQuantity();

    // Create the grid with items and quantities
    CheckoutGridWithButtons checkoutGrid = new CheckoutGridWithButtons(
        items,
        quantitys,
        6);

    layout.setAlignment(Pos.CENTER);
    layout.setPadding(new Insets(20));
    layout.getChildren().addAll(
        topBox,
        checkoutGrid,
        bottomPart);

    LanguageSetting.getInstance().smartTranslate(layout);

    // Translate button action
    langButton.addAction(event -> {
      LanguageSetting lang = LanguageSetting.getInstance();
      String newLang;
      if (lang.getSelectedLanguage().equals("en")) {
        newLang = "sv";
      } else {
        newLang = "en";
      }
      lang.changeLanguage(newLang);
      lang.smartTranslate(layout);
    });

    // Translate the whole layout before rendering
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(layout);
    lang.smartTranslate(layout);

    // Create final scene result
    CustomScene scene = new CustomScene(layout, windowWidth, windowHeight);

    // Reads and applies the customized background color
    Color bgColor = BackgroundColorStore.getCurrentBackgroundColor();

    if (bgColor != null) {
      scene.setBackgroundColor(bgColor);
    }

    return scene;
  }
}
