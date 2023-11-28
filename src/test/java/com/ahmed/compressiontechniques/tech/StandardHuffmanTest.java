package com.ahmed.compressiontechniques.tech;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StandardHuffmanTest {
//  @Test
//  void getProbabilitiesTest1() {
//    String data = "Hello World!";
//    var standardHuffman = new StandardHuffman();
//    var probabilities = standardHuffman.getProbabilities(data);
//    assertEquals(1.0 / data.length(), probabilities.get('H'));
//    assertEquals(1.0 / data.length(), probabilities.get('e'));
//    assertEquals(3.0 / data.length(), probabilities.get('l'));
//    assertEquals(2.0 / data.length(), probabilities.get('o'));
//    assertEquals(1.0 / data.length(), probabilities.get(' '));
//    assertEquals(1.0 / data.length(), probabilities.get('W'));
//    assertEquals(1.0 / data.length(), probabilities.get('r'));
//    assertEquals(1.0 / data.length(), probabilities.get('d'));
//    assertEquals(1.0 / data.length(), probabilities.get('!'));
//  }
//
//  @Test
//  void getProbabilitiesTest2() {
//    String data = "aaa bb c jamiel good";
//    var standardHuffman = new StandardHuffman();
//    var probabilities = standardHuffman.getProbabilities(data);
//    assertEquals(4.0 / data.length(), probabilities.get('a'));
//    assertEquals(2.0 / data.length(), probabilities.get('b'));
//    assertEquals(1.0 / data.length(), probabilities.get('c'));
//    assertEquals(1.0 / data.length(), probabilities.get('e'));
//    assertEquals(1.0 / data.length(), probabilities.get('j'));
//    assertEquals(1.0 / data.length(), probabilities.get('m'));
//    assertEquals(1.0 / data.length(), probabilities.get('i'));
//    assertEquals(1.0 / data.length(), probabilities.get('g'));
//    assertEquals(1.0 / data.length(), probabilities.get('d'));
//    assertEquals(2.0 / data.length(), probabilities.get('o'));
//    assertEquals(4.0 / data.length(), probabilities.get(' '));
//  }
//
//  @Test
//  void getProbabilitiesTest3() {
//    String data = "aaaabbbababaeebfdxvvcsaajkljaaoi";
//    var standardHuffman = new StandardHuffman();
//    var probabilities = standardHuffman.getProbabilities(data);
//    assertEquals(11.0 / data.length(), probabilities.get('a'));
//    assertEquals(6.0 / data.length(), probabilities.get('b'));
//    assertEquals(2.0 / data.length(), probabilities.get('e'));
//    assertEquals(1.0 / data.length(), probabilities.get('f'));
//    assertEquals(1.0 / data.length(), probabilities.get('d'));
//    assertEquals(1.0 / data.length(), probabilities.get('x'));
//    assertEquals(2.0 / data.length(), probabilities.get('v'));
//    assertEquals(1.0 / data.length(), probabilities.get('c'));
//    assertEquals(1.0 / data.length(), probabilities.get('s'));
//    assertEquals(2.0 / data.length(), probabilities.get('j'));
//    assertEquals(1.0 / data.length(), probabilities.get('k'));
//    assertEquals(1.0 / data.length(), probabilities.get('l'));
//    assertEquals(1.0 / data.length(), probabilities.get('o'));
//    assertEquals(1.0 / data.length(), probabilities.get('i'));
//  }

  @Test
  void getHuffmanCodesTest1() {
    String data = "aaaaaaabbbbbbbefsadf xcdfsasdssfasdffefaccccccdddddd";
    var standardHuffman = new StandardHuffman();
    standardHuffman.compress(data);
  }

  @Test
  void getHuffmanCodesTest2() {
    String data = "Hello, World!";
    var standardHuffman = new StandardHuffman();
    standardHuffman.compress(data);
  }
}
