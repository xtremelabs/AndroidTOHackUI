package com.xtremelabs.androidtohackui.bubbles.controllers;

import android.app.Activity;
import android.app.Fragment;

import com.xtremelabs.androidtohackui.bubbles.fragments.BubbleListFragment;
import com.xtremelabs.androidtohackui.bubbles.fragments.IBubbleFragment;
import com.xtremelabs.androidtohackui.bubbles.fragments.IBubbleFragmentHandler;

public class ActionBarBubbleController extends AbstractBubbleController implements IBubbleFragmentHandler {

	public ActionBarBubbleController(Activity activity) {
		super(activity);
	}

	/**
	 * Overridden methods to provide custom user flows
	 * 
	 */
	
	@Override
	public void onBubbleAttachedToWindow() {
		BubbleListFragment fragment = new BubbleListFragment();
		fragment.setFragmentHandler(this);
		fragment.setTitle("EMAILS");
		pushFragment(fragment);
	}

	@Override
	public void handleFragment(Fragment fragment) {
		if (fragment instanceof IBubbleFragment) {
			((IBubbleFragment)fragment).setFragmentHandler(this);
		}
		pushFragment(fragment);
	}
}
