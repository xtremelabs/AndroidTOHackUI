package com.xtremelabs.androidtohackui;

import com.google.android.maps.MapActivity;
import android.os.Bundle;

public class LocationsActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.id.mapview);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

}
