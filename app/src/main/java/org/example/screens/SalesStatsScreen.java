package org.example.screens;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButton;
import org.example.kiosk.LanguageSetting;
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
   * @param prevScene    pevious scene to go back to
   * @return the scene itself
   */
  public Scene showStatsScene(Stage primaryStage, Scene prevScene) {

    // VBox for the charts
    VBox topRight = new VBox();
    topRight.setPadding(new Insets(10));
    topRight.setPrefWidth(1280);
    topRight.setAlignment(Pos.CENTER);
    HBox.setHgrow(topRight, Priority.ALWAYS);

    // So the admin doesnt forget where he is lol
    Label pageLabel = new Label("Sales Statistics:");
    pageLabel.setStyle(
        "-fx-font-size: 45px;"
            + "-fx-font-weight: bold;");

    // Had to somehow make List final, so a wrapper class is being used
    final OrderListWrapper wrapper = new OrderListWrapper();

    try {

      // Populates the list wrapper
      wrapper.orders = queryOrders();
      queryOrderItemsFor(wrapper.orders);

    } catch (SQLException e) {

      e.printStackTrace();

    }

    // Button for Product vs Quantity ordered - BarChart
    MidButton productSalesBtn = new MidButton("Sold Products", "rgb(255, 255, 255)", 30);
    productSalesBtn.setOnAction(e -> {

      // Clears out the right side VBox for the new chart
      topRight.getChildren().clear();
      BarChart<String, Number> chart = createProductSales(wrapper.orders);
      topRight.getChildren().add(chart);

    });

    // Button for Orders vs Weekdays -- BarChart
    MidButton ordersPerDayBtn = new MidButton("Orders per Day", "rgb(255, 255, 255)", 30);
    ordersPerDayBtn.setOnAction(e -> {

      // Clears out the right side VBox for the new chart
      topRight.getChildren().clear();
      BarChart<String, Number> chart = createOrdersPerWeekday(wrapper.orders);
      topRight.getChildren().add(chart);

    });

    // Button for Orders vs Hours -- Graph/LineChart
    MidButton ordersByHourBtn = new MidButton("Orders by Hour", "rgb(255, 255, 255)", 30);
    ordersByHourBtn.setOnAction(e -> {

      // Clears out the right side VBox for the new chart
      topRight.getChildren().clear();
      LineChart<Number, Number> chart = createOrdersByHour(wrapper.orders);
      topRight.getChildren().add(chart);

    });

    // Button for Revenue Share of Products -- PieChart
    MidButton productRevenuesBtn = new MidButton("Revenue by Products", "rgb(255, 255, 255)", 30);
    productRevenuesBtn.setOnAction(e -> {

      // Clears out the right side VBox for the new chart
      topRight.getChildren().clear();
      PieChart pieChart = createProductRevenue(wrapper.orders);
      topRight.getChildren().add(pieChart);

    });

    // Button for Revenue of last 12 month
    MidButton revenuesBtn = new MidButton("Revenue (last 12 Month)", "rgb(255, 255, 255)", 30);
    revenuesBtn.setOnAction(e -> {

      // Clears out the right side VBox for the new chart
      topRight.getChildren().clear();
      BarChart<String, Number> barChart = createMonthlyRevenue(wrapper.orders);
      topRight.getChildren().add(barChart);

    });

    // VBox for the Buttons
    VBox topLeft = new VBox();
    topLeft.getChildren().addAll(
        pageLabel, productSalesBtn, ordersPerDayBtn,
        ordersByHourBtn, productRevenuesBtn, revenuesBtn);
    topLeft.setPadding(new Insets(10));
    topLeft.setSpacing(25);
    topLeft.setPrefWidth(640);
    topLeft.setAlignment(Pos.TOP_LEFT);
    HBox.setHgrow(topLeft, Priority.NEVER);

    // Wrapping the two top side VBoxes
    // Fixed size
    HBox topContainer = new HBox();
    topContainer.getChildren().addAll(topLeft, topRight);
    topContainer.setPrefHeight(695);
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

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
    // Functionality below VBox Layout
    var langButton = new LangBtn();

    // Bottom row of the screen
    HBox bottomContainer = new HBox();
    bottomContainer.setPrefHeight(335);
    bottomContainer.setAlignment(Pos.BOTTOM_LEFT);
    bottomContainer.getChildren().addAll(langButton, spacerBottom, backButton);

    // Setting positioning of all the elements
    VBox layout = new VBox();
    layout.setPadding(new Insets(50));
    layout.getChildren().addAll(topContainer, bottomContainer);

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

    Scene scene = new Scene(layout, 1920, 1080);

    return scene;

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
                + "&allowPublicKeyRetrieval=true");

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
                + "&allowPublicKeyRetrieval=true");

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

            Product product = new Product() {
            };
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

  // Rendering the Products vs Quantity ordered chart
  private BarChart<String, Number> createProductSales(ArrayList<Order> orders) {

    // Data collection Part
    Map<String, Integer> productSales = new HashMap<>();

    for (Order order : orders) {

      for (OrderItem item : order.getProducts()) {

        String productName = item.getProduct().getName();
        int quantity = item.getQuantity();

        productSales.put(productName, productSales.getOrDefault(productName, 0) + quantity);

      }

    }

    // Rendering Part
    // Parameter is named xxAxis because of checkstyle naming conventions
    CategoryAxis xxAxis = new CategoryAxis();
    xxAxis.setLabel("Product");

    // Parameter is named yyAxis because of checkstyle naming conventions
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

  // Rendering Orders vs Weekdays chart
  private BarChart<String, Number> createOrdersPerWeekday(ArrayList<Order> orders) {

    // Data collection part
    Map<DayOfWeek, Integer> weekdayCounts = new LinkedHashMap<>();

    for (Order order : orders) {

      LocalDateTime dateTime = order.getOrderDate().toLocalDateTime();
      DayOfWeek day = dateTime.getDayOfWeek();
      weekdayCounts.put(day, weekdayCounts.getOrDefault(day, 0) + 1);

    }

    // Rendering Part
    // Parameter is named xxAxis because of checkstyle naming conventions
    CategoryAxis xxAxis = new CategoryAxis();
    xxAxis.setLabel("Weekday");

    // Parameter is named yyAxis because of checkstyle naming conventions
    NumberAxis yyAxis = new NumberAxis();
    yyAxis.setLabel("Number of Orders");

    BarChart<String, Number> barChart = new BarChart<>(xxAxis, yyAxis);
    barChart.setTitle("Orders per Weekday");

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Orders");

    for (DayOfWeek day : DayOfWeek.values()) {

      String dayName = day.toString().substring(0, 1) + day.toString().substring(1).toLowerCase();
      int count = weekdayCounts.getOrDefault(day, 0);
      series.getData().add(new XYChart.Data<>(dayName, count));

    }

    barChart.getData().add(series);

    return barChart;

  }

  // Rendering Orders by Hours graph
  private LineChart<Number, Number> createOrdersByHour(ArrayList<Order> orders) {

    // Data collection Part
    Map<Double, Integer> hourCounts = new TreeMap<>();

    for (Order order : orders) {

      LocalDateTime dateTime = order.getOrderDate().toLocalDateTime();

      double hour = dateTime.getHour()
          + (dateTime.getMinute() / 60.0) + (dateTime.getSecond() / 3600.0);

      double roundedHour = Math.round(hour * 4) / 4.0;

      hourCounts.put(roundedHour, hourCounts.getOrDefault(roundedHour, 0) + 1);

    }

    // Rendering Part
    // Parameter is named xxAxis because of checkstyle naming conventions
    NumberAxis xxAxis = new NumberAxis(0, 24, 0.25);
    xxAxis.setLabel("Hour of Day");

    // Parameter is named yyAxis because of checkstyle naming conventions
    NumberAxis yyAxis = new NumberAxis();
    yyAxis.setLabel("Numbers of Orders");

    LineChart<Number, Number> lineChart = new LineChart<>(xxAxis, yyAxis);
    lineChart.setTitle("Orders by Hour");

    // Without this line, very order would create a dot on the graph
    lineChart.setCreateSymbols(false);

    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setName("Volume of Orders");

    hourCounts.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(entry -> {
          double hour = entry.getKey();
          int count = entry.getValue();
          series.getData().add(new XYChart.Data<>(hour, count));

        });

    lineChart.getData().add(series);

    return lineChart;

  }

  // Rendering revenue share of products chart
  private PieChart createProductRevenue(ArrayList<Order> orders) {

    // Data collection part
    Map<String, Double> revenueMap = new HashMap<>();

    for (Order order : orders) {

      for (OrderItem item : order.getProducts()) {

        String productName = item.getProduct().getName();
        double revenue = item.getQuantity() * item.getUnitPrice();
        revenueMap.put(productName, revenueMap.getOrDefault(productName, 0.0) + revenue);

      }

    }

    // Rendering part
    PieChart pieChart = new PieChart();
    pieChart.setTitle("Product Revenue Share");

    for (Map.Entry<String, Double> entry : revenueMap.entrySet()) {

      if (entry.getValue() > 0) {

        pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));

      }

    }

    return pieChart;

  }

  // Chart rendering month vs revenue (last 12 months)
  private BarChart<String, Number> createMonthlyRevenue(ArrayList<Order> orders) {

    // Data collection part
    Map<String, Double> monthlyRevenue = new LinkedHashMap<>();

    LocalDateTime now = LocalDateTime.now();

    for (int i = 11; i >= 0; i--) {

      LocalDateTime month = now.minusMonths(i);
      String label = month.getYear() + "-" + String.format("%02d", month.getMonthValue());
      monthlyRevenue.put(label, 0.0);

    }

    for (Order order : orders) {

      LocalDateTime date = order.getOrderDate().toLocalDateTime();
      String label = date.getYear() + "-" + String.format("%02d", date.getMonthValue());

      if (monthlyRevenue.containsKey(label)) {

        double updated = monthlyRevenue.get(label) + order.getAmountTotal();
        monthlyRevenue.put(label, updated);

      }

    }

    // Rendering part
    CategoryAxis xxAxis = new CategoryAxis();
    xxAxis.setLabel("Month");

    NumberAxis yyAxis = new NumberAxis();
    yyAxis.setLabel("Revenue in SEK");

    BarChart<String, Number> barChart = new BarChart<>(xxAxis, yyAxis);
    barChart.setTitle("Total Revenue of the last 12 Months");

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Revenue");

    for (Map.Entry<String, Double> entry : monthlyRevenue.entrySet()) {

      series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));

    }

    barChart.getData().add(series);
    return barChart;

  }

}
