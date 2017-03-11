package com.home.croaton.followme.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.home.croaton.followme.R;
import com.home.croaton.followme.domain.Excursion;
import com.home.croaton.followme.domain.ExcursionBrief;
import com.home.croaton.followme.download.ExcursionDownloadManager;
import com.home.croaton.followme.instrumentation.ConnectionHelper;
import com.home.croaton.followme.security.PermissionAndConnectionChecker;

public class ExcursionOverviewActivity extends AppCompatActivity  {

    private ExcursionBrief excursionBrief;
    private Excursion excursion;
    private String currentLanguage;
   // private FloatingActionButton openButton;
    private ExcursionDownloadManager downloadManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_overview);

        Intent intent = getIntent();
        excursionBrief = intent.getParcelableExtra(IntentNames.SELECTED_EXCURSION_BRIEF);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        currentLanguage = sharedPref.getString(getString(R.string.settings_language_preference), "ru");

        downloadManager = new ExcursionDownloadManager(this, excursionBrief, currentLanguage);
        excursion = new Excursion(excursionBrief, this);


        PermissionAndConnectionChecker.checkForPermissions(ExcursionOverviewActivity.this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionAndConnectionChecker.LocalStorageRequestCode);
    }
    public void onPostExecute(View view) {
        if (ConnectionHelper.hasInternetConnection(ExcursionOverviewActivity.this)) {
            Intent intent = new Intent(ExcursionOverviewActivity.this, MapsActivity.class);
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
