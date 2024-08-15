package com.ahmed.compression.techniques.tech.lossy.vectorquantization;

import com.ahmed.compression.techniques.tech.Technique;
import com.ahmed.compression.techniques.io.VectorQuantizationFile;
import com.ahmed.compression.techniques.io.VectorQuantizationReadResult;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class VectorQuantization implements Technique {
    private ArrayList<Vector> vectorize(Raster raster, int vecWidth, int vecHeight, boolean isGrey) {
      ArrayList<Vector> vectors = new ArrayList<>();
      for (int x = 0; x < raster.getWidth(); x += vecWidth) {
        for (int y = 0; y < raster.getHeight(); y += vecHeight) {
          int[] pixels = isGrey ? new int[vecWidth * vecHeight] : new int[vecWidth * vecHeight * 3]; // TODO grey or rgb
          int w = Math.min(raster.getWidth() - x, vecWidth);
          int h = Math.min(raster.getHeight() - y, vecHeight);
          raster.getPixels(x, y, w, h, pixels);
          vectors.add(new Vector(pixels));
        }
      }
      return vectors;
    }

    private Vector calcAvgVector(ArrayList<Vector> vectors, int vecWidth, int vecHeight, boolean isGrey) {
      int[] avgPixels = isGrey ? new int[vecWidth * vecHeight] : new int[vecWidth * vecHeight * 3];
      for (int i = 0; i < avgPixels.length; ++i) {
        int sum = 0;
        for (Vector vec : vectors) {
          sum += vec.getPixel(i);
        }
        avgPixels[i] = sum / Math.max(1, vectors.size());
      }
      return new Vector(avgPixels);
    }

    private ArrayList<ArrayList<Vector>> clusterize(ArrayList<Vector> vectors, int vecWidth, int vecHeight, int cnt, boolean isGrey) {
      if (cnt == 0) { return new ArrayList<>(List.of(vectors)); }
      Vector leftAvgVec = calcAvgVector(vectors, vecWidth, vecHeight, isGrey);
      Vector rightAvgVec = new Vector(leftAvgVec);
      rightAvgVec.shiftUp();
      ArrayList<Vector> left = new ArrayList<>();
      ArrayList<Vector> right = new ArrayList<>();
      for (Vector vec : vectors) {
        int leftDistance = vec.distanceTo(leftAvgVec);
        int rightDistance = vec.distanceTo(rightAvgVec);
        if (leftDistance <= rightDistance) {
          left.add(vec);
        } else {
          right.add(vec);
        }
      }
      ArrayList<ArrayList<Vector>> clusters = new ArrayList<>();
      clusters.addAll(clusterize(left, vecWidth, vecHeight, cnt - 1, isGrey));
      clusters.addAll(clusterize(right, vecWidth, vecHeight, cnt - 1, isGrey));
      return clusters;
    }

    private ArrayList<Vector> generateCodebooks(ArrayList<ArrayList<Vector>> clusters, int vecWidth, int vecHeight, boolean isGrey) {
      ArrayList<Vector> avgVectors = new ArrayList<>();
      boolean isChanged = true;
      // Continue until no change in the codebooks
      while (isChanged) {
        isChanged = false;
        avgVectors = new ArrayList<>();
        // Get average vector for each cluster
        for (ArrayList<Vector> cluster : clusters) {
          Vector avgVec = calcAvgVector(cluster, vecWidth, vecHeight, isGrey);
          avgVectors.add(avgVec);
        }
        // Assign each vector to the nearest cluster
        for (var cluster : clusters) {
          Stack<Integer> deletedVectorsIndexes = new Stack<>();
          for (int i = 0; i < cluster.size(); ++i) {
            int nearestClusterIdx = -1;
            int minDistance = Integer.MAX_VALUE;
            Vector vec = cluster.get(i);
            // Find the nearest cluster
            for (int j = 0; j < avgVectors.size(); ++j) {
              int distance = vec.distanceTo(avgVectors.get(j));
              if (distance < minDistance) {
                minDistance = distance;
                nearestClusterIdx = j;
              }
            }
            // If the nearest cluster is not the current cluster, move the vector to the nearest cluster
            if (clusters.get(nearestClusterIdx) != cluster) {
              deletedVectorsIndexes.push(i);
              clusters.get(nearestClusterIdx).add(vec);
              isChanged = true;
            }
          }
          // Remove moved vectors from the current cluster
          while (!deletedVectorsIndexes.isEmpty()) {
            int idx = deletedVectorsIndexes.pop();
            cluster.remove(idx);
          }
        }
      }
      return avgVectors;
    }

    private CompressResult compress(Raster raster, int vecWidth, int vecHeight, int codebooksLevels, boolean isGrey) {
      ArrayList<Vector> vectors = vectorize(raster, vecWidth, vecHeight, isGrey);
      ArrayList<ArrayList<Vector>> clusters = clusterize(vectors, vecWidth, vecHeight, codebooksLevels, isGrey);
      ArrayList<Vector> codebooks = generateCodebooks(clusters, vecWidth, vecHeight, isGrey);
      for (int i = 0; i < clusters.size(); ++i) {
        for (Vector vec : clusters.get(i)) {
          vec.setLabel(i);
        }
      }
      return new CompressResult(vectors, codebooks);
    }

    @Override
    public void compress(String inputFilePath, String outputFilePath, Map<String, Object> props) {
      int vecWidth = (int) props.getOrDefault("vecWidth", 1);
      int vecHeight = (int) props.getOrDefault("vecHeight", 1);
      int codebooksLevels = (int) props.getOrDefault("codebooksLevels", 1);
      try {
        BufferedImage img = ImageIO.read(new File(inputFilePath));
        Raster raster = img.getRaster();
        boolean isGrey = img.getType() == BufferedImage.TYPE_BYTE_GRAY;
        CompressResult compressResult = compress(raster, vecWidth, vecHeight, codebooksLevels, isGrey);
        VectorQuantizationFile vqFile = new VectorQuantizationFile();
        vqFile.writeImg(outputFilePath, isGrey, compressResult.getVectors(), compressResult.getCodebooks(),
            vecWidth, vecHeight, raster.getWidth(), raster.getHeight());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void decompress(String inputFilePath, String outputFilePath) {
      try {
        VectorQuantizationFile vqFile = new VectorQuantizationFile();
        VectorQuantizationReadResult readResult = vqFile.readImg(inputFilePath);
        BufferedImage img = new BufferedImage(readResult.getImgWidth(), readResult.getImgHeight(), readResult.isGrey() ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster raster = img.getRaster();
        int labelIdx = 0;
        for (int x = 0; x < readResult.getImgWidth(); x += readResult.getVecWidth()) {
          for (int y = 0; y < readResult.getImgHeight(); y += readResult.getVecHeight()) {
            int label = readResult.getLabels().get(labelIdx++);
            Vector codebook = readResult.getCodebooks().get(label);
            int w = Math.min(readResult.getVecWidth(), readResult.getImgWidth() - x);
            int h = Math.min(readResult.getVecHeight(), readResult.getImgHeight() - y);
            raster.setPixels(x, y, w, h, codebook.getPixels());
          }
        }
        ImageIO.write(img, "bmp", new File(outputFilePath));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public Map<String, Serializable> getProps() {
      return Map.of(
          "vecWidth", Integer.class,
          "vecHeight", Integer.class,
          "codebooksLevels", Integer.class
      );
    }
}

//  private void vectorsToImg(ArrayList<Vector> vectors, int imgWidth, int imgHeight) {
//    int vecIdx = 0;
//    BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_BYTE_GRAY);
//    WritableRaster raster = img.getRaster();
//    for (int x = 0; x < imgWidth; x += 2) {
//      for (int y = 0; y < imgHeight; y += 2) {
//        Vector vec = vectors.get(vecIdx++);
//        int w = Math.min(2, 256 - x);
//        int h = Math.min(2, 256 - y);
//        raster.setPixels(x, y, w, h, vec.getPixels());
//      }
//    }
//    try {
//      ImageIO.write(img, "bmp", new File("img/bmp/output.bmp"));
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  private BufferedImage rgbToGreyImg(BufferedImage img) {
//    BufferedImage greyImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//    for (int x = 0; x < img.getWidth(); ++x) {
//      for (int y = 0; y < img.getHeight(); ++y) {
//        Color rgbColor = new Color(img.getRGB(x, y));
//        int r = rgbColor.getRed();
//        int g = rgbColor.getGreen();
//        int b = rgbColor.getBlue();
//        int grey = (r + g + b) / 3;
//        Color greyColor = new Color(grey, grey, grey);
//        greyImg.setRGB(x, y, greyColor.getRGB());
//      }
//    }
//    return greyImg;
//  }

