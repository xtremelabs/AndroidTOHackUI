package com.xtremelabs.androidtohackui.bubbles.compatible;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xtremelabs.androidtohackui.R;
import com.xtremelabs.androidtohackui.bubbles.compatible.controllers.AbstractBubbleController;
import com.xtremelabs.androidtohackui.bubbles.compatible.controllers.AbstractBubbleController.OnCloseListener;
import com.xtremelabs.androidtohackui.bubbles.compatible.controllers.BlandBubbleController;
import com.xtremelabs.androidtohackui.bubbles.compatible.controllers.IBubbleContainer;
import com.xtremelabs.androidtohackui.bubbles.controllers.NotificationsBubbleController;
import com.xtremelabs.androidtohackui.bubbles.ui.BubbleLayout;

public class BubbleExampleActivity extends FragmentActivity implements IBubbleContainer {
	private AbstractBubbleController mBubbleController;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
        setContentView(R.layout.bubble_example_layout);
        
        setupButtons();
    }

	private void setupButtons() {
    	Button button1 = (Button) findViewById(R.id.button1);
    	Button button2 = (Button) findViewById(R.id.button2);
    	
    	button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 new BlandBubbleController(BubbleExampleActivity.this).showBubble(v);
			}
		});
    	
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new NotificationsBubbleController(BubbleExampleActivity.this).showBubble(v);
			}
		});

	}
	
	@Override
	public int getBubbleContainerId() {
		return R.id.bubble_container;
	}

	@Override
	public void initBubble(AbstractBubbleController bubbleController) {
		mBubbleController = bubbleController;
		mBubbleController.addOnCloseListener(new OnCloseListener() {
			@Override
			public void onClose(AbstractBubbleController bubbleController,
					BubbleLayout bubbleLayout,
					android.support.v4.app.FragmentManager fragmentManager) {
				if (bubbleController == mBubbleController) {
	                mBubbleController.clearOnCloseListeners();
	                mBubbleController = null;
	            }				
			}
		});
	}
	
	@Override
	public void onBackPressed() {
        boolean handled = false;
        if (mBubbleController != null) {
            handled = mBubbleController.onBackPressed(getSupportFragmentManager());
        }

        if (!handled) {
            super.onBackPressed();
        }

	}
	
}
