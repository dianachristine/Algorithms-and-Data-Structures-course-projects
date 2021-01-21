import Sheep.Sheep;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SheepTest {

   /** Number of milliseconds allowed to spend for sorting 1 million elements */
   public static int threshold = 25;

   /** Test data */
   static Sheep.Animal[] animals = null;

   /** Number of goat animals */
   static int rCount = 0;

   /** Correctness check for the result */
   static boolean check (Sheep.Animal[] animals, int r) {
       int len = animals.length;
       if (len == 0)
          return true;
       for (int i=0; i < r; i++)
          if (animals[i] != Sheep.Animal.goat)
             return false;
       for (int i=r; i < len; i++)
          if (animals[i] != Sheep.Animal.sheep)
             return false;
       return true;
   } // check

    @Test(timeout=1000)
    public void testFunctionality() {
      animals = new Sheep.Animal [100000];
      rCount = 0;
      for (int i=0; i < animals.length; i++) {
         if (Math.random() < 0.5) {
            animals[i] = Sheep.Animal.goat;
            rCount++;
         } else {
            animals[i] = Sheep.Animal.sheep;
         }
      }
      Sheep.reorder (animals);
      assertTrue ("Result incorrect", check (animals, rCount));
    }

    @Test(timeout=1000)
    public void testShort() {
      animals = new Sheep.Animal [1];
      if (Math.random() < 0.5) {
         animals[0] = Sheep.Animal.goat;
         rCount = 1;
      } else {
         animals[0] = Sheep.Animal.sheep;
         rCount = 0;
      }
      Sheep.reorder (animals);
      assertTrue ("One element array not working", check (animals, rCount));
      animals = new Sheep.Animal [0];
      rCount = 0;
      Sheep.reorder (animals);
      assertTrue ("Zero element array not working", check (animals, rCount));
      animals = new Sheep.Animal [100000];
      rCount = 0;
      for (int i=0; i < animals.length; i++) {
         if (Math.random() < 0.5) {
            animals[i] = Sheep.Animal.goat;
            rCount++;
         } else {
            animals[i] = Sheep.Animal.sheep;
         }
      }
      Sheep.reorder (animals);
      assertTrue ("Result incorrect", check (animals, rCount));
    }

    @Test(timeout=1000)
    public void testAllSheep() {
      animals = new Sheep.Animal [100000];
      rCount = 0;
      for (int i=0; i < animals.length; i++) {
            animals[i] = Sheep.Animal.sheep;
      }
      Sheep.reorder (animals);
      assertTrue ("Result incorrect for all sheep", check (animals, rCount));
      animals = new Sheep.Animal [100000];
      rCount = 0;
      for (int i=0; i < animals.length; i++) {
         if (Math.random() < 0.5) {
            animals[i] = Sheep.Animal.goat;
            rCount++;
         } else {
            animals[i] = Sheep.Animal.sheep;
         }
      }
      Sheep.reorder (animals);
      assertTrue ("Result incorrect", check (animals, rCount));
    }

    @Test(timeout=1000)
    public void testAllGoat() {
      animals = new Sheep.Animal [100000];
      rCount = 0;
      for (int i=0; i < animals.length; i++) {
            animals[i] = Sheep.Animal.goat;
            rCount++;
      }
      Sheep.reorder (animals);
      assertTrue ("Result incorrect for all goat", check (animals, rCount));
      animals = new Sheep.Animal [100000];
      rCount = 0;
      for (int i=0; i < animals.length; i++) {
         if (Math.random() < 0.5) {
            animals[i] = Sheep.Animal.goat;
            rCount++;
         } else {
            animals[i] = Sheep.Animal.sheep;
         }
      }
      Sheep.reorder (animals);
      assertTrue ("Result incorrect", check (animals, rCount));
    }

    @Test(timeout=1000)
    public void testSpeed() {
      animals = new Sheep.Animal [1000000];
      rCount = 0;
      for (int i=0; i < animals.length; i++) {
         if (Math.random() < 0.5) {
            animals[i] = Sheep.Animal.goat;
            rCount++;
         } else {
            animals[i] = Sheep.Animal.sheep;
         }
      }
      long t0 = System.currentTimeMillis();
      Sheep.reorder (animals);
      long t1 = System.currentTimeMillis();
      int delta = (int)(t1-t0);
      assertTrue ("Result incorrect", check (animals, rCount));
      assertTrue ("Too slow: "+ delta, delta < threshold);
      System.out.println ("Time spent: " + delta + " ms");
    }

   @Test(expected=RuntimeException.class)
   public void testNullArray() {
      Sheep.reorder (null);
   }
}

