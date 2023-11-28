package com.ahmed.compression.techniques.tech;

import com.ahmed.compression.techniques.util.StandardHuffmanFile;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.PriorityQueue;

class StandardHuffmanTreeNode implements Comparable<StandardHuffmanTreeNode> {
  char c;
  double probability;
  StandardHuffmanTreeNode left;
  StandardHuffmanTreeNode right;

  // Leaf node constructor
  StandardHuffmanTreeNode(char c, double probability) {
    this.c = c;
    this.probability = probability;
    left = right = null;
  }

  // Internal node constructor
  StandardHuffmanTreeNode(double probability, StandardHuffmanTreeNode left, StandardHuffmanTreeNode right) {
    this.c = '\0';
    this.probability = probability;
    this.left = left;
    this.right = right;
  }

  public void getLeafCodes(HashMap<Character, String> codes, String code) {
    if (left == null && right == null) {
      codes.put(c, code);
      return;
    }
    left.getLeafCodes(codes, code + "0");
    right.getLeafCodes(codes, code + "1");
  }

  @Override
  public int compareTo(StandardHuffmanTreeNode another) {
    return Double.compare(this.probability, another.probability);
  }
}

public class StandardHuffman implements Technique {

  private HashMap<Character, Double> getProbabilities(String data) {
    HashMap<Character, Double> probabilities = new HashMap<>();
    for (int i = 0; i < data.length(); ++i) {
      char c = data.charAt(i);
      probabilities.put(c, probabilities.getOrDefault(c, 0.0) + 1);
    }
    probabilities.keySet().forEach((c) -> probabilities.put(c, probabilities.get(c) / data.length()));
    return probabilities;
  }

  private StandardHuffmanTreeNode getStandardHuffmanTreeRoot(HashMap<Character, Double> probabilities) {
    PriorityQueue<StandardHuffmanTreeNode> leafNodes = new PriorityQueue<>();
    probabilities.keySet().forEach((c) -> leafNodes.add(new StandardHuffmanTreeNode(c, probabilities.get(c))));
    while (leafNodes.size() > 1) {
      var left = leafNodes.poll();
      var right = leafNodes.poll();
      var internal = new StandardHuffmanTreeNode(left.probability + right.probability, left, right);
      leafNodes.add(internal);
    }
    return leafNodes.poll();
  }

  private String getCompressedStream(String data, HashMap<Character, String> huffmanCodes) {
    StringBuilder compressedStream = new StringBuilder();
    for (int i = 0; i < data.length(); ++i) {
      compressedStream.append(huffmanCodes.get(data.charAt(i)));
    }
    return compressedStream.toString();
  }

  private StandardHuffmanCompressResult compress(String data) {
    var probabilities = getProbabilities(data);
    var root = getStandardHuffmanTreeRoot(probabilities);
    HashMap<Character, String> huffmanCodes = new HashMap<>();
    root.getLeafCodes(huffmanCodes, "");
    String compressedStream = getCompressedStream(data, huffmanCodes);
    return new StandardHuffmanCompressResult<>(compressedStream, huffmanCodes);
  }

  @Override
  public void compress(String inputPath, String outputPath) {
    StandardHuffmanFile standardHuffmanFile = new StandardHuffmanFile();
    String data = standardHuffmanFile.readRegularFile(Path.of(inputPath)) + "\0"; // sentinel
    StandardHuffmanCompressResult compressResult = compress(data);
    standardHuffmanFile.writeStandardHuffmanFile(Path.of(outputPath), compressResult.getCompressedStream(), compressResult.getMap());
  }

  private String decompress(String data, HashMap<String, Character> huffmanCodes) {
    StringBuilder decompressedStream = new StringBuilder();
    String currentCode = "";
    for (int i = 0; i < data.length(); ++i) {
      currentCode += data.charAt(i);
      Character c = huffmanCodes.get(currentCode);
      if (c == null) { continue; }
      if (c == '\0') { break; } // sentinel
      decompressedStream.append(c);
      currentCode = "";
    }
    return decompressedStream.toString();
  }

  @Override
  public void decompress(String inputPath, String outputPath) {
    StandardHuffmanFile standardHuffmanFile = new StandardHuffmanFile();
    StandardHuffmanCompressResult compressResult = standardHuffmanFile.readStandardHuffmanFile(Path.of(inputPath));
    String data = decompress(compressResult.getCompressedStream(), compressResult.getMap());
    standardHuffmanFile.writeRegularFile(Path.of(outputPath), data);
  }
}
