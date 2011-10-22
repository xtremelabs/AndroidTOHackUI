package com.xtremelabs.androidtohackui.bubbles.compatible.controllers;

import android.support.v4.app.FragmentActivity;

public class BlandBubbleController extends AbstractBubbleController {
	
	public BlandBubbleController(FragmentActivity activity) {
		super(activity);
	}

	@Override
	public void onBubbleAttachedToWindow() {
		//do nothing....this is a bland pop-up
	}

}
