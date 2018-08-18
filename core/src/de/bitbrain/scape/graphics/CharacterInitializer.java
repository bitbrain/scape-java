package de.bitbrain.scape.graphics;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.graphics.animation.*;
import de.bitbrain.braingdx.graphics.animation.types.AnimationTypes;

import java.util.HashMap;
import java.util.Map;

public class CharacterInitializer {

   public static SpriteSheetAnimation createAnimations(GameContext context, SpriteSheet sheet, CharacterType type, AnimationType animationType) {
      Map<Integer, SpriteSheetAnimationFactory.Index> indices = createSpriteIndices(type);
      SpriteSheetAnimationFactory animationFactory = new SpriteSheetAnimationFactory(sheet, indices);
      SpriteSheetAnimation animation = null;
      for (Map.Entry<Integer, SpriteSheetAnimationFactory.Index> entry : indices.entrySet()) {
         animation = animationFactory
               .create(entry.getKey())
               .base(0)
               .frames(8)
               .interval(0.05f)
               .type(animationType)
               .direction(SpriteSheetAnimation.Direction.HORIZONTAL)
               .origin(entry.getValue().x, entry.getValue().y)
               .source(sheet);
         SpriteSheetAnimationSupplier supplier = new SpriteSheetAnimationSupplier(orientations(), animation,
               animationType);
         context.getBehaviorManager().apply(supplier);
         context.getRenderManager().register(CharacterType.values()[entry.getKey()].name(), new DirectionAnimationRenderer(supplier));
      }
      return animation;
   }

   private static Map<Orientation, Integer> orientations() {
      Map<Orientation, Integer> map = new HashMap<Orientation, Integer>();
      map.put(Orientation.RIGHT, 0);
      map.put(Orientation.DOWN, 0);
      map.put(Orientation.LEFT, 0);
      map.put(Orientation.UP, 0);

      return map;
   }

   private static Map<Integer, SpriteSheetAnimationFactory.Index> createSpriteIndices(CharacterType type) {
      Map<Integer, SpriteSheetAnimationFactory.Index> indices = new HashMap<Integer, SpriteSheetAnimationFactory.Index>();
      indices.put(type.ordinal(), new SpriteSheetAnimationFactory.Index(0, 0));
      return indices;
   }
}