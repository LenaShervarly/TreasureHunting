package com.home.croaton.followme.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.home.croaton.followme.R;
import com.home.croaton.followme.domain.Game;
import com.home.croaton.followme.domain.GameFileManager;
import com.home.croaton.followme.instrumentation.ConnectionHelper;
import com.home.croaton.followme.security.PermissionAndConnectionChecker;

public class GameOverviewActivity extends AppCompatActivity  {

    private Game game;
    private String currentLanguage;
    private GameFileManager downloadManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_overview);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        currentLanguage = sharedPref.getString(getString(R.string.settings_language_preference), "en");
        game = new Game(this);
        PermissionAndConnectionChecker.checkForPermissions(GameOverviewActivity.this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionAndConnectionChecker.LocalStorageRequestCode);
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
