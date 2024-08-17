package com.ahmed.compression.toolkit.io.lossless;

import com.ahmed.compression.toolkit.info.lossless.lzw.LzwCompressedFileInfo;
import com.ahmed.compression.toolkit.info.lossless.lzw.LzwCompressionInfo;
import com.ahmed.compression.toolkit.info.lossless.lzw.LzwDecompressedFileInfo;
import com.ahmed.compression.toolkit.info.lossless.lzw.LzwDecompressionInfo;
import com.ahmed.compression.toolkit.io.AbstractIO;
import com.ahmed.compression.toolkit.io.ReaderWriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;

public class LzwIO extends AbstractIO implements ReaderWriter<LzwCompressedFileInfo, LzwDecompressedFileInfo, LzwCompressionInfo, LzwDecompressionInfo> {
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

  private ArrayList<Integer> convertBitsToTags(String bits, int tagBits) {
    ArrayList<Integer> nums = new ArrayList<>();
    for (int i = 0; i < bits.length(); i += tagBits) {
      if (i + tagBits > bits.length()) { break; } // Handle trailing zeros after last tag
      String tagString = bits.substring(i, i + tagBits);
      nums.add(Integer.parseInt(tagString, 2));
    }
    return nums;
  }

  @Override
  public LzwCompressedFileInfo readCompressedFile(Path path) {
    ArrayList<Integer> nums = new ArrayList<>();
    try {
      DataInputStream inputStream = new DataInputStream(new FileInputStream(path.toString()));
      int tagBits = inputStream.readByte();
      byte[] bytes = inputStream.readAllBytes();
      inputStream.close();
      String bits = convertBytesToBits(bytes);
      nums = convertBitsToTags(bits, tagBits);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    return new LzwCompressedFileInfo(nums);
  }

  @Override
  public LzwDecompressedFileInfo readDecompressedFile(Path path) {
    return new LzwDecompressedFileInfo(readRegularFile(path));
  }

  @Override
  public void writeCompressedFile(Path path, LzwCompressionInfo info) {
    try {
      String bits = convertTagsToBits(info.tags(), info.tagBits());
      byte[] bytes = convertBitsToBytes(bits);
      DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(path.toString()));
      outputStream.write((byte) info.tagBits());
      outputStream.write(bytes);
      outputStream.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public void writeDecompressedFile(Path path, LzwDecompressionInfo info) {
    writeRegularFile(path, info.data());
  }
}
