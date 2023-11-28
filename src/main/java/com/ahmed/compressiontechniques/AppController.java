package com.ahmed.compressiontechniques;

import com.ahmed.compressiontechniques.tech.Technique;
import com.ahmed.compressiontechniques.tech.TechniqueFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class AppController {
  @FXML
  public ChoiceBox choiceBox;

  private Technique getCurrentTechnique() {
    String techniqueName = choiceBox.getValue().toString();
    TechniqueFactory techniqueFactory = new TechniqueFactory();
    return techniqueFactory.createTechnique(techniqueName);
  }

  private Path chooseFile(String title, boolean isSave) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(title);
    File file = isSave ? fileChooser.showSaveDialog(new Stage()) : fileChooser.showOpenDialog(new Stage());
    return file.toPath();
  }

  @FXML
  public void compressButtonAction() {
    Path inputPath = chooseFile("Select Input File for Compression", false);
    Path outputPath = chooseFile("Select Output File for Compression", true);
    Technique tech = getCurrentTechnique();
    tech.compress(inputPath.toString(), outputPath.toString());
  }

  @FXML
  public void decompressButtonAction() {
    Path inputPath = chooseFile("Select Input File for Decompression", false);
    Path outputPath = chooseFile("Select Output File for Decompression", true);
    Technique tech = getCurrentTechnique();
    tech.decompress(inputPath.toString(), outputPath.toString());
  }
}
