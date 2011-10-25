package com.xtremelabs.androidtohackui.draggables;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.xtremelabs.androidtohackui.R;
import com.xtremelabs.androidtohackui.draggables.fragments.SimpleDraggableFragment;
import com.xtremelabs.androidtohackui.draggables.ui.DraggableLayout;
import com.xtremelabs.androidtohackui.draggables.ui.IDraggableFragment;
import com.xtremelabs.androidtohackui.draggables.ui.IDraggableFragmentHandler;

public class DraggableExampleActivity extends Activity implements IDraggableFragmentHandler {
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.draggable_example_layout);
		
		//Add a new DraggableView to the layout
		int newId = ((DraggableLayout) findViewById(R.id.draggable_layout)).addNewDraggableView();
		
		//Create a fragment
		SimpleDraggableFragment fragment = new SimpleDraggableFragment();
		fragment.setFragmentHandler(this);
		
		addFragmentToViewById(newId, fragment);
	}
	
	private void addFragmentToViewById(int id, Fragment fragment) {
		//add the fragment to view with id "id"
		getFragmentManager().beginTransaction()
		.add(id, fragment)
		.addToBackStack("transaction_id")
		.commit();
	}
	
	//Handle's pushing new fragments
	@Override
	public void handleFragment(Fragment fragment) {
		if (fragment instanceof IDraggableFragment) {
			((IDraggableFragment) fragment).setFragmentHandler(this);
			int newId = ((DraggableLayout) findViewById(R.id.draggable_layout)).addNewDraggableView();
			addFragmentToViewById(newId, fragment);
		}
	}
	
	//Handle popping fragments
	@Override
	public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() <= 1) {
        	getFragmentManager().popBackStackImmediate();
        	super.onBackPressed();
        } else {
        	getFragmentManager().popBackStackImmediate();
        	DraggableLayout layout = (DraggableLayout) findViewById(R.id.draggable_layout);
        	if (findViewById(layout.getActiveDraggableView().getId()) != null) {
        		layout.removeView(findViewById(layout.getActiveDraggableView().getId()));
        	}
        }
	}
	
}
