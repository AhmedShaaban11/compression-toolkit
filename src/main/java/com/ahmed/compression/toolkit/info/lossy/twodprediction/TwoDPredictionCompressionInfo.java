package com.ahmed.compression.toolkit.info.lossy.twodprediction;

import com.ahmed.compression.toolkit.info.CompressionInfo;

public record TwoDPredictionCompressionInfo(boolean isGrey, int imgWidth, int imgHeight, int quantizationBits, int[][] prediction) implements CompressionInfo {
}
