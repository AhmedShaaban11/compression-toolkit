package com.ahmed.compression.toolkit.info.lossy.twodprediction;

import com.ahmed.compression.toolkit.info.CompressedFileInfo;

public class TwoDPredictionCompressedFileInfo implements CompressedFileInfo {
  private boolean isGrey;
  private int imgWidth;
  private int imgHeight;
  private int quantizationBits;
  private int[][] prediction;

  public TwoDPredictionCompressedFileInfo(boolean isGrey, int imgWidth, int imgHeight, int quantizationBits, int[][] prediction) {
    this.isGrey = isGrey;
    this.imgWidth = imgWidth;
    this.imgHeight = imgHeight;
    this.quantizationBits = quantizationBits;
    this.prediction = prediction;
  }

  public boolean isGrey() {
    return isGrey;
  }

  public int imgWidth() {
    return imgWidth;
  }

  public int imgHeight() {
    return imgHeight;
  }

  public int quantizationBits() {
    return quantizationBits;
  }

  public int[][] prediction() {
    return prediction;
  }
}
