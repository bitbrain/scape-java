package de.bitbrain.scape.level;

import aurelienribon.tweenengine.Tween;
import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.levelselection.LevelOverviewUI;

import java.util.HashMap;
import java.util.Map;

public class StageManager {

   private class Level {

      private final GameObject worldObject;
      private final LevelMetaData metadata;
      private final LevelOverviewUI uiObject;

      Level(GameObject worldObject, LevelMetaData metadata, LevelOverviewUI uiObject) {
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

      LevelOverviewUI getUiObject() {
         return uiObject;
      }
   }

   private final Map<Integer, Level> levelMapping = new HashMap<Integer, Level>();
   private Integer currentlySelectedLevel;
   private GameObject selector;
   private final GameContext context;
   private Preferences prefs;
   private final LevelMetaDataLoader metaDataLoader = new LevelMetaDataLoader();

   public StageManager(final GameContext context) {
      this.context = context;
      populateLevelMapping();
      selector = context.getGameWorld().addObject();
      prefs = Gdx.app.getPreferences(GameConfig.PLAYER_PREFERENCES_PATH);
      currentlySelectedLevel = prefs.getInteger(GameConfig.PLAYER_CURRENT_LEVEL, 1) - 1;
      selectNextLevel();
      GameObject currentlySelected = getLevel(currentlySelectedLevel).getWorldObject();
      selector.setPosition(currentlySelected.getLeft(), currentlySelected.getTop());
      final GameCamera camera = context.getGameCamera();
      camera.setTrackingTarget(selector, true);
      final PointLight bgLight = context.getLightingManager().addPointLight("asdf-bg", camera.getPosition().x, camera.getPosition().y, 40f, Colors.PRIMARY_RED);
      final PointLight light = context.getLightingManager().addPointLight("asdf", camera.getPosition().x, camera.getPosition().y, 120f, Color.WHITE);
      context.getBehaviorManager().apply(new BehaviorAdapter() {
         @Override
         public void onDetach(GameObject source) {
            super.onDetach(source);
            context.getLightingManager().removePointLight("asdf-bg");
            context.getLightingManager().removePointLight("asdf");
         }

         @Override
         public void update(GameObject source, float delta) {
            bgLight.setPosition(camera.getPosition().x, camera.getPosition().y);
            light.setPosition(camera.getPosition().x, camera.getPosition().y);
            super.update(source, delta);
         }
      });
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
         previouslySelected.getUiObject().hide();
      }
      currentlySelected.getUiObject().show();
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
         previouslySelected.getUiObject().hide();
      }
      currentlySelected.getUiObject().show();
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
         bytes += progress.getPointRecord();
      }
      return bytes;
   }

   private void populateLevelMapping() {
      levelMapping.clear();
      for (GameObject o : context.getGameWorld()) {
         if ("LEVEL".equals(o.getType())) {
            MapProperties properties = (MapProperties)o.getAttribute(MapProperties.class);
            LevelMetaData metadata = metaDataLoader.loadFromWorldMapProperties(properties);
            PlayerProgress progress = new PlayerProgress(metadata);
            if (progress.getMaximumLevel() < metadata.getLevelNumber()) {
               break;
            }
            String tooltipAlignment = properties.get("tooltip", "left", String.class);
            LevelOverviewUI levelUI = new LevelOverviewUI(
                  context.getLightingManager(),
                  context.getParticleManager(),
                  metadata,
                  resolveAlignment(tooltipAlignment),
                  o
            );
            context.getWorldStage().addActor(levelUI);

            levelMapping.put(metadata.getLevelNumber(), new Level(
                  o, metadata, levelUI
            ));
         }
      }
   }

   private int resolveAlignment(String tooltipAlignment) {
      if (tooltipAlignment.equals("right")) {
         return Align.right;
      } else if (tooltipAlignment.equals("top")) {
         return Align.top;
      } else if (tooltipAlignment.equals("bottom")) {
         return Align.bottom;
      } else if (tooltipAlignment.equals("bottomLeft")) {
         return Align.bottomLeft;
      } else if (tooltipAlignment.equals("bottomRight")) {
         return Align.bottomRight;
      } else if (tooltipAlignment.equals("topLeft")) {
         return Align.topLeft;
      } else if (tooltipAlignment.equals("topRight")) {
         return Align.topRight;
      }
      return Align.left;
   }

   private Level getLevel(int level) {
      return levelMapping.get(level);
   }
}
