package com.ahmed.compression.techniques.information.lossy.vectorquantization;

import com.ahmed.compression.techniques.information.Info;

import java.awt.image.Raster;

public record VectorQuantizationDecompressedFileInfo(Raster raster) implements Info {
  public boolean isGrey() {
    return raster.getNumBands() == 1;
  }
}
