package com.xtremelabs.androidtohackui;

import android.app.ListActivity;
import android.os.Bundle;
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
    }
}