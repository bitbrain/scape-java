package de.bitbrain.scape.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.scape.GameConfig;

import java.util.ArrayList;
import java.util.List;

public class GameInputManager extends InputAdapter {

   public static interface GameInputListener {
      void onSwipe(Orientation orientation);
      void onTouch();
      void onType(int key);
   }

   private final List<GameInputListener> listeners = new ArrayList<GameInputListener>();
   private boolean touched = false;
   private boolean dragged = false;

   public void addListener(GameInputListener listener) {
      listeners.add(listener);
   }

   @Override
   public boolean touchUp(int screenX, int screenY, int pointer, int button) {
      if (touched) {
         for (GameInputListener l : listeners) {
            l.onTouch();
         }
      }
      touched = false;
      dragged = false;
      return false;
   }

   @Override
   public boolean keyDown(int keycode) {
      if (touched) {
         return false;
      }
      for (GameInputListener l : listeners) {
         l.onType(keycode);
      }
      touched = true;
      return true;
   }

   @Override
   public boolean keyUp(int keycode) {
      touched = false;
      return false;
   }

   @Override
   public boolean touchDragged(int screenX, int screenY, int pointer) {
      if (dragged) {
         return false;
      }
      int deltaX = Gdx.input.getDeltaX(pointer);
      int deltaY = Gdx.input.getDeltaY(pointer);

      int absDeltaX = Math.abs(deltaX);
      int absDeltaY = Math.abs(deltaY);

      if (absDeltaX > GameConfig.SWIPE_TOLERANCE && absDeltaX > absDeltaY) {
         if (deltaX < 0) {
            for (GameInputListener l : listeners) {
               l.onSwipe(Orientation.LEFT);
            }
         } else {
            for (GameInputListener l : listeners) {
               l.onSwipe(Orientation.RIGHT);
            }
         }
      } else if (absDeltaY > GameConfig.SWIPE_TOLERANCE) {
         if (deltaY < 0) {
            for (GameInputListener l : listeners) {
               l.onSwipe(Orientation.UP);
            }
         } else {
            for (GameInputListener l : listeners) {
               l.onSwipe(Orientation.DOWN);
            }
         }
      } else {
         touched = true;
         return false;
      }
      touched = false;
      dragged = true;
      return true;
   }
}
