package com.ahmed.compression.techniques.facade;

import com.ahmed.compression.techniques.info.lossless.lz77.Lz77CompressedFileInfo;
import com.ahmed.compression.techniques.info.lossless.lz77.Lz77CompressionInfo;
import com.ahmed.compression.techniques.info.lossless.lz77.Lz77DecompressedFileInfo;
import com.ahmed.compression.techniques.info.lossless.lz77.Lz77DecompressionInfo;
import com.ahmed.compression.techniques.info.lossless.lzw.LzwCompressedFileInfo;
import com.ahmed.compression.techniques.info.lossless.lzw.LzwCompressionInfo;
import com.ahmed.compression.techniques.info.lossless.lzw.LzwDecompressedFileInfo;
import com.ahmed.compression.techniques.info.lossless.lzw.LzwDecompressionInfo;
import com.ahmed.compression.techniques.info.lossless.standardhuffman.StandardHuffmanCompressedFileInfo;
import com.ahmed.compression.techniques.info.lossless.standardhuffman.StandardHuffmanCompressionInfo;
import com.ahmed.compression.techniques.info.lossless.standardhuffman.StandardHuffmanDecompressedFileInfo;
import com.ahmed.compression.techniques.info.lossless.standardhuffman.StandardHuffmanDecompressionInfo;
import com.ahmed.compression.techniques.info.lossy.twodprediction.TwoDPredictionCompressedFileInfo;
import com.ahmed.compression.techniques.info.lossy.twodprediction.TwoDPredictionCompressionInfo;
import com.ahmed.compression.techniques.info.lossy.twodprediction.TwoDPredictionDecompressedFileInfo;
import com.ahmed.compression.techniques.info.lossy.twodprediction.TwoDPredictionDecompressionInfo;
import com.ahmed.compression.techniques.info.lossy.vectorquantization.VectorQuantizationCompressedFileInfo;
import com.ahmed.compression.techniques.info.lossy.vectorquantization.VectorQuantizationCompressionInfo;
import com.ahmed.compression.techniques.info.lossy.vectorquantization.VectorQuantizationDecompressedFileInfo;
import com.ahmed.compression.techniques.info.lossy.vectorquantization.VectorQuantizationDecompressionInfo;
import com.ahmed.compression.techniques.io.lossless.Lz77IO;
import com.ahmed.compression.techniques.io.lossless.LzwIO;
import com.ahmed.compression.techniques.io.lossless.StandardHuffmanIO;
import com.ahmed.compression.techniques.io.lossy.TwoDPredictionIO;
import com.ahmed.compression.techniques.io.lossy.VectorQuantizationIO;
import com.ahmed.compression.techniques.tech.lossless.Lz77;
import com.ahmed.compression.techniques.tech.lossless.Lzw;
import com.ahmed.compression.techniques.tech.lossless.StandardHuffman;
import com.ahmed.compression.techniques.tech.lossy.TwoDPrediction;
import com.ahmed.compression.techniques.tech.lossy.VectorQuantization;

public class FacadeFactory {
  private final Facade<Lz77CompressedFileInfo, Lz77DecompressedFileInfo, Lz77CompressionInfo, Lz77DecompressionInfo, Lz77IO, Lz77> lz77Facade;
  private final Facade<LzwCompressedFileInfo, LzwDecompressedFileInfo, LzwCompressionInfo, LzwDecompressionInfo, LzwIO, Lzw> lzwFacade;
  private final Facade<StandardHuffmanCompressedFileInfo, StandardHuffmanDecompressedFileInfo, StandardHuffmanCompressionInfo, StandardHuffmanDecompressionInfo, StandardHuffmanIO, StandardHuffman> standardHuffmanFacade;
  private final Facade<VectorQuantizationCompressedFileInfo, VectorQuantizationDecompressedFileInfo, VectorQuantizationCompressionInfo, VectorQuantizationDecompressionInfo, VectorQuantizationIO, VectorQuantization> vectorQuantizationFacade;
  private final Facade<TwoDPredictionCompressedFileInfo, TwoDPredictionDecompressedFileInfo, TwoDPredictionCompressionInfo, TwoDPredictionDecompressionInfo, TwoDPredictionIO, TwoDPrediction> twoDPredictionFacade;

  public FacadeFactory() {
    this.lz77Facade = new Facade<>(new Lz77IO(), new Lz77());
    this.lzwFacade = new Facade<>(new LzwIO(), new Lzw());
    this.standardHuffmanFacade = new Facade<>(new StandardHuffmanIO(), new StandardHuffman());
    this.vectorQuantizationFacade = new Facade<>(new VectorQuantizationIO(), new VectorQuantization());
    this.twoDPredictionFacade = new Facade<>(new TwoDPredictionIO(), new TwoDPrediction());
  }

  public Facade<?, ?, ?, ?, ?, ?> getFacade(String techniqueName) {
    if (techniqueName.equalsIgnoreCase("LZ77")) {
      return lz77Facade;
    } else if (techniqueName.equalsIgnoreCase("LZW")) {
      return lzwFacade;
    } else if (techniqueName.equalsIgnoreCase("Standard Huffman")) {
      return standardHuffmanFacade;
    } else if (techniqueName.equalsIgnoreCase("Vector Quantization")) {
      return vectorQuantizationFacade;
    } else if (techniqueName.equalsIgnoreCase("2D Prediction")) {
      return twoDPredictionFacade;
    }
    return null;
  }
}
