# Compression Toolkit

**Compression Toolkit** is a Java-based GUI application designed to compress and decompress text and images using a variety of techniques. The toolkit provides an intuitive interface for selecting and applying compression methods suitable for your data type.

## Supported Techniques
- **Text Compression**:
  - LZ77
  - LZW
  - Standard Huffman
- **Image Compression**:
  - Vector Quantization
  - 2D Prediction

## Features
- Compress text and images with user-selected algorithms.
- Decompress previously compressed files.
- User-friendly GUI built with JavaFX for seamless interaction.

## Technologies
- **Programming Language**: Java
- **Framework**: JavaFX
- **Build Tool**: Maven

## Design Patterns
- **Generic Programming**: For reusability across different data types and algorithms.
- **Facade Pattern**: Simplifies access to complex compression and decompression functionalities.
- **Strategy Pattern**: Allows dynamic selection of compression techniques at runtime.
- **Dependency Injection**: Ensures modular and testable code structure.

## Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/AhmedShaaban11/compression-toolkit
    ```
2. Navigate to the project directory:
    ```bash
    cd compression-toolkit
    ```
3. Build the project using Maven:
    ```bash
    mvn clean install
    ```
4. Run the application:
    ```bash
    java -cp target/classes com.ahmed.compression.toolkit.AppApplication
    ```
5. Start compressing and decompressing your files!

## Usage
1. Launch the application.
2. Choose the desired compression technique from the list.
3. Click compress or decompress button.
4. Select the file to compress/decompress.
5. Select the output file.
6. Done!

# License
This project is licensed under the MIT License. 
