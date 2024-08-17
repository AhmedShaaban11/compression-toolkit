package com.ahmed.compression.toolkit;

import com.ahmed.compression.toolkit.facade.Facade;
import com.ahmed.compression.toolkit.facade.FacadeFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class AppController {
  @FXML
  public ChoiceBox<?> choiceBox;

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
