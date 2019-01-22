package de.bitbrain.scape;

import de.bitbrain.scape.preferences.PlayerProgress;

public class LevelMetaData {

   private final int number;
   private final String path;
   private final String name;
   private final String description;
   private final PlayerProgress progress;
   private final int numberOfBytes;

   public LevelMetaData(int number, String path, String name, String description, int numberOfBytes) {
      this.number = number;
      this.path = path;
      this.name = name;
      this.description = description;
      this.progress = new PlayerProgress(this);
      this.numberOfBytes = numberOfBytes;
   }

   public int getNumber() {
      return number;
   }

   public String getPath() {
      return path;
   }

   public String getName() {
      return name;
   }

   public String getDescription() {
      return description;
   }

   public PlayerProgress getProgress() {
      return progress;
   }

   public int getNumberOfBytes() {
      return numberOfBytes;
   }
}
