package com.ahmed.compression.techniques.tech;

import com.ahmed.compression.techniques.tech.lossless.Lz77;
import com.ahmed.compression.techniques.tech.lossless.Lzw;
import com.ahmed.compression.techniques.tech.lossless.StandardHuffman;
import com.ahmed.compression.techniques.tech.lossy.VectorQuantization;

public class TechniqueFactory {
  public Technique createTechnique(String name) {
    if (name.equalsIgnoreCase("LZ77")) {
      return new Lz77();
    } else if (name.equalsIgnoreCase("LZW")) {
      return new Lzw();
    } else if (name.equalsIgnoreCase("Standard Huffman")) {
      return new StandardHuffman();
    } else if (name.equalsIgnoreCase("Vector Quantization")) {
      return new VectorQuantization();
    }
    return null;
  }
}
