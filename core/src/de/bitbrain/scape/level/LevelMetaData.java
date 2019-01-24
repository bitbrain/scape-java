package de.bitbrain.scape.level;

public class LevelMetaData {

   private final int number;
   private final String path;
   private final String name;
   private final String description;
   private final int numberOfBytes;

   public LevelMetaData(
         int number,
         String path,
         String name,
         String description,
         int numberOfBytes
   ) {
      this.number = number;
      this.path = path;
      this.name = name;
      this.description = description;
      this.numberOfBytes = numberOfBytes;
   }

   public int getLevelNumber() {
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

   public int getNumberOfBytes() {
      return numberOfBytes;
   }
}
