package com.ahmed.compression.techniques;

import com.ahmed.compression.techniques.information.Info;
import com.ahmed.compression.techniques.information.lossless.lzw.LzwCompressedFileInfo;
import com.ahmed.compression.techniques.information.lossless.lzw.LzwCompressionInfo;
import com.ahmed.compression.techniques.information.lossless.lzw.LzwDecompressedFileInfo;
import com.ahmed.compression.techniques.information.lossless.lzw.LzwDecompressionInfo;
import com.ahmed.compression.techniques.io.LzwFile;
import com.ahmed.compression.techniques.io.ReaderWriter;
import com.ahmed.compression.techniques.tech.NewTechnique;
import com.ahmed.compression.techniques.tech.lossless.Lzw;

import java.nio.file.Path;

public record Facade<
    CompressedFileInfo extends Info,
    DecompressedFileInfo extends Info,
    CompressionInfo extends Info,
    DecompressionInfo extends Info,
    ReaderWriterT extends ReaderWriter<CompressedFileInfo, DecompressedFileInfo, CompressionInfo, DecompressionInfo>,
    TechniqueT extends NewTechnique<CompressedFileInfo, DecompressedFileInfo, CompressionInfo, DecompressionInfo>
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

  public static void main(String[] args) {
    Facade<LzwCompressedFileInfo, LzwDecompressedFileInfo, LzwCompressionInfo, LzwDecompressionInfo, LzwFile, Lzw>
        lzwFacade = new Facade<>(new LzwFile(), new Lzw());
    lzwFacade.compress(Path.of("input/input_compression.txt"), Path.of("input/output_compression.bin"));
    lzwFacade.decompress(Path.of("input/output_compression.bin"), Path.of("input/output_decompression.txt"));
  }
}
