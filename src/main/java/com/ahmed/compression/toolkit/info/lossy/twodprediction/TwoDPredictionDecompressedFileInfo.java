package com.ahmed.compression.toolkit.info.lossy.twodprediction;

import com.ahmed.compression.toolkit.info.DecompressedFileInfo;

import java.awt.image.Raster;

public record TwoDPredictionDecompressedFileInfo(Raster raster, int colorBytes) implements DecompressedFileInfo {
}
