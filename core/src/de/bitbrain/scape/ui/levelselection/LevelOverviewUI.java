package de.bitbrain.scape.ui.levelselection;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.ValueTween;
import de.bitbrain.braingdx.util.ValueProvider;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.i18n.Bundle;
import de.bitbrain.scape.i18n.Messages;
import de.bitbrain.scape.level.LevelMetaData;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.Styles;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LevelOverviewUI extends Table {

   private static final int MARGIN = 12;
   private static final int BG_RADIUS = 3;

   private final GameObject reference;
   private final int alignment;
   private final Actor description;
   private final Actor label;
   private final NinePatch selection;
   private final ParticleEffect selectedParticleEffect;
   private final ParticleEffect normalParticleEffect;
   private final Map<ParticleEmitter, Integer> selectedParticleCounts = new HashMap<ParticleEmitter, Integer>();
   private final Map<ParticleEmitter, Integer> normalParticleCounts = new HashMap<ParticleEmitter, Integer>();

   private ValueProvider spacing = new ValueProvider();

   public LevelOverviewUI(LightingManager lightingManager, ParticleManager particleManager, LevelMetaData metadata, int alignment, GameObject reference) {
      this.reference = reference;
      this.alignment = alignment;
      PlayerProgress progress = new PlayerProgress(metadata);
      this.label = new Label(metadata.getName().toUpperCase(), Styles.LABEL_SELECTION_CAPTION);
      setAlignment(add(label), alignment).row();
      this.description = new Label(Bundle.get(Messages.MENU_INGAME_LEVEL).toUpperCase() + " " + metadata.getLevelNumber(), Styles.LABEL_SELECTION_DESCRIPTION);
      setAlignment(add(description), alignment).row();
      invalidatePosition();
      this.selection = GraphicsFactory.createNinePatch(SharedAssetManager.getInstance().get(Assets.Textures.SELECTION_NINEPATCH, Texture.class), 3);
      getColor().a = 0;
      spacing.setValue(1f);
      Tween.to(spacing, ValueTween.VALUE, 0.2f)
            .target(2f)
            .ease(TweenEquations.easeInOutQuart)
            .repeatYoyo(Tween.INFINITY, 0)
            .start(SharedTweenManager.getInstance());
      this.selectedParticleEffect = particleManager.spawnEffect(Assets.Particles.BYTE, reference.getLeft(), reference.getTop());
      this.normalParticleEffect = particleManager.spawnEffect(Assets.Particles.STAGE, reference.getLeft(), reference.getTop());
      for (ParticleEmitter emitter :selectedParticleEffect.getEmitters()) {
         selectedParticleCounts.put(emitter, emitter.getMaxParticleCount());
         emitter.setMaxParticleCount(0);
      }
      for (ParticleEmitter emitter :normalParticleEffect.getEmitters()) {
         normalParticleCounts.put(emitter, emitter.getMaxParticleCount());
      }
      lightingManager.addPointLight(UUID.randomUUID().toString(), reference.getLeft(), reference.getTop(), 12f, Color.WHITE);
   }

   public void show() {
      for (ParticleEmitter emitter :selectedParticleEffect.getEmitters()) {
         emitter.setMaxParticleCount(selectedParticleCounts.get(emitter));
      }
      for (ParticleEmitter emitter :normalParticleEffect.getEmitters()) {
         emitter.setMaxParticleCount(0);
      }
      SharedTweenManager.getInstance().killTarget(this);
      Tween.to(this, ActorTween.ALPHA, 0.4f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
   }

   public void hide() {
      for (ParticleEmitter emitter :selectedParticleEffect.getEmitters()) {
         emitter.setMaxParticleCount(0);
      }
      for (ParticleEmitter emitter :normalParticleEffect.getEmitters()) {
         emitter.setMaxParticleCount(normalParticleCounts.get(emitter));
      }
      SharedTweenManager.getInstance().killTarget(this);
      Tween.to(this, ActorTween.ALPHA, 0.1f)
            .target(0f)
            .start(SharedTweenManager.getInstance());
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      Color shadowColor = getColor().cpy();
      shadowColor.a *= 0.3f;
      batch.setColor(shadowColor);
      Texture background = SharedAssetManager.getInstance().get(Assets.Textures.UI_BG, Texture.class);
      batch.draw(background, reference.getLeft() - BG_RADIUS, reference.getTop() - BG_RADIUS, BG_RADIUS * 2, BG_RADIUS * 2);
      batch.setColor(getColor());
      float left = (float) (Math.floor(reference.getLeft() / 8) * 8);
      float top = (float) (Math.floor(reference.getTop() / 8) * 8);
      selection.draw(batch, left - spacing.getValue(), top - spacing.getValue(), 7 + spacing.getValue() * 2, 7 + spacing.getValue() * 2);
      Texture selectionFill = SharedAssetManager.getInstance().get(Assets.Textures.SELECTION_TARGET, Texture.class);
      batch.draw(selectionFill, left + 2, top + 2);
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
