package com.ahmed.compression.techniques;

import com.ahmed.compression.techniques.facade.Facade;
import com.ahmed.compression.techniques.facade.FacadeFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class AppController {
  @FXML
  public ChoiceBox<?> choiceBox;

  @FXML
  public TextField vecWidth;

  @FXML
  public TextField vecHeight;

  @FXML
  public TextField codebooksLevels;

  @FXML
  public FlowPane vectorQuantizationPane;

  private Facade<?, ?, ?, ?, ?, ?> getCurrentFacade() {
    String techniqueName = choiceBox.getValue().toString();
    FacadeFactory facadeFactory = new FacadeFactory();
    return facadeFactory.getFacade(techniqueName);
  }

  private Path chooseFile(String title, boolean isSave) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(title);
    File file = isSave ? fileChooser.showSaveDialog(new Stage()) : fileChooser.showOpenDialog(new Stage());
    return file.toPath();
  }

  @FXML
  public void choiceBoxAction() {
    boolean isChoiceOnVectorQuantization = choiceBox.getValue().toString().equalsIgnoreCase("Vector Quantization");
//    vectorQuantizationPane.setVisible(isChoiceOnVectorQuantization); // TODO: Change visibility
    vectorQuantizationPane.setVisible(false);
  }

  @FXML
  public void compressButtonAction() {
    Path inputPath = chooseFile("Select Input File for Compression", false);
    Path outputPath = chooseFile("Select Output File for Compression", true);
    Facade<?, ?, ?, ?, ?, ?> facade = getCurrentFacade();
    facade.compress(inputPath, outputPath);
  }

  @FXML
  public void decompressButtonAction() {
    Path inputPath = chooseFile("Select Input File for Decompression", false);
    Path outputPath = chooseFile("Select Output File for Decompression", true);
    Facade<?, ?, ?, ?, ?, ?> facade = getCurrentFacade();
    facade.decompress(inputPath, outputPath);
  }
}
