package de.bitbrain.scape.ui;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.scape.GameConfig;
import java.util.ArrayList;
import java.util.List;

public class ButtonMenu extends Table {

   private TweenManager tweenManager;

   private List<Button> buttons = new ArrayList<Button>();

   private int currentCheckIndex = -1;

   private boolean checkMode;

   public ButtonMenu(TweenManager tweenManager) {
      this(tweenManager, false);
   }

   public ButtonMenu(TweenManager tweenManager, boolean checkMode) {
      this.tweenManager = tweenManager;
      this.checkMode = checkMode;
      setTouchable(Touchable.childrenOnly);
   }

   public Button add(String caption, final ClickListener listener) {
      final TextButton button = new TextButton(caption, Styles.BUTTON_MENU) {

         @Override
         public void setChecked(boolean isChecked) {
            if (!isChecked) {
               tweenManager.killTarget(this);
               Tween.to(this.getColor(), ColorTween.A, 1.0f).target(GameConfig.MENU_BUTTON_ALPHA).ease(TweenEquations.easeOutCubic).start(tweenManager);
            } else {
               tweenManager.killTarget(this);
               Tween.to(this.getColor(), ColorTween.A, 1.0f).target(1f).ease(TweenEquations.easeOutCubic).start(tweenManager);
               // TODO play audio
            }
            super.setChecked(isChecked);
         }
      };
      button.setColor(new Color(1f, 1f, 1f, GameConfig.MENU_BUTTON_ALPHA));
      button.addCaptureListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            if (!button.isDisabled()) {
               listener.clicked(event, x, y);
            }
         }

         @Override
         public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            if (!button.isDisabled()) {
               listener.enter(event, x, y, pointer, fromActor);
            }
         }

         @Override
         public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (!button.isDisabled()) {
               listener.exit(event, x, y, pointer, toActor);
            }
         }
      });
      center().add(button).width(GameConfig.MENU_BUTTON_WIDTH).height(GameConfig.MENU_BUTTON_HEIGHT).padBottom(GameConfig.MENU_BUTTON_PADDING);
      row();
      button.addCaptureListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            if (!button.isDisabled()) {
               // TODO play select sound
            }
         }

         @Override
         public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            if (!button.isDisabled() && !button.isChecked()) {
               super.enter(event, x, y, pointer, fromActor);
               tweenManager.killTarget(button);
               Tween.to(button.getColor(), ColorTween.A, 1.0f).target(1f).ease(TweenEquations.easeOutCubic).start(tweenManager);
               // TODO play hover sound
               setChecked(buttons.indexOf(button));

            }
         }

         @Override
         public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (!button.isDisabled() && !button.isChecked()) {
               super.exit(event, x, y, pointer, toActor);
               if (event.getRelatedActor() == null || (!event.getRelatedActor().equals(button) &&
                     event.getRelatedActor() instanceof TextButton)) {
                  tweenManager.killTarget(button);
                  Tween.to(button.getColor(), ColorTween.A, 1.0f).target(GameConfig.MENU_BUTTON_ALPHA).ease(TweenEquations.easeOutCubic).start(tweenManager);
               }
            }
         }
      });
      buttons.add(button);
      validateCheckState();
      return button;
   }

   public void checkNext() {
      setChecked(getNextIndex());
   }

   public void checkPrevious() {
      setChecked(getPreviusIndex());
   }

   public void clickChecked() {
      if (currentCheckIndex >= 0f && currentCheckIndex < buttons.size()) {
         Button button = buttons.get(currentCheckIndex);
         for (EventListener l : button.getCaptureListeners()) {
            if (l instanceof ClickListener) {
               ClickListener cl = (ClickListener)l;
               cl.clicked(new InputEvent(), 0f, 0f);
            }
         }
         button.setChecked(true);
      }
   }

   private void validateCheckState() {
      if (checkMode && buttons.size() == 1) {
         setChecked(0);
      }
   }

   private boolean isValidCheck(int index) {
      return index >= 0 && index < buttons.size() && !buttons.get(index).isDisabled();
   }

   private void setChecked(int index) {
      for (int i = 0; i < buttons.size(); ++i) {
         Button button = buttons.get(i);
         button.setChecked(i == index);
      }
      currentCheckIndex = index;
   }

   private int getNextIndex() {
      int index = currentCheckIndex;
      boolean inc = false;
      while (!inc || !isValidCheck(index)) {
         inc = true;
         index++;
         if (index >= buttons.size()) {
            index = 0;
         }
      }
      return index;
   }

   private int getPreviusIndex() {
      int index = currentCheckIndex;
      boolean dec = false;
      while (!dec || !isValidCheck(index)) {
         dec = true;
         index--;
         if (index < 0) {
            index = buttons.size() - 1;
         }
      }
      return index;
   }
}