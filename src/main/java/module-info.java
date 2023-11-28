module com.ahmed {
  requires javafx.controls;
  requires javafx.fxml;


  opens com.ahmed.compressiontechniques to javafx.fxml;
  opens com.ahmed.compressiontechniques.util to javafx.fxml;
  opens com.ahmed.compressiontechniques.tech to javafx.fxml;
  exports com.ahmed.compressiontechniques;
  exports com.ahmed.compressiontechniques.util;
  exports com.ahmed.compressiontechniques.tech;
}