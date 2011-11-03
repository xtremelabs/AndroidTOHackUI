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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

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
    
    //Very important variable: keeps a reference to the active DraggableView
    private DraggableView mActiveDraggableView;
    private Random mRandomGenrator = new Random(System.currentTimeMillis());
	
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
		disableActiveChild();
		super.addView(child);
		animateViewIn();
	}
	
	@Override
	public void addView(View child, int index, LayoutParams params) {
		checkChild(child);
		disableActiveChild();
		super.addView(child, index, params);
		animateViewIn();
	}
	
	@Override
	public void addView(View child, LayoutParams params) {
		checkChild(child);
		disableActiveChild();
		super.addView(child, params);
		animateViewIn();
	}
	
	@Override
	public void addView(View child, int index) {
		checkChild(child);
		disableActiveChild();
		super.addView(child, index);
		animateViewIn();
	}
	
	@Override
	public void addView(View child, int width, int height) {
		checkChild(child);
		disableActiveChild();
		super.addView(child, width, height);
		animateViewIn();
	}
	
	private void animateViewIn() {
		DraggableView activeView = getActiveDraggableView();
		activeView.setXOffset(getRight());
		animateActiveViewToAnchor();
	}
	
	@Override
	public void removeView(View view) {
		super.removeView(view);
		enableActiveChild();
	}
	
	private void checkChild(View child) {
		if (!(child instanceof DraggableView)) {
			throw new RuntimeException("Cannot add a view to DraggableLayout unless it is a DraggableView");
		}
	}
	
	private void enableActiveChild() {
		if (getChildCount() > 0) {
			DraggableView previousViewWithFocus = getActiveDraggableView();
			Log.i("AndroidHack", "setting active view to  be touchable");
			previousViewWithFocus.setIsTouchable(true);
			AlphaAnimation anim = new AlphaAnimation(0.5f, previousViewWithFocus.getAlpha());
			anim.setFillAfter(true);
			anim.setDuration(1000);
			previousViewWithFocus.startAnimation(anim);
		}
	}
	
	private void disableActiveChild() {
		if (getChildCount() > 0) {
			DraggableView previousViewWithFocus = getActiveDraggableView();
			Log.i("AndroidHack", "setting active view to not be touchable");
			previousViewWithFocus.setIsTouchable(false);
			AlphaAnimation anim = new AlphaAnimation(previousViewWithFocus.getAlpha(), 0.5f);
			anim.setFillAfter(true);
			anim.setDuration(1000);
			previousViewWithFocus.startAnimation(anim);
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		for (int i=0; i<getChildCount(); i++) {
			View child = getChildAt(i);
			int childLeft = child.getLeft();
			int childTop = child.getTop();
			int childRight = child.getMeasuredWidth() + childLeft;
			int childBottom = child.getMeasuredHeight() + childTop;
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
		for (int i=0; i<getChildCount(); i++) {
			DraggableView child = (DraggableView)getChildAt(i);
		    Log.i("AndroidHack", "Setting xoffset on resize: "+child.getAnchorX());
			child.setXOffset(child.getAnchorX());
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
	    		if (mActiveDraggableView == null && mTouchPriority == TOUCH_PRIORITY_UNKNOWN) { 
	    			mTouchPriority = TOUCH_PRIORITY_DRAGGING;
	    		}
	    		
	    		if (mTouchPriority == TOUCH_PRIORITY_UNKNOWN) {
	    			boolean handled = mActiveDraggableView.move(event);
	    			
	    			if(handled){
	    				stopTouchSample(event);
	    				mTouchPriority = TOUCH_PRIORITY_VIEW;
	    			} else {
	    				mActiveDraggableView.stopTouchSample(event);
	    				mTouchPriority = TOUCH_PRIORITY_DRAGGING;
	    			}
	    		} else {
	    			if (mTouchPriority == TOUCH_PRIORITY_VIEW) {
	    				mActiveDraggableView.move(event);
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
	    		mActiveDraggableView = getActiveDraggableView();
	    		mTouchPriority = TOUCH_PRIORITY_UNKNOWN;
	    		
	    		if (mActiveDraggableView != null) {
	    			mActiveDraggableView.startTouchSample(event);
	    		} else {
	    			mTouchPriority = TOUCH_PRIORITY_DRAGGING;
	    		}
	    		
	    		startTouchSample(event);	    		
	    		break;
	    	default:
	    		Log.i("AndroidHack", "Got other");
	    		if (mActiveDraggableView != null) {
	    			mActiveDraggableView.stopTouchSample(event);
	    		}
	    		if (mTouchPriority == TOUCH_PRIORITY_DRAGGING) {
	    			animateActiveViewToAnchor();
	    		}
	    		move(event);
	    		mActiveDraggableView = null;
	    		mTouchPriority = TOUCH_PRIORITY_UNKNOWN;
	    		break;
	    }
	    super.dispatchTouchEvent(event);
	    return true;
	}
	
	private void animateActiveViewToAnchor() {
		final DraggableView activeView = getActiveDraggableView();
		if (activeView.getXOffset() == activeView.getAnchorX()) return; 	
			
		final float originalOffset = activeView.getXOffset() - activeView.getAnchorX();
		TranslateAnimation animation = new TranslateAnimation(0, 0, activeView.getTop(), activeView.getTop()){
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				Log.i("AndroidHack", "Animating from: "+originalOffset+" to:"+activeView.getAnchorX());
				activeView.setXOffset(activeView.getAnchorX() + originalOffset - (interpolatedTime)*originalOffset);
			}
		};
		AnimationListener listener = new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Log.i("AndroidHack", "Done animating");
				activeView.setAnimation(null);
			}
		};
		animation.setDuration(500);
		animation.setInterpolator(new DecelerateInterpolator());
		animation.setAnimationListener(listener);
		animation.setFillAfter(true);
		activeView.startAnimation(animation);
	}


	public DraggableView getActiveDraggableView() {
		return (DraggableView) getChildAt(getChildCount() - 1);
	}
	
	/**
	 * 
	 * @return the id of the new view that was created
	 */
	public int addNewDraggableView() {
		DraggableView draggableView = new DraggableView(getContext());
		draggableView.setBackgroundResource(android.R.color.white);
		int newId = generateId();
		draggableView.setId(newId);
		DraggableView activeView = getActiveDraggableView();
		if (activeView != null) {
			draggableView.setAnchorX(activeView.getRight());
		} else {
			draggableView.setAnchorX(0);
		}
		addView(draggableView, new LayoutParams(500, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		return newId;
	}
	
	private int generateId() {
		int num;
        num = mRandomGenrator.nextInt(Integer.MAX_VALUE);
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

            if (mActiveDraggableView != null && (mScrollState == SCROLL_STATE_HORIZONTAL)) {
            	mActiveDraggableView.setXOffset(mActiveDraggableView.getXOffset() + xDiff);
            	invalidate();
                return true;
            }

            return false;
        }
    }

}
