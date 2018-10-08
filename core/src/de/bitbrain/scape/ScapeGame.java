package de.bitbrain.scape;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameSettings;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.event.GameEventManagerImpl;
import de.bitbrain.braingdx.graphics.GraphicsSettings;
import de.bitbrain.braingdx.graphics.VectorGameCamera;
import de.bitbrain.braingdx.graphics.postprocessing.filters.RadialBlur;
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
      configureSettings();
      //return new LevelSelectionScreen(this, true);
      return new LogoScreen(this);
      //return new IngameScreen(this, new LevelMetaData(1, "maps/001_databus.tmx", "test", "test"));
   }

   private void configureSettings() {
      GameSettings settings = new GameSettings(new GameEventManagerImpl());
      GraphicsSettings graphicsSettings = settings.getGraphics();
      if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
         graphicsSettings.setRadialBlurQuality(RadialBlur.Quality.Low);
         graphicsSettings.setRenderScale(0.5f);
         graphicsSettings.save();
      } else if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
         graphicsSettings.setRadialBlurQuality(RadialBlur.Quality.High);
         graphicsSettings.setRenderScale(1.5f);
         graphicsSettings.save();
      }
   }
}
