package com.ahmed.compression.techniques;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppApplication extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(AppApplication.class.getResource("views/view.fxml"));
    Scene scene = null;
    try {
      scene = new Scene(fxmlLoader.load(), 320, 240);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    stage.setTitle("Compression Techniques");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}