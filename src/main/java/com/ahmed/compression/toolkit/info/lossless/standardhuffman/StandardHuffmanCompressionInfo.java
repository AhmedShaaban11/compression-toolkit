package com.ahmed.compression.toolkit.info.lossless.standardhuffman;

import com.ahmed.compression.toolkit.info.CompressionInfo;

import java.util.Map;

public record StandardHuffmanCompressionInfo(String compressedStream, Map<Character, String> huffmanCodes) implements CompressionInfo {
}
