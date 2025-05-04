package org.example.screens;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.BackButton;
import org.example.buttons.RoundButton;
import org.example.orders.Order;

/**
 * Order History class.
 */
public class AdminOrdHistoryScreen {

  private Stage primaryStage;

  // for testing purposes
  private Integer tempOrderId = null;

  /**
   * Scene to display the order history.
   *
   * @param primaryStage this window
   * @param prevScene pevious scene to go back to
   * @return the scene itself
   */
  public Scene showHistoryScene(Stage primaryStage, Scene prevScene) {

    this.primaryStage = primaryStage;

    // Temporarily inserting an order into db for testing purposes
    try {

      insertTempOrder();

    } catch (SQLException e) {

      e.printStackTrace();
      
    }

    
    // So the admin doesnt forget where he is lol
    Label historyLabel = new Label("Order History:");
    historyLabel.setStyle(
        "-fx-font-size: 65px;"
        + "-fx-font-weight: bold;"
    );

    // Placeholder for the actual history
    Label tempLabel = new Label("Here will be all the orders");
    tempLabel.setStyle(
        "-fx-font-size: 35px;"
        + "-fx-font-weight: bold;"
    );

    HBox orderHistory = new HBox(tempLabel);
      
    VBox topBox = new VBox();
    topBox.getChildren().addAll(historyLabel, orderHistory);

    // Upper part of the screen
    HBox topContainer = new HBox();
    topContainer.setAlignment(Pos.TOP_LEFT);

    // Back button
    // Clicking button means user goes to previous screen
    // Also deletes the order that has previously been added to the db
    BackButton backButton = new BackButton();
    backButton.setOnAction(e -> {

      try {

        deleteTempOrder();

      } catch (SQLException ex) {

        ex.printStackTrace();

      }

      primaryStage.setScene(prevScene);

    });


    // Language Button
    // cycles images on click
    RoundButton langButton = new RoundButton("languages", 70);
    
    // Spacer for Bottom Row
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);
    
    // Bottom row of the screen
    HBox bottomContainer = new HBox();
    bottomContainer.setAlignment(Pos.BOTTOM_LEFT);
    bottomContainer.getChildren().addAll(langButton, spacerBottom, backButton);

    // Setting positioning of all the elements
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setTop(topContainer);
    layout.setBottom(bottomContainer);

    return new Scene(layout, 1920, 1080);

  }

  private ArrayList<Order> queryOrders() throws SQLException {

    // ArrayList to hold all orders queried from the db
    ArrayList<Order> history = new ArrayList<>();

    String querySql = "SELECT order_ID, kiosk_ID, customer_ID, order_date, amount_total, status "
        + "FROM `order`";

    try (

        Connection conn = DriverManager.getConnection(
            "jdbc:mysql://bdzvjxbmj2y2atbkdo4j-mysql.services"
            + ".clever-cloud.com:3306/bdzvjxbmj2y2atbkdo4j"
            + "?user=u5urh19mtnnlgmog"
            + "&password=zPgqf8o6na6pv8j8AX8r"
            + "&useSSL=true"
            + "&allowPublicKeyRetrieval=true"
        );

        PreparedStatement stmt = conn.prepareStatement(querySql);
        ResultSet results = stmt.executeQuery()

    ) {
      // Creates Orders from queried data
      while (results.next()) {

        int orderId = results.getInt("order_ID");
        int kioskId = results.getInt("kiosk_ID");
        int customerId = results.getInt("customer_ID");
        Timestamp orderDate = results.getTimestamp("order_date");
        double amountTotal = results.getDouble("amount_total");
        String status = results.getString("status");

        Order order = new Order(orderId, kioskId, customerId, orderDate, amountTotal, status);
        history.add(order);

      }

    }

    return history;

  }

  /**
   * FOR TESTING PURPOSES ONLY!
   * Inserts a temporary order to the Database.
   */
  private void insertTempOrder() throws SQLException {

    String insertSql = 
        "INSERT INTO `order`(kiosk_ID, customer_ID, amount_total, status) "
        + "VALUES (?, ?, ?, ?)";

    try (
        Connection conn = DriverManager.getConnection(
            "jdbc:mysql://bdzvjxbmj2y2atbkdo4j-mysql.services"
            + ".clever-cloud.com:3306/bdzvjxbmj2y2atbkdo4j"
            + "?user=u5urh19mtnnlgmog"
            + "&password=zPgqf8o6na6pv8j8AX8r"
            + "&useSSL=true"
            + "&allowPublicKeyRetrieval=true"
        );

        PreparedStatement stmt = conn.prepareStatement(
            insertSql,
            PreparedStatement.RETURN_GENERATED_KEYS
        )
        
    ) {

      stmt.setInt(1, 1);
      stmt.setInt(1, 1);
      stmt.setDouble(3, 123);
      stmt.setString(4, "PAID");

      stmt.executeUpdate();

      ResultSet rs = stmt.getGeneratedKeys();

      if (rs.next()) {

        tempOrderId = rs.getInt(1);

      }

    }

  }

  /**
   * FOR TESTING PURPOSES ONLY!
   * Deletes the temporary order from db.
   */
  private void deleteTempOrder() throws SQLException {

    if (tempOrderId == null) {

      return;

    }

    String deleteSql = "DELETE FROM `order` WHERE order_ID = ?";

    try (
        Connection conn = DriverManager.getConnection(
            "jdbc:mysql://bdzvjxbmj2y2atbkdo4j-mysql.services"
            + ".clever-cloud.com:3306/bdzvjxbmj2y2atbkdo4j"
            + "?user=u5urh19mtnnlgmog"
            + "&password=zPgqf8o6na6pv8j8AX8r"
            + "&useSSL=true"
            + "&allowPublicKeyRetrieval=true"
        );

        PreparedStatement stmt = conn.prepareStatement(deleteSql)) {

      stmt.setInt(1, tempOrderId);
      stmt.executeUpdate();
        
    }

  }

}
