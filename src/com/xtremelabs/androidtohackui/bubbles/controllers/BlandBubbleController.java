package com.xtremelabs.androidtohackui.bubbles.controllers;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class BlandBubbleController extends AbstractBubbleController {
	
	public BlandBubbleController(Activity activity) {
		super(activity);
	}

	@Override
	public void onBubbleAttachedToWindow() {
		Fragment fragment = new Fragment() {
			@Override
			public View onCreateView(LayoutInflater inflater,
					ViewGroup container, Bundle savedInstanceState) {
				
				EditText editText = new EditText(container.getContext());
				return editText;
				
			}
		};
		pushFragment(fragment);
	}

}
