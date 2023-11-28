package com.ahmed.compression.techniques.tech;

public interface Technique {
  void compress(String inputPath, String outputPath);
  void decompress(String inputPath, String outputPath);
}
