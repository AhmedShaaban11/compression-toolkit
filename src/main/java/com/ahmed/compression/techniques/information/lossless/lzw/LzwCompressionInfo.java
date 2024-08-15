package com.ahmed.compression.techniques.information.lossless.lzw;

import com.ahmed.compression.techniques.information.Info;

import java.util.ArrayList;

public record LzwCompressionInfo(int tagBits, ArrayList<Integer> tags) implements Info {

}
