module com.ahmed {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;


  opens com.ahmed.compression.toolkit to javafx.fxml;
  opens com.ahmed.compression.toolkit.io to javafx.fxml;
  opens com.ahmed.compression.toolkit.tech to javafx.fxml;
  opens com.ahmed.compression.toolkit.tech.lossless to javafx.fxml;
  opens com.ahmed.compression.toolkit.tech.lossy to javafx.fxml;
  exports com.ahmed.compression.toolkit;
  exports com.ahmed.compression.toolkit.io;
  exports com.ahmed.compression.toolkit.tech;
  exports com.ahmed.compression.toolkit.tech.lossless;
  exports com.ahmed.compression.toolkit.tech.lossy;
  exports com.ahmed.compression.toolkit.info;
  exports com.ahmed.compression.toolkit.info.lossless.lz77;
  exports com.ahmed.compression.toolkit.info.lossless.lzw;
  exports com.ahmed.compression.toolkit.info.lossless.standardhuffman;
  exports com.ahmed.compression.toolkit.info.lossy.vectorquantization;
  exports com.ahmed.compression.toolkit.info.lossy.twodprediction;
  exports com.ahmed.compression.toolkit.io.lossless;
  opens com.ahmed.compression.toolkit.io.lossless to javafx.fxml;
  exports com.ahmed.compression.toolkit.io.lossy;
  opens com.ahmed.compression.toolkit.io.lossy to javafx.fxml;
  exports com.ahmed.compression.toolkit.facade;
  opens com.ahmed.compression.toolkit.facade to javafx.fxml;
}
