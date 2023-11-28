package com.ahmed.compressiontechniques.util;

import com.ahmed.compressiontechniques.tech.StandardHuffman.*;
import com.ahmed.compressiontechniques.tech.StandardHuffman;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.TreeMap;

public class StandardHuffmanFile extends BinaryFile {
  private int[] getHeaderLength(TreeMap<Character, Integer> frequencyTable) {
    int maxCharValue = 0;
    int maxFreqValue = 0;
    for (var entry : frequencyTable.entrySet()) {
      maxCharValue = Math.max(maxCharValue, entry.getKey());
      maxFreqValue = Math.max(maxFreqValue, entry.getValue());
    }
    double maxCharValueDigits = Math.log10(maxCharValue) / Math.log10(2);
    double maxFreqValueDigits = Math.log10(maxFreqValue) / Math.log10(2);
    int[] result = new int[2];
    result[0] = (int) Math.floor(maxCharValueDigits) + 1;
    result[1] = (int) Math.floor(maxFreqValueDigits) + 1;
    return result;
  }

  private String intToBinaryString(int n, int digitsCount) {
    String bin = Integer.toBinaryString(n);
    if (bin.length() < digitsCount) {
      bin = "0".repeat(digitsCount - bin.length()) + bin;
    }
    return bin;
  }

//  public void writeStandardHuffmanFile(Path path, String compressedStream, TreeMap<Character, Integer> frequencyTable) {
//    // header: charDigitsCount codeDigitsCount
//    int[] headerLength = getHeaderLength(frequencyTable); // [3, 3]
//    String charDigitsByteString = intToBinaryString(headerLength[0], 8);
//    String freqDigitsByteString = intToBinaryString(headerLength[1], 8);
//    String header = charDigitsByteString + freqDigitsByteString;
//    StringBuilder rows = new StringBuilder();
//    for (var entry : frequencyTable.entrySet()) {
//      String charBits = intToBinaryString(entry.getKey(), headerLength[0]);
//      String freqBits = intToBinaryString(entry.getValue(), headerLength[1]);
//      rows.append(charBits).append(freqBits);
//    }
//    String endOfRows = intToBinaryString(0, headerLength[0] + headerLength[1]);
//    rows.append(endOfRows);
//    String bits = header + rows + compressedStream;
//    byte[] bytes = convertBitsToBytes(bits);
//    writeBinaryFile(path, bytes);
//  }
//
//  public CompressResult readStandardHuffmanFile(Path path) {
//    byte[] bytes = readBinaryFile(path);
//    String bits = convertBytesToBits(bytes);
//    int charDigitsCount = Integer.parseInt(bits.substring(0, 8), 2);
//    int freqDigitsCount = Integer.parseInt(bits.substring(8, 16), 2);
//    int rowLength = charDigitsCount + freqDigitsCount;
//    // Read frequency table
//    TreeMap<Character, Integer> frequencies = new TreeMap<>();
//    int i;
//    for (i = 16; i < bits.length(); i += rowLength) {
//      String row = bits.substring(i, i + rowLength);
//      if (row.equals("0".repeat(rowLength))) {
//        i += rowLength;
//        break;
//      }
//      String charBits = row.substring(0, charDigitsCount);
//      String freqBits = row.substring(charDigitsCount, rowLength);
//      frequencies.put((char) Integer.parseInt(charBits, 2), Integer.parseInt(freqBits, 2));
//    }
//    // Read compressed stream
//    String compressedStream = bits.substring(i);
//    return new CompressResult(compressedStream, frequencies);
//  }

  int getCharDigitsLength(TreeMap<Character, String> huffmanCodes) {
    int maxCharDigitsLength = 0;
    for (Character c : huffmanCodes.keySet()) {
      maxCharDigitsLength = Math.max(maxCharDigitsLength, c);
    }
    return maxCharDigitsLength;
  }

  public void writeStandardHuffmanFile(Path path, String compressedStream, TreeMap<Character, String> huffmanCodes) {
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

  public CompressResult readStandardHuffmanFile(Path path) {
    byte[] bytes = readBinaryFile(path);
    String bits = convertBytesToBits(bytes);
    TreeMap<String, Character> huffmanCodes = new TreeMap<>();
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
    return new CompressResult<>(compressedStream, huffmanCodes);
  }
}
