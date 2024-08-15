package com.ahmed.compression.techniques.io;

import java.nio.file.Files;
import java.nio.file.Path;

abstract public class BinaryFile {
  public String readRegularFile(Path path) {
    String data = "";
    try {
      data = Files.readString(path);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return data;
  }

  protected void writeRegularFile(Path path, String data) {
    try {
      Files.writeString(path, data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected byte[] readBinaryFile(Path path) {
    byte[] data = null;
    try {
      data = Files.readAllBytes(path);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return data;
  }

  protected void writeBinaryFile(Path path, byte[] data) {
    try {
      Files.write(path, data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected static String intToBinaryString(int n, int digitsCount) {
    String bin = Integer.toBinaryString(n);
    if (bin.length() < digitsCount) {
      bin = "0".repeat(digitsCount - bin.length()) + bin;
    }
    return bin;
  }

  protected byte[] convertBitsToBytes(String bits) {
    int sz = (bits.length() + 7) / 8;
    byte[] bytes = new byte[sz];
    int byteIdx = 0;
    while (bits.length() % 8 != 0) { bits += "0"; } // Adds trailing zeros after last tag to complete a byte
    for (int i = 0; i < bits.length(); i += 8) {
      String byteString = bits.substring(i, i + 8);
      byte byteValue = (byte) Integer.parseInt(byteString, 2);
      bytes[byteIdx++] = byteValue;
    }
    return bytes;
  }

  protected String convertBytesToBits(byte[] bytes) {
    StringBuilder bitsBuilder = new StringBuilder();
    for (int i = 0; i < bytes.length; ++i) {
      String bin = Integer.toBinaryString(bytes[i]);
      if (bin.length() > 8) { // Handle negative values of bytes
        bin = bin.substring(24);
      }
      String trail = "";
      for (int j = 8; j > bin.length(); --j) {
        trail += "0";
      }
      bitsBuilder.append(trail).append(bin);
    }
    return bitsBuilder.toString();
  }

}
