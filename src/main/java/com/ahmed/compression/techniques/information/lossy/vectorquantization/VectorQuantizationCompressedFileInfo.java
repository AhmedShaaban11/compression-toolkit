package com.ahmed.compression.techniques.information.lossy.vectorquantization;

import com.ahmed.compression.techniques.information.Info;

import java.util.ArrayList;

public record VectorQuantizationCompressedFileInfo(int vecWidth, int vecHeight, int imgWidth, int imgHeight, boolean isGrey, ArrayList<Vector> codebooks, ArrayList<Integer> labels) implements Info {

}