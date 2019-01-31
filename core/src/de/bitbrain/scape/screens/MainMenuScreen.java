package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.ui.AnimationDrawable;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.input.mainmenu.MainMenuControllerInputAdapter;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.ButtonMenu;
import de.bitbrain.scape.input.mainmenu.MainMenuKeyboardInputAdapter;
import de.bitbrain.scape.ui.Styles;

import static de.bitbrain.scape.GameConfig.*;
import static de.bitbrain.scape.i18n.Bundle.get;
import static de.bitbrain.scape.i18n.Messages.*;

public class MainMenuScreen extends AbstractScreen<ScapeGame> {

   private ButtonMenu buttonMenu;

   public MainMenuScreen(ScapeGame game) {
      super(game);
   }

   private GameContext context;

   private boolean exiting = false;

   @Override
   protected void onCreate(GameContext context) {
      this.context = context;
      context.getScreenTransitions().in(1.5f);
      setBackgroundColor(Colors.BACKGROUND_VIOLET);
      setupUI(context);
      setupShaders();
      setupInput(context);

      float effectWidth = 200;
      ParticleEffect blueEffect = context.getParticleManager()
            .spawnEffect(Assets.Particles.MENU, 0f, Gdx.graphics.getHeight());
      blueEffect.scaleEffect(Gdx.graphics.getWidth() / effectWidth);
      ParticleEffect pinkEffect = context.getParticleManager()
            .spawnEffect(Assets.Particles.MENU_ALT, 0f, Gdx.graphics.getHeight());
      pinkEffect.scaleEffect(Gdx.graphics.getWidth() / effectWidth);
   }

   @Override
   public void dispose() {
      super.dispose();
      Controllers.clearListeners();
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      if (!exiting && Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
         exiting = true;
         Gdx.app.exit();
      }
   }

   private void setupUI(final GameContext context) {
      Table layout = new Table();
      layout.setFillParent(true);

      final Texture playerTexture = SharedAssetManager.getInstance().get(Assets.Textures.MENU_LOGO);
      AnimationSpriteSheet sheet = new AnimationSpriteSheet(playerTexture, 25, 5);
      AnimationDrawable logoDrawable = new AnimationDrawable(sheet, AnimationConfig.builder()
            .registerFrames(AnimationDrawable.DEFAULT_FRAME_ID, AnimationFrames.builder()
                  .frames(14)
                  .origin(0, 0)
                  .duration(0.05f)
                  .playMode(Animation.PlayMode.LOOP)
                  .build())
            .build());

      Image logoImage = new Image(logoDrawable);

      layout.add(logoImage)
            .width(128f * 3.3f)
            .height(32f * 3.3f)
            .padLeft(35f)
            .padBottom(100f)
            .row();

      buttonMenu = new ButtonMenu(context.getTweenManager());
      buttonMenu.add(get(MENU_MAIN_CONTINUE), new ClickListener() {
               @Override
               public void clicked(InputEvent event, float x, float y) {
                  context.getScreenTransitions().out(new LevelSelectionScreen(getGame(), true), 0.5f);
                  Tween.to(context.getGameCamera(), GameCameraTween.DEFAULT_ZOOM_FACTOR, 0.7f)
                        .target(0.001f)
                        .ease(TweenEquations.easeInExpo)
                        .start(SharedTweenManager.getInstance());
               }
      });
      buttonMenu.add(get(MENU_MAIN_NEWGAME), new ClickListener() {
               @Override
               public void clicked(InputEvent event, float x, float y) {
                  PlayerProgress progress = new PlayerProgress(null);
                  progress.reset();
                  context.getScreenTransitions().out(new IntroScreen(getGame()), 0.5f);
               }
      });
      buttonMenu.add(get(MENU_MAIN_EXIT), new ClickListener() {
               @Override
               public void clicked(InputEvent event, float x, float y) {
                  Gdx.app.exit();
               }
      });

      buttonMenu.checkNext();

      layout.padTop(100f).add(buttonMenu).padBottom(100f).row();
      Label credits = new Label(get(MENU_MAIN_CREDITS) + "\n© 2019", Styles.LABEL_CREDITS);
      credits.setAlignment(Align.center);
      credits.getColor().a = 0.3f;
      layout.add(credits);
      context.getStage().addActor(layout);
   }

   @Override
   protected Viewport getViewport(int width, int height, Camera camera) {
      return new ExtendViewport(width, height, camera);
   }

   private void setupInput(GameContext context) {
      context.getInput().addProcessor(new MainMenuKeyboardInputAdapter(buttonMenu));
      Controllers.addListener(new MainMenuControllerInputAdapter(buttonMenu));
   }

   private void setupShaders() {
      AutoReloadPostProcessorEffect<Bloom> bloomEffect = context.getShaderManager().createBloomEffect();
      AutoReloadPostProcessorEffect<Vignette> vignetteEffect = context.getShaderManager().createVignetteEffect();
      bloomEffect.mutate(DEFAULT_BLOOM_CONFIG);
      context.getRenderPipeline().getPipe(RenderPipeIds.UI).addEffects(vignetteEffect, bloomEffect);
   }
}
