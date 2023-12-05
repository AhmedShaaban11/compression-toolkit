package com.ahmed.compression.techniques.tech.lossy;

import java.util.ArrayList;

public class CompressResult {
  private ArrayList<Vector> vectors;
  private ArrayList<Vector> codebooks;

  public CompressResult(ArrayList<Vector> vectors, ArrayList<Vector> codebooks) {
    this.vectors = vectors;
    this.codebooks = codebooks;
  }

  public ArrayList<Vector> getVectors() { return vectors; }

  public ArrayList<Vector> getCodebooks() { return codebooks; }
}
