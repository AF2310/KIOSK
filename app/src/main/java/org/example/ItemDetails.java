package org.example;

import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Screen for the details of an Item.
 * Customer should be able to adjust an Item here
 * (add, remove ingredients and such)
 */
public class ItemDetails {

  /**
   * Creating a scene for a specific item, displaying all item details.
   *
   * @param primaryStage what is the primary stage
   * @param prevScene what was the previous stage
   * @param name name os the item
   * @param imagePath path to the item's image
   * @return scene containing all item details
   */
  public Scene create(Stage primaryStage, Scene prevScene, String name, String imagePath) {

    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(20));

    HBox symbols = new HBox();
    symbols.setPadding(new Insets(20));
    symbols.setSpacing(10);
    CircleButtonWithSign plusbtn = new CircleButtonWithSign("+");
    CircleButtonWithSign plusbtn2 = new CircleButtonWithSign("+");
    CircleButtonWithSign minusbtn = new CircleButtonWithSign("-");
    CircleButtonWithSign minusbtn2 = new CircleButtonWithSign("-");
    minusbtn2.setInvalid(true);
    plusbtn2.setInvalid(true);
    symbols.getChildren().addAll(minusbtn, minusbtn2, plusbtn, plusbtn2);

    VBox content = new VBox(20);
    content.setAlignment(Pos.CENTER);

    InputStream inputStream = getClass().getResourceAsStream(imagePath);

    // Initiating the image view
    ImageView imageView;

    // Errorhandling when no image found
    if (inputStream == null) {
      System.err.println("ERROR: Image not found - " + imagePath);

      // using Base64-encoded string to generate 1x1 transparent PNG
      // This Base64 string is decoded into a transparent image when
      // the Image constructor is called.
      // This prevents fetching some empty image from the database.
      Image emptyImage = new Image(
          "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABC"
          + "AQAAAC1HAwCAAAAC0lEQVR42mP8/wcAAwAB/hd5JnkAAAAASUVORK5CYII="
        );

      // Add empty generated image to View
      imageView = new ImageView(emptyImage);

    // Image found (input stream not empty)
    } else {
      imageView = new ImageView(new Image(inputStream));
    }

    imageView.setFitHeight(200);
    imageView.setPreserveRatio(true);

    Label nameLabel = new Label(name);
    nameLabel.setStyle(
        "-fx-font-size: 20px;"
        + "-fx-font-weight: bold;"
    );

    Button backButton = new Button("Return");
    backButton.setOnAction(e -> primaryStage.setScene(prevScene));

    content.getChildren().addAll(symbols, imageView, nameLabel, backButton);
    layout.setCenter(content);

    return new Scene(layout, 1920, 1080);

  }
}
