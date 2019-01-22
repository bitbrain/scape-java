package de.bitbrain.scape.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import de.bitbrain.scape.ScapeGame;

import javax.swing.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useHDPI = false;
		config.vSyncEnabled = true;
		config.resizable = true;
		config.width = 1920;
		config.height = 1080;
		config.title = "scape (Evil Corp. Edition)";
		setApplicationIcon(config);
		new LwjglApplication(new ScapeGame(), config);
	}

	private static void setApplicationIcon(LwjglApplicationConfiguration config) {
		try {
			config.addIcon("icon-16.png", Files.FileType.Internal);
			config.addIcon("icon-32.png", Files.FileType.Internal);
			Class<?> cls = Class.forName("com.apple.eawt.Application");
			Object application = cls.newInstance().getClass().getMethod("getApplication").invoke(null);
			FileHandle icon = Gdx.files.internal("icon-128.png");
			application.getClass().getMethod("setDockIconImage", java.awt.Image.class)
					.invoke(application, new ImageIcon(icon.file().getAbsolutePath()).getImage());
		} catch (Exception e) {

		}
	}
}
