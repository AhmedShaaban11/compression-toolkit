package com.ahmed.compression.techniques.tech.lossless;

import com.ahmed.compression.techniques.info.lossless.lzw.LzwCompressedFileInfo;
import com.ahmed.compression.techniques.info.lossless.lzw.LzwCompressionInfo;
import com.ahmed.compression.techniques.info.lossless.lzw.LzwDecompressedFileInfo;
import com.ahmed.compression.techniques.info.lossless.lzw.LzwDecompressionInfo;
import com.ahmed.compression.techniques.tech.Technique;

import java.util.ArrayList;
import java.util.HashMap;

public class Lzw implements Technique<LzwCompressedFileInfo, LzwDecompressedFileInfo, LzwCompressionInfo, LzwDecompressionInfo> {
  private HashMap<String, Integer> getAsciiCharsDictionary() {
    HashMap<String, Integer> dictionary = new HashMap<>();
    // Fill dictionary with ascii codes from 0 to 127
    for (int i = 0; i < 128; ++i) {
      dictionary.put(String.valueOf((char) i), i);
    }
    return dictionary;
  }

  private HashMap<Integer, String> getAsciiIntegerDictionary() {
    HashMap<Integer, String> dictionary = new HashMap<>();
    // Fill dictionary with ascii codes from 0 to 127
    for (int i = 0; i < 128; ++i) {
      dictionary.put(i, String.valueOf((char) i));
    }
    return dictionary;
  }

  @Override
  public LzwCompressionInfo compress(LzwDecompressedFileInfo fileInfo) {
    HashMap<String, Integer> dictionary = getAsciiCharsDictionary();
    ArrayList<Integer> tags = new ArrayList<>();
    int tagBits = 7;
    int dictionaryCapacity = (int) Math.pow(2, tagBits);
    String prvCode = "";
    for (int i = 0; i < fileInfo.data().length(); ++i) {
      char curChar = fileInfo.data().charAt(i);
      String curCode = prvCode + curChar;
      if (dictionary.containsKey(curCode)) {
        prvCode = curCode;
        continue;
      }
      if (!prvCode.isEmpty()) { tags.add(dictionary.get(prvCode)); } // Prevents first char from putting it in the dictionary twice
      dictionary.put(curCode, dictionary.size());
      prvCode = String.valueOf(curChar);
      // Increase tagBits
      if (dictionaryCapacity < dictionary.size()) {
        tagBits += 1;
        dictionaryCapacity *= 2;
      }
    }
    // Case last code is in the dictionary
    if (!prvCode.isEmpty()) {
      tags.add(dictionary.get(prvCode));
    }
    return new LzwCompressionInfo(tagBits, tags);
  }

  @Override
  public LzwDecompressionInfo decompress(LzwCompressedFileInfo fileInfo) {
    HashMap<Integer, String> dictionary = getAsciiIntegerDictionary();
    StringBuilder decompressed = new StringBuilder();
    decompressed.append(dictionary.get(fileInfo.tags().get(0)));
    for (int i = 1; i < fileInfo.tags().size(); ++i) {
      String curCode = dictionary.get(fileInfo.tags().get(i));
      String prvCode = dictionary.get(fileInfo.tags().get(i - 1));
      char lastChar = curCode != null ? curCode.charAt(0) : prvCode.charAt(0); // Handling the case of: code not found
      dictionary.put(dictionary.size(), prvCode + lastChar); // Put first for handling null case
      decompressed.append(dictionary.get(fileInfo.tags().get(i))); // Not using cur because it might be null
    }
    return new LzwDecompressionInfo(decompressed.toString());
  }
}
