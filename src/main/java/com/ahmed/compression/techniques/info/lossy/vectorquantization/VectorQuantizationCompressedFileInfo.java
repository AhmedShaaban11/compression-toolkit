package com.ahmed.compression.techniques.info.lossy.vectorquantization;

import com.ahmed.compression.techniques.info.CompressedFileInfo;

import java.util.ArrayList;

public record VectorQuantizationCompressedFileInfo(int vecWidth, int vecHeight, int imgWidth, int imgHeight, boolean isGrey, ArrayList<Vector> codebooks, ArrayList<Integer> labels) implements CompressedFileInfo {

}