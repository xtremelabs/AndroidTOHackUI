package com.xtremelabs.androidtohackui.bubbles.controllers;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.xtremelabs.androidtohackui.bubbles.ui.AnchorInfo;
import com.xtremelabs.androidtohackui.bubbles.ui.BubbleLayout;

abstract public class AbstractBubbleController {
    public static final int BUBBLE_DEFAULT_WIDTH = 380; //standard for Xoom
    public static final int BUBBLE_DEFAULT_HEIGHT = 600; //standard for Xoom
    private long mLastBubbleAppearance = Long.MIN_VALUE;
    public static final String TRANS_ID = "8305612947";

    private BubbleLayout mBubbleLayout;
    protected Activity mActivity;

    private ArrayList<Fragment> mStack;
    private Button mBackButton;
    private boolean mOpen = false;
    private ArrayList<OnCloseListener> mOnCloseListeners;
	
	abstract public void onBubbleAttachedToWindow(ViewGroup container, FragmentManager fragmentManager, Activity activity);
	
	public AbstractBubbleController(final Activity activity)
	{
		if(!(activity instanceof IBubbleContainer))
		{
			throw new RuntimeException("Can only create bubbles in activities that implement IBubbleContainer");
		}
		mActivity = activity;
	    mStack = new ArrayList<Fragment>();

	    mBackButton = new Button(activity);
	    mBackButton.setText("Back");
	    mBackButton.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            onBackPressed(activity.getFragmentManager());
	        }
	    });

	    mOnCloseListeners = new ArrayList<OnCloseListener>();
	}
	
	public void showBubble(AnchorInfo anchorInfo) {
        showBubble(anchorInfo, 
                MeasureSpec.makeMeasureSpec(getDIPValue(BUBBLE_DEFAULT_WIDTH), MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(getDIPValue(BUBBLE_DEFAULT_HEIGHT), MeasureSpec.AT_MOST));
    }
    
    public void showBubble(View anchor) {
        showBubble(AnchorInfo.createAnchorInfo(anchor));
    }
    
	public void showBubble(View anchor,
	        int preferredBubbleWidthMeasureSpec, int preferredBubbleHeightMeasureSpec) {
	    showBubble(AnchorInfo.createAnchorInfo(anchor),
	            preferredBubbleWidthMeasureSpec, preferredBubbleHeightMeasureSpec);
	}
	
	public void showBubble(final AnchorInfo anchorInfo,
	            int preferredBubbleWidthMeasureSpec, int preferredBubbleHeightMeasureSpec) {

		((IBubbleContainer)mActivity).initBubble(this);
	    long now = System.currentTimeMillis();
	    if(now - mLastBubbleAppearance < 500 && mLastBubbleAppearance != Long.MIN_VALUE)
	        return;
	    
	    mLastBubbleAppearance = now;
	        
		if (mBubbleLayout != null) {
			closeBubble();
		}
		
	    BubbleLayout layout = new BubbleLayout(mActivity) {
	        @Override
	        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                AnchorInfo newInfo = anchorInfo;
                AnchorInfo oldInfo = getAnchor();
                if (oldInfo != null && !oldInfo.equals(newInfo)) {
                    setAnchor(newInfo);
                }

                super.onLayout(changed, left, top, right, bottom);
            }


            @Override
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                onBubbleAttachedToWindow(getContainer(), mActivity.getFragmentManager(), mActivity);
            }
        };

        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AbstractBubbleController.this.closeBubble();
            }
        });
	    
	    layout.setAnchor(anchorInfo);
	    
	    if(preferredBubbleHeightMeasureSpec != 0) {
        	layout.setPreferredBubbleHeightMeasureSpec(preferredBubbleHeightMeasureSpec);
	    }
	    if(preferredBubbleWidthMeasureSpec != 0) {
	    	layout.setPreferredBubbleWidthMeasureSpec(preferredBubbleWidthMeasureSpec);
	    }
	    if(preferredBubbleHeightMeasureSpec == 0 || preferredBubbleWidthMeasureSpec == 0)
	    {
	    	layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    }
	    
	    ViewGroup frame = (ViewGroup) mActivity.findViewById(getContainerId());
	    
    	mBubbleLayout = layout;
    	
	    frame.addView(mBubbleLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    
	    mOpen  = true;
	    //titleBarInfoChanged();
	}
	
	protected int getContainerId()
	{
		return ((IBubbleContainer)mActivity).getBubbleContainerId();
	}
	
	public void closeBubble() {
	    if (mBubbleLayout != null) {
		    hideKeyboard();
		    for (OnCloseListener onCloseListener : mOnCloseListeners) {
		        onCloseListener.onClose(this, mBubbleLayout, mActivity.getFragmentManager());
		    }
    		while (mActivity.getFragmentManager().popBackStackImmediate()) {
    		}
		    ViewGroup frame = (ViewGroup) mActivity.findViewById(getContainerId());
		    if (frame != null) frame.removeView(mBubbleLayout);
		    mBubbleLayout = null;
		    mActivity = null;
		    mOpen = false;
	    }
	}
	
	public void addOnCloseListener(OnCloseListener listener) {
	    if (!mOnCloseListeners.contains(listener)) {
	        mOnCloseListeners.add(listener);
	    }
	}
	
	public void removeOnCloseListener(OnCloseListener listener) {
	    mOnCloseListeners.remove(listener);
	}
	
	public void clearOnCloseListeners() {
	    mOnCloseListeners.clear();
	}
	
	public boolean isOpen() {
        return mOpen;
    }


