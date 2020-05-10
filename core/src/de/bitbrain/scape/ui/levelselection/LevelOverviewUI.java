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
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.StringRandomizerTween;
import de.bitbrain.braingdx.tweens.ValueTween;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.util.StringRandomizer;
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

public class LevelOverviewUI extends Table {

   private static final String MASK = "01";
   private static final float GLITCH_UPDATE_INTERVAL = 0.05f;

   private static final int MARGIN = 12;
   private static final int BG_RADIUS = 3;

   private final GameObject reference;
   private final int alignment;
   private final Label description;
   private final Label label;
   private final Label levelProgress;
   private final NinePatch selection;
   private final ParticleEffect selectedParticleEffect;
   private final ParticleEffect normalParticleEffect;
   private final Map<ParticleEmitter, Integer> selectedParticleCounts = new HashMap<ParticleEmitter, Integer>();
   private final Map<ParticleEmitter, Integer> normalParticleCounts = new HashMap<ParticleEmitter, Integer>();

   private ValueProvider spacing = new ValueProvider();

   private StringRandomizer labelRandomizer;
   private StringRandomizer progressRandomizer;
   private StringRandomizer descriptionRandomizer;
   private DeltaTimer intervalTimer = new DeltaTimer();

   public LevelOverviewUI(LightingManager lightingManager, ParticleManager particleManager, LevelMetaData metadata, int alignment, GameObject reference) {
      this.reference = reference;
      this.alignment = alignment;
      PlayerProgress playerProgress = new PlayerProgress(metadata);
      this.label = new Label(metadata.getName().toUpperCase(), Styles.LABEL_SELECTION_CAPTION);
      setAlignment(add(label), label, alignment).width(100f).row();
      this.description = new Label(Bundle.get(Messages.MENU_INGAME_LEVEL).toUpperCase() + " " + metadata.getLevelNumber(), Styles.LABEL_SELECTION_DESCRIPTION);
      setAlignment(add(description), description, alignment).width(100f).row();
      this.levelProgress = new Label(playerProgress.getPointRecord() + "/" + playerProgress.getMetadata().getNumberOfBytes(), Styles.LABEL_SELECTION_LEVEL_PROGRESS);
      setAlignment(add(levelProgress), levelProgress, alignment).width(100f).row();
      invalidatePosition();
      this.selection = GraphicsFactory.createNinePatch(Asset.get(Assets.Textures.SELECTION_NINEPATCH, Texture.class), 3);
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
      lightingManager.createPointLight(reference.getLeft(), reference.getTop(), 12f, Color.WHITE);

      labelRandomizer = new StringRandomizer(label.getText().toString(), MASK);
      descriptionRandomizer = new StringRandomizer(description.getText().toString(), MASK);
      progressRandomizer = new StringRandomizer(levelProgress.getText().toString(), MASK);
   }

   public void show() {
      for (ParticleEmitter emitter :selectedParticleEffect.getEmitters()) {
         emitter.setMaxParticleCount(selectedParticleCounts.get(emitter));
      }
      for (ParticleEmitter emitter :normalParticleEffect.getEmitters()) {
         emitter.setMaxParticleCount(0);
      }
      SharedTweenManager.getInstance().killTarget(this);
      SharedTweenManager.getInstance().killTarget(labelRandomizer);
      SharedTweenManager.getInstance().killTarget(descriptionRandomizer);
      SharedTweenManager.getInstance().killTarget(progressRandomizer);
      Tween.to(this, ActorTween.ALPHA, 0.4f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      labelRandomizer.setFactor(1f);
      descriptionRandomizer.setFactor(1f);
      progressRandomizer.setFactor(1f);
      Tween.to(labelRandomizer, StringRandomizerTween.FACTOR, 0.6f)
            .target(0f)
            .start(SharedTweenManager.getInstance());
      Tween.to(descriptionRandomizer, StringRandomizerTween.FACTOR, 0.6f).delay(0.1f)
            .target(0f)
            .start(SharedTweenManager.getInstance());
      Tween.to(progressRandomizer, StringRandomizerTween.FACTOR, 0.4f).delay(0.3f)
            .target(0f)
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
      SharedTweenManager.getInstance().killTarget(labelRandomizer);
      SharedTweenManager.getInstance().killTarget(descriptionRandomizer);
      SharedTweenManager.getInstance().killTarget(progressRandomizer);
      Tween.to(this, ActorTween.ALPHA, 0.8f)
            .target(0f)
            .ease(TweenEquations.easeOutExpo)
            .start(SharedTweenManager.getInstance());
      Tween.to(labelRandomizer, StringRandomizerTween.FACTOR, 0.5f).delay(0.3f)
            .target(1f)
            .ease(TweenEquations.easeOutExpo)
            .start(SharedTweenManager.getInstance());
      Tween.to(descriptionRandomizer, StringRandomizerTween.FACTOR, 0.5f).delay(0.1f)
            .target(1f)
            .ease(TweenEquations.easeOutExpo)
            .start(SharedTweenManager.getInstance());
      Tween.to(progressRandomizer, StringRandomizerTween.FACTOR, 0.5f)
            .target(1f)
            .ease(TweenEquations.easeOutExpo)
            .start(SharedTweenManager.getInstance());
   }

   @Override
   public void act(float delta) {
      intervalTimer.update(delta);
      super.act(delta);
      if (intervalTimer.reached(GLITCH_UPDATE_INTERVAL)) {
         label.setText(labelRandomizer.randomize());
         description.setText(descriptionRandomizer.randomize());
         levelProgress.setText(progressRandomizer.randomize());
         intervalTimer.reset();
      }
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      Color shadowColor = getColor().cpy();
      shadowColor.a *= 0.3f;
      batch.setColor(shadowColor);
      Texture background = Asset.get(Assets.Textures.UI_BG, Texture.class);
      batch.draw(background, reference.getLeft() - BG_RADIUS, reference.getTop() - BG_RADIUS, BG_RADIUS * 2, BG_RADIUS * 2);
      batch.setColor(getColor());
      float left = (float) (Math.floor(reference.getLeft() / 8) * 8);
      float top = (float) (Math.floor(reference.getTop() / 8) * 8);
      selection.draw(batch, left - spacing.getValue(), top - spacing.getValue(), 7 + spacing.getValue() * 2, 7 + spacing.getValue() * 2);
      Texture selectionFill = Asset.get(Assets.Textures.SELECTION_TARGET, Texture.class);
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
         offset.y = getPrefHeight() / 2f + MARGIN;
      }
      setPosition(reference.getLeft() + offset.x, reference.getTop() + offset.y);
   }

   private Cell<?> setAlignment(Cell<?> cell, Label label, int align) {
      if (Align.isRight(align)) {
         label.setAlignment(Align.left);
         return cell.left();
      }
      if (Align.isLeft(align)) {
         label.setAlignment(Align.right);
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
