package de.bitbrain.scape.i18n;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import de.bitbrain.scape.assets.Assets;

import java.util.Locale;

public final class Bundle {

   private static final String PATH = "i18n/translations";

   private static I18NBundle translations;

   private static FileHandle generalHandle;

   public static void load() {
      Gdx.app.log("LOAD", "Loading translation bundles...");
      generalHandle = Gdx.files.internal(PATH);
      Locale locale = Locale.getDefault();
      setLocale(locale);
      Gdx.app.log("INFO", "Done loading translation bundles.");
   }

   public static String get(Messages key) {
      return translations.get(key.getKey());
   }

   public static void setLocale(Locale locale) {
      Gdx.app.log("INFO", "Set locale to '" + locale + "'");
      translations = I18NBundle.createBundle(generalHandle, locale);
   }
}