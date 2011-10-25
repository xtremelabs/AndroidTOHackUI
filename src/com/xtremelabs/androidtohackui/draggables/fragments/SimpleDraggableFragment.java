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

public class SimpleDraggableFragment extends ListFragment implements IDraggableFragment {

	private OnItemClickListener mListener;
	private IDraggableFragmentHandler mHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] items = 
			{"Item1", 
			"Item2",
			"Item3",
			"Item4",
			"Item5",
			"Item6",
			"Item7",
			"Item8",
			"Item9",
			"Item10",
			"Item11",
			"Item12",
			"Item13",
			"Item14",
			"Item15",
			"Item16",
			"Item17",
			"Item18",
			"Item19"};
		ArrayAdapter<String> dumpArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_row, R.id.list_row_text, items);
		setListAdapter(dumpArrayAdapter);
		mListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				SimpleDraggableFragment listFragment = new SimpleDraggableFragment();
				String[] items = 
						{"Item1", 
						"Item2",
						"Item3",
						"Item4",
						"Item5",
						"Item6",
						"Item7",
						"Item8",
						"Item9",
						"Item10",
						"Item11",
						"Item12",
						"Item13",
						"Item14",
						"Item15",
						"Item16",
						"Item17",
						"Item18",
						"Item19"};
				ArrayAdapter<String> dumpArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_row, R.id.list_row_text, items);
				listFragment.setListAdapter(dumpArrayAdapter);
				if (mHandler != null) {
					mHandler.handleFragment(listFragment);
				} else{
					//do what you would normally do to push a new fragment
				}
			}
		};
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		getListView().setOnItemClickListener(mListener);
	}


	@Override
	public void setFragmentHandler(IDraggableFragmentHandler handler) {
		mHandler = handler;
	}
}
