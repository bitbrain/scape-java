package de.bitbrain.scape.ui.ingame;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.Styles;

public class PointsLabel extends Label {

   private static final float TRANSPARENCY = 0.4f;

   private final PlayerProgress context;

   private int previousPoints;

   public PointsLabel(PlayerProgress context) {
      super("0", Styles.LABEL_INGAME_POINTS);
      this.context = context;
      getColor().a =TRANSPARENCY;
   }

   @Override
   public void act(float delta) {
      super.act(delta);
      // Points have changed! Update label
      if (previousPoints != context.getPoints()) {
         SharedTweenManager.getInstance().killTarget(this);
         getColor().a = 1f;
         Tween.to(this, ActorTween.ALPHA, 1f)
               .target(TRANSPARENCY)
               .start(SharedTweenManager.getInstance());

         setText(Integer.toString(context.getPoints()));
      }
      previousPoints = context.getPoints();
   }
}
