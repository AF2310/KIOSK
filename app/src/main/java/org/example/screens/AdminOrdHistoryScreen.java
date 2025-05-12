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
import javafx.stage.Stage;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.kiosk.LanguageSetting;
import org.example.menu.OrderItem;
import org.example.menu.Product;
import org.example.orders.Order;

/**
 * Order History class.
 */
public class AdminOrdHistoryScreen {

  private LanguageSetting languageSetting = new LanguageSetting();

  /**
   * Scene to display the order history.
   *
   * @param primaryStage this window
   * @param prevScene pevious scene to go back to
   * @return the scene itself
   */
  public Scene showHistoryScene(Stage primaryStage, Scene prevScene) {
    
    // So the admin doesnt forget where he is lol
    Label historyLabel = new Label("Order History:");
    historyLabel.setStyle(
        "-fx-font-size: 45px;"
        + "-fx-font-weight: bold;"
    );

    // Setting up the table
    TableColumn<Order, Integer> idColumn = new TableColumn<>("Order ID");
    idColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
    idColumn.setPrefWidth(150);
    idColumn.setStyle("-fx-alignment: CENTER;");
    TableColumn<Order, Integer> kioskColumn = new TableColumn<>("Kiosk ID");
    kioskColumn.setCellValueFactory(new PropertyValueFactory<>("kioskId"));
    kioskColumn.setPrefWidth(150);
    kioskColumn.setStyle("-fx-alignment: CENTER;");
    TableColumn<Order, String> productsColumn = new TableColumn<>("Products");
    productsColumn.setCellValueFactory(new PropertyValueFactory<>("productSummary"));

    // To enable multi line string in one cell
    productsColumn.setCellFactory(column -> {
      return new TableCell<>() {

        private final Label label = new Label();

        {

          label.setWrapText(true);
          label.setStyle(
              "-fx-padding: 5px;"
              + "-fx-alignment: Center;"
          );
          label.maxWidthProperty().bind(this.widthProperty().subtract(10));
          label.setMinHeight(Region.USE_PREF_SIZE);
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
    productsColumn.setPrefWidth(800);
    TableColumn<Order, Double> amountColumn = new TableColumn<>("Amount Total");
    amountColumn.setCellValueFactory(new PropertyValueFactory<>("amountTotal"));
    amountColumn.setPrefWidth(250);
    amountColumn.setStyle("-fx-alignment: CENTER;");
    TableColumn<Order, String> statusColumn = new TableColumn<>("Status");
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    statusColumn.setPrefWidth(200);
    statusColumn.setStyle("-fx-alignment: CENTER;");
    TableColumn<Order, Timestamp> dateColumn = new TableColumn<>("Order Date");
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
    dateColumn.setPrefWidth(250);
    dateColumn.setStyle("-fx-alignment: CENTER;");
    
    // Displaying the fetched data in a neat table
    TableView<Order> historyTable = new TableView<>();
    historyTable.setFixedCellSize(-1);
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
    VBox.setVgrow(historyTable, Priority.ALWAYS);
    historyTable.prefWidthProperty().bind(orderHistory.widthProperty());
    orderHistory.setPadding(new Insets(20, 0, 0, 0));

    // Upper part of the screen
    VBox topContainer = new VBox();
    topContainer.setMaxWidth(Double.MAX_VALUE);
    topContainer.setAlignment(Pos.TOP_CENTER);
    topContainer.setSpacing(40);
    VBox.setVgrow(orderHistory, Priority.ALWAYS);
    topContainer.getChildren().addAll(historyLabel, orderHistory);

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
    bottomContainer.setAlignment(Pos.BOTTOM_LEFT);
    bottomContainer.getChildren().addAll(langButton, spacerBottom, backButton);

    // Setting positioning of all the elements
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setCenter(topContainer);
    layout.setBottom(bottomContainer);
    BorderPane.setMargin(bottomContainer, new Insets(40, 0, 0, 0));

    // // Just pass in the Labeled components to translate
    // langButton.addAction(event -> {
    //   // Toggle the language in LanguageSetting
    //   languageSetting.changeLanguage(
    //       languageSetting.getSelectedLanguage().equals("en") ? "sv" : "en"
    //   );
    //   languageSetting.updateAllLabels(layout);
    //   historyTable.refresh();
    // });

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

}
