package com.ahmed.compression.techniques.io.lossy;

import com.ahmed.compression.techniques.information.lossy.vectorquantization.VectorQuantizationCompressedFileInfo;
import com.ahmed.compression.techniques.information.lossy.vectorquantization.VectorQuantizationCompressionInfo;
import com.ahmed.compression.techniques.information.lossy.vectorquantization.VectorQuantizationDecompressedFileInfo;
import com.ahmed.compression.techniques.information.lossy.vectorquantization.VectorQuantizationDecompressionInfo;
import com.ahmed.compression.techniques.information.lossy.vectorquantization.Vector;
import com.ahmed.compression.techniques.io.BinaryFile;
import com.ahmed.compression.techniques.io.ReaderWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class VectorQuantizationFile extends BinaryFile implements ReaderWriter<VectorQuantizationCompressedFileInfo, VectorQuantizationDecompressedFileInfo, VectorQuantizationCompressionInfo, VectorQuantizationDecompressionInfo> {
  private String writeCodeBooks(ArrayList<Vector> codebooks, int vecWidth, int vecHeight) {
    StringBuilder bits = new StringBuilder();
    bits.append(intToBinaryString(codebooks.size(), 16));
    bits.append(intToBinaryString(vecWidth, 16));
    bits.append(intToBinaryString(vecHeight, 16));
    for (Vector codebook : codebooks) {
      for (int i = 0; i < codebook.size(); ++i) {
        bits.append(intToBinaryString(codebook.pixel(i), 8));
      }
    }
    return bits.toString();
  }

  private String imgToBits(ArrayList<Vector> vectors, int imgWidth, int imgHeight, int codebooksDigitsCount) {
    StringBuilder bits = new StringBuilder();
    bits.append(intToBinaryString(imgWidth, 16));
    bits.append(intToBinaryString(imgHeight, 16));
    for (Vector vector : vectors) {
      String labelBits = intToBinaryString(vector.label(), codebooksDigitsCount);
      bits.append(labelBits);
    }
    return bits.toString();
  }

  @Override
  public VectorQuantizationCompressedFileInfo readCompressedFile(Path path) {
    byte[] bytes = readBinaryFile(path);
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
    return new VectorQuantizationCompressedFileInfo(vecWidth, vecHeight, imgWidth, imgHeight, isGrey, codebooks, labels);
  }

  @Override
  public VectorQuantizationDecompressedFileInfo readDecompressedFile(Path path) {
    BufferedImage img = null;
    try {
      img = ImageIO.read(path.toFile());
      return new VectorQuantizationDecompressedFileInfo(img.getRaster());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new VectorQuantizationDecompressedFileInfo(null);
  }

  @Override
  public void writeCompressedFile(Path path, VectorQuantizationCompressionInfo info) {
    int codebooksDigitsCount = (int) Math.ceil(Math.log(info.codebooks().size()) / Math.log(2));
    String bits = "";
    bits += info.isGrey() ? "1" : "0";
    bits += writeCodeBooks(info.codebooks(), info.vecWidth(), info.vecHeight());
    bits += imgToBits(info.vectors(), info.raster().getWidth(), info.raster().getHeight(), codebooksDigitsCount);
    byte[] bytes = convertBitsToBytes(bits);
    writeBinaryFile(path, bytes);
  }

  @Override
  public void writeDecompressedFile(Path path, VectorQuantizationDecompressionInfo info) {
    try {
      ImageIO.write(info.img(), "bmp", path.toFile());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
