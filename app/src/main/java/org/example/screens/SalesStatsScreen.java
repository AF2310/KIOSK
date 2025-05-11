package org.example.screens;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButton;
import org.example.menu.OrderItem;
import org.example.menu.Product;
import org.example.orders.Order;
import org.example.orders.OrderListWrapper;

/**
 * Admin screen for sales stats.
 */
public class SalesStatsScreen {
    
  /**
   * Scene to display the order history.
   *
   * @param primaryStage this window
   * @param prevScene pevious scene to go back to
   * @return the scene itself
   */
  public Scene showStatsScene(Stage primaryStage, Scene prevScene) {

    
    VBox topRight = new VBox();
    topRight.setPadding(new Insets(10));
    topRight.setPrefWidth(1280);
    HBox.setHgrow(topRight, Priority.ALWAYS);

    final OrderListWrapper wrapper = new OrderListWrapper();

    try {

      wrapper.orders = queryOrders();
      queryOrderItemsFor(wrapper.orders);

    } catch (SQLException e) {

      e.printStackTrace();

    }
    
    MidButton productSalesBtn = new MidButton("Sold Products", "rgb(255, 255, 255)", 30);
    productSalesBtn.setOnAction(e -> {

      topRight.getChildren().clear();
      Map<String, Integer> salesData = countProductSales(wrapper.orders);
      BarChart<String, Number> chart = createProductSalesChart(salesData);
      topRight.getChildren().add(chart);

    });

    VBox topLeft = new VBox();
    topLeft.setPadding(new Insets(10));
    topLeft.setSpacing(10);
    topLeft.setPrefWidth(640);
    HBox.setHgrow(topLeft, Priority.NEVER);
    topLeft.getChildren().addAll(productSalesBtn);

    HBox topContainer = new HBox();
    topContainer.setPrefHeight(720);
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    topContainer.getChildren().addAll(topLeft, topRight);

    // Back button
    // Clicking button means user goes to previous screen
    var backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> {

      primaryStage.setScene(prevScene);

    });
    
    // Spacer for Bottom Row
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);

    // Language Button
    // cycles images on click
    var langButton = new LangBtn();
    
    // Bottom row of the screen
    HBox bottomContainer = new HBox();
    bottomContainer.setPrefHeight(360);
    bottomContainer.setAlignment(Pos.BOTTOM_LEFT);
    bottomContainer.getChildren().addAll(langButton, spacerBottom, backButton);

    // Setting positioning of all the elements
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setTop(topContainer);
    layout.setBottom(bottomContainer);

    return new Scene(layout, 1920, 1080);

  }

  // Query for the orders
  private ArrayList<Order> queryOrders() throws SQLException {

    // ArrayList to hold all orders queried from the db
    ArrayList<Order> history = new ArrayList<>();

    String querySql = "SELECT order_ID, kiosk_ID, customer_ID, order_date, amount_total, status "
        + "FROM `order`";

    try (

        Connection conn = DriverManager.getConnection(
              "jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
              + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd"
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

  // Query for the Products belonging to each queried order
  private void queryOrderItemsFor(ArrayList<Order> orders) throws SQLException {

    String itemQuery = "SELECT oi.order_id, oi.product_id, p.name, p.price, oi.quantity "
          + "FROM order_item oi "
          + "JOIN product p ON oi.product_id = p.product_id";

    try (

        Connection conn = DriverManager.getConnection(
            "jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
            + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd"
            + "?user=u5urh19mtnnlgmog"
            + "&password=zPgqf8o6na6pv8j8AX8r"
            + "&useSSL=true"
            + "&allowPublicKeyRetrieval=true"
        );

        PreparedStatement stmt = conn.prepareStatement(itemQuery);
        ResultSet rs = stmt.executeQuery()

    ) {

      while (rs.next()) {

        int orderId = rs.getInt("order_id");
        int productId = rs.getInt("product_id");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        int quantity = rs.getInt("quantity");

        for (Order order : orders) {

          if (order.getOrderId() == orderId) {

            Product product = new Product() {};
            product.setId(productId);
            product.setName(name);
            product.setPrice(price);

            OrderItem orderItem = new OrderItem(product, quantity, price);

            order.getProducts().add(orderItem);

            break;

          }

        }

      }

    }

  }
  
  private Map<String, Integer> countProductSales(ArrayList<Order> orders) {

    Map<String, Integer> productSales = new HashMap<>();

    for (Order order : orders) {

      for (OrderItem item : order.getProducts()) {

        String productName = item.getProduct().getName();
        int quantity = item.getQuantity();

        productSales.put(productName, productSales.getOrDefault(productName, 0) + quantity);

      }

    }

    return productSales;

  }

  private BarChart<String, Number> createProductSalesChart(Map<String, Integer> productSales) {

    CategoryAxis xxAxis = new CategoryAxis();
    xxAxis.setLabel("Product");
    
    NumberAxis yyAxis = new NumberAxis();
    yyAxis.setLabel("Quantity Sold");

    BarChart<String, Number> barChart = new BarChart<>(xxAxis, yyAxis);
    barChart.setTitle("Product Sales");

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Sales Data");

    for (Map.Entry<String, Integer> entry : productSales.entrySet()) {

      if (entry.getValue() > 0) {

        series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));

      }

    }

    barChart.getData().add(series);
    
    return barChart;
  }

}
