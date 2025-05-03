package org.example.sql;

import java.time.LocalDate;

/**
 * This is for retrieving customer data like customer id etc..
 * TODO: add insert and retrieval queries; replace dummy code
 */
public class CustomerData {
  
  private int customerId;
  private String customerName;
  private String customerEmail;
  private String customerPhone;
  private Object registrationDat;
  private int loyaltyPoints;

  /**
   * This is the constructor of the customer data
   * retrvieval class.
   */
  public CustomerData() {}


  public void setId(int customerId) {
    this.customerId = customerId;
  }
  
  public int getId() {
    return customerId;
  }


  public void setName(String customerName) {
    this.customerName = customerName.strip();
  }

  public String getName() {
    return customerName;
  }


  public void setEmail(String customerEmail) {
    this.customerEmail = customerEmail.strip();
  }

  public String getEmail() {
    return customerEmail;
  }


  /**
   * Customer phone number setter method.
   *
   * @param customerPhone Customer phone number
   *                      length 20 characters max.
   */
  public void setPhone(String customerPhone) {
    if (customerPhone.strip().length() > 20) {
      System.out.println(
          "ERROR: Customer Phone number too long!\n"
          + "Phone number will be set to 000000000000"
      );
      this.customerPhone = "000000000000";

    } else {
      this.customerPhone = customerPhone.strip();
    }
  }

  public String getPhone() {
    return customerPhone;
  }


  /**
   * Setter method for current date of registration.
   */
  public void setRegistrationDate() {
    LocalDate date = LocalDate.now();
    this.registrationDat = date;
  }

  public Object getRegistrationDate() {
    return registrationDat;
  }


  public void setLoyaltyPoints(int loyaltyPoints) {
    this.loyaltyPoints = loyaltyPoints;
  }
  
  public int getLoyaltyPoints() {
    return loyaltyPoints;
  }
}
