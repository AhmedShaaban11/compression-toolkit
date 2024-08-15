package com.ahmed.compression.techniques.tech.lossless;

import com.ahmed.compression.techniques.information.lossless.lz77.*;
import com.ahmed.compression.techniques.io.lossless.Lz77File;
import com.ahmed.compression.techniques.tech.Technique;

import java.util.ArrayList;

public class Lz77 implements Technique<Lz77CompressedFileInfo, Lz77DecompressedFileInfo, Lz77CompressionInfo, Lz77DecompressionInfo> {
  private Lz77File lz77File;

  public Lz77() {
    this.lz77File = new Lz77File();
  }

  private boolean validRange(int i, int j, int k, int n) {
    return j + k < i && i + k < n;
  }

  @Override
  public Lz77CompressionInfo compress(Lz77DecompressedFileInfo info) {
    ArrayList<Tag> tags = new ArrayList<>();
    String data = info.data();
    int n = data.length();
    for (int i = 0; i < n;) {
      int position = 0, length = 0;
      for (int j = 0; j < i; ++j) {
        int k = 0;
        while (validRange(i, j, k, n) && data.charAt(j + k) == data.charAt(i + k)) {
          k += 1;
        }
        if (k >= length && k != 0) {
          position = i - j;
          length = k;
        }
      }
      char nxt = i + length < n ? data.charAt(i + length) : '\0';
      tags.add(new Tag(position, length, nxt));
      i += length + 1;
    }
    return new Lz77CompressionInfo(tags);
  }

  @Override
  public Lz77DecompressionInfo decompress(Lz77CompressedFileInfo info) {
    if (info.tags().isEmpty()) { return new Lz77DecompressionInfo(""); }
    StringBuilder data = new StringBuilder();
    for (Tag tag : info.tags()) {
      int i = data.length() - tag.position();
      for (int j = 0; j < tag.length(); ++j) {
        data.append(data.charAt(i++));
      }
      if (tag.nextChar() != '\0') { data.append(tag.nextChar()); }
    }
    return new Lz77DecompressionInfo(data.toString());
  }
}
