package com.ahmed.compression.techniques.tech.lossy.prediction;

public class CompressResult {
  private boolean isGrey;
  private int width;
  private int height;
  private int quantizationBits;
  private int[][] prediction;

  public CompressResult(boolean isGrey, int width, int height, int quantizationBits, int[][] prediction) {
    this.isGrey = isGrey;
    this.width = width;
    this.height = height;
    this.quantizationBits = quantizationBits;
    this.prediction = prediction;
  }

  public boolean isGrey() {
    return isGrey;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getQuantizationBits() {
    return quantizationBits;
  }

  public int[][] getPrediction() {
    return prediction;
  }
}
