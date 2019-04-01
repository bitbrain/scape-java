package de.bitbrain.scape.level;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.levelselection.LevelOverviewUI;

import java.util.HashMap;
import java.util.Map;

public class LevelManager {

   private class Level {

      private final GameObject worldObject;
      private final LevelMetaData metadata;
      private final Actor uiObject;

      Level(GameObject worldObject, LevelMetaData metadata, Actor uiObject) {
         this.worldObject = worldObject;
         this.metadata = metadata;
         this.uiObject = uiObject;
      }

      GameObject getWorldObject() {
         return worldObject;
      }

      LevelMetaData getMetadata() {
         return metadata;
      }

      Actor getUiObject() {
         return uiObject;
      }
   }

   private final Map<Integer, Level> levelMapping = new HashMap<Integer, Level>();
   private Integer currentlySelectedLevel;
   private GameObject selector;
   private final GameContext context;
   private Preferences prefs;
   private final LevelMetaDataLoader metaDataLoader = new LevelMetaDataLoader();

   public LevelManager(GameContext context) {
      this.context = context;
      populateLevelMapping();
      selector = context.getGameWorld().addObject();
      prefs = Gdx.app.getPreferences(GameConfig.PLAYER_PREFERENCES_PATH);
      currentlySelectedLevel = prefs.getInteger(GameConfig.PLAYER_CURRENT_LEVEL, 1) - 1;
      selectNextLevel();
      GameObject currentlySelected = getLevel(currentlySelectedLevel).getWorldObject();
      selector.setPosition(currentlySelected.getLeft(), currentlySelected.getTop());
      GameCamera camera = context.getGameCamera();
      camera.setTrackingTarget(selector, true);
   }

   public void selectPreviousLevel() {
      Level previouslySelected = getLevel(currentlySelectedLevel);
      if (previouslySelected != null) {
         context.getTweenManager().killTarget(previouslySelected.uiObject);
      }
      currentlySelectedLevel--;
      if (getLevel(currentlySelectedLevel) == null) {
         currentlySelectedLevel = levelMapping.size();
      }
      Level currentlySelected = getLevel(currentlySelectedLevel);
      prefs.putInteger(GameConfig.PLAYER_CURRENT_LEVEL, currentlySelected.getMetadata().getLevelNumber());
      prefs.flush();
      selector.setPosition(currentlySelected.getWorldObject().getLeft(), currentlySelected.getWorldObject().getTop());
      if (previouslySelected != null) {
         Tween.to(previouslySelected.getUiObject(), ActorTween.ALPHA, 0.5f)
               .target(0f)
               .start(context.getTweenManager());
      }
      Tween.to(currentlySelected.getUiObject(), ActorTween.ALPHA, 0.5f)
            .target(1f)
            .start(context.getTweenManager());
   }

   public void selectNextLevel() {
      Level previouslySelected = getLevel(currentlySelectedLevel);
      if (previouslySelected != null) {
         context.getTweenManager().killTarget(previouslySelected.uiObject);
      }
      currentlySelectedLevel++;
      if (levelMapping.get(currentlySelectedLevel) == null) {
         currentlySelectedLevel = 1;
      }
      Level currentlySelected = getLevel(currentlySelectedLevel);
      prefs.putInteger(GameConfig.PLAYER_CURRENT_LEVEL, currentlySelected.getMetadata().getLevelNumber());
      prefs.flush();
      selector.setPosition(currentlySelected.getWorldObject().getLeft(), currentlySelected.getWorldObject().getTop());
      if (previouslySelected != null) {
         Tween.to(previouslySelected.getUiObject(), ActorTween.ALPHA, 0.5f)
               .target(0f)
               .start(context.getTweenManager());
      }
      Tween.to(currentlySelected.getUiObject(), ActorTween.ALPHA, 0.5f)
            .target(1f)
            .start(context.getTweenManager());
   }

   public LevelMetaData getCurrentMetaData() {
      return levelMapping.get(currentlySelectedLevel).getMetadata();
   }

   public int getTotalBytes() {
      int bytes = 0;
      for (Level level : levelMapping.values()) {
         bytes += level.getMetadata().getNumberOfBytes();
      }
      return bytes;
   }

   public int getTotalCollectedBytes() {
      int bytes = 0;
      for (Level level : levelMapping.values()) {
         PlayerProgress progress = new PlayerProgress(level.getMetadata());
         bytes += progress.getRecord();
      }
      return bytes;
   }

   private void populateLevelMapping() {
      levelMapping.clear();
      for (GameObject o : context.getGameWorld()) {
         if ("LEVEL".equals(o.getType())) {
            LevelMetaData metadata = metaDataLoader.loadFromWorldMapProperties((MapProperties)o.getAttribute(MapProperties.class));
            PlayerProgress progress = new PlayerProgress(metadata);
            if (progress.getMaximumLevel() < metadata.getLevelNumber()) {
               break;
            }
            LevelOverviewUI levelUI = new LevelOverviewUI(metadata);
            levelUI.setPosition(o.getLeft(), o.getTop());
            levelUI.setScale(0.2f);
            levelUI.getColor().a = 0f;
            context.getWorldStage().addActor(levelUI);

            levelMapping.put(metadata.getLevelNumber(), new Level(
                  o, metadata, levelUI
            ));
         }
      }
   }

   private Level getLevel(int level) {
      return levelMapping.get(level);
   }
}
