package com.xtremelabs.androidtohackui.bubbles.models;

import android.widget.Button;

public class BubbleTitleBarElements {

	Button mLeftButton;
	String mTitle;
	Button mRightButton;
	
	public BubbleTitleBarElements() {
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	
	public void setLeftButton(Button button) {
		mLeftButton = button;
	}
	
	public Button getLeftButton() {
		return mLeftButton;
	}
	
	public void setRightButton(Button button) {
		mRightButton = button;
	}
	
	public Button getRightButton() {
		return mRightButton;
	}
}
