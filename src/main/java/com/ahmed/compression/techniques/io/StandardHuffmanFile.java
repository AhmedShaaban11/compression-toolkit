package com.ahmed.compression.techniques.io;

import com.ahmed.compression.techniques.information.lossless.standardhuffman.StandardHuffmanCompressedFileInfo;
import com.ahmed.compression.techniques.information.lossless.standardhuffman.StandardHuffmanCompressionInfo;
import com.ahmed.compression.techniques.information.lossless.standardhuffman.StandardHuffmanDecompressedFileInfo;
import com.ahmed.compression.techniques.information.lossless.standardhuffman.StandardHuffmanDecompressionInfo;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class StandardHuffmanFile extends BinaryFile implements ReaderWriter<StandardHuffmanCompressedFileInfo, StandardHuffmanDecompressedFileInfo, StandardHuffmanCompressionInfo, StandardHuffmanDecompressionInfo> {

  public void writeStandardHuffmanFile(Path path, String compressedStream, Map<Character, String> huffmanCodes) {
    StringBuilder rows = new StringBuilder();
    for (var entry : huffmanCodes.entrySet()) {
      String charBits = intToBinaryString(entry.getKey(), 8);
      String codeLengthBits = intToBinaryString(entry.getValue().length(), 8);
      rows.append(charBits).append(codeLengthBits).append(entry.getValue());
    }
    String endOfRows = intToBinaryString(0, 16);
    rows.append(endOfRows);
    String bits = rows + compressedStream;
    byte[] bytes = convertBitsToBytes(bits);
    writeBinaryFile(path, bytes);
  }

  public StandardHuffmanCompressedFileInfo readStandardHuffmanFile(Path path) {
    byte[] bytes = readBinaryFile(path);
    String bits = convertBytesToBits(bytes);
    HashMap<String, Character> huffmanCodes = new HashMap<>();
    // Read huffmanCodes
    int i = 0;
    while (true) {
      String charBits = bits.substring(i, i += 8);
      Character c = (char) Integer.parseInt(charBits, 2);
      String codeLengthBits = bits.substring(i, i += 8);
      int codeLength = Integer.parseInt(codeLengthBits, 2);
      String code = bits.substring(i, i += codeLength);
      if (code.isEmpty()) {
        break;
      }
      huffmanCodes.put(code, c);
    }
    // Read compressed stream
    String compressedStream = bits.substring(i);
    return new StandardHuffmanCompressedFileInfo(compressedStream, huffmanCodes);
  }

  @Override
  public StandardHuffmanCompressedFileInfo readCompressedFile(Path path) {
    return readStandardHuffmanFile(path);
  }

  @Override
  public StandardHuffmanDecompressedFileInfo readDecompressedFile(Path path) {
    return new StandardHuffmanDecompressedFileInfo(readRegularFile(path));
  }

  @Override
  public void writeCompressedFile(Path path, StandardHuffmanCompressionInfo info) {
    writeStandardHuffmanFile(path, info.compressedStream(), info.huffmanCodes());
  }

  @Override
  public void writeDecompressedFile(Path path, StandardHuffmanDecompressionInfo info) {
    writeRegularFile(path, info.data());
  }
}
