package de.bitbrain.scape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.LevelMetaData;
import de.bitbrain.scape.assets.Assets;

import java.util.HashMap;
import java.util.Map;

public class LevelSelectionScreen extends AbstractScreen<BrainGdxGame> {

   private Map<Integer, GameObject> levelMapping;
   private Map<Integer, LevelMetaData> metadata;
   private Integer currentlySelectedLevel = 1;
   private GameObject selector;

   private boolean exiting = false;
   private GameContext context;

   public LevelSelectionScreen(BrainGdxGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext context) {
      setBackgroundColor(Colors.BACKGROUND_VIOLET);
      context.getTiledMapManager().load(
            SharedAssetManager.getInstance().get(Assets.TiledMaps.WORLD_MAP, TiledMap.class),
            context.getGameCamera().getInternalCamera(),
            TiledMapType.ORTHOGONAL
      );
      populateLevelMapping(context);
      selector = context.getGameWorld().addObject();
      GameObject currentlySelected = getLevel(currentlySelectedLevel);
      selector.setPosition(currentlySelected.getLeft(), currentlySelected.getTop());
      GameCamera camera = context.getGameCamera();
      camera.setStickToWorldBounds(false);
      camera.setTargetTrackingSpeed(0.2f);
      camera.setDefaultZoomFactor(0.07f);
      camera.setZoomScalingFactor(0f);
      camera.setTrackingTarget(selector, true);
      setupShaders(context);
   }

   @Override
   protected void onUpdate(float delta) {
      if (exiting) {
         return;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
         Gdx.app.exit();
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isTouched()) {
         context.getScreenTransitions().out(new IngameScreen(getGame(), getCurrentMetaData()), 1f);
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
         selectNextLevel();
      }
   }

   private void populateLevelMapping(GameContext context) {
      this.context = context;
      levelMapping = new HashMap<Integer, GameObject>();
      metadata = new HashMap<Integer, LevelMetaData>();
      for (GameObject o : context.getGameWorld()) {
         if ("LEVEL".equals(o.getType())) {
            int level = Integer.valueOf((String)((MapProperties)o.getAttribute(MapProperties.class)).get("level"));
            levelMapping.put(level, o);
            metadata.put(level, new LevelMetaData(
                  level,
                  (String)((MapProperties)o.getAttribute(MapProperties.class)).get("path"),
                  (String)((MapProperties)o.getAttribute(MapProperties.class)).get("name"),
                  (String)((MapProperties)o.getAttribute(MapProperties.class)).get("description")
            ));
         }
      }
   }

   private GameObject getLevel(int level) {
      return levelMapping.get(level);
   }

   private void setupShaders(GameContext context) {
      Bloom bloom = new Bloom(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      Vignette vignette = new Vignette(Gdx.
            graphics.getWidth(), Gdx.graphics.getHeight(), false);
      bloom.setBlurAmount(5f);
      bloom.setBloomIntesity(1.2f);
      bloom.setBlurPasses(50);
      bloom.setThreshold(0.3f);
      context.getRenderPipeline().getPipe(RenderPipeIds.UI).addEffects(vignette, bloom);
   }

   private void selectNextLevel() {
      currentlySelectedLevel++;
      if (levelMapping.get(currentlySelectedLevel) == null) {
         currentlySelectedLevel = 1;
      }
      GameObject currentlySelected = getLevel(currentlySelectedLevel);
      selector.setPosition(currentlySelected.getLeft(), currentlySelected.getTop());
   }

   private LevelMetaData getCurrentMetaData() {
      return metadata.get(currentlySelectedLevel);
   }
}
