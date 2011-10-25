package com.xtremelabs.androidtohackui.bubbles.fragments;

import com.xtremelabs.androidtohackui.bubbles.models.BubbleTitleBarElements;

public interface IBubbleFragment {

	BubbleTitleBarElements getBubbleActionBarElements();
	
	void setFragmentHandler(IBubbleFragmentHandler handler);
}
