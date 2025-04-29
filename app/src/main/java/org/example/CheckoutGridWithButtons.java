package org.example;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.menu.SimpleItem;

/**
 * The grid with the arrow buttons class.
 */
public class CheckoutGridWithButtons extends HBox {

  private GridPane itemGrid;
  private Label pageCounterLabel;
  private Button leftArrowButton;
  private Button rightArrowButton;
  private int itemsPerPage;
  private SimpleIntegerProperty currentPage;
  private SimpleItem[] items;
  private int[] quantitys;

  /**
   * Constructor for the CheckoutGridWithButtons instance.
   */
  public CheckoutGridWithButtons(SimpleItem[] items, int[] quantitys, int itemsPerPage,
      Button leftArrowButton, Button rightArrowButton) {
    this.items = items;
    this.quantitys = quantitys;
    this.itemsPerPage = itemsPerPage;
    this.leftArrowButton = leftArrowButton;
    this.rightArrowButton = rightArrowButton;
    this.currentPage = new SimpleIntegerProperty(0);
    this.itemGrid = new GridPane();
    this.pageCounterLabel = new Label();
    initialize();
  }

  // Initialize layout and functionality
  private void initialize() {
    // Setup HBox properties
    this.setAlignment(Pos.CENTER);
    this.setSpacing(20);

    // Setup GridPane for items display
    itemGrid.setHgap(20);
    itemGrid.setVgap(20);
    itemGrid.setAlignment(Pos.CENTER);

    // Setup page counter label
    pageCounterLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");

    // Setup actions for the left navigation button
    leftArrowButton.setOnAction(e -> {
      if (currentPage.get() > 0) {
        currentPage.set(currentPage.get() - 1);
        updateGrid();
      }
    });

    // And the right navigation button
    rightArrowButton.setOnAction(e -> {
      if ((currentPage.get() + 1) * itemsPerPage < items.length) {
        currentPage.set(currentPage.get() + 1);
        updateGrid();
      }
    });

    // Update the grid when created
    updateGrid();

    // Setup the layout
    VBox gradWithCounter = new VBox(20);
    gradWithCounter.setAlignment(Pos.CENTER);
    gradWithCounter.getChildren().addAll(pageCounterLabel, itemGrid);

    // Create spacers for arrows positioning
    Region leftSpacer = new Region();
    Region rightSpacer = new Region();

    HBox.setHgrow(leftSpacer, Priority.ALWAYS);
    HBox.setHgrow(rightSpacer, Priority.ALWAYS);

    // Add everything to HBox
    this.getChildren().addAll(
        leftArrowButton, leftSpacer,
        gradWithCounter,
        rightSpacer, rightArrowButton);
  }

  // Update grid content based on the current page
  private void updateGrid() {
    itemGrid.getChildren().clear(); // Clear previous items

    int pageStartIndex = currentPage.get() * itemsPerPage;
    int pageEndIndex = Math.min(pageStartIndex + itemsPerPage, items.length);

    // Populate the grid with the items
    for (int i = pageStartIndex; i < pageEndIndex; i++) {
      SimpleItem item = items[i];
      Image itemImage = new Image(item.imagePath());
      ImageView image = new ImageView(itemImage);
      image.setFitHeight(200);
      image.setFitHeight(150);
      image.setPreserveRatio(true);

      // Slot for Label and Price
      HBox labelAndPrice = new HBox();
      labelAndPrice.setAlignment(Pos.CENTER);
      labelAndPrice.getChildren().addAll(
          new Label(item.name()),
          new Label(String.format(" %.0f :-", item.price())));

      // Slot for Plus-/Minus Buttons and Quantity value
      int quantity = quantitys[i];
      HBox quantityBox = new HBox();
      quantityBox.setAlignment(Pos.CENTER);
      quantityBox.getChildren().addAll(
          new AddRemoveBlock(quantity));

      // Adding it all together in one item slot
      VBox itemSlot = new VBox();
      itemSlot.setAlignment(Pos.CENTER);
      itemSlot.getChildren().addAll(
          image,
          labelAndPrice,
          quantityBox);

      // Compute column and row from index
      int column = (i - pageStartIndex) % 3;
      int row = (i - pageStartIndex) / 3;
      itemGrid.add(itemSlot, column, row);
    }

    // Add empty slots for grid shape
    int totalItems = pageEndIndex - pageStartIndex;
    for (int i = totalItems; i < itemsPerPage; i++) {
      StackPane emptySlot = new StackPane();
      emptySlot.setPrefSize(200, 200);
      emptySlot.setMaxSize(200, 200);
      emptySlot.setMinSize(200, 200);
      itemGrid.add(emptySlot, i % 3, i / 3);
    }

    // Update page count
    pageCounterLabel.setText(
        String.format("Page %d  of  %d",
            currentPage.get() + 1,
            (int) Math.ceil((double) items.length / itemsPerPage)));

    // Disable left button if on the first page
    if (currentPage.get() == 0) {
      leftArrowButton.setDisable(true);
      leftArrowButton.setOpacity(0.2);
    } else {
      leftArrowButton.setDisable(false);
      leftArrowButton.setOpacity(1);
    }

    // Disable right button if on the last page
    if (currentPage.get() == items.length / itemsPerPage) {
      rightArrowButton.setDisable(true);
      rightArrowButton.setOpacity(0.2);
    } else {
      rightArrowButton.setDisable(false);
      rightArrowButton.setOpacity(1);
    }
  }
}
