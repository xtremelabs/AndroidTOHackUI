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
		
		// Add a new DraggableView to the layout
		int newViewId = getDraggableLayout().addNewDraggableView();
		
		// Create a fragment
		SimpleDraggableFragment fragment = new SimpleDraggableFragment();
		
		//Setup a simple callback (just to demonstrate multiple fragments)
		fragment.setFragmentHandler(this);
		
		addFragmentToViewById(newViewId, fragment);
	}
	

    private void addFragmentToViewById(int viewId, Fragment fragment) {
		//add the fragment to view with id "id"
		getFragmentManager().beginTransaction()
    		.add(viewId, fragment)
    		.addToBackStack("transaction_id")
    		.commit();
	}
	
	//Handle's pushing new fragments
	@Override
	public void handleFragment(Fragment fragment) {
		if (fragment instanceof IDraggableFragment) {
			((IDraggableFragment) fragment).setFragmentHandler(this);
			int newId = getDraggableLayout().addNewDraggableView();
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
        	DraggableLayout layout = getDraggableLayout();
        	if (findViewById(layout.getActiveDraggableView().getId()) != null) {
        		layout.removeView(findViewById(layout.getActiveDraggableView().getId()));
        	}
        }
	}
	
	private DraggableLayout getDraggableLayout() {
	    return (DraggableLayout) findViewById(R.id.draggable_layout);
	}

}
