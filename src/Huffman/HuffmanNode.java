package Huffman;/* ALLIKAD:
https://enos.itcollege.ee/~jpoial/algoritmid/Huffman/Huffman.java
https://www.journaldev.com/23246/huffman-coding-algorithm */


/**
 * Huffman node.
 * Helper class for building Huffman tree.
 */
class HuffmanNode implements Comparable<HuffmanNode> {
   int frequency;
   char data;
   HuffmanNode left, right;

   public int compareTo(HuffmanNode node) {
      return frequency - node.frequency;
   }
}

