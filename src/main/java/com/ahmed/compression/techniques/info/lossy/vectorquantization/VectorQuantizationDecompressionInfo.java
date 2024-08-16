package com.ahmed.compression.techniques.info.lossy.vectorquantization;

import com.ahmed.compression.techniques.info.DecompressionInfo;

import java.awt.image.BufferedImage;

public record VectorQuantizationDecompressionInfo(BufferedImage img) implements DecompressionInfo {
}
