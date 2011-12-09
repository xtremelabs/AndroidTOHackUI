package com.xtremelabs.androidtohackui.draggables.ui;

import java.util.Random;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.xtremelabs.androidtohackui.R;

public class DockableLayout extends RelativeLayout {
	
	private static final int VIEW_HEIGHT = 250;//android.view.ViewGroup.LayoutParams.MATCH_PARENT;
	private static final int VIEW_WIDTH = 500;
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
    private DockableView mDock;
    //Very important variable: keeps a reference to the active DraggableView
    private DockableView mActiveDraggableView;
    private Random mRandomGenrator = new Random(System.currentTimeMillis());
	
	public DockableLayout(Context context) {
		super(context);
		init(context);
	}
	
	public DockableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public DockableLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context) {
		addNewDock();
		mGestureDetector = new GestureDetector(context, new DraggingGestureListener());
	}
	
	@Override
	public void addView(View child) {
		checkChild(child);
		if (child!=mDock)disableActiveChild();
		super.addView(child);
		animateViewIn();
	}
	
//	@Override
//	public void addView(View child, int index, ViewGroup.LayoutParams params) {
//		checkChild(child);
//		disableActiveChild();
//		super.addView(child, index, params);
//		animateViewIn();
//	}
//	
//	@Override
//	public void addView(View child, ViewGroup.LayoutParams params) {
//		checkChild(child);
//		disableActiveChild();
//		super.addView(child, params);
//		animateViewIn();
//	}
	
	@Override
	public void addView(View child, int index) {
		checkChild(child);
		if (child!=mDock)disableActiveChild();
		super.addView(child, index);
		animateViewIn();
	}
	
	@Override
	public void addView(View child, int width, int height) {
		checkChild(child);
		if (child!=mDock)disableActiveChild();
		super.addView(child, width, height);
		animateViewIn();
	}
	
	private void animateViewIn() {
		DockableView activeView = getActiveDraggableView();
		activeView.setXOffset(getRight());
		activeView.setYOffset(getBottom());
		animateActiveViewToAnchor(activeView);
	}
	
	@Override
	public void removeView(View view) {
		super.removeView(view);
		enableActiveChild();
	}
	
	private void checkChild(View child) {
		if (!(child instanceof DockableView)) {
			throw new RuntimeException("Cannot add a view to DockableLayout unless it is a DraggableView");
		}
	}
	
	private void enableActiveChild() {
		if (getChildCount() > 0) {
			DockableView previousViewWithFocus = getActiveDraggableView();
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
			DockableView previousViewWithFocus = getActiveDraggableView();
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
		for (int i=0; i < getChildCount(); i++) {
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

        int modeX = MeasureSpec.getMode(widthMeasureSpec);
        int sizeX = MeasureSpec.getSize(widthMeasureSpec);
        
        int modeY = MeasureSpec.getMode(heightMeasureSpec);
        int sizeY = MeasureSpec.getSize(heightMeasureSpec);
        
        int customWidthSpec = MeasureSpec.makeMeasureSpec(sizeX, modeX);
        int customHeightSpec = MeasureSpec.makeMeasureSpec(sizeY, modeY);
        
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (i == 0) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            } else {
                measureChild(child, customWidthSpec, customHeightSpec);
            }
            if (firstPass && i > 0) {
                child.setLeft(getMeasuredWidth() - child.getMeasuredWidth());
                child.setTop(getMeasuredHeight() - child.getMeasuredHeight());
            }
        }
    }
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		for (int i=0; i<getChildCount(); i++) {
				DockableView child = (DockableView)getChildAt(i);
			    Log.i("AndroidHack", "Setting xoffset on resize: "+child.getAnchorX()+"\nSetting yoffset on resize: "+child.getAnchorY());
				child.setXOffset(child.getAnchorX());
				child.setYOffset(child.getAnchorY());
		}
	}
	
	private void startTouchSample(MotionEvent event) {
	     Log.i("AndroidHack", "Starting layout sampling");
		 mGestureDetector.onTouchEvent(event);
	}
	
	private boolean move(MotionEvent event) {
	    //Log.i("AndroidHack", "Moving layout");

		boolean handled = mGestureDetector.onTouchEvent(event);
		
		int initialScrollState = mScrollState;
		
		if (initialScrollState != SCROLL_STATE_UNKNOWN) {//scrolling
			int action = event.getAction();
			int actionCode = action & MotionEvent.ACTION_MASK;
			
			if (actionCode == MotionEvent.ACTION_CANCEL || actionCode == MotionEvent.ACTION_UP) {
				mScrollState = SCROLL_STATE_UNKNOWN;
			}
		}
		if (initialScrollState == SCROLL_STATE_HORIZONTAL || initialScrollState == SCROLL_STATE_VERTICAL) { 
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
	    //Log.i("AndroidHack", "Got Touch Event");
	    
	    switch (actionCode) {
	    	case MotionEvent.ACTION_MOVE:
	    		//Log.i("AndroidHack", "Got move");
	    		if (mActiveDraggableView == null && mTouchPriority == TOUCH_PRIORITY_UNKNOWN) { 
	    			mTouchPriority = TOUCH_PRIORITY_DRAGGING;
	    		}
	    		
	    		if (mActiveDraggableView == null) {
	    			return true;
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
	                        if (event.getX() > getWidth() /2) {
	                        	mActiveDraggableView.setAnchorX(getWidth() - VIEW_WIDTH);
	                        } else {
	                        	mActiveDraggableView.setAnchorX(0);
	                        }
	                        if (event.getY() > getHeight() /2) {
	                        	mActiveDraggableView.setAnchorY(getHeight() - VIEW_HEIGHT);
	                        } else {
	                        	mActiveDraggableView.setAnchorY(0);
	                        }
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
		    		if (mTouchPriority == TOUCH_PRIORITY_DRAGGING) {
		    			animateActiveViewToAnchor(mActiveDraggableView);
		    		}
	    		}
	    		move(event);
	    		//mActiveDraggableView = null;
	    		mTouchPriority = TOUCH_PRIORITY_UNKNOWN;
	    		break;
	    }
	    super.dispatchTouchEvent(event);
	    return true;
	}
	
	private void animateActiveViewToAnchor(final DockableView activeView, int duration) {
		if (activeView.getXOffset() != activeView.getAnchorX() || activeView.getYOffset() != activeView.getAnchorY()){ 	
				
			final float originalXOffset = activeView.getXOffset() - activeView.getAnchorX();
			final float originalYOffset = activeView.getYOffset() - activeView.getAnchorY();
			TranslateAnimation animation = new TranslateAnimation(0, 0, activeView.getTop(), activeView.getTop()){
				@Override
				protected void applyTransformation(float interpolatedTime,	Transformation t) {
					//Log.i("AndroidHack", "Animating from: "+originalXOffset+" to:"+activeView.getAnchorX() + "\n and Animating from: "+originalYOffset+" to:"+activeView.getAnchorY());
					activeView.setXOffset(activeView.getAnchorX() + originalXOffset - (interpolatedTime)*originalXOffset);
					activeView.setYOffset(activeView.getAnchorY() + originalYOffset - (interpolatedTime)*originalYOffset);
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
			animation.setDuration(duration);
			animation.setInterpolator(new DecelerateInterpolator());
			animation.setAnimationListener(listener);
			animation.setFillAfter(true);
			activeView.startAnimation(animation);
		}
	}
	
	private void animateActiveViewToAnchor(final DockableView activeView){
		animateActiveViewToAnchor(activeView,500);
	}
	
	private void animateDockToAnchor(float x, float y) {
		final DockableView activeDraggableView = getActiveDraggableView();
		if (activeDraggableView.getAnchorX() != mDock.getAnchorX() || activeDraggableView.getAnchorY() != mDock.getAnchorY()) {
			mDock.setAnchorX(activeDraggableView.getAnchorX());
			mDock.setAnchorY(activeDraggableView.getAnchorY());
			animateActiveViewToAnchor(mDock,100);
		}
	}

	public DockableView getActiveDraggableView() {
		if (mActiveDraggableView == null) {
			Log.i("AndroidHack","---------------------MMM whysit null? " + mActiveDraggableView);
			for (int i = 0; i < getChildCount(); i++) {
				if (getChildAt(i)!=mDock) {
					mActiveDraggableView = (DockableView) getChildAt(i);
					break;
				}
			}
		}
		Log.i("AndroidHack","And.. the active view is: " + mActiveDraggableView);
		return mActiveDraggableView;
	}
	
	public void setActiveDraggableView(DockableView dockableView) {
		Log.i("AndroidHack","!NEW Active View: " +dockableView);
		mActiveDraggableView = dockableView;
	}
	
	/**
	 * 
	 * @return the id of the new view that was created
	 */
	public int addNewDraggableView() {
		
		DockableView draggableView = new DockableView(getContext());
		draggableView.setBackgroundResource(android.R.color.white);
		int newId = generateId();
		draggableView.setId(newId);
		DockableView activeView = getActiveDraggableView();
		Log.i("AndroidHack","Active View: " +activeView);
		if (activeView != null) {
			if (activeView.getRight() + VIEW_WIDTH <= getWidth()) {
				Log.i("AndroidHack","right/top:"+activeView.getRight()+"/"+activeView.getTop());
				draggableView.setAnchorX(activeView.getRight()); 
				draggableView.setAnchorY(activeView.getTop());
			} else {
				Log.i("AndroidHack","0/bottom:0/"+activeView.getBottom());
				draggableView.setAnchorX(0); 
				draggableView.setAnchorY(activeView.getBottom());
			}
		} else {
			draggableView.setAnchorX(0);
			draggableView.setAnchorY(0);
		}
		
		draggableView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (!(v instanceof DockableView)) {
					return false;
				}
				v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
				setActiveDraggableView((DockableView)v);
				return true;

			}
		});

		addView(draggableView, new LayoutParams(VIEW_WIDTH, VIEW_HEIGHT));
		return newId;
	}
	
	public int addNewDock() {
		mDock = new DockableView(getContext());
		mDock.setBackgroundColor(getResources().getColor(R.color.dock_blue));
		int newId = generateId();
		mDock.setId(newId);
		DockableView activeView = getActiveDraggableView();
		if (activeView != null) {
			if (activeView.getRight() + VIEW_WIDTH <= getWidth()) {
				mDock.setAnchorX(activeView.getRight()); 
				mDock.setAnchorY(activeView.getTop());
			} else {
				mDock.setAnchorX(0); 
				mDock.setAnchorY(activeView.getBottom());
			}
		} else {
			mDock.setAnchorX(0);
			mDock.setAnchorY(0);
		}

		mDock.setFocusable(false);
		mDock.setClickable(false);
		mDock.setEnabled(false);
		mDock.setFocusableInTouchMode(false);
		mDock.setIsTouchable(false);

		addView(mDock, new LayoutParams(VIEW_WIDTH, VIEW_HEIGHT));
		return newId;
	}
	
	private int generateId() {
		int num;
        num = mRandomGenrator.nextInt(Integer.MAX_VALUE);
		return num;
	}
	
	class DraggingGestureListener extends SimpleOnGestureListener {
        private float mInitialRawX;
        private float mInitialRawY;
        private float mMostRecentRawX;
        private float mMostRecentRawY;

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mInitialRawX != e1.getRawX()) {
            	mInitialRawX = e1.getRawX();
            	mMostRecentRawX = e1.getRawX();
            }
            if (mInitialRawY != e1.getRawY()) {
            	mInitialRawY = e1.getRawY();
            	mMostRecentRawY = e1.getRawY();
            }
            
            animateDockToAnchor(mMostRecentRawX,mMostRecentRawY);
            int xDiff = Math.round(e2.getRawX() - mMostRecentRawX);
            int yDiff = Math.round(e2.getRawY() - mMostRecentRawY);
            
            if (Math.abs(yDiff) > Math.abs(xDiff)) {
            	mScrollState = SCROLL_STATE_VERTICAL;
            } else {
            	mScrollState = SCROLL_STATE_HORIZONTAL;
            }
            mMostRecentRawX = e2.getRawX();
            mMostRecentRawY = e2.getRawY();

            if (mActiveDraggableView != null) {
	            if (mScrollState == SCROLL_STATE_HORIZONTAL || mScrollState == SCROLL_STATE_VERTICAL) {
	            	mActiveDraggableView.setXOffset(mActiveDraggableView.getXOffset() + xDiff);
	            	mActiveDraggableView.setYOffset(mActiveDraggableView.getYOffset() + yDiff);
	            }
	         	invalidate();
	            return true;
            }
            return false;
        }

		
    }
}
