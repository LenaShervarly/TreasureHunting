package com.home.croaton.followme.activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.home.croaton.followme.R;
import com.home.croaton.followme.domain.Game;
import com.home.croaton.followme.domain.GameFileManager;
import com.home.croaton.followme.instrumentation.ConnectionHelper;
import com.home.croaton.followme.security.PermissionAndConnectionChecker;

public class GameOverviewActivity extends AppCompatActivity  {

    private Game excursion;
    private String currentLanguage;
    // private FloatingActionButton openButton;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_overview);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        currentLanguage = sharedPref.getString(getString(R.string.settings_language_preference), "en");

        excursion = new Game(this);

        PermissionAndConnectionChecker.checkForPermissions(GameOverviewActivity.this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionAndConnectionChecker.LocalStorageRequestCode);
        bindViews();
        animateTitle();
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
            intent.putExtra(IntentNames.SELECTED_EXCURSION, excursion);
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


        /*if (ConnectionHelper.hasInternetConnection(ExcursionOverviewActivity.this)) {
            MapView mapView = new MapView(ExcursionOverviewActivity.this);
            MapHelper.chooseBeautifulMapProvider(ExcursionOverviewActivity.this, mapView);
            mapView.setMinZoomLevel(1);
            mapView.setMaxZoomLevel(18);
        }   CacheManager cacheManager = new CacheManager(mapView);

            cacheManager.downloadAreaAsync(ExcursionOverviewActivity.this,
                    new BoundingBoxE6(excursionBrief.getArea().get(0).getLatitude(),
                            excursionBrief.getArea().get(1).getLongitude(),
                            excursionBrief.getArea().get(1).getLatitude(),
                            excursionBrief.getArea().get(0).getLongitude()), 6, mapView.getMaxZoomLevel());

        }*/
}
