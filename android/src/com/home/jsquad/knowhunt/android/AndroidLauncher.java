package com.home.jsquad.knowhunt.android;


/**
 * This class is the activity that is responsible of initiating the libgx game
 * it calls the constructor of MazeGame class, then uses the game call back interface to get
 * an answer from the game wether the player has won or not
 * it passes this data back to the maps activity to handle the score
 * @author      Aiham Alkaseer
 * @version     1.0
 */

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

	/**
	 * checks if the player won or not
	 * <p>
	 * this method gets the returned value of the libgdx game callback and
	 * adds point if the player has finished the game
	 * <p>
	 *
	 * @param  win boolean : it represents the winning state.
	 */


	@Override
	public void onGameEnd(boolean win) {

		Intent score = new Intent();
		int points=0;
		if(win) {
			points=10;
			score.putExtra("message", "Well done! You've got " + points + " points");
		}
		else
			score.putExtra("message", "Nice try! Unfortunately you haven't got point for this round");
			score.putExtra("scores", points);
			setResult(Activity.RESULT_OK, score);
			finish();

	}
}
