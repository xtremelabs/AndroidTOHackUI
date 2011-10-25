package com.xtremelabs.androidtohackui.bubbles.controllers;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtremelabs.androidtohackui.R;

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
				
		        return inflater.inflate(R.layout.sign_in_form, container, false);
			}
		};
		pushFragment(fragment);
	}

}
