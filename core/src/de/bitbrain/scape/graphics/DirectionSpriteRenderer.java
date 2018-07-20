package de.bitbrain.scape.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.model.Direction;

public class DirectionSpriteRenderer extends SpriteRenderer {

   public DirectionSpriteRenderer(String textureId) {
      super(textureId);
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      object.getScale().y = Direction.DOWN.equals(object.getAttribute(Direction.class)) ?
            1 : -1;
      super.render(object, batch, delta);
   }
}
