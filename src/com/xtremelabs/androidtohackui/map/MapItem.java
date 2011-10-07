package com.xtremelabs.androidtohackui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.xtremelabs.androidtohackui.R;

public class MapItem extends FrameLayout {
    private GeoPoint mGeoPoint;
    private String mTitle;
    private String mDescription;

    public MapItem(Context context, GeoPoint geoPoint,
            String title, String description) {
        super(context);

        mGeoPoint = geoPoint;
        mTitle = title;
        mDescription = description;
        
        inflate();
    }

    private void inflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        
        View mainView = inflater.inflate(R.layout.map_item, null);
        addView(mainView);
        
        ((TextView) findViewById(R.id.title)).setText(mTitle);
        
        TextView description = (TextView) findViewById(R.id.description);
        description.setText(mDescription);
        description.setVisibility(View.GONE);
    }
    
    public void showDescription() {
        ((TextView) findViewById(R.id.description)).setVisibility(View.VISIBLE);
    }

    public void hideDescription() {
        ((TextView) findViewById(R.id.description)).setVisibility(View.GONE);
    }
    
    public boolean isDescriptionVisible() {
        return findViewById(R.id.description).getVisibility() == View.VISIBLE;
    }
    
    public GeoPoint getGeoPoint() {
        return mGeoPoint;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }
}
