package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.input.controller.NavigateableControllerInput;
import de.bitbrain.braingdx.input.keyboard.NavigateableKeyboardInput;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.ui.NavigationMenu;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.googleplay.Leaderboards;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.Styles;
import de.golfgl.gdxgamesvcs.GameServiceException;

import static de.bitbrain.scape.GameConfig.*;
import static de.bitbrain.scape.i18n.Bundle.get;
import static de.bitbrain.scape.i18n.Messages.*;
import static de.bitbrain.scape.ui.Styles.MENU_STYLE;
import static de.bitbrain.scape.ui.UiFactory.addMenuButton;
import static de.bitbrain.scape.ui.UiFactory.createAnimatedLogo;

public class MainMenuScreen extends BrainGdxScreen2D<ScapeGame> {

   private NavigationMenu<Button> buttonMenu;
   private Music music;

   public MainMenuScreen(ScapeGame game) {
      super(game);
   }

   private GameContext2D context;

   private boolean exiting = false;

   @Override
   protected void onCreate(GameContext2D context) {
      this.context = context;
      music = SharedAssetManager.getInstance().get(Assets.Musics.BACKGROUND_MAIN_MENU, Music.class);
      music.setLooping(true);
      music.setVolume(0.2f);
      music.play();
      context.getScreenTransitions().in(1.5f);
      context.setBackgroundColor(Colors.BACKGROUND_VIOLET);
      setupUI(context);
      if (Gdx.app.getType() != Application.ApplicationType.Android && Gdx.app.getType() != Application.ApplicationType.iOS) {
         setupShaders();
      }
      setupInput(context);
   }

   @Override
   public void dispose() {
      super.dispose();
      music.stop();
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      if (!exiting && Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
         exiting = true;
         Gdx.app.exit();
      }
   }

   private void setupUI(final GameContext2D context) {
      Table layout = new Table();
      layout.setFillParent(true);

      Actor logo = createAnimatedLogo("sc pe", Styles.LABEL_LOGO, context.getTweenManager());
      layout.add(logo).padTop(20f).padBottom(30f).row();

      buttonMenu = new NavigationMenu<Button>(MENU_STYLE);
      addMenuButton(MENU_MAIN_CONTINUE, buttonMenu, new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            context.getScreenTransitions().out(new StageSelectionScreen(getGame(), true), 0.5f);
            Tween.to(context.getGameCamera(), GameCameraTween.DEFAULT_ZOOM_FACTOR, 0.7f)
                  .target(0.001f)
                  .ease(TweenEquations.easeInExpo)
                  .start(SharedTweenManager.getInstance());
         }
      });
      if (!getGame().getGameServiceClient().isSessionActive()) {
         addMenuButton(MENU_MAIN_PLAY_LOGIN, buttonMenu, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               getGame().getGameServiceClient().logIn();
            }
         });
      }
      addMenuButton(MENU_MAIN_NEWGAME, buttonMenu, new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            PlayerProgress progress = new PlayerProgress(null);
            progress.reset();
            context.getScreenTransitions().out(new IntroScreen(getGame()), 0.5f);
         }
      }, true);
      if (getGame().getGameServiceClient().isSessionActive()) {
         addMenuButton(MENU_MAIN_PLAY_ACHIEVEMENTS, buttonMenu, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               try {
                  getGame().getGameServiceClient().showAchievements();
               } catch (GameServiceException e) {
                  e.printStackTrace();
                  Gdx.app.error("GooglePlay", "Unable to login");
               }
            }
         }).padLeft(0f);
      }
      addMenuButton(MENU_MAIN_EXIT, buttonMenu, new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            Gdx.app.exit();
         }
      });
      buttonMenu.next();

      layout.add(buttonMenu).padBottom(40f).row();
      Label credits = new Label(get(MENU_MAIN_CREDITS) + " © 2020", Styles.LABEL_CREDITS);
      credits.setAlignment(Align.center);
      credits.getColor().a = 0.3f;
      layout.add(credits);
      context.getWorldStage().addActor(layout);
      context.getGameCamera().setStickToWorldBounds(false);
      context.getGameCamera().setZoom(1300, GameCamera.ZoomMode.TO_WIDTH);
      context.getGameCamera().getInternalCamera().update();
   }

   @Override
   public Viewport getViewport(int width, int height, Camera camera) {
      return new ExtendViewport(width, height, camera);
   }

   private void setupInput(GameContext2D context) {
      context.getInputManager().register(new NavigateableControllerInput(buttonMenu));
      context.getInputManager().register(new NavigateableKeyboardInput(buttonMenu));
   }

   private void setupShaders() {
      AutoReloadPostProcessorEffect<Bloom> bloomEffect = context.getShaderManager().createBloomEffect();
      AutoReloadPostProcessorEffect<Vignette> vignetteEffect = context.getShaderManager().createVignetteEffect();
      bloomEffect.mutate(DEFAULT_BLOOM_CONFIG);
      context.getRenderPipeline().addEffects(RenderPipeIds.UI, vignetteEffect, bloomEffect);
   }
}
