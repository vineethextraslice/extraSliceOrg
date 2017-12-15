package com.extraslice.walknpay.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.StoreBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.ProductModel;
import com.extraslice.walknpay.model.StoreModel;

public class HomeFragment extends Fragment implements OnClickListener {
	/* Button bYes,bNo; */
	static RadioGroup radioPayGrp;
	static Dialog payDialog;
	Fragment fragment = null;
	ImageButton bcross;
	RelativeLayout relative2;
	TableLayout t1;
	ProductModel products;
	int flag = 0;
	HorizontalScrollView hz, hz1;
	
	StoreModel storeModel = null;
	TextView tv, tv_thanks, welcomeText;
	/* public static TextView tv_storename; */
	TextView tv_restmsg, tstoretanks, mGallery, logedat;
	Button exitView;
	ImageView img_associate, img_arrow, store_name, scanImage, payment, changeStoreImg;
	static EditText change_store;
	Dialog dialog;
	public static int count = 0;
	Double price_tmp;
	static Context context;
	public static Activity a;
	String scanContent, scanFormat;
	String Username;
	public static TextView itemNum;
	RelativeLayout itemNumLinear;
	LinearLayout layout11,layout12,layout21,layout22,layout31,layout32;
	LinearLayout layout1,layout2,layout3;
	//View selectedStoreMain, homeMain;
	View homeMain;
	Spinner selectStoreList;
	Button btnGo, btnCancel;

	public HomeFragment() {
		Utilities.lastAction = "HOME";
		Utilities.currentFrag = "HOME";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Utilities.lastAction = "HOME";
		Utilities.currentFrag = "HOME";
		View rootView = inflater.inflate(R.layout.wnp_home_1, container, false);
		Username = Utilities.getUsername(getActivity());
		context = this.getActivity();
		scanImage = (ImageView) rootView.findViewById(R.id.scanButton);
		payment = (ImageView) rootView.findViewById(R.id.payment);
		
		layout11 = (LinearLayout) rootView.findViewById(R.id.layout11);
		layout12 = (LinearLayout) rootView.findViewById(R.id.layout12);
		layout21 = (LinearLayout) rootView.findViewById(R.id.layout21);
		layout22 = (LinearLayout) rootView.findViewById(R.id.layout22);
		
		layout1 = (LinearLayout) rootView.findViewById(R.id.layout1);
		layout2 = (LinearLayout) rootView.findViewById(R.id.layout2);
		layout3 = (LinearLayout) rootView.findViewById(R.id.layout3);
		
		if (Utilities.storeList != null && Utilities.storeList.size() == 1) {
			layout12.setVisibility(View.VISIBLE);
			layout11.setVisibility(View.GONE);
			layout21.setVisibility(View.GONE);
			layout2.setWeightSum(1);
			
		} else if (Utilities.storeList != null && Utilities.storeList.size() > 1) {
			layout12.setVisibility(View.GONE);
			layout11.setVisibility(View.VISIBLE);
			layout21.setVisibility(View.VISIBLE);
			layout2.setWeightSum(2);
		}

		changeStoreImg = (ImageView) rootView.findViewById(R.id.store);
		itemNum = (TextView) rootView.findViewById(R.id.checkouttext);
		selectStoreList = (Spinner) rootView.findViewById(R.id.selectStoreList);
		homeMain = (View) rootView.findViewById(R.id.homeMain);
		btnGo = (Button) rootView.findViewById(R.id.btn_go);
		btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
		if (CartFragment.productslist != null && CartFragment.productslist.size() > 0) {
			int test = 0;
			for (int i = 0; i < CartFragment.productslist.size(); i++) {
				test += (int) CartFragment.productslist.get(i).getPurchasedQuantity();
			}
			String itemNos = String.valueOf(test);
			itemNum.setText(itemNos);
		} else {
			itemNum.setText("0");
		}
		a = (Activity) getActivity();
		scanImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Utilities.confirm_store == false) {
					Utilities.scanHome = true;
					new LoadData(new CartFragment()).execute();
				} else {
					Utilities.scanHome = true;
					Fragment fragment = null;
					fragment = new CartFragment();
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

					}
				}
			}
		});
		changeStoreImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
					new LoadData(null).execute();
			}
		});
		payment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CartFragment.productslist != null && CartFragment.productslist.size() > 0) {
					fragment = new PaymentOptionScreen();
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
				} else {
					Toast.makeText(getActivity(), "No items added yet", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		layout12.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Utilities.confirm_store == false) {
					Utilities.scanHome = true;
					new LoadData(new CartFragment()).execute();
				} else {
					Utilities.scanHome = true;
					Fragment fragment = null;
					fragment = new CartFragment();
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

					}
				}
			}
		});
		layout22.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Fragment fragment = null;
				fragment = new CartFragment();
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
				}
			}
		});

		
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Fragment fragment = null;
					fragment = new HomeFragment();
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
					}
					return true;
				} else {
					return false;
				}
			}
		});
		return rootView;
	}

	@Override
	public void onPause() {
		Utilities.classname = this.getClass().getName();
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Utilities.confirm_store == true) {
			scanImage.setEnabled(true);
			payment.setEnabled(true);
			layout22.setEnabled(true);
			layout12.setEnabled(true);
			
		}
		if (Utilities.flag == true) {
			tstoretanks.setVisibility(View.GONE);
		}
	}

	class LoadData extends AsyncTask<Void, Void, Void> {
		Fragment fragment;
		public LoadData(Fragment fragment){
			this.fragment = fragment;
		}
		
		
		@Override
		protected void onPreExecute() {	
			
			ProgressClass.startProgress(context);
			
			super.onPreExecute();

		}
		@Override
		protected Void doInBackground(Void... arg0) {
			StoreBO storeBO = new StoreBO(context);
			if(Utilities.storeList == null || Utilities.storeList.size() ==0){
				Utilities.storeList = storeBO.getAllStore(Utilities.STATUS_FILTER_ACTIVE,Utilities.userLatitude,Utilities.userLongitude);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			ProgressClass.finishProgress();
			if (Utilities.storeList != null && Utilities.storeList.size() == 1) {
				layout12.setVisibility(View.VISIBLE);
				layout11.setVisibility(View.GONE);
				layout21.setVisibility(View.GONE);
				layout2.setWeightSum(1);
				
			} else if (Utilities.storeList != null && Utilities.storeList.size() > 1) {
				layout12.setVisibility(View.GONE);
				layout11.setVisibility(View.VISIBLE);
				layout21.setVisibility(View.VISIBLE);
				layout2.setWeightSum(2);
			}
			if (Utilities.storeList != null && Utilities.storeList.size() == 1) {
				Utilities.selectedStore = Utilities.storeList.get(0);
				MenuActivity.currencySymbol = Utilities.selectedStore.getCurrencySymbol();
				if(MenuActivity.currencySymbol == null || MenuActivity.currencySymbol.trim().isEmpty()){
					MenuActivity.currencySymbol="$";
				}
				MenuActivity.currencySymbol = MenuActivity.currencySymbol +" ";
				Utilities.scanHome = true;
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
				}
			} else if (Utilities.storeList != null && Utilities.storeList.size() > 0) {
				Utilities.selectStorePopup(context, getFragmentManager(),fragment);
			}
			
		}
	}

	
}
