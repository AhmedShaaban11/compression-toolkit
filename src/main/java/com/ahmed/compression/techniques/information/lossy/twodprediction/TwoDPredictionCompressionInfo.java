package com.ahmed.compression.techniques.information.lossy.twodprediction;

import com.ahmed.compression.techniques.information.Info;

public record TwoDPredictionCompressionInfo(boolean isGrey, int imgWidth, int imgHeight, int quantizationBits, int[][] prediction) implements Info {
}
