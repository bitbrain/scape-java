package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationRenderer;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.graphics.CharacterType;
import de.bitbrain.scape.preferences.PlayerProgress;
import de.bitbrain.scape.ui.Styles;

import static de.bitbrain.scape.GameConfig.DEFAULT_BLOOM_CONFIG;

public class LogoScreen extends AbstractScreen<ScapeGame> {

   private GameContext context;
   private PlayerProgress progress;

   private boolean exiting = false;

   public LogoScreen(ScapeGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final GameContext context) {
      this.context = context;
      this.progress = new PlayerProgress(null);
      context.getScreenTransitions().in(0.3f);
      setBackgroundColor(Colors.BACKGROUND_VIOLET);
      final Texture playerTexture = SharedAssetManager.getInstance().get(Assets.Textures.LOGO);
      AnimationSpriteSheet sheet = new AnimationSpriteSheet(playerTexture, 16);
      context.getRenderManager().register(CharacterType.LOGO.name(), new AnimationRenderer(sheet,
            AnimationConfig.builder()
                  .registerFrames(CharacterType.LOGO.name(), AnimationFrames.builder()
                        .origin(0, 0)
                        .frames(16)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .duration(0.1f)
                  .build())
                  .build()));


      GameObject logo = context.getGameWorld().addObject();
      logo.setDimensions(128f, 128f);
      logo.setType("LOGO");
      logo.setAttribute(Orientation.class, Orientation.RIGHT);

      context.getGameCamera().setStickToWorldBounds(false);
      context.getGameCamera().setDefaultZoomFactor(2f);
      context.getGameCamera().setZoomScalingFactor(0.0000001f);
      context.getGameCamera().setTrackingTarget(logo);
      context.getGameCamera().setTargetTrackingSpeed(0.07f);

      Tween.call(new TweenCallback() {
         @Override
         public void onEvent(int i, BaseTween<?> baseTween) {
            if (!exiting) {
               AbstractScreen<?> nextScreen = progress.isNewGame()
                     ? new IntroScreen(getGame())
                     : new MainMenuScreen(getGame());
               context.getScreenTransitions().out(nextScreen, 1f);
            }
         }
      }).delay(1.3f).start(SharedTweenManager.getInstance());

      Label slogan = new Label("a bitbrain production", Styles.LABEL_INTRO_BITBRAIN);
      slogan.setPosition(logo.getLeft() - slogan.getPrefWidth() / 2f + logo.getWidth() / 2f, logo.getTop() - logo.getHeight());
      context.getWorldStage().addActor(slogan);

      setupShaders();
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      if (!exiting && Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
         AbstractScreen<?> nextScreen = progress.isNewGame()
               ? new IntroScreen(getGame())
               : new MainMenuScreen(getGame());
         context.getScreenTransitions().out(nextScreen, 1f);
         exiting = true;
      }
   }

   private void setupShaders() {
      AutoReloadPostProcessorEffect<Bloom> bloomEffect = context.getShaderManager().createBloomEffect();
      AutoReloadPostProcessorEffect<Vignette> vignetteEffect = context.getShaderManager().createVignetteEffect();
      bloomEffect.mutate(DEFAULT_BLOOM_CONFIG);
      context.getRenderPipeline().getPipe(RenderPipeIds.UI).addEffects(vignetteEffect, bloomEffect);
   }
}