module com.ahmed {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;


  opens com.ahmed.compression.techniques to javafx.fxml;
  opens com.ahmed.compression.techniques.util to javafx.fxml;
  opens com.ahmed.compression.techniques.tech to javafx.fxml;
  opens com.ahmed.compression.techniques.tech.lossless to javafx.fxml;
  opens com.ahmed.compression.techniques.tech.lossy to javafx.fxml;
  opens com.ahmed.compression.techniques.tech.prediction to javafx.fxml;
  exports com.ahmed.compression.techniques;
  exports com.ahmed.compression.techniques.util;
  exports com.ahmed.compression.techniques.tech;
  exports com.ahmed.compression.techniques.tech.lossless;
  exports com.ahmed.compression.techniques.tech.lossy;
  exports com.ahmed.compression.techniques.tech.prediction;
  exports com.ahmed.compression.techniques.tech.lossy.vectorquantization;
  opens com.ahmed.compression.techniques.tech.lossy.vectorquantization to javafx.fxml;
}