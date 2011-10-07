package com.xtremelabs.androidtohackui.bubbles.fragments;

import com.xtremelabs.androidtohackui.bubbles.models.BubbleActionBarElements;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;

public class BubbleListFragment extends ListFragment implements IBubbleFragment {

	private OnItemClickListener mListener;
	BubbleActionBarElements mActionBarElements; 
	
	public BubbleListFragment() {
		mActionBarElements = new BubbleActionBarElements();
	}
	
	
	public void setTitle(String title) {
		mActionBarElements.setTitle(title);
	}
	
	public void setClickListener(OnItemClickListener listener){
		// check if we should set the listener immediately
//		if (getListView() != null && getListView().is()) { 
//			getListView().setOnItemClickListener(listener);
//		}
		mListener = listener;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getListView().setCacheColorHint(0x00000000);
		if (mListener != null) {
			getListView().setOnItemClickListener(mListener);
		}
	}

	@Override
	public BubbleActionBarElements getBubbleActionBarElements() {
		return mActionBarElements;
	}

}
