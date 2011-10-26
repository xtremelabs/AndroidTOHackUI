package com.xtremelabs.androidtohackui.draggables.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class DraggableView extends FrameLayout {

    private boolean mIsTouchable = true;
    private float mXOffset; 
    private float mAnchorX;
    
    
	private GestureDetector mGestureDetector;
	private boolean mIsScrolling;
	
	public DraggableView(Context context) {
		super(context);
		init(context);
	}
	
	public DraggableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public DraggableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context) {
		mGestureDetector = new GestureDetector(context, new DraggableViewGestureListener());
	}
	
	public void setXOffset(float offset) {
		mXOffset = offset;
		setLeft((mXOffset>0) ? (int)mXOffset : 0);
		setRight(getLeft()+getMeasuredWidth());
	}
	
	public float getXOffset() {
		return mXOffset;
	}
	
	public void setAnchorX(float anchorX) {
		mAnchorX = anchorX;
	}
	
	public float getAnchorX() {
		return mAnchorX;
	}
	
	public void setIsTouchable(boolean touchable){
		mIsTouchable = touchable;
	}
	
	public void startTouchSample(MotionEvent event) {
		if (!mIsTouchable) return;
		Log.i("AndroidHack", "Starting sampling for view");
        mGestureDetector.onTouchEvent(event);
        mIsScrolling = false;
	}
	
	public boolean move(MotionEvent event) {
		Log.i("AndroidHack", "moving view");
		if (!mIsTouchable) return false;

        mGestureDetector.onTouchEvent(event);
        return mIsScrolling;
	}
	
	public void stopTouchSample(MotionEvent event) {
		Log.i("AndroidHack", "stopping sampling for view");
		event = MotionEvent.obtain(event);
        event.setAction(MotionEvent.ACTION_CANCEL);
        mGestureDetector.onTouchEvent(event);
		mIsScrolling = false;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (!mIsTouchable) return true;
		Log.i("AndroidHack", "view dispatched touch");
		return super.dispatchTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		Log.i("AndroidHack", "view got touch");
		return true;
	}
		
	
	class DraggableViewGestureListener extends SimpleOnGestureListener {
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        	Log.i("AndroidHack", "Scrolling for view");
        	mIsScrolling = false;
            // No gestures if touch isn't enabled on this pane
            if (!mIsTouchable) return false;
            mIsScrolling = true;
            return true;
        }
    }



}
