package com.ahmed.compression.techniques.io.lossless;

import com.ahmed.compression.techniques.info.lossless.lz77.*;
import com.ahmed.compression.techniques.io.AbstractIO;
import com.ahmed.compression.techniques.io.ReaderWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Lz77IO extends AbstractIO implements ReaderWriter<Lz77CompressedFileInfo, Lz77DecompressedFileInfo, Lz77CompressionInfo, Lz77DecompressionInfo> {
  @Override
  public Lz77CompressedFileInfo readCompressedFile(Path path) {
    ArrayList<Tag> tags = new ArrayList<>();
    try {
      List<String> lines = Files.readAllLines(path);
      for (String line : lines) {
        // handle \n as the next character
        if (!line.startsWith("<")) { continue; }
        if (!line.endsWith(">")) { line += ">"; }
        // extract position, length, and next character
        int i = 1;
        String strPos = "", strLen = "";
        for (; line.charAt(i) != ','; ++i) { strPos += line.charAt(i); }
        for (++i; line.charAt(i) != ','; ++i) { strLen += line.charAt(i); }
        int pos = Integer.parseInt(strPos);
        int len = Integer.parseInt(strLen);
        char nxt = line.charAt(i + 1);
        Tag tag = new Tag(pos, len, nxt);
        tags.add(tag);
      }
    } catch (IOException e) {
      System.out.println("ERROR: " + path + ": cannot be open");
    } catch (NullPointerException e) {
      System.out.println("ERROR: path parameter cannot be null");
    }
    return new Lz77CompressedFileInfo(tags);
  }

  @Override
  public Lz77DecompressedFileInfo readDecompressedFile(Path path) {
    return new Lz77DecompressedFileInfo(readRegularFile(path));
  }

  @Override
  public void writeCompressedFile(Path path, Lz77CompressionInfo info) {
    try {
      FileWriter writer = new FileWriter(path.toString());
      for (Tag tag : info.tags()) {
        writer.write(tag.toString() + "\n");
      }
      writer.close();
    } catch (IOException e) {
      System.out.println("ERROR: " + path + ": cannot be open");
    } catch (NullPointerException e) {
      System.out.println("ERROR: path parameter cannot be null");
    }
  }

  @Override
  public void writeDecompressedFile(Path path, Lz77DecompressionInfo info) {
    writeRegularFile(path, info.data());
  }
}
