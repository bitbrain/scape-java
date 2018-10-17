package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.graphics.animation.SpriteSheet;
import de.bitbrain.braingdx.graphics.animation.types.AnimationTypes;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessor;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.graphics.CharacterType;
import de.bitbrain.scape.ui.Styles;
import de.bitbrain.scape.ui.TerminalUI;

import java.util.ArrayList;
import java.util.List;

import static de.bitbrain.scape.GameConfig.DEFAULT_BLOOM_CONFIG;
import static de.bitbrain.scape.graphics.CharacterInitializer.createAnimations;

public class LogoScreen extends AbstractScreen<ScapeGame> {

   private GameContext context;

   private boolean exiting = false;

   public LogoScreen(ScapeGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final GameContext context) {
      this.context = context;
      context.getScreenTransitions().in(0.3f);
      setBackgroundColor(Colors.BACKGROUND_VIOLET);
      final Texture playerTexture = SharedAssetManager.getInstance().get(Assets.Textures.LOGO);
      SpriteSheet sheet = new SpriteSheet(playerTexture, 16, 1);
      createAnimations(context, sheet, CharacterType.LOGO, AnimationTypes.FORWARD)
            .origin(0, 0)
            .frames(16)
            .scale(1f, -1f)
            .interval(0.1f);
      GameObject logo = context.getGameWorld().addObject();
      logo.setDimensions(8f, 8f);
      logo.setType("LOGO");
      logo.setAttribute(Orientation.class, Orientation.RIGHT);

      context.getGameCamera().setStickToWorldBounds(false);
      context.getGameCamera().setDefaultZoomFactor(0.15f);
      context.getGameCamera().setZoomScalingFactor(1f);
      context.getGameCamera().setTrackingTarget(logo, true);
      context.getGameCamera().setTargetTrackingSpeed(0f);
      Tween.call(new TweenCallback() {
         @Override
         public void onEvent(int i, BaseTween<?> baseTween) {
            if (!exiting) {
               context.getScreenTransitions().out(new IntroScreen(getGame()), 1f);
            }
         }
      }).delay(1.3f).start(SharedTweenManager.getInstance());

      HorizontalGroup layout = new HorizontalGroup();
      //Label bitbrain = new Label("a bitbrain production", Styles.LABEL_INTRO_BITBRAIN);
      List<String> content = new ArrayList<String>();
      content.add("a bitbrain production");
      TerminalUI bitbrain = new TerminalUI(content, Styles.LABEL_INTRO_BITBRAIN);
      layout.addActor(bitbrain);
      layout.setScale(0.075f);
      layout.setPosition(-(44f) / 2f + logo.getWidth() / 2f, 8f);
      context.getWorldStage().addActor(layout);

      setupShaders();
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      if (!exiting && Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
         context.getScreenTransitions().out(new IntroScreen(getGame()), 1f);
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
