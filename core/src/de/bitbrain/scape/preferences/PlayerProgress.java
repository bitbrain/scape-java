package de.bitbrain.scape.preferences;

public class PlayerProgress {

   private int points;

   public PlayerProgress() {
   }

   public void addPoint() {
      points++;
   }

   public int getPoints() {
      return points;
   }
}
