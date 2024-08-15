package com.ahmed.compression.techniques.information.lossy.vectorquantization;

import com.ahmed.compression.techniques.information.Info;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public record VectorQuantizationDecompressionInfo(BufferedImage img) implements Info {
}
