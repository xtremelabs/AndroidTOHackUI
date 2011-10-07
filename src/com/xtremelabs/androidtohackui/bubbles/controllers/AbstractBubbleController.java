package com.xtremelabs.androidtohackui.bubbles.controllers;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManager.BackStackEntry;
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

import com.xtremelabs.androidtohackui.R;
import com.xtremelabs.androidtohackui.bubbles.ui.AnchorInfo;
import com.xtremelabs.androidtohackui.bubbles.ui.BubbleLayout;

abstract public class AbstractBubbleController {
    public static final int BUBBLE_DEFAULT_WIDTH = 380; //standard for Xoom
    public static final int BUBBLE_DEFAULT_HEIGHT = 600; //standard for Xoom
    private long mLastBubbleAppearance = Long.MIN_VALUE; //sentinel value
    private static final String FRAGMENT_TRANSACTION_NAME = "FRAGMENT_TRANSACTION_NAME";

    private BubbleLayout mBubbleLayout;
    protected Activity mActivity;

//    private ArrayList<Fragment> mStack;
    private Button mBackButton;
    private boolean mOpen = false;
    private ArrayList<OnCloseListener> mOnCloseListeners;
	
	abstract public void onBubbleAttachedToWindow();
	
	public AbstractBubbleController(final Activity activity)
	{
		if(!(activity instanceof IBubbleContainer))
		{
			throw new RuntimeException("Can only create bubbles in activities that implement IBubbleContainer");
		}
		mActivity = activity;
//	    mStack = new ArrayList<Fragment>();

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
                onBubbleAttachedToWindow();
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


    protected void pushFragment(Fragment fragment, String title) {

      int bodyId = mBubbleLayout.getContainer().getId();
      FragmentManager fragmentManager = mActivity.getFragmentManager();
      if (fragmentManager.findFragmentById(bodyId) == null) fragmentManager.beginTransaction()
              .add(bodyId, fragment)
              .setBreadCrumbShortTitle(title)
              .addToBackStack(FRAGMENT_TRANSACTION_NAME).commit();
      else {
          FragmentTransaction ft = fragmentManager.beginTransaction();
          ft.replace(bodyId, fragment)
          .addToBackStack(FRAGMENT_TRANSACTION_NAME)
          .setBreadCrumbShortTitle(title).commit();
      }

      fragmentManager.executePendingTransactions();
      configureTitleBar();
  }
    
    
    protected void pushFragment(Fragment fragment) {
//        mStack.add(fragment);

        int bodyId = mBubbleLayout.getContainer().getId();
        FragmentManager fragmentManager = mActivity.getFragmentManager();
        if (fragmentManager.findFragmentById(bodyId) == null) fragmentManager.beginTransaction()
                .add(bodyId, fragment)
        		.addToBackStack(FRAGMENT_TRANSACTION_NAME).commit();
        else {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(bodyId, fragment);
            ft.addToBackStack(FRAGMENT_TRANSACTION_NAME).commit();
        }

        fragmentManager.executePendingTransactions();
        configureTitleBar();
    }

    /**
     * Custom callback for when the back button is pressed
     * 
     * @param fragmentManager
     * @return
     */
    public boolean onBackPressed(FragmentManager fragmentManager) {
        if (fragmentManager.getBackStackEntryCount() <= 0) {
        	closeBubble();
        } else {
            popFragment();
        }
        return true;
    }



    protected boolean popFragment() {
    	FragmentManager fragmentManager = mActivity.getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() <= 0) return false;

        hideKeyboard();
        fragmentManager.popBackStackImmediate();

        if (fragmentManager.getBackStackEntryCount() <= 0) {
            closeBubble();
        } else {
        	configureTitleBar();
        }
        return true;
    }

    private void configureTitleBar() {
    	FragmentManager fragmentManager = mActivity.getFragmentManager();
//    	if (fragmentManager.getBackStackEntryCount() <= 0) return;
    	BackStackEntry entry = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1);
    	if (entry != null && entry.getBreadCrumbShortTitle() != null) {
    		setTitle(entry.getBreadCrumbShortTitle().toString());
    	} //else if current fragment is a IBubbleFragment
    }

    public TextView getTitleView() {
        return (TextView)mActivity.findViewById(R.id.bubble_action_bar_title);
    }


    public void setTitle(String title) {
        getTitleView().setText(title);
    }


    public void setTitle(int stringId) {
        getTitleView().setText(stringId);
    }


    public void hideActionBar() {
        mBubbleLayout.findViewById(R.id.bubble_action_bar).setVisibility(View.GONE);
    }


    public void showActionBar() {
        mBubbleLayout.findViewById(R.id.bubble_action_bar).setVisibility(View.VISIBLE);
    }


    public static interface OnCloseListener {
    	
    	//used to inform and activity that the bubble is closing, and that it should remove references to it/clean up
        public void onClose(AbstractBubbleController bubbleController, BubbleLayout bubbleLayout,
                FragmentManager fragmentManager);
    }
    
    private static int getDIPValue(int value) {
    	DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }
    
}
