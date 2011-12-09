package com.xtremelabs.androidtohackui.draggables;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.xtremelabs.androidtohackui.R;
import com.xtremelabs.androidtohackui.bubbles.fragments.SignInFragment;
import com.xtremelabs.androidtohackui.draggables.fragments.EmptyDockableFragment;
import com.xtremelabs.androidtohackui.draggables.fragments.SimpleDraggableFragment;
import com.xtremelabs.androidtohackui.draggables.ui.DockableLayout;
import com.xtremelabs.androidtohackui.draggables.ui.IDraggableFragment;
import com.xtremelabs.androidtohackui.draggables.ui.IDraggableFragmentHandler;

public class DockingActivity extends Activity implements IDraggableFragmentHandler {

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);		
		setContentView(R.layout.dockable_example_layout);
		
		for (Fragment fragment: new Fragment[]{new EmptyDockableFragment()}){//,new SignInFragment()}) {
			handleFragment(fragment);
		}
	}

    private void addFragmentToViewById(int viewId, Fragment fragment) {
		//add the fragment to view with id "id"
		getFragmentManager().beginTransaction().add(viewId, fragment).addToBackStack("transaction_id").commit();
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
        	if (findViewById(getDraggableLayout().getActiveDraggableView().getId()) != null) {
        		getDraggableLayout().removeView(findViewById(getDraggableLayout().getActiveDraggableView().getId()));
        	}
        }
	}
	
	private DockableLayout getDraggableLayout() {
	    return (DockableLayout) findViewById(R.id.dockable_layout);
	}
}
