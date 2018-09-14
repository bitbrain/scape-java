package de.bitbrain.scape;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.graphics.VectorGameCamera;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.screens.LogoScreen;
import de.bitbrain.scape.ui.Styles;

public class ScapeGame extends BrainGdxGame {

   @Override
   protected GameAssetLoader getAssetLoader() {
      return new SmartAssetLoader(Assets.class);
   }

   @Override
   protected AbstractScreen<?> getInitialScreen() {
      Styles.init();
      Tween.registerAccessor(VectorGameCamera.class, new GameCameraTween());
      Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
      Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
      //return new LevelSelectionScreen(this, true);
      return new LogoScreen(this);
      //return new IngameScreen(this, new LevelMetaData(1, "maps/001_databus.tmx", "test", "test"));
   }
}
