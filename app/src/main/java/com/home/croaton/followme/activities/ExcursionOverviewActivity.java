package com.home.croaton.followme.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.daimajia.slider.library.SliderLayout;
import com.home.croaton.followme.R;
import com.home.croaton.followme.billing.IabHelper;
import com.home.croaton.followme.domain.Excursion;
import com.home.croaton.followme.domain.ExcursionBrief;
import com.home.croaton.followme.download.ExcursionDownloadManager;
import com.home.croaton.followme.instrumentation.ConnectionHelper;
import com.home.croaton.followme.security.PermissionAndConnectionChecker;

public class ExcursionOverviewActivity extends AppCompatActivity {//implements IabHelper.OnIabSetupFinishedListener {

    private ExcursionBrief excursionBrief;
    private Excursion excursion;
    private String currentLanguage;
    private FloatingActionButton openButton;
    private Button loadButton;
    private SliderLayout slider;
    private boolean mInAppBillingSetupFinished = false;
    private boolean mPendingPurchase = false;

    private ExcursionDownloadManager downloadManager;
    private ProgressDialog progressDialog;
    //private volatile DownloadExcursionTask downloadTask;

    private IabHelper mIabHelper;

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
/*
        mIabHelper = new IabHelper(this, CommonConstants.PK1 + CommonConstants.PK2 + CommonConstants.PK3 + CommonConstants.PK4);
        mIabHelper.startSetup(this);
        */
        //initUI();
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

    /*@Override
    public void onIabSetupFinished(IabResult result) {
        if (!result.isSuccess()) {
            Log.d("InAppBilling", "Problem setting up In-app Billing: " + result);
            return;
        }

        mInAppBillingSetupFinished = true;
        if (mPendingPurchase) {
            purchaseExcursion();
            mPendingPurchase = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.excursion_overview, menu);
        setExcursionDeletionVisibility(menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        setExcursionDeletionVisibility(menu);
        return true;
    }

    private void setExcursionDeletionVisibility(Menu menu) {
        menu.findItem(R.id.trash_bin).setVisible(loadButton.getVisibility() == View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.trash_bin:
                deleteCurrentExcursion();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
 */


   /* private void initUI() {
        initSlider();

        openButton = (FloatingActionButton) findViewById(R.id.open_button);
        openButton.setVisibility(excursionIsLoaded ? View.VISIBLE : View.GONE);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (excursion == null)
                    throw new IllegalStateException("Should not show open button with null excursion");

                Intent intent = new Intent(ExcursionOverviewActivity.this, MapsActivity.class);
                intent.putExtra(IntentNames.SELECTED_EXCURSION, excursion);
                startActivity(intent);

        //    }
        } //);*/

        /*downloadTask = new DownloadExcursionTask(ExcursionOverviewActivity.this);

        loadButton = (Button) findViewById(R.id.load_button);
        loadButton.setVisibility(excursionIsLoaded ? View.GONE : View.VISIBLE);

        if (excursionBrief.canBePurchased() && !excursionBrief.getPurchased())
            loadButton.setText(R.string.buy);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionAndConnectionChecker.checkForPermissions(ExcursionOverviewActivity.this, new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionAndConnectionChecker.LocalStorageRequestCode);

                if (excursionBrief.canBePurchased() && !excursionBrief.getPurchased())
                    buyExcursion();
                else
                    downloadTask.execute();
            }
        });

        progressDialog = new ProgressDialog(ExcursionOverviewActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.downloading_excursion));
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
                downloadTask = new DownloadExcursionTask(ExcursionOverviewActivity.this);
            }
        });

        TextView aboutExcursion = (TextView)findViewById(R.id.excursion_overview);
        aboutExcursion.setText(excursionBrief.getContentByLanguage(currentLanguage).getOverview());

        TextView excursionDuration = (TextView)findViewById(R.id.excursion_duration);
        excursionDuration.setText(getStringForDuration(excursionBrief.getDuration()));

        TextView excursionLength = (TextView)findViewById(R.id.excursion_length);
        excursionLength.setText(Double.toString(excursionBrief.getLength()) + getString(R.string.kilometers));

        TextView excursionCost = (TextView) findViewById(R.id.excursion_cost);
        excursionCost.setText(excursionBrief.getStringCost());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(excursionBrief.getContentByLanguage(currentLanguage).getName());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }  */




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



    /*private class DownloadExcursionTask { //extends AsyncTask<Void, Integer, Excursion> {
        private final Context context;

        public DownloadExcursionTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }*/

        /*@Override
        protected Excursion doInBackground(Void... inputs) {
            IExcursionDownloader downloader = new S3ExcursionDownloader(context,
                    downloadManager.getExcursionLocalDir(), downloadManager.getAudioLocalDir(context, excursionBrief.getKey(), currentLanguage));

            downloader.getProgressObservable().subscribe(new IObserver<Integer>() {
                @Override
                public void notify(Integer progress) {
                    publishProgress(progress);
                }
            });
            return downloader.downloadExcursion(excursionBrief, currentLanguage);
            return null;
        } */

        /*@Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(progress[0]);
        }*/

        /*@Override
        public void onPostExecute() {
            /*progressDialog.dismiss();

            if (excursion != null && excursion.isLoaded(context, currentLanguage)) {

                loadButton.setVisibility(View.GONE);
                openButton.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
                ExcursionOverviewActivity.this.excursion = excursion;

                if (ConnectionHelper.hasInternetConnection(ExcursionOverviewActivity.this)) {
                    MapView mapView = new MapView(ExcursionOverviewActivity.this);
                    MapHelper.chooseBeautifulMapProvider(ExcursionOverviewActivity.this, mapView);
                    mapView.setMinZoomLevel(1);
                    mapView.setMaxZoomLevel(18);
                }   /*CacheManager cacheManager = new CacheManager(mapView);

                    cacheManager.downloadAreaAsync(ExcursionOverviewActivity.this,
                            new BoundingBoxE6(excursionBrief.getArea().get(0).getLatitude(),
                                    excursionBrief.getArea().get(1).getLongitude(),
                                    excursionBrief.getArea().get(1).getLatitude(),
                                    excursionBrief.getArea().get(0).getLongitude()), 6, mapView.getMaxZoomLevel());

        }


    }*/
}
