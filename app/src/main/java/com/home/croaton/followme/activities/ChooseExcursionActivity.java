package com.home.croaton.followme.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.home.croaton.followme.R;
import com.home.croaton.followme.billing.CommonConstants;
import com.home.croaton.followme.billing.IabHelper;
import com.home.croaton.followme.billing.IabResult;
import com.home.croaton.followme.billing.Inventory;
import com.home.croaton.followme.billing.Purchase;
import com.home.croaton.followme.billing.SkuDetails;
import com.home.croaton.followme.domain.ExcursionBrief;
import com.home.croaton.followme.domain.ExcursionRepository;
import com.home.croaton.followme.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class ChooseExcursionActivity extends AppCompatActivity  implements IabHelper.OnIabSetupFinishedListener {
    private final ExcursionRepository excursionRepository = new ExcursionRepository(this);
    private List<ExcursionBrief> mExcursions;
    IabHelper mHelper;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_route);

        // ToDo: consider moving in app billing call to the repo
        mExcursions = excursionRepository.getGallery().getAvailableExcursions();
        mHelper = new IabHelper(this, CommonConstants.PK1 + CommonConstants.PK2 + CommonConstants.PK3 + CommonConstants.PK4);
        mHelper.startSetup(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.stockholm_excursions);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(R.id.excursion_gallery);
        ExcursionBriefAdapter adapter = new ExcursionBriefAdapter(this,
                R.layout.excursion_brief_item, mExcursions);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExcursionBrief excursion = (ExcursionBrief) parent.getItemAtPosition(position);

                Intent intent = new Intent(ChooseExcursionActivity.this, ExcursionOverviewActivity.class);
                intent.putExtra(IntentNames.SELECTED_EXCURSION_BRIEF, excursion);
                startActivity(intent);
            }
        });

        Menu menu = (Menu)findViewById(R.id.choose_excursion_menu_id);

        if ((getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) == 0)
            menu.clear();
    }

    @Override
    public void onIabSetupFinished(IabResult result) {
        if (!result.isSuccess()) {
            Log.d("InAppBilling", "Problem setting up In-app Billing: " + result);
            return;
        }

        List<String> productsCollection = new ArrayList<>();
        for(ExcursionBrief excursion : mExcursions)
            productsCollection.add(excursion.getKey());

        try {
            mHelper.queryInventoryAsync(true, productsCollection, new ArrayList<String>(),
                    new IabHelper.QueryInventoryFinishedListener() {
                        @Override
                        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                            if (result.isFailure()) {
                                Log.d("InAppBilling", "Problem getting products list : " + result);
                                return;
                            }

                            for(ExcursionBrief excursion : mExcursions) {
                                SkuDetails details = inventory.getSkuDetails(excursion.getKey());
                                Purchase purchase = inventory.getPurchase(excursion.getKey());
                                if (details == null) {
                                    //excursion.setCost(0.0);
                                    //excursion.setStringCost(getString(R.string.cost_free));
                                }
                                else {
                                    excursion.setPurchased(purchase != null);
                                    double price = MathHelper.round(details.getPriceAmountMicros() / 1000000, 2);
                                    //excursion.setCost(price);
                                    //excursion.setStringCost(details.getPrice());
                                }
                            }
                            ((ExcursionBriefAdapter)mListView.getAdapter()).notifyDataSetChanged();
                        }
                    });
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


}
