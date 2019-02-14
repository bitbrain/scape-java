package de.bitbrain.scape.event;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.animation.Animator;
import de.bitbrain.scape.graphics.CharacterType;
import de.bitbrain.scape.screens.IngameScreen;

public class GameOverEventListener implements GameEventListener<GameOverEvent> {

   private final IngameScreen ingameScreen;
   private final GameContext context;

   public GameOverEventListener(IngameScreen screen, GameContext context) {
      this.ingameScreen = screen;
      this.context = context;
   }

   @Override
   public void onEvent(GameOverEvent event) {
      if (ingameScreen.isGameOver()) {
         return;
      }
      ingameScreen.setGameOver(true);
      SharedTweenManager.getInstance().killTarget(ingameScreen.getPlayer());
      Tween.to(ingameScreen.getPlayer(), GameObjectTween.SCALE_X, 0.2f)
            .target(0f)
            .start(SharedTweenManager.getInstance());
      Tween.to(ingameScreen.getPlayer(), GameObjectTween.SCALE_Y, 0.2f)
            .target(0f)
            .start(SharedTweenManager.getInstance());
      Tween.call(new TweenCallback() {
         @Override
         public void onEvent(int type, BaseTween<?> source) {
            for (GameObject o : context.getGameWorld()) {
               if (CharacterType.BYTE.name().equals(o.getType())) {
                  context.getGameWorld().remove(o);
               }
            }
            for (final GameObject o : ingameScreen.getAllLoadedBytes()) {
               GameObject newByte = context.getGameWorld().addObject(o.mutator(), false);
               Animator.animateByte(context, newByte);
            }
            ingameScreen.resetUI();
            SharedTweenManager.getInstance().killTarget(ingameScreen.getPlayer(), GameObjectTween.ALPHA);
            Tween.to(ingameScreen.getPlayer(), GameObjectTween.ALPHA, 0.2f)
                  .target(1f)
                  .setCallback(new TweenCallback() {
                     @Override
                     public void onEvent(int type, BaseTween<?> source) {
                        ingameScreen.setGameOver(false);
                     }
                  })
                  .setCallbackTriggers(TweenCallback.COMPLETE)
                  .start(SharedTweenManager.getInstance());
         }
      }).delay(0.2f).start(SharedTweenManager.getInstance());
      Tween.to(ingameScreen.getPlayer(), GameObjectTween.ALPHA, 0.2f)
            .target(0f)
            .ease(TweenEquations.easeOutCubic)
            .start(SharedTweenManager.getInstance());
   }
}
