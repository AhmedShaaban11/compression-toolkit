package com.ahmed.compression.toolkit.tech.lossy;

import com.ahmed.compression.toolkit.info.lossy.vectorquantization.*;
import com.ahmed.compression.toolkit.tech.Technique;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class VectorQuantization implements Technique<VectorQuantizationCompressedFileInfo, VectorQuantizationDecompressedFileInfo, VectorQuantizationCompressionInfo, VectorQuantizationDecompressionInfo> {
  private static final int VECTOR_WIDTH = 4;
  private static final int VECTOR_HEIGHT = 4;
  private static final int CODEBOOKS_LEVELS = 3;

  private ArrayList<Vector> vectorize(Raster raster, boolean isGrey) {
    ArrayList<Vector> vectors = new ArrayList<>();
    for (int x = 0; x < raster.getWidth(); x += VECTOR_WIDTH) {
      for (int y = 0; y < raster.getHeight(); y += VECTOR_HEIGHT) {
        int[] pixels = isGrey ? new int[VECTOR_WIDTH * VECTOR_HEIGHT] : new int[VECTOR_WIDTH * VECTOR_HEIGHT * 3];
        int w = Math.min(raster.getWidth() - x, VECTOR_WIDTH);
        int h = Math.min(raster.getHeight() - y, VECTOR_HEIGHT);
        raster.getPixels(x, y, w, h, pixels);
        vectors.add(new Vector(pixels));
      }
    }
    return vectors;
  }

  private Vector calcAvgVector(ArrayList<Vector> vectors, boolean isGrey) {
    int[] avgPixels = isGrey ? new int[VECTOR_WIDTH * VECTOR_HEIGHT] : new int[VECTOR_WIDTH * VECTOR_HEIGHT * 3];
    for (int i = 0; i < avgPixels.length; ++i) {
      int sum = 0;
      for (Vector vec : vectors) {
        sum += vec.pixel(i);
      }
      avgPixels[i] = sum / Math.max(1, vectors.size());
    }
    return new Vector(avgPixels);
  }

  private ArrayList<ArrayList<Vector>> clusterize(ArrayList<Vector> vectors, int cnt, boolean isGrey) {
    if (cnt == 0) { return new ArrayList<>(List.of(vectors)); }
    Vector leftAvgVec = calcAvgVector(vectors, isGrey);
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
    clusters.addAll(clusterize(left, cnt - 1, isGrey));
    clusters.addAll(clusterize(right, cnt - 1, isGrey));
    return clusters;
  }

  private ArrayList<Vector> generateCodebooks(ArrayList<ArrayList<Vector>> clusters, boolean isGrey) {
    ArrayList<Vector> avgVectors = new ArrayList<>();
    boolean isChanged = true;
    // Continue until no change in the codebooks
    while (isChanged) {
      isChanged = false;
      avgVectors = new ArrayList<>();
      // Get average vector for each cluster
      for (ArrayList<Vector> cluster : clusters) {
        Vector avgVec = calcAvgVector(cluster, isGrey);
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

  @Override
  public VectorQuantizationCompressionInfo compress(VectorQuantizationDecompressedFileInfo fileInfo) {
    ArrayList<Vector> vectors = vectorize(fileInfo.raster(), fileInfo.isGrey());
    ArrayList<ArrayList<Vector>> clusters = clusterize(vectors, CODEBOOKS_LEVELS, fileInfo.isGrey());
    ArrayList<Vector> codebooks = generateCodebooks(clusters, fileInfo.isGrey());
    for (int i = 0; i < clusters.size(); ++i) {
      for (Vector vec : clusters.get(i)) {
        vec.setLabel(i);
      }
    }
    return new VectorQuantizationCompressionInfo(fileInfo.raster(), VECTOR_WIDTH, VECTOR_HEIGHT, vectors, codebooks);
  }

  @Override
  public VectorQuantizationDecompressionInfo decompress(VectorQuantizationCompressedFileInfo fileInfo) {
    BufferedImage img = new BufferedImage(fileInfo.imgWidth(), fileInfo.imgHeight(), fileInfo.isGrey() ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR);
    WritableRaster raster = img.getRaster();
    int labelIdx = 0;
    for (int x = 0; x < fileInfo.imgWidth(); x += VECTOR_WIDTH) {
      for (int y = 0; y < fileInfo.imgHeight(); y += VECTOR_HEIGHT) {
        int label = fileInfo.labels().get(labelIdx++);
        Vector codebook = fileInfo.codebooks().get(label);
        int w = Math.min(VECTOR_WIDTH, fileInfo.imgWidth() - x);
        int h = Math.min(VECTOR_HEIGHT, fileInfo.imgHeight() - y);
        raster.setPixels(x, y, w, h, codebook.pixels());
      }
    }
    return new VectorQuantizationDecompressionInfo(img);
  }
}
