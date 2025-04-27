package org.example.menu;

public class SimpleItem {
  private final String name;
  private final String imagePath;
  private final double price;

  public SimpleItem(String name, String imagePath, double price) {
      this.name = name;
      this.imagePath = imagePath;
      this.price = price;
  }

  public String name() { return name; }
  public String imagePath() { return imagePath; }
  public double price() { return price; }
}

