package com.ahmed.compression.toolkit.tech;

import com.ahmed.compression.toolkit.info.CompressedFileInfo;
import com.ahmed.compression.toolkit.info.CompressionInfo;
import com.ahmed.compression.toolkit.info.DecompressedFileInfo;
import com.ahmed.compression.toolkit.info.DecompressionInfo;

public interface Technique<
    CompressedFileInfoT extends CompressedFileInfo,
    DecompressedFileInfoT extends DecompressedFileInfo,
    CompressionInfoT extends CompressionInfo,
    DecompressionInfoT extends DecompressionInfo
    > {
  CompressionInfoT compress(DecompressedFileInfoT decompressedFileInfo);
  DecompressionInfoT decompress(CompressedFileInfoT compressedFileInfo);
}
