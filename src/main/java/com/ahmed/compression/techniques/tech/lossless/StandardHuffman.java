package com.ahmed.compression.techniques.tech.lossless;

import com.ahmed.compression.techniques.information.lossless.standardhuffman.*;
import com.ahmed.compression.techniques.tech.NewTechnique;
import com.ahmed.compression.techniques.tech.Technique;
import com.ahmed.compression.techniques.io.StandardHuffmanFile;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class StandardHuffman implements Technique, NewTechnique<StandardHuffmanCompressedFileInfo, StandardHuffmanDecompressedFileInfo, StandardHuffmanCompressionInfo, StandardHuffmanDecompressionInfo> {

  private HashMap<Character, Double> getProbabilities(String data) {
    HashMap<Character, Double> probabilities = new HashMap<>();
    for (int i = 0; i < data.length(); ++i) {
      char c = data.charAt(i);
      probabilities.put(c, probabilities.getOrDefault(c, 0.0) + 1);
    }
    probabilities.keySet().forEach((c) -> probabilities.put(c, probabilities.get(c) / data.length()));
    return probabilities;
  }

  private TreeNode getStandardHuffmanTreeRoot(HashMap<Character, Double> probabilities) {
    PriorityQueue<TreeNode> leafNodes = new PriorityQueue<>();
    probabilities.keySet().forEach((c) -> leafNodes.add(new TreeNode(c, probabilities.get(c))));
    while (leafNodes.size() > 1) {
      var left = leafNodes.poll();
      var right = leafNodes.poll();
      var internal = new TreeNode(left.probability() + right.probability(), left, right);
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

  private StandardHuffmanCompressionInfo compress(String data) {
    var probabilities = getProbabilities(data);
    var root = getStandardHuffmanTreeRoot(probabilities);
    HashMap<Character, String> huffmanCodes = new HashMap<>();
    root.getLeafCodes(huffmanCodes, "");
    String compressedStream = getCompressedStream(data, huffmanCodes);
    return new StandardHuffmanCompressionInfo(compressedStream, huffmanCodes);
  }

  @Override
  public void compress(String inputPath, String outputPath) {
    StandardHuffmanFile standardHuffmanFile = new StandardHuffmanFile();
    String data = standardHuffmanFile.readRegularFile(Path.of(inputPath)) + "\0"; // sentinel
    StandardHuffmanCompressionInfo compressionInfo = compress(data);
    standardHuffmanFile.writeStandardHuffmanFile(Path.of(outputPath), compressionInfo.compressedStream(), compressionInfo.huffmanCodes());
  }

  private String decompress(String data, Map<String, Character> huffmanCodes) {
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
    StandardHuffmanCompressedFileInfo fileInfo = standardHuffmanFile.readStandardHuffmanFile(Path.of(inputPath));
    String data = decompress(fileInfo.compressedStream(), fileInfo.huffmanCodes());
    standardHuffmanFile.writeRegularFile(Path.of(outputPath), data);
  }

  @Override
  public StandardHuffmanCompressionInfo compress(StandardHuffmanDecompressedFileInfo info) {
    return compress(info.data());
  }

  @Override
  public StandardHuffmanDecompressionInfo decompress(StandardHuffmanCompressedFileInfo info) {
    String data = decompress(info.compressedStream(), info.huffmanCodes());
    return new StandardHuffmanDecompressionInfo(data);
  }
}
