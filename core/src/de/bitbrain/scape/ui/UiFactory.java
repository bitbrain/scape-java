package de.bitbrain.scape.ui;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.ui.NavigationMenu;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.i18n.Messages;

import static de.bitbrain.scape.GameConfig.MENU_BUTTON_HEIGHT;
import static de.bitbrain.scape.GameConfig.MENU_BUTTON_WIDTH;
import static de.bitbrain.scape.i18n.Bundle.get;

public class UiFactory {
   public static Cell<Button> addMenuButton(final Messages message, final NavigationMenu<Button> target, final ClickListener listener) {
      return addMenuButton(message, target, listener, null);
   }


   public static Cell<Button> addMenuButton(final Messages message, final NavigationMenu<Button> target, final ClickListener listener, Boolean newRow) {
      final TextButton button = new TextButton(get(message), Styles.BUTTON_MENU);
      GlitchLabel.GlitchLabelStyle glStyle = new GlitchLabel.GlitchLabelStyle();
      glStyle.fadeInDuration = 0.3f;
      glStyle.font = Styles.BUTTON_MENU.font;
      glStyle.fontColor = Styles.BUTTON_MENU.fontColor;
      final GlitchLabel glitchLabel = new GlitchLabel(get(message), glStyle);
      glitchLabel.setAlignment(Align.center);
      button.setLabel(glitchLabel);
      return target.add(button, new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            listener.clicked(event, x, y);
         }

         @Override
         public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            glitchLabel.glitchIn();
         }

         @Override
         public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            super.exit(event, x, y, pointer, toActor);
         }
      }, newRow).width(MENU_BUTTON_WIDTH)
        .height(MENU_BUTTON_HEIGHT);
   }

   public static Actor createAnimatedLogo(String text, Label.LabelStyle style, TweenManager tweenManager) {
      HorizontalGroup logoGroup = new HorizontalGroup();
      logoGroup.space(12f);
      for (int i = 0; i < text.length(); ++i) {
         Actor character = getLogoActor(text.charAt(i), style);
         character.getColor().a = 0.3f;
         Tween.to(character, ActorTween.ALPHA, 1.9f)
               .delay(0.35f * i)
               .target(1f)
               .repeatYoyo(Tween.INFINITY, 0f)
               .ease(TweenEquations.easeInOutSine)
               .start(tweenManager);
         logoGroup.addActor(character);
      }
      return logoGroup;
   }

   private static Actor getLogoActor(char character, Label.LabelStyle style) {
      if (character == ' ') {
         Sprite sprite = new Sprite(SharedAssetManager.getInstance().get(Assets.Textures.LOGO_PLAYER, Texture.class));
         sprite.setSize(56f, 56f);
         return new Image(new SpriteDrawable(sprite));
      } else {
         return new Label(character + "", style);
      }
   }
}
