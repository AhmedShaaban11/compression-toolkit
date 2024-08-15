package com.ahmed.compression.techniques.information.lossy.twodprediction;

import com.ahmed.compression.techniques.information.Info;

import java.awt.image.Raster;

public record TwoDPredictionDecompressedFileInfo(Raster raster, int colorBytes) implements Info {
}
