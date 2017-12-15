package com.app.extraslice.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.extraslice.R;
import com.app.extraslice.utils.Utilities;

public class SelectRoomType extends Fragment {
	
	
	
	Context context;
	public static View rootView;
	Fragment fragment = null;
	TextView confRoom,trainingRoom,eventRoom;
	
	public SelectRoomType() {
	}
	//    unsubscribeLyt
	//confRoomScrView""plnResLyt"""
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context 						= this.getActivity();
		rootView 						= inflater.inflate(R.layout.select_room_type, container, false);
		
		confRoom     				= (TextView)rootView.findViewById(R.id.confRoom);
		trainingRoom     				= (TextView)rootView.findViewById(R.id.trainingRoom);
		eventRoom     				= (TextView)rootView.findViewById(R.id.eventRoom);
		
		confRoom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Fragment fragment = new ReserveAConfRoom();
				Utilities.loadFragment(getFragmentManager(), fragment, R.id.frame_container, false);
			}
		});
		
		trainingRoom.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View arg0) {
				Fragment fragment = new ReserveAConfRoom();
				Utilities.loadFragment(getFragmentManager(), fragment, R.id.frame_container, false);
			}
		});
		eventRoom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Fragment fragment = new ReserveAConfRoom();
				Utilities.loadFragment(getFragmentManager(), fragment, R.id.frame_container, false);
			}
		});
		
		
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {

					/*Fragment fragment = null;
					fragment = new HomeFragment();
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						//fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
						Utilities.loadFragment(context,fragmentManager,fragment,R.id.frame_container,true);
					}*/
					return true;
				} else {
					return true;
				}
			}
		});
		
		return rootView;
	}
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//inilize();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}






	
}
