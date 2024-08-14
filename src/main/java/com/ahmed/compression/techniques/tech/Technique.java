package com.ahmed.compression.techniques.tech;

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
  void decompress(String inputPath, String outputPath);

  default Map<String, Serializable> getProps() {
    return Map.of();
  }
}
