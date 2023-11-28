package com.ahmed.compressiontechniques.tech;

import com.ahmed.compressiontechniques.util.StandardHuffmanFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.TreeMap;

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
      // TODO: Remove this line
      System.out.println(c + ": " + code);
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
//  public static class CompressResult {
//    String compressedStream;
//    TreeMap<Character, Integer> frequencies;
//
//    public CompressResult(String compressedStream, TreeMap<Character, Integer> frequencies) {
//      this.compressedStream = compressedStream;
//      this.frequencies = frequencies;
//    }
//
//    public String getCompressedStream() {
//      return compressedStream;
//    }
//
//    public TreeMap<Character, Integer> getFrequencies() {
//      return frequencies;
//    }
//  }

  public static class CompressResult<T, V> {
    String compressedStream;
    TreeMap<T, V> huffmanCodes;

    public CompressResult(String compressedStream, TreeMap<T, V> huffmanCodes) {
      this.compressedStream = compressedStream;
      this.huffmanCodes = huffmanCodes;
    }

    public String getCompressedStream() {
      return compressedStream;
    }

    public TreeMap<T, V> getMap() {
      return huffmanCodes;
    }
  }

  TreeMap<Character, Integer> getFrequencies(String data) {
    TreeMap<Character, Integer> frequencies = new TreeMap<>();
    for (int i = 0; i < data.length(); ++i) {
      char c = data.charAt(i);
      frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
    }
    return frequencies;
  }

  HashMap<Character, Double> getProbabilities(TreeMap<Character, Integer> frequencies, int dataLength) {
    HashMap<Character, Double> probabilities = new HashMap<>();
    frequencies.keySet().forEach((c) -> probabilities.put(c, (double) frequencies.get(c) / dataLength));
    return probabilities;
  }

  StandardHuffmanTreeNode getStandardHuffmanTreeRoot(HashMap<Character, Double> probabilities) {
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

  String getCompressedStream(String data, HashMap<Character, String> huffmanCodes) {
    StringBuilder compressedStream = new StringBuilder();
    for (int i = 0; i < data.length(); ++i) {
      compressedStream.append(huffmanCodes.get(data.charAt(i)));
    }
    return compressedStream.toString();
  }

  String decompress(String data, HashMap<String, Character> huffmanCodes) {
    StringBuilder decompressedStream = new StringBuilder();
    String currentCode = "";
    for (int i = 0; i < data.length(); ++i) {
      currentCode += data.charAt(i);
      Character c = huffmanCodes.get(currentCode);
      if (c == null) { continue; }
      decompressedStream.append(c);
      currentCode = "";
    }
    return decompressedStream.toString();
  }

  CompressResult compress(String data) {
    var frequencies = getFrequencies(data);
    var probabilities = getProbabilities(frequencies, data.length());
    var root = getStandardHuffmanTreeRoot(probabilities);
    HashMap<Character, String> huffmanCodes = new HashMap<>();
    root.getLeafCodes(huffmanCodes, "");
    String compressedStream = getCompressedStream(data, huffmanCodes);
    return new CompressResult(compressedStream, frequencies);
  }

//  @Override
//  public void compress(String inputPath, String outputPath) {
//    StandardHuffmanFile standardHuffmanFile = new StandardHuffmanFile();
//    String data = standardHuffmanFile.readRegularFile(Path.of(inputPath));
//    CompressResult compressResult = compress(data);
//    standardHuffmanFile.writeStandardHuffmanFile(Path.of(outputPath), compressResult.getCompressedStream(), compressResult.getFrequencies());
//    for (var entry : getFrequencies(data).entrySet()) {
//      System.out.println(entry.getKey() + ": " + entry.getValue());
//    }
//  }

  @Override
  public void compress(String inputPath, String outputPath) {
    StandardHuffmanFile standardHuffmanFile = new StandardHuffmanFile();
    String data = standardHuffmanFile.readRegularFile(Path.of(inputPath));
    CompressResult compressResult = compress(data);
    standardHuffmanFile.writeStandardHuffmanFile(Path.of(outputPath), compressResult.getCompressedStream(), compressResult.getFrequencies());
    for (var entry : getFrequencies(data).entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue());
    }
  }

  @Override
  public void decompress(String inputPath, String outputPath) {
    StandardHuffmanFile standardHuffmanFile = new StandardHuffmanFile();
    CompressResult compressResult = standardHuffmanFile.readStandardHuffmanFile(Path.of(inputPath));
    int dataLength = 0;
    for (int freq : compressResult.getFrequencies().values()) { dataLength += freq; }
    var probabilities = getProbabilities(compressResult.getFrequencies(), dataLength);
    var root = getStandardHuffmanTreeRoot(probabilities);
    HashMap<Character, String> huffmanCodes = new HashMap<>();
    root.getLeafCodes(huffmanCodes, "");
    HashMap<String, Character> huffmanCodes2 = new HashMap<>();
    for (var entry : huffmanCodes.entrySet()) { huffmanCodes2.put(entry.getValue(), entry.getKey()); }
    String data = decompress(compressResult.getCompressedStream(), huffmanCodes2);
    standardHuffmanFile.writeRegularFile(Path.of(outputPath), data);
  }
}
