package de.bitbrain.scape.event;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.screens.TransitionCallback;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.animation.Animator;
import de.bitbrain.scape.graphics.CharacterType;
import de.bitbrain.scape.movement.PlayerMovement;
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
         Tween.to(ingameScreen.getPlayer(), GameObjectTween.SCALE_X, 0.3f)
            .target(0f)
            .start(SharedTweenManager.getInstance());
         Tween.to(ingameScreen.getPlayer(), GameObjectTween.SCALE_Y, 0.3f)
            .target(0f)
            .start(SharedTweenManager.getInstance());
         Tween.to(ingameScreen.getPlayer(), GameObjectTween.ALPHA, 0.3f)
            .target(0f)
            .start(SharedTweenManager.getInstance());
         Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
               context.getScreenTransitions().out(new TransitionCallback() {
                  @Override
                  public void beforeTransition() {
                  }
                  @Override
                  public void afterTransition() {
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
                     Tween.to(ingameScreen.getPlayer(), GameObjectTween.ALPHA, 0.3f)
                           .target(1f)
                           .start(SharedTweenManager.getInstance());
                     context.getScreenTransitions().in(new TransitionCallback() {
                        @Override
                        public void beforeTransition() {

                        }

                        @Override
                        public void afterTransition() {
                           ingameScreen.setGameOver(false);
                        }
                     }, 0.3f);
                  }
               }, 0.3f);
            }
         });
   }
}
