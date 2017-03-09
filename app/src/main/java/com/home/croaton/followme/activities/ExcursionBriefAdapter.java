package com.home.croaton.followme.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.croaton.followme.R;
import com.home.croaton.followme.domain.ExcursionBrief;

import java.util.List;

public class ExcursionBriefAdapter extends ArrayAdapter<ExcursionBrief> {
    private final Context context;
    private final List<ExcursionBrief> items;

    public ExcursionBriefAdapter(Context context, int textViewResourceId, List<ExcursionBrief> items) {
        super(context, textViewResourceId, items);

        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.excursion_brief_item, null);
        }
        ExcursionBrief excursion = items.get(position);
        if (excursion != null) {
            ImageView thumbnailView = (ImageView) convertView.findViewById(R.id.thumbnail);
            ImageView purchasedView = (ImageView) convertView.findViewById(R.id.excursion_purchased);
            TextView nameText = (TextView) convertView.findViewById(R.id.name);
            TextView costText = (TextView) convertView.findViewById(R.id.cost);

            if (thumbnailView != null) {
                int imageId = getImageId(excursion.getThumbnailFilePath());
                thumbnailView.setImageResource(imageId);
            }
            if (nameText != null) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                String currentLanguage = sharedPref.getString(context.getString(R.string.settings_language_preference), "ru");
                nameText.setText(excursion.getContentByLanguage(currentLanguage).getName());
            }
            //if (costText != null)
            //    costText.setText(excursion.getStringCost());
            purchasedView.setVisibility(excursion.getPurchased() ? View.VISIBLE : View.INVISIBLE);

        }
        return convertView;
    }

    @Override
    public ExcursionBrief getItem(int position) {
        return items.get(position);
    }

    private int getImageId(String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }
}
