package com.ahmed.compression.techniques.info.lossless.standardhuffman;

import com.ahmed.compression.techniques.info.CompressedFileInfo;

import java.util.Map;

public record StandardHuffmanCompressedFileInfo(String compressedStream, Map<String, Character> huffmanCodes) implements CompressedFileInfo {
}
