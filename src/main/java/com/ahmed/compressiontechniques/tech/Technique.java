package com.ahmed.compressiontechniques.tech;

public interface Technique {
  void compress(String inputPath, String outputPath);
  void decompress(String inputPath, String outputPath);
}
