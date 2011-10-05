package com.xtremelabs.androidtohackui.bubbles.ui;

import java.io.Serializable;

import android.view.View;

/**
 * Models the view to use as an anchor for the arrow of a {@link BubbleLayout}.
 */
public class AnchorInfo implements Serializable {
    private static final long serialVersionUID = 378687385670347516L;
    public int width = 0;
    public int height = 0;
    public float x = 0;
    public float y = 0;
    public int id = 0;


    /**
     * Create anchor info from a given view.
     * @param view
     * @return anchorinfo
     */
    public static AnchorInfo createAnchorInfo(View view) {
        AnchorInfo info = new AnchorInfo();

        info.width = view.getMeasuredWidth();
        info.height = view.getMeasuredHeight();

        int[] location = new int[2];
        view.getLocationInWindow(location);
        info.x = location[0];
        info.y = location[1];

        info.id = view.getId();

        return info;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof AnchorInfo) {
            AnchorInfo other = (AnchorInfo) o;
            if (other.width == width && other.height == height && other.x == x && other.y == y
                    && other.id == id) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
