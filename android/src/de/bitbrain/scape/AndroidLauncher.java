package de.bitbrain.scape;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import de.bitbrain.scape.gsv.GameServiceFactory;
import de.golfgl.gdxgamesvcs.GpgsClient;
import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class AndroidLauncher extends AndroidApplication {

	private GpgsClient gpgsClient;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		gpgsClient = new GpgsClient().initialize(this, false);;

		initialize(new ScapeGame(new GameServiceFactory() {
			@Override
			public IGameServiceClient create() {
				return gpgsClient;
			}
		}), config);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (gpgsClient != null)
			gpgsClient.onGpgsActivityResult(requestCode, resultCode, data);
	}


}
