package com.ahmed.compression.techniques.io;

import com.ahmed.compression.techniques.information.lzw.LzwCompressedFileInfo;
import com.ahmed.compression.techniques.information.lzw.LzwCompressionInfo;
import com.ahmed.compression.techniques.information.lzw.LzwDecompressedFileInfo;
import com.ahmed.compression.techniques.information.lzw.LzwDecompressionInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class LzwFile extends BinaryFile implements ReaderWriter<LzwCompressedFileInfo, LzwDecompressedFileInfo, LzwCompressionInfo, LzwDecompressionInfo> {
  private String convertTagsToBits(ArrayList<Integer> nums, int tag_size) {
    StringBuilder binary_nums = new StringBuilder();
    for (int i = 0; i < nums.size(); ++i) {
      String bin = Integer.toBinaryString(nums.get(i));
      String trail = "";
      for (int j = tag_size; j > bin.length(); --j) {
        trail += "0";
      }
      binary_nums.append(trail).append(bin);
    }
    return binary_nums.toString();
  }

  public void writeTags(String path, int tagBits, ArrayList<Integer> nums) {
    try {
      String bits = convertTagsToBits(nums, tagBits);
      byte[] bytes = convertBitsToBytes(bits);
      DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(path));
      outputStream.write((byte) tagBits);
      outputStream.write(bytes);
      outputStream.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }

  private ArrayList<Integer> convertBitsToTags(String bits, int tagBits) {
    ArrayList<Integer> nums = new ArrayList<>();
    for (int i = 0; i < bits.length(); i += tagBits) {
      if (i + tagBits > bits.length()) { break; } // Handle trailing zeros after last tag
      String tagString = bits.substring(i, i + tagBits);
      nums.add(Integer.parseInt(tagString, 2));
    }
    return nums;
  }

  public ArrayList<Integer> readTags(String path) {
    ArrayList<Integer> nums = new ArrayList<>();
    try {
      DataInputStream inputStream = new DataInputStream(new FileInputStream(path));
      int tagBits = inputStream.readByte();
      byte[] bytes = inputStream.readAllBytes();
      inputStream.close();
      String bits = convertBytesToBits(bytes);
      nums = convertBitsToTags(bits, tagBits);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    return nums;
  }

  @Override
  public LzwCompressedFileInfo readCompressedFile(Path path) {
    return new LzwCompressedFileInfo(readTags(path.toString()));
  }

  @Override
  public LzwDecompressedFileInfo readDecompressedFile(Path path) {
    return new LzwDecompressedFileInfo(readRegularFile(path));
  }

  @Override
  public void writeCompressedFile(Path path, LzwCompressionInfo info) {
    writeTags(path.toString(), info.tagBits(), info.tags());
  }

  @Override
  public void writeDecompressedFile(Path path, LzwDecompressionInfo info) {
    writeRegularFile(path, info.data());
  }
}
