package com.xtremelabs.androidtohackui.bubbles.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.xtremelabs.androidtohackui.R;
import com.xtremelabs.androidtohackui.bubbles.models.BubbleTitleBarElements;

public class BubbleListFragment extends ListFragment implements IBubbleFragment {

	private BubbleTitleBarElements mActionBarElements; 
	private IBubbleFragmentHandler mHandler;
	private OnItemClickListener mListener;
	
	public BubbleListFragment() {
		mActionBarElements = new BubbleTitleBarElements();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] items = {"Email1", "Email2", "Email3", "Email4", "Email5", "Email6", "Email7", "Email8", "Email9", "Email10", "Email11", "Email12"};
		ArrayAdapter<String> dumpArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_row, R.id.list_row_text, items);
		setListAdapter(dumpArrayAdapter);
		mListener = new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String itemTitle = (String) parent.getItemAtPosition(position);
				BubbleListFragment fragment = new BubbleListFragment();
				fragment.setTitle(itemTitle);
				if (mHandler != null) {
					mHandler.handleFragment(fragment);
				}else{
					//manually add fragment as you would in the general case.
				}
			}
		};
	}
	
	
	public void setTitle(String title) {
		mActionBarElements.setTitle(title);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		getListView().setCacheColorHint(0x00000000);
		getListView().setOnItemClickListener(mListener);
	}

	@Override
	public BubbleTitleBarElements getBubbleActionBarElements() {
		return mActionBarElements;
	}

	@Override
	public void setFragmentHandler(IBubbleFragmentHandler handler) {
		mHandler = handler;
	}

}
