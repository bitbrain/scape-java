package de.bitbrain.scape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.*;
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
import de.bitbrain.braingdx.ui.AnimationDrawable;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.preferences.PlayerProgress;
import de.bitbrain.scape.ui.ButtonMenu;
import de.bitbrain.scape.ui.ButtonMenuControls;
import de.bitbrain.scape.ui.Styles;

import static de.bitbrain.scape.GameConfig.DEFAULT_BLOOM_CONFIG;
import static de.bitbrain.scape.i18n.Bundle.get;
import static de.bitbrain.scape.i18n.Messages.*;

public class MainMenuScreen extends AbstractScreen<ScapeGame> {

   private static final String LOGO_ID = "LOGO";

   public MainMenuScreen(ScapeGame game) {
      super(game);
   }

   private GameContext context;

   private boolean exiting = false;

   @Override
   protected void onCreate(GameContext context) {
      this.context = context;
      context.getScreenTransitions().in(0.3f);
      setBackgroundColor(Colors.BACKGROUND_VIOLET);
      setupUI(context);
      setupShaders();

      float effectWidth = 200;
      context.getParticleManager()
            .spawnEffect(Assets.Particles.MENU, 0f, 0f)
            .scaleEffect(Gdx.graphics.getWidth() / effectWidth);
      context.getParticleManager()
            .spawnEffect(Assets.Particles.MENU_ALT, 0f, 0f)
            .scaleEffect(Gdx.graphics.getWidth() / effectWidth);

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
                  .duration(0.07f)
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

      ButtonMenu buttonMenu = new ButtonMenu(context.getTweenManager());
      buttonMenu.add(get(MENU_MAIN_CONTINUE), new ClickListener() {
               @Override
               public void clicked(InputEvent event, float x, float y) {
                  context.getScreenTransitions().out(new LevelSelectionScreen(getGame(), true), 1f);
               }
      });
      buttonMenu.add(get(MENU_MAIN_NEWGAME), new ClickListener() {
               @Override
               public void clicked(InputEvent event, float x, float y) {
                  PlayerProgress progress = new PlayerProgress(null);
                  progress.reset();
                  context.getScreenTransitions().out(new IntroScreen(getGame()), 1f);
               }
      });
      buttonMenu.add(get(MENU_MAIN_EXIT), new ClickListener() {
               @Override
               public void clicked(InputEvent event, float x, float y) {
                  Gdx.app.exit();
               }
      });

      buttonMenu.checkNext();

      context.getInput().addProcessor(new ButtonMenuControls(buttonMenu));

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

   private void setupShaders() {
      AutoReloadPostProcessorEffect<Bloom> bloomEffect = context.getShaderManager().createBloomEffect();
      AutoReloadPostProcessorEffect<Vignette> vignetteEffect = context.getShaderManager().createVignetteEffect();
      bloomEffect.mutate(DEFAULT_BLOOM_CONFIG);
      context.getRenderPipeline().getPipe(RenderPipeIds.UI).addEffects(vignetteEffect, bloomEffect);
   }
}
