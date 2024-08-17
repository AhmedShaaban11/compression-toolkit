package com.ahmed.compression.toolkit.info.lossless.standardhuffman;

import com.ahmed.compression.toolkit.info.CompressedFileInfo;

import java.util.Map;

public record StandardHuffmanCompressedFileInfo(String compressedStream, Map<String, Character> huffmanCodes) implements CompressedFileInfo {
}
