package com.ahmed.compression.techniques.info.lossy.twodprediction;

import com.ahmed.compression.techniques.info.DecompressedFileInfo;

import java.awt.image.Raster;

public record TwoDPredictionDecompressedFileInfo(Raster raster, int colorBytes) implements DecompressedFileInfo {
}
