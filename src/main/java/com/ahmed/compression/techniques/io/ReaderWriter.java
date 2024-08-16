package com.ahmed.compression.techniques.io;

import com.ahmed.compression.techniques.info.CompressedFileInfo;
import com.ahmed.compression.techniques.info.CompressionInfo;
import com.ahmed.compression.techniques.info.DecompressedFileInfo;
import com.ahmed.compression.techniques.info.DecompressionInfo;

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
