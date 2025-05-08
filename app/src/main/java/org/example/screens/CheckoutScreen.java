package org.example.screens;

import java.sql.Connection;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.animations.FadingAnimation;
import org.example.boxes.CheckoutGridWithButtons;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.CancelButtonWithText;
import org.example.buttons.ConfirmOrderButton;
import org.example.buttons.EatHereButton;
import org.example.buttons.LangBtn;
import org.example.buttons.TakeAwayButton;
import org.example.menu.Product;
import org.example.orders.Cart;
import org.example.users.Customer;

/**
 * This is the Screen that displays the order
 * of the customer. Here, the customer can check,
 * edit and confirm his order. It also displays the
 * final price of the order.
 */
public class CheckoutScreen {

  private Stage primaryStage;
  // private Connection conn;
  // private float totalPrice = 0.0f;
  // private Label totalLabel;

  /**
   * Creating a scene for the checkout menu.
   * TODO: We still need the other database variables etc..
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
  public Scene createCheckoutScreen(
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
    Label checkoutLabel = new Label("Checkout");
    checkoutLabel.setStyle(
        "-fx-font-size: 60px;"
            + "-fx-font-weight: bold;");
    // Alignment of label
    checkoutLabel.setAlignment(Pos.TOP_LEFT);
    checkoutLabel.setPadding(new Insets(50, 100, 50, 50));

    // Promo code section
    TextField promoField = new TextField();
    promoField.setPromptText("Enter Promo Code");
    promoField.setStyle(
        "-fx-background-color: white;"
            + "-fx-text-fill: white;"
            + "-fx-font-size: 24px;"
            + "-fx-font-weight: bold;"
            + "-fx-background-radius: 15;"
            + "-fx-alignment: center;");
    promoField.setMaxWidth(300);
    promoField.setMaxHeight(100);

    // // Create the Apply Promo button
    // Button applyPromoButton = new Button();
    // applyPromoButton.setGraphic(promoField);
    // applyPromoButton.setStyle(
    //     "-fx-background-color: #4CAF50;"
    //         + "-fx-background-radius: 15;"
    //         + "-fx-padding: 20;");
    // applyPromoButton.setPrefSize(590, 90);
    // applyPromoButton.setOnAction(e -> applyPromo(promoField.getText()));

    // Top of layout - combining elements

    // HBox leftsideBox = new HBox(100);
    // leftsideBox.setAlignment(Pos.CENTER);
    // leftsideBox.getChildren().addAll(
    //     checkoutLabel,
    //     promoField,
    //     topLeftSpacer
    // );

    HBox topBox = new HBox();
    topBox.setAlignment(Pos.TOP_LEFT);
    topBox.getChildren().addAll(
        checkoutLabel,
        modeIndicatorBox,
        topspacer,
        promoField);

    // Bottom buttons

    HBox bottomButtons = new HBox();
    bottomButtons.setPadding(new Insets(10));

    // Create confirm order button instance
    ConfirmOrderButton confirmOrderButton = new ConfirmOrderButton();

    // Add confirmation button to listeners of cart changes
    // -> so price label of button updates when cart changes
    Cart.getInstance().addListener(() -> confirmOrderButton.updatePriceLabel());

    // User confirms order
    confirmOrderButton.setOnAction(e -> {
      int orderId = -1;

      Customer customer = new Customer();
      try {
        orderId = customer.placeOrder(conn);
      } catch (SQLException err) {
        // TODO Auto-generated catch block
        err.printStackTrace();
      }

      try {
        Cart.getInstance().saveQuantityToDb(conn, orderId);
      } catch (SQLException e1) {
        e1.printStackTrace();
      }

      // Create order confirmation screen
      OrderConfirmationScreen ordConfirmation = new OrderConfirmationScreen();

      Scene ordConfirmScene = ordConfirmation.createOrderConfirmationScreen(
          this.primaryStage,
          windowWidth,
          windowHeight,
          welcomeScrScene,
          orderId
      );
      this.primaryStage.setScene(ordConfirmScene);

      // Creating fading animation
      // to transition smoothly to welcome screen after order confirmation
      FadingAnimation fadingAnimation = new FadingAnimation(primaryStage);
      // Fading out of current scene
      fadingAnimation.fadeOutAnimation(
          "white",
          ordConfirmScene,
          welcomeScrScene);

      // Clear cart after order has been done
      Cart.getInstance().clearCart();
    });

    // Back button
    var backButton = new BackBtnWithTxt();
    // clicking button means user goes to previous screen
    backButton.setOnAction(e -> primaryStage.setScene(mainMenuScreen));

    // Cancel button
    var cancelButton = new CancelButtonWithText();
    // clicking button means cancellation of order
    // and user gets send back to welcome screen
    cancelButton.setOnAction(e -> {
      Cart.getInstance().clearCart();
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

    //Language button
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

    // Create final scene result
    return new Scene(layout, windowWidth, windowHeight);
  }

  // private void applyPromo(String code) {
  //   try {
  //     String sql = "";
  //     PreparedStatement stmt = conn.prepareStatement(sql);
  //     stmt.setString(1, code);
  //     ResultSet rs = stmt.executeQuery();
  //     if (rs.next()) {
  //       float discount = rs.getFloat("discount");
  //       totalPrice *= (1 - discount);
  //       totalLabel.setText(String.format("Total: %.2f :- (%.0f%% discount applied)",
  //           totalPrice, discount * 100));
  //     } else {
  //       totalLabel.setText("Invalid promo code");
  //     }

  //   } catch (SQLException ex) {
  //     ex.printStackTrace();
  //   }

  // }
}
