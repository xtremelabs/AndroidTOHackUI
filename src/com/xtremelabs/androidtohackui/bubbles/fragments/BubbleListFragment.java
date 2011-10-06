package com.xtremelabs.androidtohackui.bubbles.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;

public class BubbleListFragment extends ListFragment implements IBubbleFragment {

	private String mTitle = "";
	private OnItemClickListener mListener;
	
	@Override
	public String getTitle() {
		return mTitle;
	}
	
	public void setTitle(String title) {
		mTitle = title;
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

}
