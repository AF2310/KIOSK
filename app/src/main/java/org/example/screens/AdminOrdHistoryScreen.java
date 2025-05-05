package org.example.screens;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.buttons.BackButton;
import org.example.buttons.RoundButton;
import org.example.menu.Product;
import org.example.orders.Order;

/**
 * Order History class.
 */
public class AdminOrdHistoryScreen {

  private Stage primaryStage;

  // for testing purposes
  //private Integer tempOrderId = null;

  /**
   * Scene to display the order history.
   *
   * @param primaryStage this window
   * @param prevScene pevious scene to go back to
   * @return the scene itself
   */
  public Scene showHistoryScene(Stage primaryStage, Scene prevScene) {

    this.primaryStage = primaryStage;
    
    // So the admin doesnt forget where he is lol
    Label historyLabel = new Label("Order History:");
    historyLabel.setStyle(
        "-fx-font-size: 45px;"
        + "-fx-font-weight: bold;"
    );

    // Setting up the table
    TableColumn<Order, Integer> idColumn = new TableColumn<>("Order ID");
    idColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
    idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
    TableColumn<Order, Integer> kioskColumn = new TableColumn<>("Kiosk ID");
    kioskColumn.setCellValueFactory(new PropertyValueFactory<>("kioskId"));
    kioskColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
    TableColumn<Order, String> productsColumn = new TableColumn<>("Products");
    productsColumn.setCellValueFactory(new PropertyValueFactory<>("productSummary"));

    // To enable multi line string in one cell
    productsColumn.setCellFactory(column -> {
      return new TableCell<>() {

        private final Label label = new Label();

        {

          label.setWrapText(true);
          label.setStyle("-fx-padding: 5px;");
          setGraphic(label);

        }
        
        // Overrides default cell update behaviour of javafx

        @Override
        protected void updateItem(String item, boolean empty) {

          super.updateItem(item, empty);

          if (empty || item == null) {

            label.setText(null);
            setGraphic(null);

          } else {

            label.setText(item);
            setGraphic(label);

          }

        }

      };

    });
    productsColumn.setMaxWidth(1f * Integer.MAX_VALUE * 40);
    TableColumn<Order, Double> amountColumn = new TableColumn<>("Amount Total");
    amountColumn.setCellValueFactory(new PropertyValueFactory<>("amountTotal"));
    amountColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
    TableColumn<Order, String> statusColumn = new TableColumn<>("Status");
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    statusColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
    TableColumn<Order, Timestamp> dateColumn = new TableColumn<>("Order Date");
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
    dateColumn.setMaxWidth(1f * Integer.MAX_VALUE * 20);
    
    // Displaying the fetched data in a neat table
    TableView<Order> historyTable = new TableView<>();
    historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    historyTable.setMaxWidth(Double.MAX_VALUE);
    historyTable.setPrefWidth(Region.USE_COMPUTED_SIZE);
    
    // Puts the table together
    historyTable.getColumns().add(idColumn);
    historyTable.getColumns().add(kioskColumn);
    historyTable.getColumns().add(productsColumn);
    historyTable.getColumns().add(amountColumn);
    historyTable.getColumns().add(statusColumn);
    historyTable.getColumns().add(dateColumn);
    
    // Querys data into the table
    try {
      
      // Gets orders
      ArrayList<Order> orders = queryOrders();
      // Gets Products for each order
      queryOrderItemsFor(orders);
      // Inputs it into the table
      historyTable.getItems().addAll(orders);
      
    } catch (SQLException e) {
      
      e.printStackTrace();
      
    }
    
    // VBox for the table
    VBox orderHistory = new VBox(historyTable);
    VBox.setVgrow(orderHistory, Priority.ALWAYS);
    historyTable.prefWidthProperty().bind(orderHistory.widthProperty());
    orderHistory.setPadding(new Insets(20, 0, 0, 0));
    
    // VBox to align screen label and table
    VBox topBox = new VBox();
    topBox.setMaxWidth(Double.MAX_VALUE);
    topBox.setAlignment(Pos.TOP_CENTER);
    topBox.setSpacing(40);
    topBox.getChildren().addAll(historyLabel, orderHistory);

    // Upper part of the screen
    HBox topContainer = new HBox();
    topContainer.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(topBox, Priority.ALWAYS);
    topContainer.setAlignment(Pos.CENTER);
    topContainer.getChildren().addAll(topBox);

    // Back button
    // Clicking button means user goes to previous screen
    // Also deletes the order that has previously been added to the db
    BackButton backButton = new BackButton();
    backButton.setOnAction(e -> {

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

  // Query for the orders
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

  // Query for the Products belonging to each queried order
  private void queryOrderItemsFor(ArrayList<Order> orders) throws SQLException {

    String itemQuery = "SELECT oi.order_id, p.product_id, p.name, p.price "
          + "From order_item oi "
          + "JOIN product p on oi.product_id = p.product_id";

    try (

        Connection conn = DriverManager.getConnection(
            "jdbc:mysql://bdzvjxbmj2y2atbkdo4j-mysql.services"
            + ".clever-cloud.com:3306/bdzvjxbmj2y2atbkdo4j"
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

        for (Order order : orders) {

          if (order.getOrderId() == orderId) {

            Product product = new Product() {};
            product.setId(productId);
            product.setName(name);
            product.setPrice(price);

            order.getProducts().add(product);
            break;

          }

        }

      }

    }

  } 

  // /**
  //  * FOR TESTING PURPOSES ONLY!
  //  * Inserts a temporary order to the Database.
  //  */
  // private void insertTempOrder() throws SQLException {

  //   String insertSql = 
  //       "INSERT INTO `order`(kiosk_ID, customer_ID, amount_total, status) "
  //       + "VALUES (?, ?, ?, ?)";

  //   try (
  //       Connection conn = DriverManager.getConnection(
  //           "jdbc:mysql://bdzvjxbmj2y2atbkdo4j-mysql.services"
  //           + ".clever-cloud.com:3306/bdzvjxbmj2y2atbkdo4j"
  //           + "?user=u5urh19mtnnlgmog"
  //           + "&password=zPgqf8o6na6pv8j8AX8r"
  //           + "&useSSL=true"
  //           + "&allowPublicKeyRetrieval=true"
  //       );

  //       PreparedStatement stmt = conn.prepareStatement(
  //           insertSql,
  //           PreparedStatement.RETURN_GENERATED_KEYS
  //       )
        
  //   ) {

  //     stmt.setInt(1, 123);
  //     stmt.setInt(2, 1);
  //     stmt.setDouble(3, 123);
  //     stmt.setString(4, "PAID");

  //     stmt.executeUpdate();

  //     ResultSet rs = stmt.getGeneratedKeys();

  //     if (rs.next()) {

  //       tempOrderId = rs.getInt(1);

  //     }

  //   }

  // }

  // /**
  //  * FOR TESTING PURPOSES ONLY!
  //  * Deletes the temporary order from db.
  //  */
  // private void deleteTempOrder() throws SQLException {

  //   if (tempOrderId == null) {

  //     return;

  //   }

  //   String deleteSql = "DELETE FROM `order` WHERE order_ID = ?";

  //   try (
  //       Connection conn = DriverManager.getConnection(
  //           "jdbc:mysql://bdzvjxbmj2y2atbkdo4j-mysql.services"
  //           + ".clever-cloud.com:3306/bdzvjxbmj2y2atbkdo4j"
  //           + "?user=u5urh19mtnnlgmog"
  //           + "&password=zPgqf8o6na6pv8j8AX8r"
  //           + "&useSSL=true"
  //           + "&allowPublicKeyRetrieval=true"
  //       );

  //       PreparedStatement stmt = conn.prepareStatement(deleteSql)) {

  //     stmt.setInt(1, tempOrderId);
  //     stmt.executeUpdate();
        
  //   }

  // }

}
