package com.ahmed.compression.toolkit.info.lossless.lzw;

import com.ahmed.compression.toolkit.info.CompressionInfo;

import java.util.ArrayList;

public record LzwCompressionInfo(int tagBits, ArrayList<Integer> tags) implements CompressionInfo {

}
