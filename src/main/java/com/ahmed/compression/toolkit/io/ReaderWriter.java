package com.ahmed.compression.toolkit.io;

import com.ahmed.compression.toolkit.info.CompressedFileInfo;
import com.ahmed.compression.toolkit.info.CompressionInfo;
import com.ahmed.compression.toolkit.info.DecompressedFileInfo;
import com.ahmed.compression.toolkit.info.DecompressionInfo;

import java.nio.file.Path;

public interface ReaderWriter<
    CompressedFileInfoT extends CompressedFileInfo,
    DecompressedFileInfoT extends DecompressedFileInfo,
    CompressionInfoT extends CompressionInfo,
    DecompressionInfoT extends DecompressionInfo
    > {
  CompressedFileInfoT readCompressedFile(Path path);
  DecompressedFileInfoT readDecompressedFile(Path path);
  void writeCompressedFile(Path path, CompressionInfoT compressionInfo);
  void writeDecompressedFile(Path path, DecompressionInfoT decompressionInfo);
}
