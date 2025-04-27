package org.example.menu;

public class SimpleItem {
  private String name;
  private String imagePath;
  private double price;

  public SimpleItem(String name, String imagePath, double price) {
      this.name = name;
      this.imagePath = imagePath;
      this.price = price;
  }

  public String name() { return name; }
  public String imagePath() { return imagePath; }
  public double price() { return price; }
}

