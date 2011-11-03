package com.xtremelabs.androidtohackui;

import com.xtremelabs.androidtohackui.bubbles.BubbleExampleActivity;
import com.xtremelabs.androidtohackui.draggables.DraggableExampleActivity;
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
    	"Pop-up Bubbles",
    	"Map Overlays",
    	"Draggable Views",
    	"Compatible Pop-up Bubbles"
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setupList();
    }

    private void setupList() {
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
                this, R.layout.launch_item_row, R.id.title, mListItems);
        setListAdapter(listAdapter);
        
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                select(position);
            }
        });
    }

    protected void select(int position) {
        switch (position) {
        case 0:
        	launchBubbles();
        	break;
        case 1:
            launchLocations();
            break;
        case 2:
        	launchDraggables();
            break;
        case 3:
        	launchCompatibleBubbles();
        	break;
        }
    }

	private void launchLocations() {
        Intent intent = new Intent(this, LocationsActivity.class);
        startActivity(intent);
    }
    
    private void launchBubbles() {
    	Intent intent = new Intent(this, BubbleExampleActivity.class);
        startActivity(intent);
    }
    
    private void launchDraggables() {
    	Intent intent = new Intent(this, DraggableExampleActivity.class);
    	startActivity(intent);
    }
    
    private void launchCompatibleBubbles() {
    	Intent intent = new Intent(this, com.xtremelabs.androidtohackui.bubbles.compatible.BubbleExampleActivity.class);
    	startActivity(intent);
	}

}
