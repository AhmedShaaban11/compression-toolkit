package com.ahmed.compression.techniques.information.lossy.vectorquantization;

import com.ahmed.compression.techniques.information.Info;

import java.awt.image.Raster;
import java.util.ArrayList;

public record VectorQuantizationCompressionInfo(Raster raster, int vecWidth, int vecHeight, ArrayList<Vector> vectors, ArrayList<Vector> codebooks) implements Info {
  public boolean isGrey() {
    return raster.getNumBands() == 1;
  }
}
