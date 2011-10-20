package com.xtremelabs.androidtohackui.bubbles;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xtremelabs.androidtohackui.R;
import com.xtremelabs.androidtohackui.bubbles.controllers.AbstractBubbleController;
import com.xtremelabs.androidtohackui.bubbles.controllers.AbstractBubbleController.OnCloseListener;
import com.xtremelabs.androidtohackui.bubbles.controllers.ActionBarBubbleController;
import com.xtremelabs.androidtohackui.bubbles.controllers.BlandBubbleController;
import com.xtremelabs.androidtohackui.bubbles.controllers.FragmentBubbleController;
import com.xtremelabs.androidtohackui.bubbles.controllers.IBubbleContainer;
import com.xtremelabs.androidtohackui.bubbles.ui.BubbleLayout;

public class BubbleExampleActivity extends Activity implements IBubbleContainer {
	private AbstractBubbleController mBubbleController;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.bubble_example_layout);
        
        setupButtons();
    }
	
    private void setupButtons() {
    	Button button1 = (Button) findViewById(R.id.button1);
    	Button button2 = (Button) findViewById(R.id.button2);
    	Button button3 = (Button) findViewById(R.id.button3);
    	
    	button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 new BlandBubbleController(BubbleExampleActivity.this).showBubble(v);
			}
		});
    	
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new FragmentBubbleController(BubbleExampleActivity.this).showBubble(v);
			}
		});

		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new ActionBarBubbleController(BubbleExampleActivity.this).showBubble(v);
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
					BubbleLayout bubbleLayout, FragmentManager fragmentManager) {
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
            handled = mBubbleController.onBackPressed(getFragmentManager());
        }

        if (!handled) {
            super.onBackPressed();
        }

	}
	
}
