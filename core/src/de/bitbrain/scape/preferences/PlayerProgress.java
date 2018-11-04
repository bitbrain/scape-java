package de.bitbrain.scape.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.LevelMetaData;

public class PlayerProgress {

   private final Preferences preferences;
   private final LevelMetaData metadata;
   private int points;

   public PlayerProgress(LevelMetaData metadata) {
      this.preferences = Gdx.app.getPreferences(GameConfig.PLAYER_PREFERENCES_PATH);
      this.metadata = metadata;
   }

   public boolean isNewGame() {
      return getMaximumLevel() < 2;
   }

   public float getTotalProgress() {
      return preferences.getFloat(GameConfig.PLAYER_TOTAL_PROGRESS, 0f);
   }

   public void addPoint() {
      points++;
   }

   public int getPoints() {
      return points;
   }

   public int getRecord() {
      return preferences.getInteger(GameConfig.PLAYER_LEVEL_RECORD + metadata.getPath(), 0);
   }

   public int getCurrentLevel() {
      return preferences.getInteger(GameConfig.PLAYER_CURRENT_LEVEL, 1);
   }

   public int getMaximumLevel() {
      return preferences.getInteger(GameConfig.PLAYER_MAXIMUM_LEVEL, 1);
   }

   public void increaseMaxLevel() {
      preferences.putInteger(GameConfig.PLAYER_MAXIMUM_LEVEL, getMaximumLevel() + 1);
      preferences.flush();
   }

   public void setCurrentLevel(int level) {
      preferences.putInteger(GameConfig.PLAYER_CURRENT_LEVEL, level);
      preferences.flush();
   }

   public void save() {
      if (points > getRecord()) {
         preferences.putInteger(GameConfig.PLAYER_LEVEL_RECORD + metadata.getPath(), points);
      }
      preferences.flush();
   }

   public void reset() {
      preferences.clear();
      preferences.flush();
   }
}
