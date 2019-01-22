package de.bitbrain.scape.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class ButtonMenuControls extends InputAdapter {

   private final ButtonMenu buttonMenu;

   public ButtonMenuControls(ButtonMenu buttonMenu) {
      this.buttonMenu = buttonMenu;
   }

   @Override
   public boolean keyDown(int keycode) {
      if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
         buttonMenu.checkPrevious();
         return true;
      }
      if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
         buttonMenu.checkNext();
         return true;
      }
      if (keycode == Input.Keys.ENTER) {
         buttonMenu.clickChecked();
         return true;
      }
      return false;
   }
}
