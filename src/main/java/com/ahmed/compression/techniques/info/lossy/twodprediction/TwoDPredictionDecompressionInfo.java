package com.ahmed.compression.techniques.info.lossy.twodprediction;

import com.ahmed.compression.techniques.info.DecompressionInfo;

import java.awt.image.BufferedImage;

public record TwoDPredictionDecompressionInfo(BufferedImage img) implements DecompressionInfo {
}
