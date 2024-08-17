package com.ahmed.compression.toolkit.info.lossless.lz77;

import com.ahmed.compression.toolkit.info.CompressionInfo;

import java.util.ArrayList;

public record Lz77CompressionInfo(ArrayList<Tag> tags) implements CompressionInfo {
}
