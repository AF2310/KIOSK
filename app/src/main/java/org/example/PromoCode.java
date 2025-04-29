package org.example;
import java.time.LocalDate;

public class PromoCode {
  
  private String code;
  private float discountAmount;
  private boolean percentage;
  private LocalDate expiryDate;

  public PromoCode(String code, float discountAmount, boolean percentage, LocalDate expiryDate) {
    this.code = code;
    this.discountAmount = discountAmount;
    this.percentage = percentage;
    this.expiryDate = expiryDate;

  }

  public String getCode() {
    return code;
  }

  public float getDiscountAmount() {
    return discountAmount;
  }

  public boolean isPercentage() {
    return percentage;
  }
  public boolean isValid() {
    return LocalDate.now().isBefore(expiryDate);
  }

  //public  PromoCodeManager findPromoCode(String inputCode)
}
