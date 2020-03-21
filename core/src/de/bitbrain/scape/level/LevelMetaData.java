package de.bitbrain.scape.level;

public class LevelMetaData {

   private final int number;
   private final String path;
   private final String name;
   private final String description;
   private final int numberOfBytes;
   private final float baseScrollingSpeed;
   private final float playerSpeed;
   private final float playerSpeedIncrease;

   public LevelMetaData(
         int number,
         String path,
         String name,
         String description,
         int numberOfBytes,
         float baseScrollingSpeed,
         float playerSpeed,
         float playerSpeedIncrease) {
      this.number = number;
      this.path = path;
      this.name = name;
      this.description = description;
      this.numberOfBytes = numberOfBytes;
      this.baseScrollingSpeed = baseScrollingSpeed;
      this.playerSpeed = playerSpeed;
      this.playerSpeedIncrease = playerSpeedIncrease;
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

   public float getBaseScrollingSpeed() {
      return baseScrollingSpeed;
   }

   public float getPlayerSpeed() {
      return playerSpeed;
   }

   public float getPlayerSpeedIncrease() {
      return playerSpeedIncrease;
   }
}
