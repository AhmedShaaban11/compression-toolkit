package com.ahmed.compression.techniques.tech.lossy;

import com.ahmed.compression.techniques.information.lossy.twodprediction.TwoDPredictionCompressedFileInfo;
import com.ahmed.compression.techniques.information.lossy.twodprediction.TwoDPredictionCompressionInfo;
import com.ahmed.compression.techniques.information.lossy.twodprediction.TwoDPredictionDecompressedFileInfo;
import com.ahmed.compression.techniques.information.lossy.twodprediction.TwoDPredictionDecompressionInfo;
import com.ahmed.compression.techniques.tech.Technique;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class TwoDPrediction implements Technique<TwoDPredictionCompressedFileInfo, TwoDPredictionDecompressedFileInfo, TwoDPredictionCompressionInfo, TwoDPredictionDecompressionInfo> {
  private static final int QUANTIZE_BITS_COUNT = 8;
  private static final int MIN_DIFFERENCE = -255;
  private static final int MAX_DIFFERENCE = 255;

  private int[][] getPredictionArr(Raster raster, int bytes) {
    int width = raster.getWidth(), height = raster.getHeight();
    int[][] predictionArr = new int[height][width * bytes];
    for (int i = 0; i < raster.getHeight(); ++i) {
      raster.getPixels(0, i, width, 1, predictionArr[i]);
    }
    return predictionArr;
  }

  private void computePredictDifference(int[][] predictionArr, int bytes) {
    for (int i = predictionArr.length - 1; i >= 1; --i) {
      for (int j = predictionArr[i].length - 1; j >= bytes; --j) {
        int A = predictionArr[i][j - bytes];
        int B = predictionArr[i - 1][j - bytes];
        int C = predictionArr[i - 1][j];
        int prediction = 0;
        if (B <= Math.min(A, C)) {
          prediction = Math.max(A, C);
        } else if (B >= Math.max(A, C)) {
          prediction = Math.min(A, C);
        } else {
          prediction = A + C - B;
        }
        predictionArr[i][j] -= prediction; // difference
      }
    }
  }

  private void quantize(int[][] predictionArr, int bytes) {
    double levels = Math.pow(2, QUANTIZE_BITS_COUNT);
    int step = (int) Math.ceil((MAX_DIFFERENCE - MIN_DIFFERENCE) / levels);
    for (int i = 1; i < predictionArr.length; ++i) {
      for (int j = bytes; j < predictionArr[i].length; ++j) {
        int quantized = (predictionArr[i][j] - MIN_DIFFERENCE) / step;
        predictionArr[i][j] = quantized;
      }
    }
  }

  private void deQuantize(int[][] predictionArr, int bytes, int quantizationBitsCount) {
    double levels = Math.pow(2, quantizationBitsCount);
    int step = (int) Math.ceil((MAX_DIFFERENCE - MIN_DIFFERENCE) / levels);
    for (int i = 1; i < predictionArr.length; ++i) {
      for (int j = bytes; j < predictionArr[i].length; ++j) {
        int deQuantized = MIN_DIFFERENCE + predictionArr[i][j] * step + (int) Math.ceil(0.5 * step);
        predictionArr[i][j] = deQuantized;
      }
    }
  }

  private void computeOriginal(int[][] predictionArr, int bytes) {
    for (int i = 1; i < predictionArr.length; ++i) {
      for (int j = bytes; j < predictionArr[i].length; ++j) {
        int A = predictionArr[i][j - bytes];
        int B = predictionArr[i - 1][j - bytes];
        int C = predictionArr[i - 1][j];
        int prediction = 0;
        if (B <= Math.min(A, C)) {
          prediction = Math.max(A, C);
        } else if (B >= Math.max(A, C)) {
          prediction = Math.min(A, C);
        } else {
          prediction = A + C - B;
        }
        predictionArr[i][j] += prediction; // difference
        predictionArr[i][j] = Math.max(0, Math.min(255, predictionArr[i][j]));
      }
    }
  }

  @Override
  public TwoDPredictionCompressionInfo compress(TwoDPredictionDecompressedFileInfo fileInfo) {
    int[][] predictionArr = getPredictionArr(fileInfo.raster(), fileInfo.colorBytes());
    computePredictDifference(predictionArr, fileInfo.colorBytes());
    quantize(predictionArr, fileInfo.colorBytes());
    return new TwoDPredictionCompressionInfo(
        fileInfo.colorBytes() == 1,
        fileInfo.raster().getWidth(),
        fileInfo.raster().getHeight(),
        QUANTIZE_BITS_COUNT,
        predictionArr
    );
  }

  @Override
  public TwoDPredictionDecompressionInfo decompress(TwoDPredictionCompressedFileInfo fileInfo) {
    int colorBytes = fileInfo.isGrey() ? 1 : 3;
    deQuantize(fileInfo.prediction(), colorBytes, fileInfo.quantizationBits());
    computeOriginal(fileInfo.prediction(), colorBytes);
    BufferedImage img = new BufferedImage(
        fileInfo.imgWidth(),
        fileInfo.imgHeight(),
        fileInfo.isGrey() ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR
    );
    WritableRaster raster = img.getRaster();
    for (int i = 0; i < fileInfo.imgHeight(); ++i) {
      raster.setPixels(0, i, fileInfo.imgWidth(), 1, fileInfo.prediction()[i]);
    }
    return new TwoDPredictionDecompressionInfo(img);
  }
}
