package com.ahmed.compression.techniques.tech.lossy.vectorquantization;

public class Vector {
  private int[] pixels;
  private int label;

  public Vector(int[] pixels) {
    this.pixels = pixels;
    this.label = -1;
  }

  public Vector(Vector vec) {
    this.pixels = vec.pixels.clone();
    this.label = vec.label;
  }

  public void setLabel(int label) {
    this.label = label;
  }

  public int getLabel() {
    return label;
  }

  public int getPixel(int idx) {
    return pixels[idx];
  }

  public int[] getPixels() { return pixels; }

  public int distanceTo(Vector v) {
    int sum = 0;
    for (int i = 0; i < pixels.length; ++i) {
      sum += Math.abs(pixels[i] - v.getPixel(i));
    }
    return sum;
  }

  public int getSize() { return pixels.length; }

  public void shiftUp() {
    for (int i = 0; i < pixels.length; ++i) {
      pixels[i] = Math.min(pixels[i] + 1, 255);
    }
  }
}
