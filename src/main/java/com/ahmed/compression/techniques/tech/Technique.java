package com.ahmed.compression.techniques.tech;

import com.ahmed.compression.techniques.info.CompressedFileInfo;
import com.ahmed.compression.techniques.info.CompressionInfo;
import com.ahmed.compression.techniques.info.DecompressedFileInfo;
import com.ahmed.compression.techniques.info.DecompressionInfo;

public interface Technique<
    CompressedFileInfoT extends CompressedFileInfo,
    DecompressedFileInfoT extends DecompressedFileInfo,
    CompressionInfoT extends CompressionInfo,
    DecompressionInfoT extends DecompressionInfo
    > {
  CompressionInfoT compress(DecompressedFileInfoT decompressedFileInfo);
  DecompressionInfoT decompress(CompressedFileInfoT compressedFileInfo);
}
