package com.xtremelabs.androidtohackui.bubbles.controllers;

import android.app.Activity;
import android.app.Fragment;

import com.xtremelabs.androidtohackui.bubbles.fragments.SignInFragment;

public class SignInBubbleController extends AbstractBubbleController {
	
	public SignInBubbleController(Activity activity) {
		super(activity);
	}

	@Override
	public void onBubbleAttachedToWindow() {
	    hideActionBar();

		Fragment signInFragment = new SignInFragment();
		pushFragment(signInFragment);
	}

}
