package com.ahmed.compression.techniques.util;

import com.ahmed.compression.techniques.tech.lossless.StandardHuffmanCompressResult;

import java.nio.file.Path;
import java.util.HashMap;

public class StandardHuffmanFile extends BinaryFile {

  public void writeStandardHuffmanFile(Path path, String compressedStream, HashMap<Character, String> huffmanCodes) {
    StringBuilder rows = new StringBuilder();
    for (var entry : huffmanCodes.entrySet()) {
      String charBits = intToBinaryString(entry.getKey(), 8);
      String codeLengthBits = intToBinaryString(entry.getValue().length(), 8);
      rows.append(charBits).append(codeLengthBits).append(entry.getValue());
    }
    String endOfRows = intToBinaryString(0, 16);
    rows.append(endOfRows);
    String bits = rows + compressedStream;
    byte[] bytes = convertBitsToBytes(bits);
    writeBinaryFile(path, bytes);
  }

  public StandardHuffmanCompressResult readStandardHuffmanFile(Path path) {
    byte[] bytes = readBinaryFile(path);
    String bits = convertBytesToBits(bytes);
    HashMap<String, Character> huffmanCodes = new HashMap<>();
    // Read huffmanCodes
    int i = 0;
    while (true) {
      String charBits = bits.substring(i, i += 8);
      Character c = (char) Integer.parseInt(charBits, 2);
      String codeLengthBits = bits.substring(i, i += 8);
      int codeLength = Integer.parseInt(codeLengthBits, 2);
      String code = bits.substring(i, i += codeLength);
      if (code.isEmpty()) {
        break;
      }
      huffmanCodes.put(code, c);
    }
    // Read compressed stream
    String compressedStream = bits.substring(i);
    return new StandardHuffmanCompressResult<>(compressedStream, huffmanCodes);
  }
}
