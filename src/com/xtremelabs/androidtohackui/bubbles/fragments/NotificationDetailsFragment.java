package com.xtremelabs.androidtohackui.bubbles.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xtremelabs.androidtohackui.R;
import com.xtremelabs.androidtohackui.bubbles.models.BubbleTitleBarElements;

public class NotificationDetailsFragment extends Fragment implements IBubbleFragment {
    private String mTitle;
    private BubbleTitleBarElements mActionBarElements;
    private IBubbleFragmentHandler mHandler; 

    public NotificationDetailsFragment(String title) {
        mTitle = title;
        mActionBarElements = new BubbleTitleBarElements();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        
        View view = inflater.inflate(R.layout.notification_details, container, false);
        getNotificationTitle(view).setText(mTitle);
        
        return view;
    }

    private TextView getNotificationTitle(View layout) {
        return (TextView) layout.findViewById(R.id.notification_name);
    }
    
    public void setTitle(String title) {
        mActionBarElements.setTitle(title);
    }

    @Override
    public BubbleTitleBarElements getBubbleActionBarElements() {
        return mActionBarElements;
    }

    @Override
    public void setFragmentHandler(IBubbleFragmentHandler handler) {
        mHandler = handler;
    }
}
