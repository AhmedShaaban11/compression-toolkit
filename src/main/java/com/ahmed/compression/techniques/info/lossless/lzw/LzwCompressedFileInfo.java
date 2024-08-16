package com.ahmed.compression.techniques.info.lossless.lzw;

import com.ahmed.compression.techniques.info.CompressedFileInfo;

import java.util.ArrayList;

public record LzwCompressedFileInfo(ArrayList<Integer> tags) implements CompressedFileInfo {
}
