package com.home.jsquad.knowhunt.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.home.jsquad.knowhunt.android.activities.MapsActivity;
import com.home.knowhunt.maze.MazeGame;

public class AndroidLauncher extends AndroidApplication implements MazeGame.MyGameCallback {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		MazeGame.setMyGameCallback(this);
		initialize(new MazeGame(), config);

	}

	@Override
	public void onStartSomeActivity(boolean win, int points) {

		Intent score = new Intent();
		int ppoints=0;
		if(win) {
		ppoints=10;
			score.putExtra("message", "Well done! You've got " + 10 + " points");
		}
		else
			score.putExtra("message", "Nice try! Unfortunately you haven't got point for this round");
		score.putExtra("scores", ppoints);
		setResult(Activity.RESULT_OK, score);
		finish();

	}
}
