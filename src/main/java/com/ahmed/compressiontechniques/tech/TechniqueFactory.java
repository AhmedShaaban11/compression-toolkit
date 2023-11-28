package com.ahmed.compressiontechniques.tech;

public class TechniqueFactory {
  public Technique createTechnique(String name) {
    if (name.equalsIgnoreCase("LZ77")) {
      return new Lz77();
    } else if (name.equalsIgnoreCase("LZW")) {
      return new Lzw();
    } else if (name.equalsIgnoreCase("Standard Huffman")) {
      return new StandardHuffman();
    }
    return null;
  }
}
