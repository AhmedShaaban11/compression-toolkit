package com.ahmed.compression.techniques.info.lossy.vectorquantization;

import com.ahmed.compression.techniques.info.CompressionInfo;

import java.awt.image.Raster;
import java.util.ArrayList;

public record VectorQuantizationCompressionInfo(Raster raster, int vecWidth, int vecHeight, ArrayList<Vector> vectors, ArrayList<Vector> codebooks) implements CompressionInfo {
  public boolean isGrey() {
    return raster.getNumBands() == 1;
  }
}
