package com.ahmed.compression.techniques.info.lossless.lzw;

import com.ahmed.compression.techniques.info.CompressionInfo;

import java.util.ArrayList;

public record LzwCompressionInfo(int tagBits, ArrayList<Integer> tags) implements CompressionInfo {

}
