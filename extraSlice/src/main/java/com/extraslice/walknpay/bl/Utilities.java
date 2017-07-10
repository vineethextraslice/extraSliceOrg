package com.extraslice.walknpay.bl;

import java.security.Key;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.R;
import com.extraslice.walknpay.adapter.CartListAdapter;
import com.extraslice.walknpay.model.CustAcctModel;
import com.extraslice.walknpay.model.PurchasedProductModel;
import com.extraslice.walknpay.model.StoreModel;
import com.extraslice.walknpay.model.UserModel;
import com.extraslice.walknpay.ui.CartFragment;
import com.extraslice.walknpay.ui.HomeFragment;
import com.extraslice.walknpay.ui.MenuActivity;

public class Utilities {
	public static StoreModel selectedStore = null;
	public static UserModel loggedInUser = null;
	public static CustAcctModel saveCardList;
	public static String userName = "";
	public static String userPassword = "";
	public static String currentTab = "";
	public static boolean scanHome = false;
	public static final String STATUS_FILTER_ACTIVE = "ACTIVE";
	public static final String STATUS_FILTER_INACTIVE = "INACTIVE";
	public static final String STATUS_FILTER_ALL = "ALL";
	public static final String ERROR_MESSAGE = "ERRORMESSAGE";
	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String STATUS_FAILED = "FAILED";
	public static final String STATUS_STRING = "STATUS";
	public static final String WARNING_MESAGE = "WARNING";
	public static int CAMERA_ID = 0;
	static Dialog dialog;
	public static FragmentManager fragmentManager;
	public static Set<String> selectedCoupons =new HashSet<String>();
	public static double offerAmount =0;
	public static Fragment fragment;
	public static double userLatitude = 0.00;
	public static double userLongitude = 0.00;

	//public static String mainUrl = "http://walknpaydev01.cloudapp.net:8080/WalkNPayWebService/jsonws";
	public static String mainUrl = "https://extraslice.com/WalkNPayWebService/jsonws";
	
	public static String WARNING_FROM_SERVER = null;
	public static long billNo = 100;
	public static String statusValue = null;
	public static Context currentContext = null;
	public static List<StoreModel> storeList;
	public static double total = 0.00;
	public static double rewards = 0.00;
	public static String storeId = "1";
	public static int posReceipts;
	public static int posReceiptsnew;
	public static int storeSelectionproduct = 0;
	public static int loginUserID;

	public static String card;
	public static String errorMessage = null;;
	public static String errorMessagenew = null;
	public static String errorMessageLogin = null;
	public static String card_holder = "sam";
	public static String expiry_date;
	public static String store = "ABC STORE, Kirkland , US";
	public static boolean islogged = false;
	public static boolean confirm_store = false;
	public static boolean pass_wallet = true;
	public static boolean firstrun = false;
	public static String store_info = "";
	public static boolean pay_finish = false;
	public static boolean flag = false;
	public static String password = "sam";
	public static String lastAction = "HOME";
	public static String currentFrag = null;
	public static String FROM_EMAIL = "cc.smartstore@gmail.com";
	public static String FROM_EMAIL_PASSWORD = "Slicer@001";
	Typeface tfTangerine;
	public static String SCAN_FOR = "PURCHASE";
	public static final String SCAN_FOR_PURCHASE = "PURCHASE";
	public static final String SCAN_FOR_STORE = "STORE";
	public static final String SCAN_FOR_PAYPAL = "PAYPAL";
	public static final String SCAN_FOR_WLT_PRE_RECH = "SCAN_FOR_WLT_PRE_RECH";
	public static final String SCAN_FOR_WNP_PAY = "SCAN_FOR_WNP_PAY";
	public static final String SCAN_FOR_WLT_CARD = "SCAN_FOR_WLT_CARD";
	
	public static String classname = "";

	public static String total1 = "00.00";
	public static String subtotal_amt = "00.00";
	public static String Tax1 = "00.00";

	private static final String ALGORITHM = "AES";
	private static final byte[] keyValue = new byte[] { 'S', 'e', 'c', 'K', 'e', 'y', '@', 'S', 'l', 'i', 'c', 'e', '4', 'W', 'n', 'P' };

