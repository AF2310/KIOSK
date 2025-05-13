package org.example.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Label;

/**
 * The class to check Database connection.
 */
public class SqlConnectionCheck {

  // Declare the Label
  private Label mysql;
  private Connection connection;

  /**
   * Construct the class.
   */
  public SqlConnectionCheck() {
    // SQL Connection starts here
    mysql = new Label(); // Initialize the Label variable

    try {
      // Establish connection
      connection = DriverManager.getConnection(
          "jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
          + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd"
          + "?user=u5urh19mtnnlgmog"
          + "&password=zPgqf8o6na6pv8j8AX8r"
          + "&useSSL=true"
          + "&allowPublicKeyRetrieval=true"
      );
      
      mysql.setText("Driver found and connected");

      connection.setAutoCommit(false);

      // Error handling
    } catch (SQLException e) {
      mysql.setText("An error has occurred: " + e.getMessage());
      mysql.setStyle(
          "-fx-background-color: transparent;"
          + "-fx-text-fill: black;"
          + "-fx-font-size: 10;"
          + "-fx-padding: 5 10;"
          + "-fx-background-radius: 10;");
    }
    // End of SQL Section
  }

  /**
   * Getter for the mysql label.
   *
   * @return the mysql Label.
   */
  public Label getMysqlLabel() {
    return mysql;
  }

  public Connection getConnection() {
    return connection;
  }

  /**
  * Closes the database connection.
  */
  public void closeConnection() {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
