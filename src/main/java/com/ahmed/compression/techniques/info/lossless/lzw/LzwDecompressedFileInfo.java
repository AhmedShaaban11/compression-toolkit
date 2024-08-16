package com.ahmed.compression.techniques.info.lossless.lzw;

import com.ahmed.compression.techniques.info.DecompressedFileInfo;

public record LzwDecompressedFileInfo(String data) implements DecompressedFileInfo {
}
