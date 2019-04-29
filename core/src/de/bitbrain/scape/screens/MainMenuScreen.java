package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.input.controller.NavigateableControllerInput;
import de.bitbrain.braingdx.input.keyboard.NavigateableKeyboardInput;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.ui.NavigationMenu;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.Styles;

import static de.bitbrain.scape.GameConfig.*;
import static de.bitbrain.scape.i18n.Bundle.get;
import static de.bitbrain.scape.i18n.Messages.*;

public class MainMenuScreen extends AbstractScreen<ScapeGame> {

   private NavigationMenu<TextButton> buttonMenu;

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
      if (Gdx.app.getType() != Application.ApplicationType.Android && Gdx.app.getType() != Application.ApplicationType.iOS) {
         setupShaders();
      }
      setupInput(context);
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

      Actor logo = createAnimatedLogo("scape_");
      layout.add(logo).padBottom(80f).row();

      NavigationMenu.NavigationMenuStyle style = new NavigationMenu.NavigationMenuStyle();
      style.padding = MENU_BUTTON_PADDING;
      style.hoverSound = SharedAssetManager.getInstance().get(Assets.Sounds.SELECT, Sound.class);
      style.enterSound = SharedAssetManager.getInstance().get(Assets.Sounds.SUBMIT, Sound.class);
      buttonMenu = new NavigationMenu<TextButton>(style);
      buttonMenu.add(new TextButton(get(MENU_MAIN_CONTINUE), Styles.BUTTON_MENU), new ClickListener() {

         @Override
               public void clicked(InputEvent event, float x, float y) {
                  context.getScreenTransitions().out(new LevelSelectionScreen(getGame(), true), 0.5f);
                  Tween.to(context.getGameCamera(), GameCameraTween.DEFAULT_ZOOM_FACTOR, 0.7f)
                        .target(0.001f)
                        .ease(TweenEquations.easeInExpo)
                        .start(SharedTweenManager.getInstance());
               }
      }).width(MENU_BUTTON_WIDTH).height(MENU_BUTTON_HEIGHT);
      buttonMenu.add(new TextButton(get(MENU_MAIN_NEWGAME), Styles.BUTTON_MENU), new ClickListener() {
               @Override
               public void clicked(InputEvent event, float x, float y) {
                  PlayerProgress progress = new PlayerProgress(null);
                  progress.reset();
                  context.getScreenTransitions().out(new IntroScreen(getGame()), 0.5f);
               }
      }).width(MENU_BUTTON_WIDTH).height(MENU_BUTTON_HEIGHT);
      buttonMenu.add(new TextButton(get(MENU_MAIN_EXIT), Styles.BUTTON_MENU), new ClickListener() {
               @Override
               public void clicked(InputEvent event, float x, float y) {
                  Gdx.app.exit();
               }
      }).width(MENU_BUTTON_WIDTH).height(MENU_BUTTON_HEIGHT);

      buttonMenu.next();

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
      context.getInputManager().register(new NavigateableControllerInput(buttonMenu));
      context.getInputManager().register(new NavigateableKeyboardInput(buttonMenu));
   }

   private void setupShaders() {
      AutoReloadPostProcessorEffect<Bloom> bloomEffect = context.getShaderManager().createBloomEffect();
      AutoReloadPostProcessorEffect<Vignette> vignetteEffect = context.getShaderManager().createVignetteEffect();
      bloomEffect.mutate(DEFAULT_BLOOM_CONFIG);
      context.getRenderPipeline().addEffects(RenderPipeIds.UI, vignetteEffect, bloomEffect);
   }

   private Actor createAnimatedLogo(String text) {
      HorizontalGroup logoGroup = new HorizontalGroup();
      for (int i = 0; i < text.length(); ++i) {
         Label character = new Label(text.charAt(i) + "", Styles.LABEL_LOGO);
         character.getColor().a = 0.3f;
         Tween.to(character, ActorTween.ALPHA, 1.9f)
               .delay(0.35f * i)
               .target(1f)
               .repeatYoyo(Tween.INFINITY, 0f)
               .ease(TweenEquations.easeInOutSine)
               .start(context.getTweenManager());
         logoGroup.addActor(character);
      }

      return logoGroup;
   }
}
