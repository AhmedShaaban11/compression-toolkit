package com.ahmed.compression.techniques.io.lossless;

import com.ahmed.compression.techniques.information.lossless.standardhuffman.StandardHuffmanCompressedFileInfo;
import com.ahmed.compression.techniques.information.lossless.standardhuffman.StandardHuffmanCompressionInfo;
import com.ahmed.compression.techniques.information.lossless.standardhuffman.StandardHuffmanDecompressedFileInfo;
import com.ahmed.compression.techniques.information.lossless.standardhuffman.StandardHuffmanDecompressionInfo;
import com.ahmed.compression.techniques.io.BinaryFile;
import com.ahmed.compression.techniques.io.ReaderWriter;

import java.nio.file.Path;
import java.util.HashMap;

public class StandardHuffmanFile extends BinaryFile implements ReaderWriter<StandardHuffmanCompressedFileInfo, StandardHuffmanDecompressedFileInfo, StandardHuffmanCompressionInfo, StandardHuffmanDecompressionInfo> {
  @Override
  public StandardHuffmanCompressedFileInfo readCompressedFile(Path path) {
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
  public StandardHuffmanDecompressedFileInfo readDecompressedFile(Path path) {
    return new StandardHuffmanDecompressedFileInfo(readRegularFile(path));
  }

  @Override
  public void writeCompressedFile(Path path, StandardHuffmanCompressionInfo info) {
    StringBuilder rows = new StringBuilder();
    for (var entry : info.huffmanCodes().entrySet()) {
      String charBits = intToBinaryString(entry.getKey(), 8);
      String codeLengthBits = intToBinaryString(entry.getValue().length(), 8);
      rows.append(charBits).append(codeLengthBits).append(entry.getValue());
    }
    String endOfRows = intToBinaryString(0, 16);
    rows.append(endOfRows);
    String bits = rows + info.compressedStream();
    byte[] bytes = convertBitsToBytes(bits);
    writeBinaryFile(path, bytes);
  }

  @Override
  public void writeDecompressedFile(Path path, StandardHuffmanDecompressionInfo info) {
    writeRegularFile(path, info.data());
  }
}
