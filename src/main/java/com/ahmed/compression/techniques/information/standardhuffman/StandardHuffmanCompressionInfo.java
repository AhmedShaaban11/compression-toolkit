package com.ahmed.compression.techniques.information.standardhuffman;

import com.ahmed.compression.techniques.information.Info;

import java.util.Map;

public record StandardHuffmanCompressionInfo(String compressedStream, Map<Character, String> huffmanCodes) implements Info {
}