//    /**
//     * Creates a publisher button for
//     * 
//     * @param context
//     * @return
//     */
//    public static Button createButton(Context context) {
//        String layoutService = Context.LAYOUT_INFLATER_SERVICE;
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(layoutService);
//        View view = inflater.inflate(R.layout.back_button, null);
//        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, Units.getDIPValue(32)));
//        return (Button) view;
//    }


    /**
     * Hides the keyboard. Intended to be called whenever a bubble is closed.
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) mActivity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = mActivity.getCurrentFocus();
        if (view == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    protected Fragment getVisibleFragment() {
        if (mActivity == null) return null;

        int bodyId = mBubbleLayout.getContainer().getId();
        FragmentManager fragmentManager = mActivity.getFragmentManager();
        return fragmentManager.findFragmentById(bodyId);
    }


    protected void pushFragment(Fragment fragment) {
        mStack.add(fragment);

        int bodyId = mBubbleLayout.getContainer().getId();
        FragmentManager fragmentManager = mActivity.getFragmentManager();
        if (fragmentManager.findFragmentById(bodyId) == null) fragmentManager.beginTransaction()
                .add(bodyId, fragment).commit();
        else {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(bodyId, fragment);
            ft.addToBackStack(TRANS_ID).commit();
        }

        fragmentManager.executePendingTransactions();
//        titleBarInfoChanged();
    }


    /**
     * Custom callback for when the back button is pressed
     * 
     * @param fragmentManager
     * @return
     */
    public boolean onBackPressed(FragmentManager fragmentManager) {
        if (mStack.size() <= 0) {
            return true;
        } else {
            popFragment();
            return true;
        }
    }



    protected boolean popFragment() {
        if (mStack.size() <= 0) return false;

        Fragment fragment = mStack.get(mStack.size() - 1);

        mStack.remove(fragment);

        FragmentManager fragmentManager = mActivity.getFragmentManager();
        hideKeyboard();
        fragmentManager.popBackStackImmediate();

        if (mStack.size() <= 0) {
            closeBubble();
        } else {
//            titleBarInfoChanged();
        }

        return true;
    }


    protected ArrayList<Fragment> getStack() {
        return mStack;
    }


    public TextView getTitleView() {
    	return null;
//        return mBubbleLayout.getActionBar().getTitle();
    }


    public void setTitle(String title) {
        getTitleView().setText(title);
    }


    public void setTitle(int stringId) {
        getTitleView().setText(stringId);
    }


//    public ViewGroup getLeft() {
//        return mBubbleLayout.getActionBar().getLeftContainer();
//    }
//
//
//    public ViewGroup getRight() {
//        return mBubbleLayout.getActionBar().getRightContainer();
//    }
//
//
//    public void hideActionBar() {
//        mBubbleLayout.findViewById(R.id.pane_action_bar).setVisibility(View.GONE);
//    }
//
//
//    public void showActionBar() {
//        mBubbleLayout.findViewById(R.id.pane_action_bar).setVisibility(View.VISIBLE);
//    }


    public static interface OnCloseListener {
        /**
         * Called when bubble is about to the closed, before the visible fragment is removed, and
         * the bubble view removed from its parent
         * 
         * @param bubbleLayout
         *            the {@link BubbleLayout} for the bubble about to be closed
         * @param fragmentManager
         *            the fragment manager for activity containing the bubble
         */
        public void onClose(AbstractBubbleController bubbleController, BubbleLayout bubbleLayout,
                FragmentManager fragmentManager);
    }
    
    private static int getDIPValue(int value) {
    	DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }
    
}
