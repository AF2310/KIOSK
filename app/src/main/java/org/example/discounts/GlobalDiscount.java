package org.example.discounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.sql.SqlConnectionCheck;


/**
 * Class for global discounts.
 */
public class GlobalDiscount implements IdiscountStrategy {
    
  public double percentage;

  /**
   * Applying discount on ALL items.
   */
  public void applyDiscount(double percentage) {
    SqlConnectionCheck connectionCheck = new SqlConnectionCheck();
    Connection connection = connectionCheck.getConnection();
    double discountFactor = (1 - (percentage / 100));
    String selectItemsSql = "SELECT product_id, price FROM product";
    String updatingItemsSql = "UPDATE product SET price = ? WHERE product_id = ?";
    
    try (PreparedStatement selectStmt = connection.prepareStatement(selectItemsSql);
         PreparedStatement updateStmt = connection.prepareStatement(updatingItemsSql)) {

      connection.setAutoCommit(false);
      ResultSet selectRs = selectStmt.executeQuery();
    
      while (selectRs.next()) {
        int productId = selectRs.getInt("product_id");
        double originalPrice = selectRs.getDouble("price");
        double discountedPrice = originalPrice * discountFactor;

        discountedPrice = Math.round(discountedPrice * 100.0) / 100.0;

        updateStmt.setDouble(1, discountedPrice);
        updateStmt.setInt(2, productId);
        updateStmt.addBatch();
      }

      updateStmt.executeBatch();
      connection.commit();
    
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
      e.printStackTrace();
    } finally {
      try {
        connection.setAutoCommit(true);
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
