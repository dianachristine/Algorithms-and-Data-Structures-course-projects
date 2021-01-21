package Sheep;

public class Sheep {

   public enum Animal {sheep, goat};

   public static void reorder (Animal[] animals) {
      int goatCount = getGoatCount(animals);

      for (int i = 0; i < animals.length; i++) {
         if (i < goatCount) {
            animals[i] = Animal.goat;
         } else {
            animals[i] = Animal.sheep;
         }
      }
   }

   private static int getGoatCount(Animal[] animals) {
      int goatCounter = 0;
      for (Animal animal : animals) {
         if (animal.equals(Animal.goat)) {
            goatCounter++;
         }
      }
      return goatCounter;
   }

}