	public static String encode(String valueToEnc) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encValue = c.doFinal(valueToEnc.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encValue);
		return encryptedValue;
	}

	public static String decode(String encryptedValue) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGORITHM);
		return key;
	}

	public static String getUsername(Context context) {
		/*
		 * String value = "No"; SharedPreferences prefs =
		 * context.getSharedPreferences("pref", Context.MODE_PRIVATE); value =
		 * prefs.getString("Username", "");
		 */
		return Utilities.userName;
	}

	public static String getPassword(Context context) {
		/*
		 * String value = "No"; SharedPreferences prefs =
		 * context.getSharedPreferences("pref", Context.MODE_PRIVATE); value =
		 * prefs.getString("Password", "");
		 */
		return Utilities.userPassword;
	}

	/** saving login Credentials in sharedpreferences */
	public static void setCredentials(String username, String password, Context context) {
		SharedPreferences prefs = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = prefs.edit();

		prefsEditor.putString("Username", username);
		prefsEditor.putString("Password", password);

		prefsEditor.commit();
	}

	public static Typeface getStyleTangerine(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "tangerinebold.ttf");
	}

	public static Typeface getStyleNyala(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "nyala.ttf");
	}

	public static String roundto2Decimal(double amount) {
		String v = String.valueOf(new DecimalFormat("##.##").format(amount));
		if (v.indexOf(".") >= 0) {
			String v1 = "00";
			if (v.indexOf(".") > 0) {
				v1 = v.substring(0, v.indexOf("."));
			}
			String v2 = v.substring((v.indexOf(".") + 1), v.length());
			if (v1.length() < 2) {
				v1 = 0 + v1;
			}
			if (v2.length() < 2) {
				v2 = v2 + 0;
			}
			v = v1 + "." + v2;
		}
		return v;
	}

	public static double roundto2Decimaldouble(double amount) {
		String v = String.valueOf(new DecimalFormat("##.##").format(amount));
		if (v.indexOf(".") >= 0) {
			String v1 = "00";
			if (v.indexOf(".") > 0) {
				v1 = v.substring(0, v.indexOf("."));
			}
			String v2 = v.substring((v.indexOf(".") + 1), v.length());
			if (v1.length() < 2) {
				v1 = 0 + v1;
			}
			if (v2.length() < 2) {
				v2 = v2 + 0;
			}
			v = v1 + "." + v2;
		}
		return Double.parseDouble(v);
	}
	public static void hideVirtualKeyBoard(Context context, EditText edtTxtView) {
		try {
			InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

			if (inputManager.isActive()) {
				InputMethodManager imanager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imanager.hideSoftInputFromWindow(edtTxtView.getWindowToken(), 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showVirtualKeyBoard(Context context, EditText edtTxtView) {
		try {
			InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

			if (inputManager.isActive()) {
				InputMethodManager imanager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imanager.showSoftInput(edtTxtView, InputMethodManager.SHOW_FORCED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean haveInterenetConnection(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert);
		dialog.setTitle("Error");
		dialog.show();

		// set the custom dialog components - text, image and
		// button
		TextView text = (TextView) dialog.findViewById(R.id.content);
		text.setText("Please check your internet Connection!");

		Button dialogButton = (Button) dialog.findViewById(R.id.alertpositivebutton);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});
		return false;

	}

	public static boolean checkNumeric(Context context, String value) {
		String regex = "[0-9]+";
		if (value.matches(regex)) {
			return true;
		}
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert);
		dialog.setTitle("Error");
		dialog.show();

		// set the custom dialog components - text, image and
		// button
		TextView text = (TextView) dialog.findViewById(R.id.content);
		text.setText("Please enter numeric value!");

		Button dialogButton = (Button) dialog.findViewById(R.id.alertpositivebutton);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});
		return false;

	}

	public static boolean checkEmail(Context context, String value, boolean showPopoup) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		if (value.matches(regex)) {
			return true;
		}
		if (showPopoup) {
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.alert);
			dialog.setTitle("Error");
			dialog.show();

			// set the custom dialog components - text, image and
			// button
			TextView text = (TextView) dialog.findViewById(R.id.content);
			text.setText("Please enter valid email");

			Button dialogButton = (Button) dialog.findViewById(R.id.alertpositivebutton);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					dialog.dismiss();
				}
			});
		}
		return false;

	}

	public static boolean checkDouble(Context context, String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (Exception e) {
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.alert);
			dialog.setTitle("Error");
			dialog.show();

			// set the custom dialog components - text, image and
			// button
			TextView text = (TextView) dialog.findViewById(R.id.content);
			text.setText("Please enter decimal value!");

			Button dialogButton = (Button) dialog.findViewById(R.id.alertpositivebutton);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					dialog.dismiss();
				}
			});
			return false;
		}

	}

	public static boolean checkInteger(Context context, String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.alert);
			dialog.setTitle("Error");
			dialog.show();

			// set the custom dialog components - text, image and
			// button
			TextView text = (TextView) dialog.findViewById(R.id.content);
			text.setText("Please enter integer value!");

			Button dialogButton = (Button) dialog.findViewById(R.id.alertpositivebutton);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					dialog.dismiss();
				}
			});
			return false;
		}

	}

	public static boolean checkInternetConnection(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static void showInternetStatus(Context context, boolean value) {
		if (value == true) {

		} else {
			Toast.makeText(context, "Sorry, no internet connection", Toast.LENGTH_SHORT).show();
		}
	}
	
	/*public static boolean selectStorePopup(Context mContext,FragmentManager frgMngr){
		Fragment fragment = new CartFragment();
		return selectStorePopup(mContext, frgMngr, fragment);
		
	}*/
	public static void selectStorePopup(Context mContext,FragmentManager frgMngr,Fragment fragment){

		Utilities.fragmentManager = frgMngr;
		Utilities.fragment = fragment;
		dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.selectstore);
		Spinner selectStoreList = (Spinner)dialog.findViewById(R.id.selectStoreList);
		ArrayAdapter<String> listAdapter;
		final List<String> list = new ArrayList<String>();
		list.add("Select a store");
		int selectedPosition =0;
		for (int i = 0; i < Utilities.storeList.size(); i++) {
			String storeName = Utilities.storeList.get(i).getName();
			list.add(storeName);
			if(Utilities.selectedStore!= null && Utilities.selectedStore .getStoreId() == Utilities.storeList.get(i).getStoreId()){
				selectedPosition =i+1;
			}
		}	
		listAdapter = new ArrayAdapter<String>(mContext, R.layout.wnp_spinner_item, list);
		selectStoreList.setAdapter(listAdapter);
		selectStoreList.setSelection(selectedPosition);
		selectStoreList.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
				
				if(position==0)
				{
					CartFragment.productslist.clear();
					Utilities.setTotalCount();
					Utilities.selectedStore=null;
					Utilities.confirm_store = false;
					Utilities.scanHome = false;
				}else if(Utilities.selectedStore!= null && Utilities.selectedStore .getStoreId() != Utilities.storeList.get(position-1).getStoreId()){
					CartFragment.productslist.clear();
					Utilities.setTotalCount();
					Utilities.selectedStore = Utilities.storeList.get(position-1);
					MenuActivity.currencySymbol = Utilities.selectedStore.getCurrencySymbol();
					if(MenuActivity.currencySymbol != null && MenuActivity.currencySymbol.trim().equalsIgnoreCase("INR")){
						MenuActivity.currencySymbol= "\u20B9";
					}
					if(MenuActivity.currencySymbol == null || MenuActivity.currencySymbol.trim().isEmpty()){
						MenuActivity.currencySymbol="$";
					}
					MenuActivity.currencySymbol = MenuActivity.currencySymbol+" ";
				}else{
					Utilities.selectedStore = Utilities.storeList.get(position-1);	
					MenuActivity.currencySymbol = Utilities.selectedStore.getCurrencySymbol();
					if(MenuActivity.currencySymbol != null && MenuActivity.currencySymbol.trim().equalsIgnoreCase("INR")){
						MenuActivity.currencySymbol= "\u20B9";
					}
					if(MenuActivity.currencySymbol == null || MenuActivity.currencySymbol.trim().isEmpty()){
						MenuActivity.currencySymbol="$";
					}
					MenuActivity.currencySymbol = MenuActivity.currencySymbol+" ";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});
		Button goButton = (Button) dialog.findViewById(R.id.btn_go );
		Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);
		goButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Utilities.selectedStore!=null){
					Utilities.confirm_store = true;
					Utilities.scanHome = true;
					if (fragmentManager != null && Utilities.fragment != null) {
						fragmentManager.beginTransaction().replace(R.id.frame_container, Utilities.fragment).addToBackStack(null).commit();
					}
				}
				dialog.dismiss();
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});

				dialog.show();

			
	
	}

	public static void setTotalCount(){
		int count = 0;
		if(CartFragment.productslist != null && CartFragment.productslist.size() > 0){
			for (PurchasedProductModel product :CartFragment.productslist){
				count =count+ (int) product.getPurchasedQuantity();
			}
		}
		HomeFragment.itemNum.setText(count+"");
	}
}
