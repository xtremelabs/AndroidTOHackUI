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
	private DraggableLayout mLayout;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		setContentView(R.layout.draggable_example_layout);
		mLayout = (DraggableLayout) findViewById(R.id.draggable_layout);
		int newId = mLayout.addNewDraggableView();
		SimpleDraggableFragment fragment = new SimpleDraggableFragment();
		fragment.setFragmentHandler(this);
		addFragmentToViewById(newId, fragment);
	}
	
	private void addFragmentToViewById(int id, Fragment fragment) {
		getFragmentManager().beginTransaction()
		.add(id, fragment)
		.addToBackStack("transaction_id")
		.commit();
	}
	
	@Override
	public void handleFragment(Fragment fragment) {
		if (fragment instanceof IDraggableFragment) {
			((IDraggableFragment) fragment).setFragmentHandler(this);
			int newId = mLayout.addNewDraggableView();
			addFragmentToViewById(newId, fragment);
		}
	}
	
	@Override
	public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() <= 1) {
        	getFragmentManager().popBackStackImmediate();
        	super.onBackPressed();
        } else {
        	getFragmentManager().popBackStackImmediate();
        	if (findViewById(mLayout.getActiveDraggableView().getId()) != null) {
        		mLayout.removeView(findViewById(mLayout.getActiveDraggableView().getId()));
        	}
        }
	}
	
}
