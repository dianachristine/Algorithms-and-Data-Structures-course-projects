package Node;

import java.util.*;
/*
VIITED:
https://enos.itcollege.ee/~jpoial/algoritmid/TreeNode.java
 */

public class Node {

   private String name;
   private Node rightSibling;
   private Node firstChild;

   public Node(String s, Node r, Node d) {
      setName (s);
      setRightSibling(r);
      setFirstChild (d);
   }

   public void setName(String s) {
      name = s;
   }

   public String getName() {
      return name;
   }

   public void setRightSibling(Node p) {
      rightSibling = p;
   }

   public Node getRightSibling() {
      return rightSibling;
   }

   public void setFirstChild (Node a) {
      firstChild = a;
   }

   public Node getFirstChild() {
      return firstChild;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Node node = (Node) o;
      return name.equals(node.name) &&
              Objects.equals(rightSibling, node.rightSibling) &&
              Objects.equals(firstChild, node.firstChild);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, rightSibling, firstChild);
   }

   public static Node parsePostfix (String s) {
      List<Character> characters = Arrays.asList('(', ')', ',');
      boolean rootElementRead = false;
      int startBracket = 0;
      int endBracket = 0;
      Stack<Node> nodes = new Stack<>();

      if(s.trim().length() == 0) throw new RuntimeException("String can not be empty " + s);
      if(s.contains("))")) throw new RuntimeException("String can not contain double brackets " + s);
      if(s.contains("()")) throw new RuntimeException("String contains empty subtree " + s);
      s = s.trim();

      Node currentNode = new Node(null, null, null);
      for (int i = s.length() - 1 ; i >= 0 ; i--) {
         if(i == s.length() - 1) rootElementRead = true;

         switch (s.charAt(i)) {
            case ',':
               if(rootElementRead && startBracket == endBracket) throw new RuntimeException("Incorrect syntax " + s);
               if(characters.contains(s.charAt( i - 1))) throw new RuntimeException("Incorrect syntax " + s);
               currentNode = new Node(null, currentNode, null);
               break;
            case '(':
               startBracket++;
               if (nodes.isEmpty()) throw new NullPointerException("Incorrect syntax " + s);
               Node tmp = nodes.pop();
               tmp.setFirstChild(currentNode);
               currentNode = tmp;
               break;
            case ')':
               endBracket++;
               nodes.push(currentNode);
               currentNode = new Node(null, null, null);
               break;
            default:
               String value = String.valueOf(s.charAt(i)).trim();
               if (currentNode.name == null) {
                  currentNode.name = value;
               }
               else {
                  currentNode.name = value + currentNode.name;
               }
               if (i > 0 && s.charAt(i - 1) != ',' && s.charAt(i) == ' ') throw new RuntimeException("Spaces can not be in node names " + s);
               break;
         }
      }
      return currentNode;
   }

   public String leftParentheticRepresentation() {
      StringBuffer leftParentheticRepresentation = new StringBuffer();

      leftParentheticRepresentation.append(this.getName());

      if(this.getFirstChild() != null) {
         leftParentheticRepresentation.append("(");
         leftParentheticRepresentation.append(this.getFirstChild().leftParentheticRepresentation());
         leftParentheticRepresentation.append(")");
      }

      if(this.getRightSibling() != null) {
         leftParentheticRepresentation.append(",");
         leftParentheticRepresentation.append(this.getRightSibling().leftParentheticRepresentation());
      }

      return leftParentheticRepresentation.toString();
   }

   public String xmlRepresentation() {
      StringBuffer xmlRepresentation = new StringBuffer();

      int tagCounter = 0;
      StringBuffer tabs = new StringBuffer();
      String leftParentheticRepresentation = "(" + leftParentheticRepresentation() + ")";

      for (int i = 0; i < leftParentheticRepresentation.length(); i++) {

         if (leftParentheticRepresentation.charAt(i) == '(') {
            tagCounter++;
            tabs.append("\t");
            xmlRepresentation.append("\n").append(tabs).append("<L").append(tagCounter).append(">").append(" ");

         } else if (leftParentheticRepresentation.charAt(i) == ')') {
            if (leftParentheticRepresentation.charAt(i-1) == ')') {
               xmlRepresentation.append("\n").append(tabs);
            }
            tabs.deleteCharAt(tabs.length()-1);
            xmlRepresentation.append(" ").append("</L").append(tagCounter).append(">");
            tagCounter--;

         } else if (leftParentheticRepresentation.charAt(i) == ',') {
            if (leftParentheticRepresentation.charAt(i-1) == ')') {
               xmlRepresentation.append("\n").append(tabs);
            } else {
               xmlRepresentation.append(" ");
            }
            xmlRepresentation.append("</L").append(tagCounter).append(">");
            xmlRepresentation.append("\n").append(tabs).append("<L").append(tagCounter).append(">").append(" ");

         } else {
            xmlRepresentation.append(leftParentheticRepresentation.charAt(i));
         }
      }

      return xmlRepresentation.toString();
   }

   public static void main (String[] param) {
      String s = "(B1,C)A";
      Node t = Node.parsePostfix (s);
      String v = t.leftParentheticRepresentation();
      System.out.println (s + " ==> " + v); // (B1,C)A ==> A(B1,C)
   }

}
