package com.xtremelabs.androidtohackui.bubbles.fragments;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.xtremelabs.androidtohackui.R;
import com.xtremelabs.androidtohackui.bubbles.models.BubbleTitleBarElements;

public class NotificationsFragment extends ListFragment implements IBubbleFragment {

	private BubbleTitleBarElements mActionBarElements; 
	private IBubbleFragmentHandler mHandler;
	private OnItemClickListener mListener;
    private static final String[] NOTIFICATION_TITLES = {
            "Notification 1", "Notification 2", "Notification 3", "Notification 4",
            "Notification 5", "Notification 6", "Notification 7", "Notification 8",
            "Notification 9", "Notification 10", "Notification 11", "Notification 12" };

	
	public NotificationsFragment() {
		mActionBarElements = new BubbleTitleBarElements();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayAdapter<String> dumpArrayAdapter = new ArrayAdapter<String>(
		        getActivity(), R.layout.simple_list_row, R.id.list_row_text,
		        NOTIFICATION_TITLES);
		setListAdapter(dumpArrayAdapter);
		mListener = new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String notificationTitle = (String) parent.getItemAtPosition(position);
				Fragment fragment = new NotificationDetailsFragment(notificationTitle);
				if (mHandler != null) {
					mHandler.handleFragment(fragment);
				} else {
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
