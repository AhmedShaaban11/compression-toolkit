package com.ahmed.compression.techniques.tech;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public interface Technique {
  default void compress(String inputPath, String outputPath) {
    compress(inputPath, outputPath, new HashMap<>());
  }

  default void compress(String inputPath, String outputPath, Map<String, Object> props) {
    compress(inputPath, outputPath);
  }

  default void compress(BufferedImage img, String inputPath, String outputPath, Map<String, Object> props) {
    compress(inputPath, outputPath, props);
  }

  void decompress(String inputPath, String outputPath);

  default Map<String, Serializable> getProps() {
    return Map.of();
  }
}
