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
import de.bitbrain.braingdx.tweens.ValueTween;
import de.bitbrain.braingdx.util.ValueProvider;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.googleplay.PlayAchievementManager;
import de.bitbrain.scape.googleplay.PlayEventManager;
import de.bitbrain.scape.googleplay.GameServiceFactory;
import de.bitbrain.scape.i18n.Bundle;
import de.bitbrain.scape.screens.StageSelectionScreen;
import de.bitbrain.scape.screens.LogoScreen;
import de.bitbrain.scape.ui.Styles;
import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;
import org.apache.commons.lang.SystemUtils;

public class ScapeGame extends BrainGdxGame {

   private boolean screenshots;
   private boolean devMode;
   private boolean debugMode;
   private boolean gifMode;
   private final GameServiceFactory gameServiceFactory;
   private IGameServiceClient gameServiceClient;
   private PlayAchievementManager achievementManager;
   private PlayEventManager eventManager;

   public ScapeGame(GameServiceFactory gameServiceFactory, String ... args) {
      this.gameServiceFactory = gameServiceFactory;
      for (String arg : args) {
         if ("dev".equalsIgnoreCase(arg)) {
            devMode = true;
         }
         if ("debug".equalsIgnoreCase(arg)) {
            debugMode = true;
         }
         if ("gif".equalsIgnoreCase(arg)) {
            gifMode = true;
         }
         if ("screenshots".equalsIgnoreCase(arg)) {
            screenshots = true;
         }
      }
   }

   public IGameServiceClient getGameServiceClient() {
      return gameServiceClient;
   }

   public boolean isDevMode() {
      return devMode;
   }

   public boolean isDebugMode() {
      return debugMode;
   }

   public PlayAchievementManager getPlayAchievementManager() {
      return achievementManager;
   }

   public PlayEventManager getPlayEventManager() {
      return eventManager;
   }

   @Override
   protected GameAssetLoader getAssetLoader() {
      return new SmartAssetLoader(Assets.class);
   }

   @Override
   protected AbstractScreen<?, ?> getInitialScreen() {
      this.gameServiceClient = initialiseGameClient();
      // establish a connection to the game service without error messages or login screens
      gameServiceClient.resumeSession();

      achievementManager = new PlayAchievementManager(gameServiceClient);
      eventManager = new PlayEventManager(gameServiceClient);

      Bundle.load();
      Tween.registerAccessor(VectorGameCamera.class, new GameCameraTween());
      Tween.registerAccessor(ValueProvider.class, new ValueTween());
      configureSettings();
      Styles.init();
      if (isDebugMode()) {
         return new StageSelectionScreen(this);
      } else {
         return new LogoScreen(this);
      }
   }

   private IGameServiceClient initialiseGameClient() {
      try {
         return  gameServiceFactory != null ? gameServiceFactory.create() : new NoGameServiceClient();
      } catch (Exception e) {
         Gdx.app.error("GameClient", "Unable to initialise game client: " + e.getMessage());
         return new NoGameServiceClient();
      }
   }

   private void configureSettings() {
      GameSettings settings = new GameSettings(new GameEventManagerImpl());
      GraphicsSettings graphicsSettings = settings.getGraphics();
      if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
         graphicsSettings.setRadialBlurQuality(RadialBlur.Quality.Low);
         Gdx.input.setCatchBackKey(true);
         graphicsSettings.setRenderScale(0.05f);
         graphicsSettings.setParticleMultiplier(0.1f);
         graphicsSettings.save();
      } else if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
         if (screenshots) {
            Gdx.graphics.setWindowedMode(3840, 2160);
         } else if (gifMode) {
            Gdx.graphics.setWindowedMode(1248, 770);
         } else if (SystemUtils.IS_OS_WINDOWS) {
            Gdx.graphics.setUndecorated(true);
            Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
         } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
         }
         graphicsSettings.setRadialBlurQuality(RadialBlur.Quality.High);
         graphicsSettings.setRenderScale(0.3f);
         graphicsSettings.setParticleMultiplier(0.5f);
         graphicsSettings.save();
      }
   }
}
