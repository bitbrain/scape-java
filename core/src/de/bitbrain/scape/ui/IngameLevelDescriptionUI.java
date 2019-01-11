package de.bitbrain.scape.ui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.scape.i18n.Bundle;
import de.bitbrain.scape.i18n.Messages;

public class IngameLevelDescriptionUI extends Table {

   private final float TRANSPARENCY = 0.8f;

   private final Label caption, description, pressAnyKey;

   public IngameLevelDescriptionUI(String levelName, int levelNumber) {
      setFillParent(true);
      caption = new Label(levelName, Styles.LABEL_INGAME_CAPTION);
      description = new Label(Bundle.get(Messages.MENU_INGAME_LEVEL) + " " + levelNumber, Styles.LABEL_INGAME_DESCRIPTION);
      pressAnyKey = new Label(Bundle.get(Messages.MENU_INGAME_PRESS_ANY_KEY), Styles.LABEL_INGAME_INTERACTION);
      add(caption).row();
      add(description).padBottom(Gdx.graphics.getHeight() / 8).row();
      add(pressAnyKey);

      Sprite background = new Sprite(GraphicsFactory.createTexture(2, 2, Color.BLACK));
      background.setAlpha(TRANSPARENCY);
      setBackground(new SpriteDrawable(background));

      caption.getColor().a = 0f;
      description.getColor().a = 0f;
      pressAnyKey.getColor().a = 0f;
   }

   public void show(float duration) {
      Tween.to(caption, ActorTween.ALPHA, duration / 3)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(description, ActorTween.ALPHA, duration / 3).delay(duration / 3)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(pressAnyKey, ActorTween.ALPHA, duration / 3).delay(duration / 3 * 2)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> source) {
                  Tween.to(pressAnyKey, ActorTween.ALPHA, 0.5f)
                        .target(0.5f)
                        .repeatYoyo(Tween.INFINITY, 0f)
                        .start(SharedTweenManager.getInstance());
               }
            })
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .target(1f)
            .start(SharedTweenManager.getInstance());
   }

   public void hide(float duration) {
      SharedTweenManager.getInstance().killTarget(pressAnyKey);
      Tween.to(caption, ActorTween.ALPHA, duration / 3)
            .target(0f)
            .start(SharedTweenManager.getInstance());
      Tween.to(description, ActorTween.ALPHA, duration / 3).delay(duration / 3)
            .target(0f)
            .start(SharedTweenManager.getInstance());
      Tween.to(pressAnyKey, ActorTween.ALPHA, duration / 3).delay(duration / 3 * 2)
            .target(0f)
            .start(SharedTweenManager.getInstance());
   }
}
