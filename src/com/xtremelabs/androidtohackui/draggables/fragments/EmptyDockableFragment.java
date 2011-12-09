package com.xtremelabs.androidtohackui.draggables.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.xtremelabs.androidtohackui.R;
import com.xtremelabs.androidtohackui.draggables.ui.IDraggableFragment;
import com.xtremelabs.androidtohackui.draggables.ui.IDraggableFragmentHandler;

public class EmptyDockableFragment extends ListFragment implements IDraggableFragment {
	private IDraggableFragmentHandler mHandler;	
	@Override
	public void setFragmentHandler(IDraggableFragmentHandler handler) {
		mHandler = handler;
	}
}
