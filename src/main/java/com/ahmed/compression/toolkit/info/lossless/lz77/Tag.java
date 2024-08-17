package com.ahmed.compression.toolkit.info.lossless.lz77;

public record Tag(int position, int length, char nextChar) {
  @Override
  public String toString() {
    return "<" + position + "," + length + "," + nextChar + ">";
  }
}