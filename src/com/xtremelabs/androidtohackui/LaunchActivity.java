package com.xtremelabs.androidtohackui;

import com.xtremelabs.androidtohackui.map.LocationsActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;


public class LaunchActivity extends ListActivity {
    private static final String[] mListItems = new String[] {
        "Map Overlays"
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setupList();
    }

    private void setupList() {
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, mListItems);
        setListAdapter(listAdapter);
        
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                select(position);
            }
        });
    }

    protected void select(int position) {
        switch (position) {
        case 0:
            launchLocations();
            break;
        }
    }

    private void launchLocations() {
        Intent intent = new Intent(this, LocationsActivity.class);
        startActivity(intent);
    }
    
}
