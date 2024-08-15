package com.ahmed.compression.techniques.information.lossless.standardhuffman;

import com.ahmed.compression.techniques.information.Info;

import java.util.Map;

public record StandardHuffmanCompressedFileInfo(String compressedStream, Map<String, Character> huffmanCodes) implements Info {
}
