package com.ahmed.compression.toolkit.info.lossless.lz77;

import com.ahmed.compression.toolkit.info.CompressedFileInfo;

import java.util.ArrayList;

public record Lz77CompressedFileInfo(ArrayList<Tag> tags) implements CompressedFileInfo {
}
