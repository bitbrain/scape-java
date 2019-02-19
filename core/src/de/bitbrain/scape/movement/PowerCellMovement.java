package de.bitbrain.scape.movement;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;

public class PowerCellMovement extends BehaviorAdapter {

   private static final float STRENGTH = 1.5f;

   private Vector2 offset = new Vector2();

   @Override
   public void update(GameObject source, float delta) {
      offset.x = (float) (Math.random() < 0.5f ? Math.random() : Math.random() * -1f) * STRENGTH;
      offset.y = (float) (Math.random() < 0.5f ? Math.random() : Math.random() * -1f) * STRENGTH;
      source.setOffset(offset.x, offset.y);
      super.update(source, delta);
   }
}
