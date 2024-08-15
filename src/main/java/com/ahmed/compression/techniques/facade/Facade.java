package com.ahmed.compression.techniques.facade;

import com.ahmed.compression.techniques.information.Info;
import com.ahmed.compression.techniques.io.ReaderWriter;
import com.ahmed.compression.techniques.tech.Technique;

import java.nio.file.Path;

public record Facade<
    CompressedFileInfo extends Info,
    DecompressedFileInfo extends Info,
    CompressionInfo extends Info,
    DecompressionInfo extends Info,
    ReaderWriterT extends ReaderWriter<CompressedFileInfo, DecompressedFileInfo, CompressionInfo, DecompressionInfo>,
    TechniqueT extends Technique<CompressedFileInfo, DecompressedFileInfo, CompressionInfo, DecompressionInfo>
    >(ReaderWriterT readerWriter, TechniqueT technique) {

  public void compress(Path inputFilePath, Path outputFilePath) {
    DecompressedFileInfo decompressedFileInfo = readerWriter.readDecompressedFile(inputFilePath);
    CompressionInfo compressionInfo = technique.compress(decompressedFileInfo);
    readerWriter.writeCompressedFile(outputFilePath, compressionInfo);
  }

  public void decompress(Path inputFilePath, Path outputFilePath) {
    CompressedFileInfo compressedFileInfo = readerWriter.readCompressedFile(inputFilePath);
    DecompressionInfo decompressionInfo = technique.decompress(compressedFileInfo);
    readerWriter.writeDecompressedFile(outputFilePath, decompressionInfo);
  }
}
