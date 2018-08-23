package de.bitbrain.scape;

public class LevelMetaData {

   private final int number;
   private final String path;
   private final String name;
   private final String description;

   public LevelMetaData(int number, String path, String name, String description) {
      this.number = number;
      this.path = path;
      this.name = name;
      this.description = description;
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
}
