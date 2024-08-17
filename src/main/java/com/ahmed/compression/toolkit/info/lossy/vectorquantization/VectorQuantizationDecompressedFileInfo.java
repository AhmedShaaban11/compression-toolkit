package com.ahmed.compression.toolkit.info.lossy.vectorquantization;

import com.ahmed.compression.toolkit.info.DecompressedFileInfo;

import java.awt.image.Raster;

public record VectorQuantizationDecompressedFileInfo(Raster raster) implements DecompressedFileInfo {
  public boolean isGrey() {
    return raster.getNumBands() == 1;
  }
}
