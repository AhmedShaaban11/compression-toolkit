package com.ahmed.compression.techniques.information.lz77;

import com.ahmed.compression.techniques.information.Info;

import java.util.ArrayList;

public record Lz77CompressedFileInfo(ArrayList<Tag> tags) implements Info {
}
