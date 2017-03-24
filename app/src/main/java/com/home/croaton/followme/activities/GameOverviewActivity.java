package com.home.croaton.followme.activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.home.croaton.followme.R;
import com.home.croaton.followme.database.DatabaseHelper;
import com.home.croaton.followme.domain.Game;
import com.home.croaton.followme.domain.GameFileManager;
import com.home.croaton.followme.instrumentation.ConnectionHelper;
import com.home.croaton.followme.security.PermissionAndConnectionChecker;

public class GameOverviewActivity extends AppCompatActivity  {

    private Game game;
    private String currentLanguage;
    private GameFileManager downloadManager;
    // private FloatingActionButton openButton;
    private TextView title;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_overview);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        currentLanguage = sharedPref.getString(getString(R.string.settings_language_preference), "en");

        game = new Game(this);

        PermissionAndConnectionChecker.checkForPermissions(GameOverviewActivity.this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionAndConnectionChecker.LocalStorageRequestCode);
        bindViews();
        animateTitle();
        setUpDB();
    }

    public void setUpDB() {
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

    }

    public void animateTitle() {
        Animation anim = new ScaleAnimation(
                1f, 1.5f, // Start and end values for the X axis scaling
                1f, 1.5f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        title.startAnimation(anim);
    }

    public void bindViews(){
        title = (TextView) findViewById(R.id.title_text_view);
    }

    public void onPostExecute(View view) {
        if (ConnectionHelper.hasInternetConnection(GameOverviewActivity.this)) {
            Intent intent = new Intent(GameOverviewActivity.this, MapsActivity.class);
            intent.putExtra(IntentNames.SELECTED_GAME, game);
            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (requestCode == PermissionAndConnectionChecker.LocalStorageRequestCode) {
            for (int i = 0; i < grantResults.length; i++)
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                    System.exit(0);
                }
        }
    }

}
