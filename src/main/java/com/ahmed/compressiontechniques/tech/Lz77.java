package com.ahmed.compressiontechniques.tech;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class Tag {
  int position;
  int length;
  char nextChar;

  public Tag(int p, int l, char nc) {
    position = p;
    length = l;
    nextChar = nc;
  }

  public Tag(String line) {
    int i = 1;
    String strPos = "", strLen = "";
    for (; line.charAt(i) != ','; ++i) { strPos += line.charAt(i); }
    for (++i; line.charAt(i) != ','; ++i) { strLen += line.charAt(i); }
    position = Integer.parseInt(strPos);
    length = Integer.parseInt(strLen);
    nextChar = line.charAt(i + 1);
  }

  public String toString() {
    return "<" + position + "," + length + "," + nextChar + ">";
  }

  public int getPosition() { return position; }

  public int getLength() { return length; }

  public char getNextChar() { return nextChar; }

}

public class Lz77 implements Technique {

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
      int i = str.length() - tag.getPosition();
      for (int j = 0; j < tag.getLength(); ++j) {
        str.append(str.charAt(i++));
      }
      if (tag.getNextChar() != '\0') { str.append(tag.getNextChar()); }
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
    try {
      List<String> lines = Files.readAllLines(Path.of(path));
      ArrayList<Tag> tags = new ArrayList<>();
      for (String line : lines) {
        // handle \n as the next character
        if (!line.startsWith("<")) { continue; }
        if (!line.endsWith(">")) { line += ">"; }
        Tag tag = new Tag(line);
        tags.add(tag);
      }
      return decompress(tags);
    } catch (IOException e) {
      System.out.println("ERROR: " + path + ": cannot be open");
    } catch (NullPointerException e) {
      System.out.println("ERROR: path parameter cannot be null");
    }
    return "";
  }

  private void storeCompressAtFile(String path, ArrayList<Tag> tags) {
    try {
      FileWriter writer = new FileWriter(path);
      for (Tag tag : tags) {
        writer.write(tag.toString() + "\n");
      }
      writer.close();
    } catch (IOException e) {
      System.out.println("ERROR: " + path + ": cannot be open");
    } catch (NullPointerException e) {
      System.out.println("ERROR: path parameter cannot be null");
    }
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
}
