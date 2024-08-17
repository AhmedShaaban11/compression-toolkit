package com.ahmed.compression.toolkit.tech.lossless;

import com.ahmed.compression.toolkit.info.lossless.standardhuffman.*;
import com.ahmed.compression.toolkit.tech.Technique;

import java.util.HashMap;
import java.util.PriorityQueue;

public class StandardHuffman implements Technique<StandardHuffmanCompressedFileInfo, StandardHuffmanDecompressedFileInfo, StandardHuffmanCompressionInfo, StandardHuffmanDecompressionInfo> {

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

  @Override
  public StandardHuffmanCompressionInfo compress(StandardHuffmanDecompressedFileInfo info) {
    var probabilities = getProbabilities(info.data());
    var root = getStandardHuffmanTreeRoot(probabilities);
    HashMap<Character, String> huffmanCodes = new HashMap<>();
    root.getLeafCodes(huffmanCodes, "");
    String compressedStream = getCompressedStream(info.data(), huffmanCodes);
    return new StandardHuffmanCompressionInfo(compressedStream, huffmanCodes);
  }

  @Override
  public StandardHuffmanDecompressionInfo decompress(StandardHuffmanCompressedFileInfo info) {
    StringBuilder decompressedStream = new StringBuilder();
    String currentCode = "";
    for (int i = 0; i < info.compressedStream().length(); ++i) {
      currentCode += info.compressedStream().charAt(i);
      Character c = info.huffmanCodes().get(currentCode);
      if (c == null) { continue; }
      if (c == '\0') { break; } // sentinel
      decompressedStream.append(c);
      currentCode = "";
    }
    return new StandardHuffmanDecompressionInfo(decompressedStream.toString());
  }

}
