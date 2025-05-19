package org.example.buttons;

/**
 * Class for changing the name of the kiosk.
 */
public class KioskName {
  
  private static String companyTitle = "Bun & Patty";
  
  /**
   * Getter for the current name.
   *
   * @return the name
   */
  public static String getCompanyTitle() {
    
    return companyTitle;
    
  }
  
  /**
   * Sets the new title.
   *
   * @param newTitle the new title from the customization screen
   */
  public static void setCompanyTitle(String newTitle) {
    
    companyTitle = newTitle;
    
  }
  
}
