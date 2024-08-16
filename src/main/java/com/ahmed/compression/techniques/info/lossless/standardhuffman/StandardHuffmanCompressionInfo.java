package com.ahmed.compression.techniques.info.lossless.standardhuffman;

import com.ahmed.compression.techniques.info.CompressionInfo;

import java.util.Map;

public record StandardHuffmanCompressionInfo(String compressedStream, Map<Character, String> huffmanCodes) implements CompressionInfo {
}
