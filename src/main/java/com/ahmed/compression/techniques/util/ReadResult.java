package com.ahmed.compression.techniques.util;

import com.ahmed.compression.techniques.tech.lossy.Vector;

import java.util.ArrayList;

public class ReadResult {
  private ArrayList<Vector> codebooks;
  private ArrayList<Integer> labels;
  private int vecWidth;
  private int vecHeight;
  private int imgWidth;
  private int imgHeight;
  public ReadResult(ArrayList<Vector> codebooks, ArrayList<Integer> labels, int vecWidth, int vecHeight, int imgWidth, int imgHeight) {
    this.codebooks = codebooks;
    this.labels = labels;
    this.vecWidth = vecWidth;
    this.vecHeight = vecHeight;
    this.imgWidth = imgWidth;
    this.imgHeight = imgHeight;
  }

  public ArrayList<Vector> getCodebooks() { return codebooks; }

  public ArrayList<Integer> getLabels() { return labels; }

  public int getVecWidth() { return vecWidth; }

  public int getVecHeight() { return vecHeight; }

  public int getImgWidth() { return imgWidth; }

  public int getImgHeight() { return imgHeight; }

}
