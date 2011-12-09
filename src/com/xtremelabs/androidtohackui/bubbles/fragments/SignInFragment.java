package com.xtremelabs.androidtohackui.bubbles.fragments;

import com.xtremelabs.androidtohackui.R;
import com.xtremelabs.androidtohackui.draggables.ui.IDraggableFragment;
import com.xtremelabs.androidtohackui.draggables.ui.IDraggableFragmentHandler;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SignInFragment extends Fragment implements IDraggableFragment{
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        return inflater.inflate(R.layout.sign_in_form, container, false);

    }

	@Override
	public void setFragmentHandler(IDraggableFragmentHandler handler) {
		// TODO Auto-generated method stub
		
	}
}
