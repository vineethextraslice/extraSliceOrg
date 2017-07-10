package com.extraslice.walknpay.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.R;
import com.extraslice.walknpay.adapter.CartListAdapter2;
import com.extraslice.walknpay.adapter.CartListAdapter3;
import com.extraslice.walknpay.bl.StoreBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.ProductModel;
import com.extraslice.walknpay.model.PurchasedProductModel;
import com.extraslice.walknpay.model.StoreModel;
import com.paypal.android.sdk.E;

public class CartFragment3 extends Fragment {

	public static ArrayList<PurchasedProductModel> productslist = new ArrayList<PurchasedProductModel>();
	Fragment fragment = null;
	ProductModel products;
	static FragmentManager fragmentManager;
	//public static ImageView checkout_button;
	StoreModel storeModel = null;
	TextView checkoutTextView;
	int flag = 0;
	static int storeId = 1;
	public static Context context;
	public static Button ok, dismiss;
	Dialog dialog;
	ListView cartlist;


	public CartFragment3() {
		Utilities.lastAction = "CART";
		Utilities.SCAN_FOR = Utilities.SCAN_FOR_PURCHASE;
	}

	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Utilities.lastAction = "CART";
		rootView = inflater.inflate(R.layout.cart_tab4, container, false);
		/*checkout_button = (ImageView) rootView.findViewById(R.id.checkoutbutton);
		checkout_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				checkOut();

			}
		});*/
		context = this.getActivity();
		checkoutTextView = (TextView) rootView.findViewById(R.id.checkoutbuttontext);
		dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		cartlist = (ListView) rootView.findViewById(R.id.lbllist);
		List<ProductModel> ss = new ArrayList<ProductModel>();
		ProductModel mdl = new ProductModel();
		mdl.setName("Half Girlfriend by Chetan Bhagat Book ");
		mdl.setPrice(99);
		ss.add(mdl);	
		
		mdl = new ProductModel();
		mdl.setName("The Facebook Advertising Formula ");
		mdl.setPrice(25);
		ss.add(mdl);	
		
		mdl = new ProductModel();
		mdl.setName("Blogging Profit Formula");
		mdl.setPrice(25);
		ss.add(mdl);	
		
		mdl = new ProductModel();
		mdl.setName("2 STATES by Chetan Bhagat");
		mdl.setPrice(174);
		ss.add(mdl);	
		
		mdl = new ProductModel();
		mdl.setName("Diary of a Wimpy Kid: The Long Haul (Book 9) ");
		mdl.setPrice(379);
		ss.add(mdl);	
		
		mdl = new ProductModel();
		mdl.setName("HORROR OF THE BLACK RING, BRAIN JUICE AND DANGER TIME BOOKS SET OF 3");
		mdl.setPrice(399);
		ss.add(mdl);	
		
		mdl = new ProductModel();
		mdl.setName("Guinness World Records (2002 Edition)");
		mdl.setPrice(650);
		ss.add(mdl);	
		
		
		CartListAdapter3 adp = new CartListAdapter3(context, R.layout.product_rows2, ss);
		//checkout_button.setClickable(true);
		fragmentManager = getFragmentManager();

		cartlist.setAdapter(adp);

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	
	private void checkOut() {
		// TODO Auto-generated method stub

		if (productslist != null && productslist.size() > 0) {
			//checkout_button.setClickable(false);
			fragment = new PaymentOptionScreen();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
		} else {
			Toast toast = Toast.makeText(context, "No items added to cart!", Toast.LENGTH_SHORT);
			toast.show();
		}

		// }

	}


	
	
	/*private void scrollMyListViewToBottom() {
		cartlist.post(new Runnable() {
			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				cartlist.setSelection(cartlistadapter.getCount() - 1);
			}
		});
	}
*/
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

		}
		return true;
	}

	
	
	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	class StoreBG extends AsyncTask<Void, Void, Void> {
		boolean selectStore ;
		public StoreBG(boolean selectStore) {
			this.selectStore=selectStore;
		}
		
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(context);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			StoreBO storeBO = new StoreBO(context);
			if(selectStore){
				if(Utilities.storeList == null || Utilities.storeList.size() ==0){
					Utilities.storeList = storeBO.getAllStore(Utilities.STATUS_FILTER_ACTIVE,Utilities.userLatitude,Utilities.userLongitude);
				}
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(selectStore){
				Fragment fragment = new CartFragment3();
				if (Utilities.storeList != null && Utilities.storeList.size() == 1) {
					Utilities.selectedStore = Utilities.storeList.get(0);
					MenuActivity.currencySymbol = Utilities.selectedStore.getCurrencySymbol();
					if(MenuActivity.currencySymbol != null && MenuActivity.currencySymbol.trim().equalsIgnoreCase("INR")){
						MenuActivity.currencySymbol= "\u20B9";
					}
					if(MenuActivity.currencySymbol == null || MenuActivity.currencySymbol.trim().isEmpty()){
						MenuActivity.currencySymbol="$";
					}
					MenuActivity.currencySymbol = MenuActivity.currencySymbol+" ";
					Utilities.scanHome = true;
					Utilities.confirm_store =true;
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
					}
				} else if (Utilities.storeList != null && Utilities.storeList.size() > 0) {
					Utilities.selectStorePopup(context, getFragmentManager(), fragment);
				}
			}
			ProgressClass.finishProgress();
		}
	}
	
}