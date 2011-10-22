package com.xtremelabs.androidtohackui.draggables.ui;

import java.util.Random;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class DraggableLayout extends ViewGroup {
	
	
	//Help determine where to send touch event
    private final static int TOUCH_PRIORITY_UNKNOWN = 0;
    private final static int TOUCH_PRIORITY_VIEW = 1;
    private final static int TOUCH_PRIORITY_DRAGGING = 2;

    public final static int SCROLL_STATE_UNKNOWN = 0;
    public final static int SCROLL_STATE_VERTICAL = 1;
    public final static int SCROLL_STATE_HORIZONTAL = 2;
	
    private int mTouchPriority;
    private int mScrollState;
    private GestureDetector mGestureDetector;
    private DraggableView mDraggableView;
    
	
	public DraggableLayout(Context context) {
		super(context);
		init(context);
	}

	
	public DraggableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	
	public DraggableLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context) {
		mGestureDetector = new GestureDetector(context, new DraggingGestureListener());
	}
	
	@Override
	public void addView(View child) {
		checkChild(child);
		super.addView(child);
	}
	
	@Override
	public void addView(View child, int index, LayoutParams params) {
		checkChild(child);
		super.addView(child, index, params);
	}
	
	@Override
	public void addView(View child, LayoutParams params) {
		checkChild(child);
		super.addView(child, params);
	}
	
	@Override
	public void addView(View child, int index) {
		checkChild(child);
		super.addView(child, index);
	}
	
	@Override
	public void addView(View child, int width, int height) {
		checkChild(child);
		super.addView(child, width, height);
	}
	
	private void checkChild(View child) {
		if (!(child instanceof DraggableView)) {
			throw new RuntimeException("Cannot add a view to DraggableLayout unless it is a DraggableView");
		}
		if (getChildCount() > 0) {
			DraggableView previousViewWithFocus = getActiveDraggableView();
			Log.i("AndroidHack", "setting first view to not be touchable");
			previousViewWithFocus.setIsTouchable(false);
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		for (int i=0; i<getChildCount(); i++) {
			View child = getChildAt(i);
			
			int childLeft = child.getLeft();
			int childTop = child.getTop();
			int childRight = child.getMeasuredWidth() + left;
			int childBottom = child.getMeasuredHeight() + top;
			child.layout(childLeft, childTop, childRight, childBottom);
			
		}
	}
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean firstPass = getMeasuredWidth() == 0;

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));

        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

        int buffer = 45;
        size -= buffer;

        int customWidthSpec = MeasureSpec.makeMeasureSpec(size, mode);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (i == 0) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            } else {
                measureChild(child, customWidthSpec, heightMeasureSpec);
            }

            if (firstPass && i > 0) {
                child.setLeft(getMeasuredWidth() - child.getMeasuredWidth());
            }
        }

    }
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//reset the subviews to be at 0 on rotation
		for (int i=0; i<getChildCount(); i++) {
			DraggableView child = (DraggableView)getChildAt(i);
			child.setXOffset(0);
		}
	}
	
	private void startTouchSample(MotionEvent event) {
	     Log.i("AndroidHack", "Starting layout sampling");
		 mGestureDetector.onTouchEvent(event);
	}
	
	private boolean move(MotionEvent event) {
	     Log.i("AndroidHack", "Moving layout");

		boolean handled = mGestureDetector.onTouchEvent(event);
		
		int initialScrollState = mScrollState;
		
		if (initialScrollState != SCROLL_STATE_UNKNOWN) {//scrolling
			int action = event.getAction();
			int actionCode = action & MotionEvent.ACTION_MASK;
			
			if (actionCode == MotionEvent.ACTION_CANCEL || actionCode == MotionEvent.ACTION_UP) {
				mScrollState = SCROLL_STATE_UNKNOWN;
			}
		}
		if (initialScrollState == SCROLL_STATE_HORIZONTAL) { 
			return true;
		} else {
            return handled;
		}
	}
	
	private void stopTouchSample(MotionEvent event) {
		Log.i("AndroidHack", "Stopping layout sampling");
		MotionEvent cancel = MotionEvent.obtain(event);
        cancel.setAction(MotionEvent.ACTION_CANCEL);
        mGestureDetector.onTouchEvent(cancel);
	}
	
	
	/*
	 * Very important to override this.  This is where we analayze the touch
	 * events and determine how to handle them.
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		//Grab the touch event type
		int action = event.getAction();
	    int actionCode = action & MotionEvent.ACTION_MASK;
	    Log.i("AndroidHack", "Got Touch Event");
	    switch (actionCode) {
	    	case MotionEvent.ACTION_MOVE:
	    		Log.i("AndroidHack", "Got move");
	    		if (mDraggableView == null && mTouchPriority == TOUCH_PRIORITY_UNKNOWN) { 
	    			mTouchPriority = TOUCH_PRIORITY_DRAGGING;
	    		}
	    		
	    		if (mTouchPriority == TOUCH_PRIORITY_UNKNOWN) {
	    			boolean handled = mDraggableView.move(event);
	    			
	    			if(handled){
	    				stopTouchSample(event);
	    				mTouchPriority = TOUCH_PRIORITY_VIEW;
	    			} else {
	    				mDraggableView.stopTouchSample(event);
	    				mTouchPriority = TOUCH_PRIORITY_DRAGGING;
	    			}
	    		} else {
	    			if (mTouchPriority == TOUCH_PRIORITY_VIEW) {
	    				mDraggableView.move(event);
	    			} else if (mTouchPriority == TOUCH_PRIORITY_DRAGGING) {
	    				if (move(event)) {
	                        event = MotionEvent.obtain(event);
	                        event.setAction(MotionEvent.ACTION_CANCEL);
	                    }
	    			}
	    		}
	    		
	    		break;
	    	case MotionEvent.ACTION_DOWN:
	    		Log.i("AndroidHack", "Got down");
	    		mDraggableView = getActiveDraggableView();
	    		mTouchPriority = TOUCH_PRIORITY_UNKNOWN;
	    		
	    		if (mDraggableView != null) {
	    			mDraggableView.startTouchSample(event);
	    		} else {
	    			mTouchPriority = TOUCH_PRIORITY_DRAGGING;
	    		}
	    		
	    		startTouchSample(event);	    		
	    		break;
	    	default:
	    		Log.i("AndroidHack", "Got other");
	    		if (mDraggableView != null) {
	    			mDraggableView.stopTouchSample(event);
	    		}
	    		move(event);
	    		mDraggableView = null;
	    		mTouchPriority = TOUCH_PRIORITY_UNKNOWN;
	    		break;
	    }
	    super.dispatchTouchEvent(event);
	    return true;
	}
	
	public DraggableView getActiveDraggableView() {
		return (DraggableView) getChildAt(getChildCount() - 1);
	}
	
	/**
	 * 
	 * @return the id of the new view that was created
	 */
	public int addNewViewForFragment() {
		DraggableView draggableView = new DraggableView(getContext());
		draggableView.setBackgroundResource(android.R.color.white);
		int newId = generateId();
		draggableView.setId(newId);
		addView(draggableView, new LayoutParams(500, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		return newId;
	}
	
	private int generateId() {
		int num;
		Random rand = new Random(System.currentTimeMillis());
        num = rand.nextInt(Integer.MAX_VALUE);
		return num;
	}
	
	
	class DraggingGestureListener extends SimpleOnGestureListener {
        private float mInitialRawX;
        private float mMostRecentRawX;

        private float mMostRecentRawY;


        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mInitialRawX != e1.getRawX()) {
                mInitialRawX = e1.getRawX();
                mMostRecentRawX = e1.getRawX();
                mMostRecentRawY = e1.getRawY();

                int xDiff = Math.round(e2.getRawX() - mMostRecentRawX);
                int yDiff = Math.round(e2.getRawY() - mMostRecentRawY);

                if (Math.abs(yDiff) > Math.abs(xDiff)) {
                	mScrollState = SCROLL_STATE_VERTICAL;
                } else {
                	mScrollState = SCROLL_STATE_HORIZONTAL;
                }
            }

            int xDiff = Math.round(e2.getRawX() - mMostRecentRawX);

            mMostRecentRawX = e2.getRawX();
            mMostRecentRawY = e2.getRawY();

            if (mDraggableView != null && (mScrollState == SCROLL_STATE_HORIZONTAL)) {
            	mDraggableView.setXOffset(mDraggableView.getXOffset() + xDiff);
            	invalidate();
                return true;
            }
//            } else if (mLayoutListener != null && isScrolling()) {
//                areTouchesHandled = false;
//                mLayoutListener.scrollUnhandled(mScrollStatus, xDiff, yDiff);
//            }

            return false;
        }
    }

}
