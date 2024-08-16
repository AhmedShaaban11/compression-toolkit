package com.ahmed.compression.techniques.info.lossless.lz77;

import com.ahmed.compression.techniques.info.CompressionInfo;

import java.util.ArrayList;

public record Lz77CompressionInfo(ArrayList<Tag> tags) implements CompressionInfo {
}
