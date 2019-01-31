package de.bitbrain.scape.input.mainmenu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import de.bitbrain.scape.ui.ButtonMenu;

public class MainMenuKeyboardInputAdapter extends InputAdapter {

   private final ButtonMenu buttonMenu;

   public MainMenuKeyboardInputAdapter(ButtonMenu buttonMenu) {
      this.buttonMenu = buttonMenu;
   }

   @Override
   public boolean keyDown(int keycode) {
      if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A || keycode == Input.Keys.UP) {
         buttonMenu.checkPrevious();
         return true;
      }
      if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D || keycode == Input.Keys.DOWN) {
         buttonMenu.checkNext();
         return true;
      }
      if (keycode == Input.Keys.ENTER) {
         buttonMenu.clickChecked();
         return true;
      }
      return false;
   }

   @Override
   public boolean scrolled(int amount) {
      if (amount < 0) {
         buttonMenu.checkNext();
         return true;
      } else if (amount > 0) {
         buttonMenu.checkPrevious();
         return true;
      }
      return super.scrolled(amount);
   }
}
