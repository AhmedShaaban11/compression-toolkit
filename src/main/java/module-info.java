module com.ahmed {
  requires javafx.controls;
  requires javafx.fxml;


  opens com.ahmed.compression.techniques to javafx.fxml;
  opens com.ahmed.compression.techniques.util to javafx.fxml;
  opens com.ahmed.compression.techniques.tech to javafx.fxml;
  exports com.ahmed.compression.techniques;
  exports com.ahmed.compression.techniques.util;
  exports com.ahmed.compression.techniques.tech;
}