package com.xtremelabs.androidtohackui.bubbles.ui;

import java.util.Random;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.xtremelabs.androidtohackui.R;


public class BubbleLayout extends ViewGroup {
	
	public static final int BUBBLE_BORDER_WIDTH = 18;
	
    private View mBubble;
    private AnchorInfo mAnchorInfo;
    private int mPreferredBubbleWidthMeasureSpec = Integer.MIN_VALUE;
    private int mPreferredBubbleHeightMeasureSpec = Integer.MIN_VALUE;
    
    private int mContainerId = -1;
    
    public BubbleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleLayout(Context context) {
        super(context);
        init();
    }
    
    private void init() {
        if (mBubble == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mBubble = inflater.inflate(R.layout.bubble, this, false);
            addView(mBubble, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            
            Random rand = new Random(System.currentTimeMillis());
            mContainerId = rand.nextInt(Integer.MAX_VALUE);
            mBubble.findViewById(R.id.bubble_content).setId(mContainerId);
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        
        // measure width
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int finalWidth = 0;
        // if UNSPECIFIED, take as much width as the views need
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            finalWidth = displayMetrics.widthPixels;
        }
        // else if AT_MOST or EXACTLY, then use all available width
        else {
            finalWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        
        // measure height
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int finalHeight = 0;
        // if UNSPECIFIED, take as much height as the views need
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            finalHeight = displayMetrics.heightPixels;
        }
        // else if AT_MOST or EXACTLY, then use all available height
        else {
            finalHeight = MeasureSpec.getSize(heightMeasureSpec);
        }
        
        setMeasuredDimension(finalWidth, finalHeight);
        
        int bubbleHeightMeasureSpec = heightMeasureSpec;
        int bubbleWidthMeasureSpec = widthMeasureSpec;
        if (mAnchorInfo != null) {
            int bubbleTop = Math.round(mAnchorInfo.y + mAnchorInfo.height);
            bubbleHeightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - bubbleTop, MeasureSpec.getMode(heightMeasureSpec));
            if (mPreferredBubbleHeightMeasureSpec != Integer.MIN_VALUE) {
                int mode = MeasureSpec.getMode(mPreferredBubbleHeightMeasureSpec);
                
                switch (mode) {
                case MeasureSpec.AT_MOST:
                    int maxBubbleHeight = Math.min(
                            MeasureSpec.getSize(mPreferredBubbleHeightMeasureSpec),
                            MeasureSpec.getSize(heightMeasureSpec) - bubbleTop);
                    bubbleHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                            maxBubbleHeight, MeasureSpec.EXACTLY);
                    break;
                case MeasureSpec.EXACTLY:
                    bubbleHeightMeasureSpec = mPreferredBubbleHeightMeasureSpec;
                    break;
                }
            }
            
            if (mPreferredBubbleWidthMeasureSpec != Integer.MIN_VALUE) {
                int mode = MeasureSpec.getMode(mPreferredBubbleWidthMeasureSpec);
                
                switch (mode) {
                case MeasureSpec.AT_MOST:
                    int maxBubbleWidth = Math.min(
                            MeasureSpec.getSize(mPreferredBubbleWidthMeasureSpec),
                            MeasureSpec.getSize(widthMeasureSpec));
                    bubbleWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                            maxBubbleWidth, MeasureSpec.EXACTLY);
                    break;
                case MeasureSpec.EXACTLY:
                    bubbleWidthMeasureSpec = mPreferredBubbleWidthMeasureSpec;
                    break;
                }
            }
        }
        
        measureChild(mBubble, bubbleWidthMeasureSpec, bubbleHeightMeasureSpec);
        if (mAnchorInfo != null) {
            int bubbleWidth = mBubble.getMeasuredWidth();
            int layoutWidth = getMeasuredWidth();
            int anchorCenter = Math.round(mAnchorInfo.x + mAnchorInfo.width / 2);
            int bubbleLeft = anchorCenter - bubbleWidth / 2;
            
            bubbleLeft = Math.max(bubbleLeft, 0);
            bubbleLeft = Math.min(bubbleLeft, layoutWidth - bubbleWidth);
            
            View topLeftCorner = findViewById(R.id.top_left_corner);
            View leftStretcher = findViewById(R.id.top_stretcher_left);
            View arrow = findViewById(R.id.arrow);
            View rightStretcher = findViewById(R.id.top_stretcher_right);
            View topRightCorner = findViewById(R.id.top_right_corner);

            int desiredLeftStretcherWidth = anchorCenter - bubbleLeft - arrow.getMeasuredWidth() / 2 - topRightCorner.getMeasuredWidth();
            int maxLeftStretcherWidth = MeasureSpec.getSize(bubbleWidthMeasureSpec) - topLeftCorner.getMeasuredWidth() - arrow.getMeasuredWidth() - topRightCorner.getMeasuredWidth();
            int finalLeftStretcherWidth = Math.min(Math.round(desiredLeftStretcherWidth), maxLeftStretcherWidth);
            
            leftStretcher.getLayoutParams().width = finalLeftStretcherWidth;
            int rightStretcherWidth = maxLeftStretcherWidth - finalLeftStretcherWidth;
            if (finalLeftStretcherWidth<=0) {
            	rightStretcherWidth = rightStretcherWidth - 5 - arrow.getMeasuredWidth() / 2;//hack fix..something wrong with specs
            }
            rightStretcher.getLayoutParams().width = rightStretcherWidth;
        }
        measureChild(mBubble, bubbleWidthMeasureSpec, bubbleHeightMeasureSpec);
        
        Log.i(getClass().getSimpleName(), "finalWidth: " + finalWidth + ", finalHeight: " + finalHeight);
        Log.i(getClass().getSimpleName(), "bubble width: " + mBubble.getMeasuredWidth() + ", bubble height: " + mBubble.getMeasuredHeight());
        Log.i(getClass().getSimpleName(), "bubble height spec: " + MeasureSpec.toString(bubbleHeightMeasureSpec));
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        float left = 0, top = 0;
        if (mAnchorInfo != null) {
            int bubbleWidth = mBubble.getMeasuredWidth();
            int layoutWidth = getMeasuredWidth();
            left = mAnchorInfo.x + mAnchorInfo.width / 2 - bubbleWidth / 2;
            
            left = Math.max(left, 0);
            left = Math.min(left, layoutWidth - bubbleWidth);
            top = mAnchorInfo.y + mAnchorInfo.height;
        }

        Log.i(getClass().getSimpleName(), "bubble left: " + left + ", bubble top: " + top);
        mBubble.layout(0, 0, mBubble.getMeasuredWidth(), mBubble.getMeasuredHeight());
        mBubble.setTranslationX(left);
        mBubble.setTranslationY(top);
    }
    
    public ViewGroup getContainer() {
        return (ViewGroup) mBubble.findViewById(mContainerId);
    }
    
    public void setAnchor(AnchorInfo anchorInfo) {
        mAnchorInfo = anchorInfo;
    }

    public AnchorInfo getAnchor() {
        return mAnchorInfo;
    }
    
    public void setPreferredBubbleWidthMeasureSpec(int preferredBubbleWidthMeasureSpec) {
        mPreferredBubbleWidthMeasureSpec = preferredBubbleWidthMeasureSpec;
    }
    
    public void setPreferredBubbleHeightMeasureSpec(int preferredBubbleHeightMeasureSpec) {
        mPreferredBubbleHeightMeasureSpec = preferredBubbleHeightMeasureSpec;
    }
    
}