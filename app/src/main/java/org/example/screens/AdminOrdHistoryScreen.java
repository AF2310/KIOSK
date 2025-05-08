package org.example.screens;

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
import org.example.orders.Order;
import org.example.sql.SqlQueries;

/**
 * Order History class.
 */
public class AdminOrdHistoryScreen {
  private SqlQueries queries = new SqlQueries();

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
      ArrayList<Order> orders = queries.queryOrders();
      // Gets Products for each order
      queries.queryOrderItemsFor(orders);
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
    var backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> {

      primaryStage.setScene(prevScene);

    });

    // Language Button
    // cycles images on click
    var langButton = new LangBtn();
    
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

}
