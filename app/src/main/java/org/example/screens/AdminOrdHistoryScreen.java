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
import org.example.orders.TestOrder;

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
    
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);
    
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

    ArrayList<Order> history = new ArrayList<>();

    Connection conn = DriverManager.getConnection(
        "jdbc:mysql://bdzvjxbmj2y2atbkdo4j-mysql.services"
          + ".clever-cloud.com:3306/bdzvjxbmj2y2atbkdo4j"
          + "?user=u5urh19mtnnlgmog"
          + "&password=zPgqf8o6na6pv8j8AX8r"
          + "&useSSL=true"
          + "&allowPublicKeyRetrieval=true");

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
