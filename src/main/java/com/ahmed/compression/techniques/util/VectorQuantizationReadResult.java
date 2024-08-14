package com.ahmed.compression.techniques.util;

import com.ahmed.compression.techniques.tech.lossy.vectorquantization.Vector;

import java.util.ArrayList;

public class VectorQuantizationReadResult {
  private ArrayList<Vector> codebooks;
  private ArrayList<Integer> labels;
  private int vecWidth;
  private int vecHeight;
  private int imgWidth;
  private int imgHeight;
  private boolean isGrey;
  public VectorQuantizationReadResult(ArrayList<Vector> codebooks, ArrayList<Integer> labels, int vecWidth, int vecHeight, int imgWidth, int imgHeight, boolean isGrey) {
    this.codebooks = codebooks;
    this.labels = labels;
    this.vecWidth = vecWidth;
    this.vecHeight = vecHeight;
    this.imgWidth = imgWidth;
    this.imgHeight = imgHeight;
    this.isGrey = isGrey;
  }

  public ArrayList<Vector> getCodebooks() { return codebooks; }

  public ArrayList<Integer> getLabels() { return labels; }

  public int getVecWidth() { return vecWidth; }

  public int getVecHeight() { return vecHeight; }

  public int getImgWidth() { return imgWidth; }

  public int getImgHeight() { return imgHeight; }

  public boolean isGrey() { return isGrey; }

}
