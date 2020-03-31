package de.bitbrain.scape.progress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.level.LevelMetaData;

public class PlayerProgress {

   private final Preferences preferences;
   private final LevelMetaData metadata;
   private int points;
   private long currentTime;

   public PlayerProgress(LevelMetaData metadata) {
      this.preferences = Gdx.app.getPreferences(GameConfig.PLAYER_PREFERENCES_PATH);
      this.metadata = metadata;
   }

   public LevelMetaData getMetadata() {
      return metadata;
   }

   public boolean isNewGame() {
      return getMaximumLevel() == 1;
   }

   public float getTotalProgress() {
      return preferences.getFloat(GameConfig.PLAYER_TOTAL_PROGRESS, 0f);
   }

   public void addPoint() {
      points++;
   }

   public void setCurrentTime(long currentTime) {
      this.currentTime = currentTime;
   }

   public long getCurrentTime() {
      return currentTime;
   }

   public void setPoints(int points) {
      this.points = points;
   }

   public int getPoints() {
      return points;
   }

   public int getPointRecord() {
      return preferences.getInteger(GameConfig.PLAYER_LEVEL_POINT_RECORD + metadata.getPath(), 0);
   }

   public long getTimeRecord() {
      return preferences.getLong(GameConfig.PLAYER_LEVEL_TIME_RECORD + metadata.getPath(), 0);
   }

   public int getCurrentLevel() {
      return preferences.getInteger(GameConfig.PLAYER_CURRENT_LEVEL, 1);
   }

   public int getMaximumLevel() {
      return preferences.getInteger(GameConfig.PLAYER_MAXIMUM_LEVEL, 1);
   }

   public void setMaxLevel(int maxLevel) {
      if (maxLevel > getMaximumLevel()) {
         preferences.putInteger(GameConfig.PLAYER_MAXIMUM_LEVEL, maxLevel);
         preferences.flush();
      }
   }

   public void setCurrentLevel(int level) {
      preferences.putInteger(GameConfig.PLAYER_CURRENT_LEVEL, level);
      preferences.flush();
   }

   public void save() {
      if (points > getPointRecord()) {
         preferences.putInteger(GameConfig.PLAYER_LEVEL_POINT_RECORD + metadata.getPath(), points);
      }
      long timeRecord = getTimeRecord();
      if (currentTime < timeRecord || (timeRecord == 0 && currentTime > timeRecord)) {
         preferences.putLong(GameConfig.PLAYER_LEVEL_TIME_RECORD + metadata.getPath(), currentTime);
      }
      preferences.flush();
   }

   public void reset() {
      preferences.clear();
      preferences.flush();
   }
}
