package org.example;

import java.sql.Connection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class DatabaseTest {
    public static void main(String[] args) {
        Application.launch(TestApp.class, args); // Need to start JavaFX "Toolkit" to be able to run the test
    }
    
    public static class TestApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            SqlConnectionCheck connectionCheck = new SqlConnectionCheck();
            Connection connection = connectionCheck.getConnection();
            
            if (connection != null) {
                System.out.println("Database connection successful!");
                TestingQueries queries = new TestingQueries(connection);
                boolean result = queries.insertAdmin("TEST", "TEST!");
                System.out.println("Insert result: " + result);
            } else {
                System.out.println("Connection failed");
            }
            
            // Exit after test
            Platform.exit();
        }
    }
}