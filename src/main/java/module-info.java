module com.ahmed {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;


  opens com.ahmed.compression.techniques to javafx.fxml;
  opens com.ahmed.compression.techniques.io to javafx.fxml;
  opens com.ahmed.compression.techniques.tech to javafx.fxml;
  opens com.ahmed.compression.techniques.tech.lossless to javafx.fxml;
  opens com.ahmed.compression.techniques.tech.lossy to javafx.fxml;
  exports com.ahmed.compression.techniques;
  exports com.ahmed.compression.techniques.io;
  exports com.ahmed.compression.techniques.tech;
  exports com.ahmed.compression.techniques.tech.lossless;
  exports com.ahmed.compression.techniques.tech.lossy;
  exports com.ahmed.compression.techniques.information;
  exports com.ahmed.compression.techniques.information.lossless.lz77;
  exports com.ahmed.compression.techniques.information.lossless.lzw;
  exports com.ahmed.compression.techniques.information.lossless.standardhuffman;
  exports com.ahmed.compression.techniques.information.lossy.vectorquantization;
  exports com.ahmed.compression.techniques.information.lossy.twodprediction;
  exports com.ahmed.compression.techniques.io.lossless;
  opens com.ahmed.compression.techniques.io.lossless to javafx.fxml;
  exports com.ahmed.compression.techniques.io.lossy;
  opens com.ahmed.compression.techniques.io.lossy to javafx.fxml;
  exports com.ahmed.compression.techniques.facade;
  opens com.ahmed.compression.techniques.facade to javafx.fxml;
}
