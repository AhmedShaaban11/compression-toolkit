package com.ahmed.compression.techniques.facade;

import com.ahmed.compression.techniques.information.lossless.lz77.Lz77CompressedFileInfo;
import com.ahmed.compression.techniques.information.lossless.lz77.Lz77CompressionInfo;
import com.ahmed.compression.techniques.information.lossless.lz77.Lz77DecompressedFileInfo;
import com.ahmed.compression.techniques.information.lossless.lz77.Lz77DecompressionInfo;
import com.ahmed.compression.techniques.information.lossless.lzw.LzwCompressedFileInfo;
import com.ahmed.compression.techniques.information.lossless.lzw.LzwCompressionInfo;
import com.ahmed.compression.techniques.information.lossless.lzw.LzwDecompressedFileInfo;
import com.ahmed.compression.techniques.information.lossless.lzw.LzwDecompressionInfo;
import com.ahmed.compression.techniques.information.lossless.standardhuffman.StandardHuffmanCompressedFileInfo;
import com.ahmed.compression.techniques.information.lossless.standardhuffman.StandardHuffmanCompressionInfo;
import com.ahmed.compression.techniques.information.lossless.standardhuffman.StandardHuffmanDecompressedFileInfo;
import com.ahmed.compression.techniques.information.lossless.standardhuffman.StandardHuffmanDecompressionInfo;
import com.ahmed.compression.techniques.information.lossy.twodprediction.TwoDPredictionCompressedFileInfo;
import com.ahmed.compression.techniques.information.lossy.twodprediction.TwoDPredictionCompressionInfo;
import com.ahmed.compression.techniques.information.lossy.twodprediction.TwoDPredictionDecompressedFileInfo;
import com.ahmed.compression.techniques.information.lossy.twodprediction.TwoDPredictionDecompressionInfo;
import com.ahmed.compression.techniques.information.lossy.vectorquantization.VectorQuantizationCompressedFileInfo;
import com.ahmed.compression.techniques.information.lossy.vectorquantization.VectorQuantizationCompressionInfo;
import com.ahmed.compression.techniques.information.lossy.vectorquantization.VectorQuantizationDecompressedFileInfo;
import com.ahmed.compression.techniques.information.lossy.vectorquantization.VectorQuantizationDecompressionInfo;
import com.ahmed.compression.techniques.io.lossless.Lz77File;
import com.ahmed.compression.techniques.io.lossless.LzwFile;
import com.ahmed.compression.techniques.io.lossless.StandardHuffmanFile;
import com.ahmed.compression.techniques.io.lossy.TwoDPredictionFile;
import com.ahmed.compression.techniques.io.lossy.VectorQuantizationFile;
import com.ahmed.compression.techniques.tech.lossless.Lz77;
import com.ahmed.compression.techniques.tech.lossless.Lzw;
import com.ahmed.compression.techniques.tech.lossless.StandardHuffman;
import com.ahmed.compression.techniques.tech.lossy.TwoDPrediction;
import com.ahmed.compression.techniques.tech.lossy.VectorQuantization;

public class FacadeFactory {
  private final Facade<Lz77CompressedFileInfo, Lz77DecompressedFileInfo, Lz77CompressionInfo, Lz77DecompressionInfo, Lz77File, Lz77> lz77Facade;
  private final Facade<LzwCompressedFileInfo, LzwDecompressedFileInfo, LzwCompressionInfo, LzwDecompressionInfo, LzwFile, Lzw> lzwFacade;
  private final Facade<StandardHuffmanCompressedFileInfo, StandardHuffmanDecompressedFileInfo, StandardHuffmanCompressionInfo, StandardHuffmanDecompressionInfo, StandardHuffmanFile, StandardHuffman> standardHuffmanFacade;
  private final Facade<VectorQuantizationCompressedFileInfo, VectorQuantizationDecompressedFileInfo, VectorQuantizationCompressionInfo, VectorQuantizationDecompressionInfo, VectorQuantizationFile, VectorQuantization> vectorQuantizationFacade;
  private final Facade<TwoDPredictionCompressedFileInfo, TwoDPredictionDecompressedFileInfo, TwoDPredictionCompressionInfo, TwoDPredictionDecompressionInfo, TwoDPredictionFile, TwoDPrediction> twoDPredictionFacade;

  public FacadeFactory() {
    this.lz77Facade = new Facade<>(new Lz77File(), new Lz77());
    this.lzwFacade = new Facade<>(new LzwFile(), new Lzw());
    this.standardHuffmanFacade = new Facade<>(new StandardHuffmanFile(), new StandardHuffman());
    this.vectorQuantizationFacade = new Facade<>(new VectorQuantizationFile(), new VectorQuantization());
    this.twoDPredictionFacade = new Facade<>(new TwoDPredictionFile(), new TwoDPrediction());
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
