package com.xtremelabs.androidtohackui.map;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.xtremelabs.androidtohackui.R;

public class LocationsActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.map);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

}
