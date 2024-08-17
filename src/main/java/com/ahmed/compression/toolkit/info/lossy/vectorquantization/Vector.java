package com.ahmed.compression.toolkit.info.lossy.vectorquantization;

public class Vector {
  private final int[] pixels;
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

  public int label() {
    return label;
  }

  public int pixel(int idx) {
    return pixels[idx];
  }

  public int[] pixels() { return pixels; }

  public int distanceTo(Vector v) {
    int sum = 0;
    for (int i = 0; i < pixels.length; ++i) {
      sum += Math.abs(pixels[i] - v.pixel(i));
    }
    return sum;
  }

  public int size() { return pixels.length; }

  public void shiftUp() {
    for (int i = 0; i < pixels.length; ++i) {
      pixels[i] = Math.min(pixels[i] + 1, 255);
    }
  }
}
