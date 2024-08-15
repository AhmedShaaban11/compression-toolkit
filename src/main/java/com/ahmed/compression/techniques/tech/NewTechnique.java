package com.ahmed.compression.techniques.tech;

import com.ahmed.compression.techniques.information.Info;

public interface NewTechnique<
    CompressedFileInfo extends Info,
    DecompressedFileInfo extends Info,
    CompressionInfo extends Info,
    DecompressionInfo extends Info
    > {
  CompressionInfo compress(DecompressedFileInfo decompressedFileInfo);
  DecompressionInfo decompress(CompressedFileInfo compressedFileInfo);
}
