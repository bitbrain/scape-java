package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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
import de.bitbrain.braingdx.ui.AnimationDrawable;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.graphics.CharacterType;
import de.bitbrain.scape.i18n.Bundle;
import de.bitbrain.scape.i18n.Messages;
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
      context.getSettings().getGraphics().setRenderScale(0.5f);
      context.getSettings().getGraphics().save();
      this.context = context;
      this.progress = new PlayerProgress(null);
      context.getScreenTransitions().in(0.3f);
      setBackgroundColor(Colors.BACKGROUND_VIOLET);
      final Texture playerTexture = SharedAssetManager.getInstance().get(Assets.Textures.LOGO);
      AnimationSpriteSheet sheet = new AnimationSpriteSheet(playerTexture, 16);
      AnimationDrawable drawable = new AnimationDrawable(sheet,
            AnimationConfig.builder()
                  .registerFrames(AnimationDrawable.DEFAULT_FRAME_ID, AnimationFrames.builder()
                        .origin(0, 0)
                        .frames(16)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .duration(0.1f)
                  .build())
                  .build());

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

      Table layout = new Table();
      layout.setFillParent(true);

      Image image = new Image(drawable);
      layout.add(image).width(50f).height(50f).padBottom(20f).row();

      Label slogan = new Label(Bundle.get(Messages.MENU_LOGO_CREDITS), Styles.LABEL_INTRO_BITBRAIN);
      layout.add(slogan);

      context.getStage().addActor(layout);

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

   @Override
   protected Viewport getViewport(int width, int height, Camera camera) {
      return new ExtendViewport(width, height, camera);
   }

   private void setupShaders() {
      AutoReloadPostProcessorEffect<Bloom> bloomEffect = context.getShaderManager().createBloomEffect();
      AutoReloadPostProcessorEffect<Vignette> vignetteEffect = context.getShaderManager().createVignetteEffect();
      bloomEffect.mutate(DEFAULT_BLOOM_CONFIG);
      context.getRenderPipeline().getPipe(RenderPipeIds.UI).addEffects(vignetteEffect, bloomEffect);
   }
}
