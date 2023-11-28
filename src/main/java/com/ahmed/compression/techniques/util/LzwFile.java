package com.ahmed.compression.techniques.util;

import java.io.*;
import java.util.ArrayList;

public class LzwFile extends BinaryFile {
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

  public void writeTags(String path, ArrayList<Integer> nums, int tagBits) {
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

}
