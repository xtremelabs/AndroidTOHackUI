package com.xtremelabs.androidtohackui.bubbles.controllers;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.xtremelabs.androidtohackui.R;
import com.xtremelabs.androidtohackui.bubbles.fragments.BubbleListFragment;

public class ActionBarBubbleController extends AbstractBubbleController {

	public ActionBarBubbleController(Activity activity) {
		super(activity);
	}

	@Override
	public void onBubbleAttachedToWindow() {
		BubbleListFragment fragment = new BubbleListFragment();
		String[] items = {"Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8", "Item9", "Item10", "Item11", "Item12"};
		ArrayAdapter<String> dumpArrayAdapter = new ArrayAdapter<String>(mActivity, R.layout.simple_list_row, R.id.list_row_text, items);
		fragment.setListAdapter(dumpArrayAdapter);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String itemTitle = (String) parent.getItemAtPosition(position);
				BubbleListFragment fragment = new BubbleListFragment();
				String[] items = {"Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8", "Item9", "Item10", "Item11", "Item12"};
				ArrayAdapter<String> dumpArrayAdapter = new ArrayAdapter<String>(mActivity, R.layout.simple_list_row, R.id.list_row_text, items);
				fragment.setListAdapter(dumpArrayAdapter);
				fragment.setClickListener(this);
				fragment.getBubbleActionBarElements().setTitle(itemTitle);
				pushFragment(fragment);
			}
		};
		fragment.setClickListener(listener);
		fragment.getBubbleActionBarElements().setTitle("ITEMS");
		pushFragment(fragment);

	}
}
