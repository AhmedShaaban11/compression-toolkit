package com.ahmed.compression.techniques.info.lossy.twodprediction;

import com.ahmed.compression.techniques.info.CompressionInfo;

public record TwoDPredictionCompressionInfo(boolean isGrey, int imgWidth, int imgHeight, int quantizationBits, int[][] prediction) implements CompressionInfo {
}
