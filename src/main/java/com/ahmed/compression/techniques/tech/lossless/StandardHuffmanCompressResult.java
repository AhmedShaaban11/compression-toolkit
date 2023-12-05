package com.ahmed.compression.techniques.tech.lossless;

import java.util.HashMap;

public class StandardHuffmanCompressResult<T, V> {
  String compressedStream;
  HashMap<T, V> huffmanCodes;

  public StandardHuffmanCompressResult(String compressedStream, HashMap<T, V> huffmanCodes) {
    this.compressedStream = compressedStream;
    this.huffmanCodes = huffmanCodes;
  }

  public String getCompressedStream() {
    return compressedStream;
  }

  public HashMap<T, V> getMap() {
    return huffmanCodes;
  }
}