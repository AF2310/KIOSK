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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Screen for the details of an Item.
 * Customer should be able to adjust an Item here
 * (add, remove ingredients and such)
 */
public class ItemDetails {

  public Scene create(Stage primaryStage, Scene prevScene, String name, String imagePath) {

    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(20));

    VBox content = new VBox(20);
    content.setAlignment(Pos.CENTER);

    InputStream inputStream = getClass().getResourceAsStream(imagePath);

    if (inputStream == null) {

      System.err.println("Image not found - " + imagePath);

    }

    ImageView imageView = new ImageView(new Image(inputStream));
    imageView.setFitHeight(200);
    imageView.setPreserveRatio(true);

    Label nameLabel = new Label(name);
    nameLabel.setStyle(
      "-fx-font-size: 20px;"
      + "-fx-font-weight: bold;"
    );

    Button backButton = new Button("Return");
    backButton.setOnAction(e -> primaryStage.setScene(prevScene));

    content.getChildren().addAll(imageView, nameLabel, backButton);
    layout.setCenter(content);

    return new Scene(layout, 1920, 1080);

  }
    
}
