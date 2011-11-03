package com.xtremelabs.androidtohackui.bubbles.controllers;

import android.app.Activity;
import android.app.Fragment;

import com.xtremelabs.androidtohackui.bubbles.fragments.NotificationsFragment;
import com.xtremelabs.androidtohackui.bubbles.fragments.IBubbleFragment;
import com.xtremelabs.androidtohackui.bubbles.fragments.IBubbleFragmentHandler;

public class NotificationsBubbleController extends AbstractBubbleController implements IBubbleFragmentHandler {

	public NotificationsBubbleController(Activity activity) {
		super(activity);
	}

	/**
	 * Overridden methods to provide custom user flows
	 * 
	 */
	
	@Override
	public void onBubbleAttachedToWindow() {
		NotificationsFragment fragment = new NotificationsFragment();
		fragment.setFragmentHandler(this);
		fragment.setTitle("Notifications");
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
