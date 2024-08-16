package com.ahmed.compression.techniques.info.lossless.lz77;

import com.ahmed.compression.techniques.info.CompressedFileInfo;

import java.util.ArrayList;

public record Lz77CompressedFileInfo(ArrayList<Tag> tags) implements CompressedFileInfo {
}
