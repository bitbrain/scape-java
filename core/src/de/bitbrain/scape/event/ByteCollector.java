package de.bitbrain.scape.event;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.audio.Sound;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.progress.PlayerProgress;

public class ByteCollector implements GameEventListener<ByteCollectedEvent> {

   private final GameWorld gameWorld;
   private final ParticleManager particleManager;
   private final PlayerProgress playerContext;

   public ByteCollector(GameWorld gameWorld, ParticleManager particleManager, PlayerProgress playerContext) {
      this.gameWorld = gameWorld;
      this.particleManager = particleManager;
      this.playerContext = playerContext;
   }

   @Override
   public void onEvent(final ByteCollectedEvent event) {
      final GameObject object = event.getByteObject();
      if (!object.isActive()) {
         return;
      }
      SharedAssetManager.getInstance().get(Assets.Sounds.COLLECT, Sound.class)
            .play(0.13f, (float) (0.65f + Math.random() * 0.3f), 0f);
      playerContext.addPoint();
      float centerX = object.getLeft() + object.getWidth() / 2f;
      float centerY = object.getTop() + object.getHeight() / 2f;
      particleManager.spawnEffect(Assets.Particles.COLLECT, centerX, centerY);
      event.getByteObject().setActive(false);
      Tween.to(event.getByteObject(), GameObjectTween.SCALE, 0.5f)
            .target(2.5f)
            .ease(TweenEquations.easeOutExpo)
            .start(SharedTweenManager.getInstance());
      Tween.to(event.getByteObject(), GameObjectTween.ALPHA, 0.5f)
            .target(0f)
            .ease(TweenEquations.easeOutExpo)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> source) {
                  gameWorld.remove(object);
               }
            })
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .start(SharedTweenManager.getInstance());
   }
}
