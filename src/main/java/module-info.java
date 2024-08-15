module com.ahmed {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;


  opens com.ahmed.compression.techniques to javafx.fxml;
  opens com.ahmed.compression.techniques.io to javafx.fxml;
  opens com.ahmed.compression.techniques.tech to javafx.fxml;
  opens com.ahmed.compression.techniques.tech.lossless to javafx.fxml;
  opens com.ahmed.compression.techniques.tech.lossy.prediction to javafx.fxml;
  opens com.ahmed.compression.techniques.tech.lossy.vectorquantization to javafx.fxml;
  exports com.ahmed.compression.techniques;
  exports com.ahmed.compression.techniques.io;
  exports com.ahmed.compression.techniques.tech;
  exports com.ahmed.compression.techniques.tech.lossless;
  exports com.ahmed.compression.techniques.tech.lossy.prediction;
  exports com.ahmed.compression.techniques.tech.lossy.vectorquantization;
  exports com.ahmed.compression.techniques.information;
  exports com.ahmed.compression.techniques.information.lz77;
  exports com.ahmed.compression.techniques.information.lzw;
  exports com.ahmed.compression.techniques.information.standardhuffman;
}
