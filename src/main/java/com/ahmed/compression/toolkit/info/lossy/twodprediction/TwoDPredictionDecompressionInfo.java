package com.ahmed.compression.toolkit.info.lossy.twodprediction;

import com.ahmed.compression.toolkit.info.DecompressionInfo;

import java.awt.image.BufferedImage;

public record TwoDPredictionDecompressionInfo(BufferedImage img) implements DecompressionInfo {
}
