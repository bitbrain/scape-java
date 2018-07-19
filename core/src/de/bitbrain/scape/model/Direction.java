package de.bitbrain.scape.model;

public enum Direction {
   UP(1),
   DOWN(-1);

   private int multiplier;

   Direction(int multiplier) {
      this.multiplier = multiplier;
   }

   public int getMultiplier() {
      return multiplier;
   }
}
