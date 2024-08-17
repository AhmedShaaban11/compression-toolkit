package com.ahmed.compression.toolkit.info.lossless.lzw;

import com.ahmed.compression.toolkit.info.CompressedFileInfo;

import java.util.ArrayList;

public record LzwCompressedFileInfo(ArrayList<Integer> tags) implements CompressedFileInfo {
}
