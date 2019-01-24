package de.bitbrain.scape.ui.effects;

import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.scape.ui.intro.TerminalUI;

import java.util.Random;

public class TextGlitchRandomizer {

   private static final float GLITCH_INTERVAL = 0.05f;
   private static final String CHAR_LIST =
         "-_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

   private boolean started = false;

   private final TerminalUI terminalUI;

   private final DeltaTimer interval = new DeltaTimer();

   public TextGlitchRandomizer(TerminalUI terminalUI) {
      this.terminalUI = terminalUI;
   }

   public void start() {
      started = true;
   }

   public void update(float delta) {
      if (!started) {
         return;
      }
      interval.update(delta);
      if (interval.reached(GLITCH_INTERVAL)) {
         interval.reset();
         randomize();
      }
   }

   private void randomize() {
      String text = terminalUI.getText();

      StringBuilder randStr = new StringBuilder();
      for(int i=0; i<text.length(); i++){
         if (text.charAt(i) == '\n') {
            randStr.append("\n");
            continue;
         }
         int number = getRandomNumber();
         char ch = CHAR_LIST.charAt(number);
         randStr.append(ch);
      }
      terminalUI.setText(randStr.toString());
   }

   private int getRandomNumber() {
      int randomInt = 0;
      Random randomGenerator = new Random();
      randomInt = randomGenerator.nextInt(CHAR_LIST.length());
      if (randomInt - 1 == -1) {
         return randomInt;
      } else {
         return randomInt - 1;
      }
   }

}
