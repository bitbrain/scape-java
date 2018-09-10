package de.bitbrain.scape;

import de.bitbrain.scape.preferences.PlayerProgress;

public class LevelMetaData {

   private final int number;
   private final String path;
   private final String name;
   private final String description;
   private final PlayerProgress progress;

   public LevelMetaData(int number, String path, String name, String description) {
      this.number = number;
      this.path = path;
      this.name = name;
      this.description = description;
      this.progress = new PlayerProgress(this);
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
}
