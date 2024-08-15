package com.ahmed.compression.techniques.io.lossy;

import com.ahmed.compression.techniques.information.lossy.twodprediction.TwoDPredictionCompressedFileInfo;
import com.ahmed.compression.techniques.information.lossy.twodprediction.TwoDPredictionCompressionInfo;
import com.ahmed.compression.techniques.information.lossy.twodprediction.TwoDPredictionDecompressedFileInfo;
import com.ahmed.compression.techniques.information.lossy.twodprediction.TwoDPredictionDecompressionInfo;
import com.ahmed.compression.techniques.io.BinaryFile;
import com.ahmed.compression.techniques.io.ReaderWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class TwoDPredictionFile extends BinaryFile implements ReaderWriter<TwoDPredictionCompressedFileInfo, TwoDPredictionDecompressedFileInfo, TwoDPredictionCompressionInfo, TwoDPredictionDecompressionInfo> {
  @Override
  public TwoDPredictionCompressedFileInfo readCompressedFile(Path path) {
    byte[] bytes = readBinaryFile(path);
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
    return new TwoDPredictionCompressedFileInfo(isGrey, width, height, quantizationBitsCount, prediction);
  }

  @Override
  public TwoDPredictionDecompressedFileInfo readDecompressedFile(Path path) {
    try {
      BufferedImage img = ImageIO.read(path.toFile());
      int colorBytes = img.getType() == BufferedImage.TYPE_BYTE_GRAY ? 1 : 3;
      return new TwoDPredictionDecompressedFileInfo(img.getData(), colorBytes);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new TwoDPredictionDecompressedFileInfo(null, 0);
  }

  @Override
  public void writeCompressedFile(Path path, TwoDPredictionCompressionInfo info) {
    StringBuilder bits = new StringBuilder();
    bits.append(info.isGrey() ? "1" : "0");
    bits.append(intToBinaryString(info.imgWidth(), 16));
    bits.append(intToBinaryString(info.imgHeight(), 16));
    bits.append(intToBinaryString(info.quantizationBits(), 4));
    int bytesPerPixel = info.isGrey() ? 1 : 3;
    // Write the first row of the prediction matrix
    for (int i = 0; i < info.imgWidth() * bytesPerPixel; ++i) {
      bits.append(intToBinaryString(info.prediction()[0][i], 8));
    }
    // Write the first column of the prediction matrix
    for (int i = 0; i < info.imgHeight(); ++i) {
      bits.append(intToBinaryString(info.prediction()[i][0], 8));
    }
    // Write the rest of the prediction matrix
    for (int i = 1; i < info.imgHeight(); i++) {
      for (int j = 1; j < info.imgWidth() * bytesPerPixel; j++) {
        bits.append(intToBinaryString(info.prediction()[i][j], info.quantizationBits()));
      }
    }
    byte[] bytes = convertBitsToBytes(bits.toString());
    writeBinaryFile(path, bytes);
  }

  @Override
  public void writeDecompressedFile(Path path, TwoDPredictionDecompressionInfo fileInfo) {
    try {
      ImageIO.write(fileInfo.img(), "bmp", path.toFile());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
