package com.ahmed.compression.toolkit.info.lossy.vectorquantization;

import com.ahmed.compression.toolkit.info.DecompressionInfo;

import java.awt.image.BufferedImage;

public record VectorQuantizationDecompressionInfo(BufferedImage img) implements DecompressionInfo {
}
