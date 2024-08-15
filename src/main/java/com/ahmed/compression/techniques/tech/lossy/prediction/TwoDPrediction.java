package com.ahmed.compression.techniques.tech.lossy.prediction;

import com.ahmed.compression.techniques.tech.Technique;
import com.ahmed.compression.techniques.io.TwoDPredictionFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;

public class TwoDPrediction implements Technique {
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

  public CompressResult compress(Raster raster, int bytes) {
    int[][] predictionArr = getPredictionArr(raster, bytes);
    computePredictDifference(predictionArr, bytes);
    quantize(predictionArr, bytes);
    return new CompressResult(
      bytes == 1,
      raster.getWidth(),
      raster.getHeight(),
      QUANTIZE_BITS_COUNT,
      predictionArr
    );
  }

  @Override
  public void compress(String inPath, String outPath) {
    try {
      BufferedImage img = ImageIO.read(new File(inPath));
      int bytes = img.getType() == BufferedImage.TYPE_BYTE_GRAY ? 1 : 3;
      CompressResult compressResult = compress(img.getData(), bytes);
      TwoDPredictionFile file = new TwoDPredictionFile();
      file.write(outPath, compressResult);
    } catch (Exception e) {
      System.out.println(e.getMessage());
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

  private void decompress(CompressResult readResult) {
    int bytes = readResult.isGrey() ? 1 : 3;
    deQuantize(readResult.getPrediction(), bytes, readResult.getQuantizationBits());
    computeOriginal(readResult.getPrediction(), bytes);
  }

  @Override
  public void decompress(String inPath, String outPath) {
    TwoDPredictionFile file = new TwoDPredictionFile();
    CompressResult readResult = file.read(inPath);
    decompress(readResult);
    try {
      BufferedImage img = new BufferedImage(
        readResult.getWidth(),
        readResult.getHeight(),
        readResult.isGrey() ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR
      );
      WritableRaster raster = img.getRaster();
      for (int i = 0; i < readResult.getHeight(); ++i) {
        raster.setPixels(0, i, readResult.getWidth(), 1, readResult.getPrediction()[i]);
      }
      img.setData(raster);
      ImageIO.write(img, "bmp", new File(outPath));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    TwoDPrediction twoDPrediction = new TwoDPrediction();
    twoDPrediction.compress("img/bmp/fruit.bmp", "img/bmp/output.bin");
    twoDPrediction.decompress("img/bmp/output.bin", "img/bmp/output.bmp");
  }
}
