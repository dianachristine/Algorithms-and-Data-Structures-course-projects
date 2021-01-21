package Huffman;/* ALLIKAD:
https://enos.itcollege.ee/~jpoial/algoritmid/Huffman/Huffman.java
https://www.journaldev.com/23246/huffman-coding-algorithm */

import java.util.*;


/**
 * Prefix codes and Huffman tree.
 * Tree depends on source data.
 */
public class Huffman {

   private static Map<Character, String> charPrefixHashMap = new HashMap<>();
   private Map<Character, Integer> freq = new HashMap<>();
   HuffmanNode root;

   /** Constructor to build the Huffman code for a given bytearray.
    * @param original source data
    */
   public Huffman(byte[] original) {
      if (original == null) {
         throw new RuntimeException("Empty input");
      }
   }

   /** Length of encoded data in bits.
    * @return number of bits
    */
   public int bitLength() {
      int resultbits = 0;

      for (Character chr : charPrefixHashMap.keySet()) {
         if (charPrefixHashMap.get(chr) != null && freq.get(chr) != null) {
            resultbits += freq.get(chr) * charPrefixHashMap.get(chr).length();
         }
      }
      return resultbits;
   }

   /**
    * Method for building Huffman tree.
    * Used for encoding.
    * @return root
    */
   private HuffmanNode buildTree() {

      PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
      Set<Character> keySet = freq.keySet();

      for (Character c : keySet) {
         HuffmanNode huffmanNode = new HuffmanNode();
         huffmanNode.data = c;
         huffmanNode.frequency = freq.get(c);
         huffmanNode.left = null;
         huffmanNode.right = null;
         priorityQueue.offer(huffmanNode);
      }

      while (priorityQueue.size() > 1) {

         HuffmanNode x = priorityQueue.peek();
         priorityQueue.poll();

         HuffmanNode y = priorityQueue.peek();
         priorityQueue.poll();

         HuffmanNode sum = new HuffmanNode();
         sum.frequency = x.frequency + y.frequency;
         sum.data = '-';
         sum.left = x;
         sum.right = y;

         root = sum;

         priorityQueue.offer(sum);
      }

      return priorityQueue.poll();
   }

   /**
    * Method that sets prefix codes in charPrefixHashMap.
    * Used for encoding.
    */
   private void setPrefixCodes(HuffmanNode node, StringBuilder prefix) {

      if (node != null) {
         if (node.left == null && node.right == null) {
            charPrefixHashMap.put(node.data, prefix.toString());

         } else {
            prefix.append('0');
            setPrefixCodes(node.left, prefix);
            prefix.deleteCharAt(prefix.length() - 1);

            prefix.append('1');
            setPrefixCodes(node.right, prefix);
            prefix.deleteCharAt(prefix.length() - 1);
         }
      }

      if (freq.size() == 1) { // root is the only node
         charPrefixHashMap.put(node.data, "0");
      }
   }

   /** Encoding the byte array using this prefixcode.
    * @param origData original data
    * @return encoded data
    */
   public byte[] encode(byte[] origData) {

      String test = new String(origData);

      for (int i = 0; i < test.length(); i++) {
         if (!freq.containsKey(test.charAt(i))) {
            freq.put(test.charAt(i), 0);
         }
         freq.put(test.charAt(i), freq.get(test.charAt(i)) + 1);
      }

      System.out.println("Character Frequency Map = " + freq);
      root = buildTree();

      setPrefixCodes(root, new StringBuilder());
      System.out.println("Character Prefix Map = " + charPrefixHashMap);

      byte[] bytes = new byte[origData.length];

      for (int i = 0; i < test.length(); i++) {
         char c = test.charAt(i);
         bytes[i] = Byte.parseByte(charPrefixHashMap.get(c), 2);
      }
      return bytes;
   }

   /** Decoding the byte array using this prefixcode.
    * @param encodedData encoded data
    * @return decoded data (hopefully identical to original)
    */
   public byte[] decode(byte[] encodedData) {

      StringBuilder s = new StringBuilder();
      for (byte encodedDatum : encodedData) {
         s.append(Integer.toBinaryString(encodedDatum));
      }
      System.out.println("Encoded: " + s);

      StringBuilder stringBuilder = new StringBuilder();
      HuffmanNode temp = root;

      for (int i = 0; i < s.length(); i++) {
         if (root.left == null && root.right == null) {
            stringBuilder.append(temp.data); // root is the only node

         } else {
            int j = Integer.parseInt(String.valueOf(s.charAt(i)));

            if (j == 0) {
               temp = temp.left;
               if (temp.left == null && temp.right == null) {
                  stringBuilder.append(temp.data);
                  temp = root;
               }
            }
            if (j == 1) {
               temp = temp.right;
               if (temp.left == null && temp.right == null) {
                  stringBuilder.append(temp.data);
                  temp = root;
               }
            }
         }
      }
      System.out.println("Decoded string is " + stringBuilder.toString());

      return stringBuilder.toString().getBytes();
   }

   /** Main method. */
   public static void main(String[] args) {
      String tekst = "AAAAAAAAAAAAABBBBBBCCCDDEEF";
      byte[] orig = tekst.getBytes();
      Huffman huf = new Huffman (orig);
      byte[] kood = huf.encode (orig);
      byte[] orig2 = huf.decode (kood);
      // must be equal: orig, orig2
      System.out.println (Arrays.equals (orig, orig2));
      int lngth = huf.bitLength();
      System.out.println ("Length of encoded data in bits: " + lngth);

   }
}
