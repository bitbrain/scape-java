package de.bitbrain.scape.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.animation.AnimationSupplier;
import de.bitbrain.braingdx.graphics.renderer.AnimationRenderer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.model.Direction;

public class DirectionAnimationRenderer extends AnimationRenderer {

   public DirectionAnimationRenderer(AnimationSupplier<GameObject> supplier) {
      super(supplier);
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      object.getScale().y = Direction.DOWN.equals(object.getAttribute(Direction.class)) ?
            1 : -1;
      System.out.println(object.getScale().y);
      super.render(object, batch, delta);
   }
}
