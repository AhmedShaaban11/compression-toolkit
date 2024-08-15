package com.ahmed.compression.techniques.tech.lossless;

import com.ahmed.compression.techniques.information.lz77.*;
import com.ahmed.compression.techniques.io.Lz77File;
import com.ahmed.compression.techniques.tech.NewTechnique;
import com.ahmed.compression.techniques.tech.Technique;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Lz77 implements Technique, NewTechnique<Lz77CompressedFileInfo, Lz77DecompressedFileInfo, Lz77CompressionInfo, Lz77DecompressionInfo> {
  private Lz77File lz77File;

  public Lz77() {
    this.lz77File = new Lz77File();
  }

  private boolean validRange(int i, int j, int k, int n) {
    return j + k < i && i + k < n;
  }

  private ArrayList<Tag> compress(String str) {
    ArrayList<Tag> tags = new ArrayList<>();
    int n = str.length();
    for (int i = 0; i < n;) {
      int position = 0, length = 0;
      for (int j = 0; j < i; ++j) {
        int k = 0;
        while (validRange(i, j, k, n) && str.charAt(j + k) == str.charAt(i + k)) {
          k += 1;
        }
        if (k >= length && k != 0) {
          position = i - j;
          length = k;
        }
      }
      char nxt = i + length < n ? str.charAt(i + length) : '\0';
      tags.add(new Tag(position, length, nxt));
      i += length + 1;
    }
    return tags;
  }

  private String decompress(ArrayList<Tag> tags) {
    if (tags.isEmpty()) { return ""; }
    StringBuilder str = new StringBuilder();
    for (Tag tag : tags) {
      int i = str.length() - tag.position();
      for (int j = 0; j < tag.length(); ++j) {
        str.append(str.charAt(i++));
      }
      if (tag.nextChar() != '\0') { str.append(tag.nextChar()); }
    }
    return str.toString();
  }

  private ArrayList<Tag> compressFromFile(String path) {
    try {
      String data = Files.readString(Path.of(path));
      return compress(data);
    } catch (IOException e) {
      System.out.println("ERROR: " + path + ": cannot be open");
    } catch (NullPointerException e) {
      System.out.println("ERROR: path parameter cannot be null");
    } catch (Exception e) {
      System.out.println("ERROR: " + e.getMessage());
    }
    return new ArrayList<>();
  }

  private String decompressFromFile(String path) {
    var decompressionInfo = lz77File.readCompressedFile(Path.of(path));
    return decompress(decompressionInfo.tags());
  }

  private void storeCompressAtFile(String path, ArrayList<Tag> tags) {
    lz77File.writeCompressedFile(Path.of(path), new Lz77CompressionInfo(tags));
  }

  private void storeDecompressAtFile(String path, String decompressed) {
    try {
      FileWriter writer = new FileWriter(path);
      writer.write(decompressed);
      writer.close();
    } catch (IOException e) {
      System.out.println("ERROR: " + path + ": cannot be open");
    } catch (NullPointerException e) {
      System.out.println("ERROR: path parameter cannot be null");
    }
  }

  @Override
  public void compress(String inputPath, String outputPath) {
    ArrayList<Tag> tags = compressFromFile(inputPath);
    storeCompressAtFile(outputPath, tags);
  }

  @Override
  public void decompress(String inputPath, String outputPath) {
    String decompressed = decompressFromFile(inputPath);
    storeDecompressAtFile(outputPath, decompressed);
  }

  @Override
  public Lz77CompressionInfo compress(Lz77DecompressedFileInfo info) {
    ArrayList<Tag> tags = compress(info.data());
    return new Lz77CompressionInfo(tags);
  }

  @Override
  public Lz77DecompressionInfo decompress(Lz77CompressedFileInfo info) {
    String data = decompress(info.tags());
    return new Lz77DecompressionInfo(data);
  }
}
