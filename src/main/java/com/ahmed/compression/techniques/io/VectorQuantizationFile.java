package com.ahmed.compression.techniques.io;

import com.ahmed.compression.techniques.tech.lossy.vectorquantization.Vector;

import java.nio.file.Path;
import java.util.ArrayList;
public class VectorQuantizationFile extends BinaryFile {
  private String writeCodeBooks(ArrayList<Vector> codebooks, int vecWidth, int vecHeight) {
    StringBuilder bits = new StringBuilder();
    bits.append(intToBinaryString(codebooks.size(), 16));
    bits.append(intToBinaryString(vecWidth, 16));
    bits.append(intToBinaryString(vecHeight, 16));
    for (Vector codebook : codebooks) {
      for (int i = 0; i < codebook.getSize(); ++i) {
        bits.append(intToBinaryString(codebook.getPixel(i), 8));
      }
    }
    return bits.toString();
  }

  private String writeImage(ArrayList<Vector> vectors, int imgWidth, int imgHeight, int codebooksDigitsCount) {
    StringBuilder bits = new StringBuilder();
    bits.append(intToBinaryString(imgWidth, 16));
    bits.append(intToBinaryString(imgHeight, 16));
    for (Vector vector : vectors) {
      String labelBits = intToBinaryString(vector.getLabel(), codebooksDigitsCount);
      bits.append(labelBits);
    }
    return bits.toString();
  }

  public void writeImg(String path, boolean isGrey, ArrayList<Vector> vectors, ArrayList<Vector> codebooks, int vecWidth, int vecHeight, int imgWidth, int imgHeight) {
    int codebooksDigitsCount = (int) Math.ceil(Math.log(codebooks.size()) / Math.log(2));
    String bits = "";
    bits += isGrey ? "1" : "0";
    bits += writeCodeBooks(codebooks, vecWidth, vecHeight);
    bits += writeImage(vectors, imgWidth, imgHeight, codebooksDigitsCount);
    byte[] bytes = convertBitsToBytes(bits);
    writeBinaryFile(Path.of(path), bytes);
  }

  public VectorQuantizationReadResult readImg(String path) {
    byte[] bytes = readBinaryFile(Path.of(path));
    String bits = convertBytesToBits(bytes);
    int bitsIdx = 0;
    boolean isGrey = Integer.parseInt(bits.substring(bitsIdx, bitsIdx += 1), 2) == 1;
    int codebooksCount = Integer.parseInt(bits.substring(bitsIdx, bitsIdx += 16), 2);
    int vecWidth = Integer.parseInt(bits.substring(bitsIdx, bitsIdx += 16), 2);
    int vecHeight = Integer.parseInt(bits.substring(bitsIdx, bitsIdx += 16), 2);
    ArrayList<Vector> codebooks = new ArrayList<>();
    for (int i = 0; i < codebooksCount; ++i) {
      int[] pixels = isGrey ? new int[vecWidth * vecHeight] : new int [vecWidth * vecHeight * 3];
      for (int j = 0; j < pixels.length; ++j) {
        String pixelBits = bits.substring(bitsIdx, bitsIdx += 8);
        pixels[j] = Integer.parseInt(pixelBits, 2);
      }
      codebooks.add(new Vector(pixels));
    }
    int imgWidth = Integer.parseInt(bits.substring(bitsIdx, bitsIdx += 16), 2);
    int imgHeight = Integer.parseInt(bits.substring(bitsIdx, bitsIdx += 16), 2);
    int codebooksDigitsCount = (int) Math.ceil(Math.log(codebooks.size()) / Math.log(2));
    ArrayList<Integer> labels = new ArrayList<>();
    while (bitsIdx + codebooksDigitsCount <= bits.length()) {
      String labelBits = bits.substring(bitsIdx, bitsIdx += codebooksDigitsCount);
      labels.add(Integer.parseInt(labelBits, 2));
    }
    return new VectorQuantizationReadResult(codebooks, labels, vecWidth, vecHeight, imgWidth, imgHeight, isGrey);
  }
}
