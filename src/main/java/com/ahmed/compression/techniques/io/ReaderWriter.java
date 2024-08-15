package com.ahmed.compression.techniques.io;

import com.ahmed.compression.techniques.information.Info;

import java.nio.file.Path;

public interface ReaderWriter<
    CompressedFileInfo extends Info,
    DecompressedFileInfo extends Info,
    CompressionInfo extends Info,
    DecompressionInfo extends Info
    > {
  CompressedFileInfo readCompressedFile(Path path);
  DecompressedFileInfo readDecompressedFile(Path path);
  void writeCompressedFile(Path path, CompressionInfo compressionInfo);
  void writeDecompressedFile(Path path, DecompressionInfo decompressionInfo);
}
