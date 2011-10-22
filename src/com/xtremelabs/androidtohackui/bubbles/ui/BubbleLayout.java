package com.xtremelabs.androidtohackui.bubbles.ui;

import java.util.Random;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
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
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            finalWidth = displayMetrics.widthPixels;
        } else {
            finalWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int finalHeight = 0;
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            finalHeight = displayMetrics.heightPixels;
        } else {
            finalHeight = MeasureSpec.getSize(heightMeasureSpec);
        }
        
        setMeasuredDimension(finalWidth, finalHeight);
        
        int bubbleHeightMeasureSpec = heightMeasureSpec;
        int bubbleWidthMeasureSpec = widthMeasureSpec;
        
        if (mAnchorInfo != null) {
        	float bubbleTop = 0;
        	int[] containerLocation = new int[2];
        	getLocationInWindow(containerLocation);
        	int containerTop = containerLocation[1];
        	
            bubbleTop = mAnchorInfo.y + mAnchorInfo.height - containerTop;
            
            bubbleHeightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - (int) bubbleTop, MeasureSpec.getMode(heightMeasureSpec));
            if (mPreferredBubbleHeightMeasureSpec != Integer.MIN_VALUE) {
                int mode = MeasureSpec.getMode(mPreferredBubbleHeightMeasureSpec);
                
                switch (mode) {
                case MeasureSpec.AT_MOST:
                    int maxBubbleHeight = Math.min(
                            MeasureSpec.getSize(mPreferredBubbleHeightMeasureSpec),
                            MeasureSpec.getSize(heightMeasureSpec) - (int) bubbleTop);
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
        	float bubbleLeft = 0;
        	int[] containerLocation = new int[2];
        	getLocationInWindow(containerLocation);
        	int containerLeft = containerLocation[0];
        	int containerWidth = getMeasuredWidth();
        	
            int bubbleWidth = mBubble.getMeasuredWidth();
            bubbleLeft = mAnchorInfo.x + mAnchorInfo.width / 2 - bubbleWidth / 2 - containerLeft;
            
            bubbleLeft = Math.max(bubbleLeft, 0);
            bubbleLeft = Math.min(bubbleLeft, containerWidth - bubbleWidth);
            
            int anchorCenter = Math.round(mAnchorInfo.x + mAnchorInfo.width / 2) - containerLeft;
            
            View topLeftCorner = findViewById(R.id.top_left_corner);
            View leftStretcher = findViewById(R.id.top_stretcher_left);
            View arrow = findViewById(R.id.arrow);
            View rightStretcher = findViewById(R.id.top_stretcher_right);
            View topRightCorner = findViewById(R.id.top_right_corner);

            int desiredLeftStretcherWidth = anchorCenter - (int) bubbleLeft - arrow.getMeasuredWidth() / 2 - topRightCorner.getMeasuredWidth();
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
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        float bubbleLeft = 0, bubbleTop = 0;
        if (mAnchorInfo != null) {
        	int[] containerLocation = new int[2];
        	getLocationInWindow(containerLocation);
        	int containerLeft = containerLocation[0];
        	int containerTop = containerLocation[1];
        	int containerWidth = getMeasuredWidth();
        	
            int bubbleWidth = mBubble.getMeasuredWidth();
            bubbleLeft = mAnchorInfo.x + mAnchorInfo.width / 2 - bubbleWidth / 2 - containerLeft;
            
            bubbleLeft = Math.max(bubbleLeft, 0);
            bubbleLeft = Math.min(bubbleLeft, containerWidth - bubbleWidth);
            bubbleTop = mAnchorInfo.y + mAnchorInfo.height - containerTop;
        }
        mBubble.layout((int) bubbleLeft, (int) bubbleTop, (int) bubbleLeft + mBubble.getMeasuredWidth(), (int) bubbleTop + mBubble.getMeasuredHeight());
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