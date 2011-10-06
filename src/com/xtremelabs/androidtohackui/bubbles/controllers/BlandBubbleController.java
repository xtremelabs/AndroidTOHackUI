package com.xtremelabs.androidtohackui.bubbles.controllers;

import android.app.Activity;

public class BlandBubbleController extends AbstractBubbleController {
	
	public BlandBubbleController(Activity activity) {
		super(activity);
	}

	@Override
	public void onBubbleAttachedToWindow() {
		//do nothing....this is a bland pop-up
	}

}
