package de.bitbrain.scape.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import de.bitbrain.braingdx.ui.NavigationMenu;
import de.bitbrain.scape.i18n.Messages;

import static de.bitbrain.scape.GameConfig.MENU_BUTTON_HEIGHT;
import static de.bitbrain.scape.GameConfig.MENU_BUTTON_WIDTH;
import static de.bitbrain.scape.i18n.Bundle.get;
import static de.bitbrain.scape.i18n.Messages.MENU_MAIN_CONTINUE;

public class UiFactory {

   public static void addMenuButton(final Messages message, final NavigationMenu<TextButton> target, final ClickListener listener) {
      TextButton button = new TextButton(get(message), Styles.BUTTON_MENU);
      GlitchLabel.GlitchLabelStyle glStyle = new GlitchLabel.GlitchLabelStyle();
      glStyle.fadeInDuration = 0.3f;
      glStyle.font = Styles.BUTTON_MENU.font;
      glStyle.fontColor = Styles.BUTTON_MENU.fontColor;
      final GlitchLabel glitchLabel = new GlitchLabel(get(message), glStyle);
      glitchLabel.setAlignment(Align.center);
      button.setLabel(glitchLabel);
      target.add(button, new ClickListener() {

         @Override
         public void clicked(InputEvent event, float x, float y) {
            listener.clicked(event, x, y);
         }

         @Override
         public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            glitchLabel.glitchIn();
         }
      }).width(MENU_BUTTON_WIDTH)
        .height(MENU_BUTTON_HEIGHT);
   }
}
