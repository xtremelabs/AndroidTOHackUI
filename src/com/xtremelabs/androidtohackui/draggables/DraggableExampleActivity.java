package com.xtremelabs.androidtohackui.draggables;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout.LayoutParams;

import com.xtremelabs.androidtohackui.R;
import com.xtremelabs.androidtohackui.draggables.ui.DraggableLayout;
import com.xtremelabs.androidtohackui.draggables.ui.DraggableView;

public class DraggableExampleActivity extends Activity {

	private int mDraggableViewId = 8;
	private int mSecondDraggableViewId = 9;
	private ListFragment mListFragment;
	private DraggableLayout mLayout;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		setContentView(R.layout.draggable_example_layout);
		DraggableView draggableView = new DraggableView(this);
		draggableView.setBackgroundResource(android.R.color.white);
		draggableView.setId(mDraggableViewId);
		
		
		mLayout = (DraggableLayout) findViewById(R.id.draggable_layout);
		mLayout.addView(draggableView, new LayoutParams(500, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		
		mListFragment = new ListFragment();
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
		ArrayAdapter<String> dumpArrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_row, R.id.list_row_text, items);
		mListFragment.setListAdapter(dumpArrayAdapter);
		getFragmentManager().beginTransaction().add(mDraggableViewId, mListFragment).commit();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mListFragment.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				DraggableView draggableView = new DraggableView(DraggableExampleActivity.this);
				draggableView.setBackgroundResource(android.R.color.white);
				draggableView.setId(mSecondDraggableViewId);
				mLayout.addView(draggableView, new LayoutParams(500, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
				
				ListFragment listFragment = new ListFragment();
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
				ArrayAdapter<String> dumpArrayAdapter = new ArrayAdapter<String>(DraggableExampleActivity.this, R.layout.simple_list_row, R.id.list_row_text, items);
				listFragment.setListAdapter(dumpArrayAdapter);
				getFragmentManager().beginTransaction().add(mSecondDraggableViewId, listFragment).commit();
			}
		});
	}

}
