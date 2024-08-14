package com.ahmed.compression.techniques;

import com.ahmed.compression.techniques.tech.Technique;
import com.ahmed.compression.techniques.tech.TechniqueFactory;
import com.ahmed.compression.techniques.tech.lossy.vectorquantization.VectorQuantization;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AppController {
  @FXML
  public ChoiceBox choiceBox;

  @FXML
  public TextField vecWidth;

  @FXML
  public TextField vecHeight;

  @FXML
  public TextField codebooksLevels;

  @FXML
  public FlowPane vectorQuantizationPane;

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
  public void choiceBoxAction() {
    if (choiceBox.getValue().toString().equalsIgnoreCase("Vector Quantization")) {
      vectorQuantizationPane.setVisible(true);
    } else {
      vectorQuantizationPane.setVisible(false);
    }
  }

  @FXML
  public void compressButtonAction() {
    Path inputPath = chooseFile("Select Input File for Compression", false);
    Path outputPath = chooseFile("Select Output File for Compression", true);
    Technique tech = getCurrentTechnique();
    if (tech instanceof VectorQuantization vectorQuantization) {
      int vecWidth = Integer.parseInt(this.vecWidth.getText());
      int vecHeight = Integer.parseInt(this.vecHeight.getText());
      int codebooksLevels = Integer.parseInt(this.codebooksLevels.getText());
      Map<String, Object> props = new HashMap<>();
      props.put("vecWidth", vecWidth);
      props.put("vecHeight", vecHeight);
      props.put("codebooksLevels", codebooksLevels);
      vectorQuantization.compress(inputPath.toString(), outputPath.toString(), props);
      return;
    }
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
