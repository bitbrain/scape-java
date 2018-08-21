package de.bitbrain.scape.ui;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;

public class LevelDescriptionUI extends Table {

   private final float TRANSPARENCY = 0.8f;

   private final Label caption, description, pressAnyKey;

   public LevelDescriptionUI() {
      setFillParent(true);
      caption = new Label("Data Bus", Styles.LABEL_INGAME_CAPTION);
      description = new Label("Level 1", Styles.LABEL_INGAME_DESCRIPTION);
      pressAnyKey = new Label("press any key", Styles.LABEL_INGAME_INTERACTION);
      add(caption).row();
      add(description).padBottom(Gdx.graphics.getHeight() / 8).row();
      add(pressAnyKey);

      Tween.to(pressAnyKey, ActorTween.ALPHA, 0.5f)
            .target(0.5f)
            .repeatYoyo(Tween.INFINITY, 0f)
            .start(SharedTweenManager.getInstance());

      Sprite background = new Sprite(GraphicsFactory.createTexture(2, 2, Color.BLACK));
      background.setAlpha(TRANSPARENCY);
      setBackground(new SpriteDrawable(background));
   }

   public void hide(float duration) {
      SharedTweenManager.getInstance().killTarget(pressAnyKey);
      Tween.to(description, ActorTween.ALPHA, duration / 2).delay(duration / 2)
            .target(0f)
            .start(SharedTweenManager.getInstance());
      Tween.to(caption, ActorTween.ALPHA, duration / 4 * 3)
            .target(0f)
            .start(SharedTweenManager.getInstance());
      Tween.to(description, ActorTween.ALPHA, duration / 2).delay(duration / 2)
            .target(0f)
            .start(SharedTweenManager.getInstance());
   }
}
