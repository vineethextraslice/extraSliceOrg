package com.app.extraslice.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.extraslice.MenuActivity;
import com.app.extraslice.R;
import com.app.extraslice.model.Organization;
import com.app.extraslice.utils.ProgressClass;
import com.app.extraslice.utils.Utilities;
import com.extraslice.walknpay.bl.StoreBO;
import com.extraslice.walknpay.bl.UserBO;
import com.extraslice.walknpay.model.UserModel;
import com.extraslice.walknpay.ui.WalletFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
	
	private final String LOAD_SMARTSPACE="LOAD_SMARTSPACE";
	
	public static int count = 0;
	Context context;
	public static View rootView;
	Fragment fragment = null;
	private static String LOAD_RECEIPTS="LOAD_RECEIPTS";
	private static String LOAD_WALKNPAY="LOAD_WALKNPAY";
	Dialog dialog;
	FragmentManager fragmentManager;
	public HomeFragment() {
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		

		context = this.getActivity();
		rootView = inflater.inflate(R.layout.home_screen, container, false);
		
		//new LoadData(LOAD_SMARTSPACE,null).execute();
		LinearLayout reservLyt = (LinearLayout)rootView.findViewById(R.id.reservLyt);
		reservLyt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MenuActivity.menuShowing = true;
				MenuActivity.showHideMenu();
				List<Organization> apprvdOrgList = new ArrayList<Organization>();
				for(Organization org : Utilities.loggedInUser.getOrgList()){
					if(org.isApproved()){
						apprvdOrgList.add(org);
					}
				}
				if(apprvdOrgList.size()==0){
					Toast toast = Toast.makeText(context, "Please update you Company details", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					
				
				}else{
					fragment = new ReserveAConfRoom();//new SelectRoomType();
					Utilities.loadFragment(getFragmentManager(),fragment,R.id.frame_container,false);
				}
				
			}
		});
		
		LinearLayout supportLyt = (LinearLayout)rootView.findViewById(R.id.supportLyt);
		supportLyt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MenuActivity.menuShowing = true;
				MenuActivity.showHideMenu();
				fragment = new TicketDashboardFragment();
				Utilities.loadFragment(getFragmentManager(), fragment, R.id.frame_container, false);
					
				
				
			}
		});
		LinearLayout wnpLyt = (LinearLayout)rootView.findViewById(R.id.wnpLyt);
		wnpLyt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MenuActivity.menuShowing = true;
				MenuActivity.showHideMenu();
				new LoadData(LOAD_WALKNPAY).execute();
			}
		});
		LinearLayout walletLyt = (LinearLayout)rootView.findViewById(R.id.walletLyt);
		walletLyt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MenuActivity.menuShowing = true;
				MenuActivity.showHideMenu();
				new LoadData(LOAD_RECEIPTS).execute();
			}
		});
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					MenuActivity.menuShowing = true;
					MenuActivity.showHideMenu();
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
		outState.putInt("test", 1);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	
	class LoadData extends AsyncTask<Void, Void, Void> {
		
		UserModel userModel ;
		String purpose;
		public LoadData(String purpose){
			this.purpose=purpose;
		}
		@Override
		protected void onPreExecute() {	
			
			ProgressClass.startProgress(context);
			
			super.onPreExecute();

		}
		@Override
		protected Void doInBackground(Void... arg0) {
			if(com.extraslice.walknpay.bl.Utilities.loggedInUser ==null){
				UserBO userBo = new UserBO(context);
				int osVersion = android.os.Build.VERSION.SDK_INT;
				String appVersion = getResources().getString(
						R.string.version);
				com.extraslice.walknpay.bl.Utilities.loggedInUser = userBo.authenticateESliceUser(Utilities.loggedInUser, "Android", osVersion+"", appVersion);
			}
			StoreBO storeBO = new StoreBO(context);
			if(com.extraslice.walknpay.bl.Utilities.storeList == null || com.extraslice.walknpay.bl.Utilities.storeList.size() ==0){
				com.extraslice.walknpay.bl.Utilities.storeList = storeBO.getAllStore(com.extraslice.walknpay.bl.Utilities.STATUS_FILTER_ACTIVE,0,0);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			ProgressClass.finishProgress();
			
				if(com.extraslice.walknpay.bl.Utilities.loggedInUser != null){
					if(purpose.equalsIgnoreCase(LOAD_WALKNPAY)){
						Fragment fragment = new com.extraslice.walknpay.ui.HomeFragment();
						if (fragment != null) {
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
						}
					}else{
						Fragment fragment = new WalletFragment(0);
						Utilities.loadFragment(getFragmentManager(), fragment, R.id.frame_container, false);
					}
						
				}else{
					Toast tst = Toast.makeText(context, "WalkNPay authentication failed", Toast.LENGTH_LONG);
					tst.setGravity(Gravity.CENTER, 0, 0);
					tst.show();
				}
			
			
		}
	}



	
}
