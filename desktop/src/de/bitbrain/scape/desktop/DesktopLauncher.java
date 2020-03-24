package de.bitbrain.scape.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.gsv.GameServiceFactory;
import de.golfgl.gdxgamesvcs.GpgsClient;
import de.golfgl.gdxgamesvcs.IGameServiceClient;

import javax.swing.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useHDPI = true;
		config.vSyncEnabled = true;
		config.resizable = true;
		config.title = "scape (Evil Corp. Edition)";
		setApplicationIcon(config);

		final GameServiceFactory gameServiceFactory = new GameServiceFactory() {
			@Override
			public IGameServiceClient create() {
				// TODO setup client secret
				return new GpgsClient().initialize("scape",
						Gdx.files.internal("gpgs-client_secret.json"), true);
			}
		};

		new LwjglApplication(new ScapeGame(gameServiceFactory, arg), config);
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
