package de.bitbrain.scape.ui.ingame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.progress.PlayerProgress;

import java.util.concurrent.TimeUnit;

public class CurrentTimeLabel extends Label {

   private final PlayerProgress progress;
   private final Texture background;

   public CurrentTimeLabel(PlayerProgress progress, LabelStyle labelStyle) {
      super("", labelStyle);
      this.progress = progress;
      background = SharedAssetManager.getInstance().get(Assets.Textures.UI_BG, Texture.class);
   }

   @Override
   public void act(float delta) {
      super.act(delta);
      int minutes = (int) (progress.getCurrentTime() /(1000 * 60));
      int seconds = (int) (progress.getCurrentTime() / 1000 % 60);
      int millis  = (int) (progress.getCurrentTime() % 1000);
      setText( String.format("%02d:%02d.%03d", minutes, seconds, millis));
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      batch.setColor(Color.WHITE.cpy());
      batch.draw(background, getX() - 60, getY() - 60, getPrefWidth() + 120, getPrefHeight() + 120);
      super.draw(batch, parentAlpha);
   }
}
