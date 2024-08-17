package com.ahmed.compression.toolkit.info.lossless.standardhuffman;

import java.util.HashMap;

public record TreeNode(char c, double probability, TreeNode left, TreeNode right) implements Comparable<TreeNode> {
  // Leaf node constructor
  public TreeNode(char c, double probability) {
    this(c, probability, null, null);
  }

  // Internal node constructor
  public TreeNode(double probability, TreeNode left, TreeNode right) {
    this('\0', probability, left, right);
  }

  public void getLeafCodes(HashMap<Character, String> codes, String code) {
    if (left == null && right == null) {
      codes.put(c, code);
      return;
    }
    left.getLeafCodes(codes, code + "0");
    right.getLeafCodes(codes, code + "1");
  }

  @Override
  public int compareTo(TreeNode another) {
    return Double.compare(this.probability, another.probability);
  }
}