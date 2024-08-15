package com.ahmed.compression.techniques.tech.lossless;

import com.ahmed.compression.techniques.information.lossless.lzw.*;
import com.ahmed.compression.techniques.information.lossless.lzw.LzwDecompressedFileInfo;
import com.ahmed.compression.techniques.tech.NewTechnique;
import com.ahmed.compression.techniques.tech.Technique;
import com.ahmed.compression.techniques.io.LzwFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class Lzw implements Technique, NewTechnique<LzwCompressedFileInfo, LzwDecompressedFileInfo, LzwCompressionInfo, LzwDecompressionInfo> {
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


  private LzwCompressionInfo compress(String str) {
    HashMap<String, Integer> dictionary = getAsciiCharsDictionary();
    ArrayList<Integer> tags = new ArrayList<>();
    int tagBits = 7;
    int dictionaryCapacity = (int) Math.pow(2, tagBits);
    String prvCode = "";
    for (int i = 0; i < str.length(); ++i) {
      char curChar = str.charAt(i);
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
  public void compress(String inputPath, String outputPath) {
    try {
      String data = Files.readString(Path.of(inputPath));
      LzwCompressionInfo compressionInfo = compress(data);
      LzwFile handler = new LzwFile();
      handler.writeTags(outputPath, compressionInfo.tagBits(), compressionInfo.tags());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }

  private LzwDecompressionInfo decompress(ArrayList<Integer> nums) {
    HashMap<Integer, String> dictionary = getAsciiIntegerDictionary();
    StringBuilder decompressed = new StringBuilder();
    decompressed.append(dictionary.get(nums.get(0)));
    for (int i = 1; i < nums.size(); ++i) {
      String curCode = dictionary.get(nums.get(i));
      String prvCode = dictionary.get(nums.get(i - 1));
      char lastChar = curCode != null ? curCode.charAt(0) : prvCode.charAt(0); // Handling the case of: code not found
      dictionary.put(dictionary.size(), prvCode + lastChar); // Put first for handling null case
      decompressed.append(dictionary.get(nums.get(i))); // Not using cur because it might be null
    }
    return new LzwDecompressionInfo(decompressed.toString());
  }

  @Override
  public void decompress(String inputPath, String outputPath) {
    try {
      LzwFile handler = new LzwFile();
      ArrayList<Integer> tags = handler.readTags(inputPath);
      LzwDecompressionInfo decompressionInfo = decompress(tags);
      Files.writeString(Path.of(outputPath), decompressionInfo.data());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public LzwCompressionInfo compress(LzwDecompressedFileInfo fileInfo) {
    return compress(fileInfo.data());
  }

  @Override
  public LzwDecompressionInfo decompress(LzwCompressedFileInfo fileInfo) {
    return decompress(fileInfo.tags());
  }
}
