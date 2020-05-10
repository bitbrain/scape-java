package de.bitbrain.scape.ui.ingame;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.level.LevelMetaData;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.Styles;

public class PointsLabel extends Actor {

   private static final float TRANSPARENCY = 1f;

   private final PlayerProgress context;
   private final LevelMetaData metadata;

   private final Label pointLabel;
   private final Label overlayLabel;
   private final Label allPointsLabel;
   private final Texture background;

   private int previousPoints;

   public PointsLabel(PlayerProgress context, LevelMetaData metadata) {
      pointLabel = new Label("0", Styles.LABEL_INGAME_POINTS);
      allPointsLabel = new Label("/" +  metadata.getNumberOfBytes(), Styles.LABEL_INGAME_POINTS_ALL);
      overlayLabel = new Label("0", Styles.LABEL_INGAME_POINTS);
      overlayLabel.getColor().a = 0f;
      this.context = context;
      this.metadata = metadata;
      getColor().a =TRANSPARENCY;
      background = Asset.get(Assets.Textures.UI_BG, Texture.class);
   }

   @Override
   public float getWidth() {
      return pointLabel.getPrefWidth() + allPointsLabel.getPrefWidth();
   }

   @Override
   public float getHeight() {
      return pointLabel.getPrefHeight();
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      batch.setColor(Color.WHITE.cpy());
      batch.draw(background, getX() - 60, getY() - 60, getWidth() + 120, getHeight() + 120);

      pointLabel.setPosition(getX(), getY());
      allPointsLabel.setPosition(getX() + pointLabel.getPrefWidth() + 10f, getY());
      overlayLabel.setPosition(getX(), getY());


      pointLabel.draw(batch, parentAlpha);
      allPointsLabel.draw(batch, parentAlpha);
      overlayLabel.draw(batch, parentAlpha);

      if (context.getPoints() == metadata.getNumberOfBytes()) {
         pointLabel.setStyle(Styles.LABEL_INGAME_POINTS_COMPLETE);
         allPointsLabel.setStyle(Styles.LABEL_INGAME_POINTS_ALL_COMPLETE);
      } else {
         pointLabel.setStyle(Styles.LABEL_INGAME_POINTS);
         allPointsLabel.setStyle(Styles.LABEL_INGAME_POINTS_ALL);
      }
      // Points have changed! Update label
      if (previousPoints != context.getPoints()) {
         pointLabel.setText(Integer.toString(context.getPoints()));
         overlayLabel.setOrigin(Align.center);
         overlayLabel.setText(pointLabel.getText());
         if (context.getPoints() != 0) {
            overlayLabel.setScale(1f);
            overlayLabel.getColor().a = 1f;
            Tween.to(overlayLabel, ActorTween.ALPHA, 0.5f)
                  .target(0f)
                  .start(SharedTweenManager.getInstance());
            Tween.to(overlayLabel, ActorTween.SCALE, 0.5f)
                  .target(5f)
                  .start(SharedTweenManager.getInstance());
         }
      }
      previousPoints = context.getPoints();
   }
}
