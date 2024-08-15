package com.ahmed.compression.techniques.io;

import com.ahmed.compression.techniques.tech.lossy.prediction.CompressResult;

import java.nio.file.Path;

public class TwoDPredictionFile extends BinaryFile {
  public void write(String outPath, CompressResult compressResult) {
    StringBuilder bits = new StringBuilder();
    bits.append(compressResult.isGrey() ? "1" : "0");
    bits.append(intToBinaryString(compressResult.getWidth(), 16));
    bits.append(intToBinaryString(compressResult.getHeight(), 16));
    bits.append(intToBinaryString(compressResult.getQuantizationBits(), 4));
    int bytesPerPixel = compressResult.isGrey() ? 1 : 3;
    // Write the first row of the prediction matrix
    for (int i = 0; i < compressResult.getWidth() * bytesPerPixel; ++i) {
      bits.append(intToBinaryString(compressResult.getPrediction()[0][i], 8));
    }
    // Write the first column of the prediction matrix
    for (int i = 0; i < compressResult.getHeight(); ++i) {
      bits.append(intToBinaryString(compressResult.getPrediction()[i][0], 8));
    }
    // Write the rest of the prediction matrix
    for (int i = 1; i < compressResult.getHeight(); i++) {
      for (int j = 1; j < compressResult.getWidth() * bytesPerPixel; j++) {
        bits.append(intToBinaryString(compressResult.getPrediction()[i][j], compressResult.getQuantizationBits()));
      }
    }
    byte[] bytes = convertBitsToBytes(bits.toString());
    writeBinaryFile(Path.of(outPath), bytes);
  }

  public CompressResult read(String inPath) {
    byte[] bytes = readBinaryFile(Path.of(inPath));
    String bits = convertBytesToBits(bytes);
    int bitsIdx = 0;
    boolean isGrey = Integer.parseInt(bits.substring(bitsIdx, bitsIdx += 1), 2) == 1;
    int width = Integer.parseInt(bits.substring(bitsIdx, bitsIdx += 16), 2);
    int height = Integer.parseInt(bits.substring(bitsIdx, bitsIdx += 16), 2);
    int quantizationBitsCount = Integer.parseInt(bits.substring(bitsIdx, bitsIdx += 4), 2);
    int[][] prediction = new int[height][width * (isGrey ? 1 : 3)];
    int bytesPerPixel = isGrey ? 1 : 3;
    // Read the first row of the prediction matrix
    for (int i = 0; i < width * bytesPerPixel; ++i) {
      prediction[0][i] = Integer.parseInt(bits.substring(bitsIdx, bitsIdx += 8), 2);
    }
    // Read the first column of the prediction matrix
    for (int j = 0; j < height; ++j) {
      prediction[j][0] = Integer.parseInt(bits.substring(bitsIdx, bitsIdx += 8), 2);
    }
    // Read the rest of the prediction matrix
    for (int i = 1; i < height; i++) {
      for (int j = 1; j < width * bytesPerPixel; j++) {
        prediction[i][j] = Integer.parseInt(bits.substring(bitsIdx, bitsIdx += quantizationBitsCount), 2);
      }
    }
    return new CompressResult(isGrey, width, height, quantizationBitsCount, prediction);
  }

}
