package com.ahmed.compression.techniques.facade;

import com.ahmed.compression.techniques.info.CompressedFileInfo;
import com.ahmed.compression.techniques.info.CompressionInfo;
import com.ahmed.compression.techniques.info.DecompressedFileInfo;
import com.ahmed.compression.techniques.info.DecompressionInfo;
import com.ahmed.compression.techniques.io.ReaderWriter;
import com.ahmed.compression.techniques.tech.Technique;

import java.nio.file.Path;

public record Facade<
    CompressedFileInfoT extends CompressedFileInfo,
    DecompressedFileInfoT extends DecompressedFileInfo,
    CompressionInfoT extends CompressionInfo,
    DecompressionInfoT extends DecompressionInfo,
    ReaderWriterT extends ReaderWriter<CompressedFileInfoT, DecompressedFileInfoT, CompressionInfoT, DecompressionInfoT>,
    TechniqueT extends Technique<CompressedFileInfoT, DecompressedFileInfoT, CompressionInfoT, DecompressionInfoT>
    >(ReaderWriterT readerWriter, TechniqueT technique) {

  public void compress(Path inputFilePath, Path outputFilePath) {
    DecompressedFileInfoT decompressedFileInfo = readerWriter.readDecompressedFile(inputFilePath);
    CompressionInfoT compressionInfo = technique.compress(decompressedFileInfo);
    readerWriter.writeCompressedFile(outputFilePath, compressionInfo);
  }

  public void decompress(Path inputFilePath, Path outputFilePath) {
    CompressedFileInfoT compressedFileInfo = readerWriter.readCompressedFile(inputFilePath);
    DecompressionInfoT decompressionInfo = technique.decompress(compressedFileInfo);
    readerWriter.writeDecompressedFile(outputFilePath, decompressionInfo);
  }
}
