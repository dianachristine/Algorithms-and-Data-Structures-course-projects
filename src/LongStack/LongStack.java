package LongStack;

import java.util.*;

public class LongStack {

   private LinkedList<Long> stack;

   private static ArrayList<String> operators = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "SWAP", "ROT"));

   public static void main(String[] argum) {
      System.out.println(interpret("2 5 SWAP -"));
   }

   public LongStack() {
      stack = new LinkedList<>();
   }

   LongStack(LinkedList<Long> lifo) {
      this.stack = lifo;
   }

   @Override
   public Object clone() throws CloneNotSupportedException {
      return new LongStack((LinkedList<Long>) this.stack.clone());
   }

   public boolean stEmpty() {
      return stack.isEmpty();
   }

   public void push(long a) {
      stack.push(a);
   }

   public long pop() {
      if (stack.isEmpty()){
         throw new RuntimeException("Stack is empty, cannot pop empty stack.");
      }
      return stack.pop();
   }

   public void op(String s) {
      // operators: +-*/ SWAP ROT

      if (stack.isEmpty() && !s.trim().isEmpty()){
         throw new EmptyStackException();
      }

      if (!operators.contains(s)) {
         if (s.trim().isEmpty()) {
            return;
         }
         throw new IllegalArgumentException("Illegal operator " + s);
      }

      if (stack.size() == 1) {
         throw new RuntimeException("Stack size needs to be at least 2 for " + s + " operation");
      }

      long a = stack.pop();
      long b = stack.pop();

      switch (s) {
         case "+":
            stack.push(a + b);
            break;
         case "-":
            stack.push(b - a);
            break;
         case "*":
            stack.push(a * b);
            break;
         case "/":
            stack.push(b / a);
            break;
         case "SWAP":
            stack.push(a);
            stack.push(b);
            break;
         case "ROT":
            long c = stack.pop();
            stack.push(b);
            stack.push(a);
            stack.push(c);
            break;
      }
   }

   public long tos() {
      if (stack.isEmpty()){
         throw new RuntimeException("Stack is empty, cannot get top of stack.");
      }
      return stack.getFirst();
   }

   @Override
   public boolean equals(Object o) {
      return stack.equals(((LongStack)o).stack);
   }

   @Override
   public String toString() {
      StringBuilder sBuilder = new StringBuilder();
      Iterator<Long> i = stack.descendingIterator();
      while (i.hasNext()) {
         sBuilder.append(i.next());
         sBuilder.append(" ");
      }

      return sBuilder.toString();
   }

   public static long interpret(String pol) {
      //https://git.wut.ee/i231/home3/src/commit/e7158c799e24a1d7142acbca23ed45e349eb08b8/src/LongStack.java#L92
      if (pol == null || pol.trim().isEmpty()) {
         throw new RuntimeException("Empty expression");
      }
      LongStack longStack = new LongStack();

      for(int i = 0; i < pol.length(); i++) {
         char c = pol.charAt(i);
         char nextChar = ' ';

         if (i+1 < pol.length()) {
            nextChar = pol.charAt(i + 1);
         }

         if ((c >= '0' & c <= '9') | (c == '-' & nextChar >= '0' & nextChar <= '9')) {
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(c);
            i++;

            for (; i < pol.length(); i++) {
               c = pol.charAt(i);
               if (c >= '0' & c <= '9') {
                  sBuilder.append(c);
               } else {
                  break;
               }
            }

            longStack.push(Long.parseLong(sBuilder.toString()));
         } else {
            try {
               if (c == 'S') {
                  if (pol.substring(i, i + 4).equals("SWAP")) {
                     longStack.op("SWAP");
                     i += 3;
                  } else {
                     throw new IllegalArgumentException();
                  }
               } else if (c == 'R') {
                  if(pol.substring(i, i + 3).equals("ROT")) {
                     longStack.op("ROT");
                     i += 2;
                  } else {
                     throw new IllegalArgumentException();
                  }
               } else {
                  longStack.op(String.valueOf(c));
               }
            } catch (IllegalArgumentException e) {
               throw new RuntimeException("Illegal symbol " + c + " in expression " + pol);
            } catch (EmptyStackException e) {
               throw new RuntimeException("Stack is empty while trying to perform " + c + " operation in expression " + pol);
            } catch (RuntimeException e) {
               throw new RuntimeException("Cannot perform " + c + " in expression " + pol);
            }
         }
      }

      if (longStack.stack.size() == 1) {
         return longStack.pop();
      } else {
         throw new RuntimeException("Too many numbers in expression " + pol);
      }
   }
}

