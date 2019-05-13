package de.bitbrain.scape.ui.levelselection;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.i18n.Bundle;
import de.bitbrain.scape.i18n.Messages;
import de.bitbrain.scape.level.LevelMetaData;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.Styles;

public class LevelOverviewUI extends Table {

   private static final int MARGIN = 8;

   private final GameObject reference;
   private final int alignment;
   private final Actor description;
   private final Actor label;

   public LevelOverviewUI(LevelMetaData metadata, int alignment, GameObject reference) {
      this.reference = reference;
      this.alignment = alignment;
      PlayerProgress progress = new PlayerProgress(metadata);
      this.label = new Label(metadata.getName().toUpperCase(), Styles.LABEL_SELECTION_CAPTION);
      setAlignment(add(label), alignment).row();
      this.description = new Label(Bundle.get(Messages.MENU_INGAME_LEVEL).toUpperCase() + " " + metadata.getLevelNumber(), Styles.LABEL_SELECTION_DESCRIPTION);
      setAlignment(add(description), alignment).row();
      invalidatePosition();
      getColor().a = 0;
   }

   public void show() {
      SharedTweenManager.getInstance().killTarget(this);
      Tween.to(this, ActorTween.ALPHA, 0.8f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
   }

   public void hide() {
      SharedTweenManager.getInstance().killTarget(this);
      Tween.to(this, ActorTween.ALPHA, 0.1f)
            .target(0f)
            .start(SharedTweenManager.getInstance());
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      batch.setColor(getColor());
      super.draw(batch, parentAlpha);
   }

   private void invalidatePosition() {
      Vector2 offset = new Vector2();
      if (Align.isLeft(alignment)) {
         offset.x = -getPrefWidth() / 2f - MARGIN;
      }
      if (Align.isRight(alignment)) {
         offset.x = getPrefWidth() / 2f + MARGIN;
      }
      if (Align.isBottom(alignment)) {
         offset.y = -getPrefHeight() / 2f - MARGIN;
      }
      if (Align.isTop(alignment)) {
         offset.y = getPrefWidth() / 2f + MARGIN;
      }
      setPosition(reference.getLeft() + offset.x, reference.getTop() + offset.y);
   }

   private Cell<?> setAlignment(Cell<?> cell, int align) {
      if (Align.isRight(align)) {
         return cell.left();
      }
      if (Align.isLeft(align)) {
         return cell.right();
      }
      if (Align.isTop(align)) {
         return cell.bottom();
      }
      if (Align.isBottom(align)) {
         return cell.top();
      }
      return cell;
   }
}
