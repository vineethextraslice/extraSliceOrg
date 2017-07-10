package com.extraslice.walknpay.ui;

import java.util.ArrayList;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.LoginActivity;
import com.app.extraslice.R;
import com.extraslice.walknpay.adapter.NavDrawerListAdapter;
import com.extraslice.walknpay.bl.CustSupportBO;
import com.extraslice.walknpay.bl.StoreBO;
import com.extraslice.walknpay.bl.UserBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.NavDrawerItem;
import com.extraslice.walknpay.model.StoreModel;
import com.extraslice.walknpay.ui.transaction.PaypalPaymentGateway;

public class MenuActivity extends Activity implements OnClickListener {

	public ActionBarDrawerToggle mDrawerToggle;
	// ActionBar mactionBar;
	// nav drawer title
	private CharSequence mDrawerTitle;
	GPSTracker gps;
	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	public static TextView itemCountView;
	PopupWindow popUp;
	FrameLayout layout;
	LayoutParams params;
	Fragment fragment;
	FragmentManager fragmentManager;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	Dialog dialog;
	EditText pswrdcnfm, storename;
	String Password, actionbartitle;
	TextView myText,profileName;
	ActionBar mactionBar;

	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	LinearLayout drawerLinear,profileLyt;
	Context context;
	static ActionBar actionBar;

	View homeMain;
	Spinner selectStoreList;
	Button btnGo, btnCancel;
	EditText newPass, newConfPass;
	Button tempPassContinue;
	
	LinearLayout errortemp;
	TextView errorText;
	public static String currencySymbol ="$";
	double userLatitude=0.00 ,userLongitude=0.00;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = this;

		drawerLinear = (LinearLayout) findViewById(R.id.drawer222);
		profileLyt = (LinearLayout) findViewById(R.id.drawer444); 
		/*profileEmail = (TextView) findViewById(R.id.userProfileEmail);*/
		profileName = (TextView) findViewById(R.id.userProfileName);
		profileLyt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mDrawerLayout.closeDrawer(drawerLinear);
				fragment = new ProfileFragment(Utilities.loggedInUser);
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
				
			}
		});
		boolean is_tempPassword = Utilities.loggedInUser.getUsingTempPwd();
		 gps = new GPSTracker(MenuActivity.this);
	       

			// check if GPS enabled		
	        if(gps.canGetLocation()){
	        	
	        	userLatitude = gps.getLatitude();
	        	 userLongitude = gps.getLongitude();
	        	if(userLatitude==0.00&&userLongitude==0.00)
	        	{
	        		 gps = new GPSTracker(MenuActivity.this);
	        		 
	        	}
	        	else if(userLatitude!=0.00&&userLongitude!=0.00){
	        		Utilities.userLatitude = gps.getLatitude();
	        		 Utilities.userLongitude = gps.getLongitude();
	        		
	        		 
	        		 
	        		/* AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
	        	   	
	        	        alertDialog.setTitle("GPS ");
	        	 
	        	        alertDialog.setMessage("lat "+Utilities.userLatitude+"user long"+Utilities.userLongitude);
	        	 
	        	        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
	        	            public void onClick(DialogInterface dialog,int which) {
	        	            	 dialog.cancel();
	        	            }
	        	        });
	        	 
	        	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        	            public void onClick(DialogInterface dialog, int which) {
	        	            dialog.cancel();
	        	            }
	        	        });
	        	       // alertDialog.show();
*/	        	        
		             //Call the api for sending the location of user to store table to fetch all neary stores
	        	        
	        	        UserBO userBo = new UserBO(context);
	        	       // Log.e("ID", ""+Utilities.loggedInUser.getUserId());
	        	        new userLocationSend().execute();
	        	        
	        	      /*  if(Utilities.loggedInUser.getUserId()!=0){
	        	        	
	        	     
	        	  //  rootObj   = userBo.updateLocationLoggedInUser(Utilities.loggedInUser.getUserId(),Utilities.userLatitude,Utilities.userLongitude);
	        	  //  alertDialog.setMessage("ROOTObj= "+rootObj);
	        	   // alertDialog.show();
	        	        }*/
	        	}
	        	//Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();	
	        }else{
	        	// can't get location
	        	// GPS or Network is not enabled
	        	// Ask user to enable GPS/network in settings
	        	gps.showSettingsAlert();
	        }
		if(is_tempPassword)
		{

			final Dialog dialog1;
			dialog1 = new Dialog(context);
			dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog1.setContentView(R.layout.changepassword);
			dialog1.setCancelable(false); 
			
			newPass = (EditText) dialog1.findViewById(R.id.newPass);
			newConfPass= (EditText) dialog1.findViewById(R.id.confNewPass);
			Button tempPassContinue = (Button) dialog1.findViewById(R.id.contTempPass);
			errortemp = (LinearLayout) dialog1.findViewById(R.id.tempMsg);
			errorText = (TextView) dialog1.findViewById(R.id.errorPasstext);
			tempPassContinue.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
						String newPassfileld = newPass.getText().toString();
						String confPassFiled = newConfPass.getText().toString();
						if(newPassfileld==null || newPassfileld.length()==0){
							errortemp.setVisibility(View.VISIBLE);
							errorText.setText("Please enter your new password");
						}else if(confPassFiled==null || confPassFiled.length()==0){
							errortemp.setVisibility(View.VISIBLE);
							errorText.setText("Please enter your confirm password");
						}else if(newPassfileld.equals(confPassFiled)){
							try {
								Utilities.loggedInUser.setPassword(Utilities.encode(newPassfileld));
							} catch (Exception e) {
								e.printStackTrace();
							}
							new UserDataBG(true, dialog1,null,null).execute();
						}else{
							errortemp.setVisibility(View.VISIBLE);
							errorText.setText("Password and Confirm password doesn't match");
						}
					}
			});
			//dialog1.show();
		}
		
		
		actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		selectStoreList = (Spinner) findViewById(R.id.selectStoreList);
		btnGo = (Button) findViewById(R.id.btn_go);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		navMenuTitles = getResources().getStringArray(R.array.wnp_drawer_items);
		mTitle = mDrawerTitle = getTitle();

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.wnp_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();
		if (Utilities.loggedInUser == null) {
			String name = savedInstanceState.getString("userName");
			String userpass = savedInstanceState.getString("userPass");
	       new UserDataBG(false,null,name,userpass).execute();
		} 
		profileName.setText(Utilities.loggedInUser.getUserName());
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
		mDrawerList.setAdapter(adapter);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer2, // nav																		// icon
			R.string.app_name, // nav drawer open - description for accessibility
			R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle("WalkNPay");
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("WalkNPay");
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		ActionBar ab = getActionBar();
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.act_title, null);
		v.setPadding(0, 0, 0, 0);
		itemCountView = (TextView)v.findViewById(R.id.count);
		FrameLayout checkoutframe =  (FrameLayout)v.findViewById(R.id.checkoutframe);
		checkoutframe.setClickable(true);
		checkoutframe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				displayView(4);
				
			}
		});
		Utilities.setTotalCount();
		ab.setCustomView(v);
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}
	class userLocationSend extends AsyncTask<Void, Void, Void> {
		 JSONObject rootObj= null;
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			 UserBO userBo = new UserBO(context);
			
 	      
 	        
 	        if(Utilities.loggedInUser.getUserId()!=0){
 	        	
 	     
 	    rootObj   = userBo.updateLocationLoggedInUser(Utilities.loggedInUser.getUserId(),Utilities.userLatitude,Utilities.userLongitude);
 	   
 	        }
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			/* AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
    	   	 
 	        // Setting Dialog Title
 	        alertDialog.setTitle("GPS ");
 	 
 	        // Setting Dialog Message
 	        alertDialog.setMessage("lat "+Utilities.userLatitude+"user long"+Utilities.userLongitude);
 	 
 	        // On pressing Settings button
 	        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
 	            public void onClick(DialogInterface dialog,int which) {
 	            	 dialog.cancel();
 	            }
 	        });
 	 
 	        // on pressing cancel button
 	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
 	            public void onClick(DialogInterface dialog, int which) {
 	            dialog.cancel();
 	            }
 	        });
 	       alertDialog.setMessage("ROOTObj= "+rootObj);*/
 	       try {
			if(rootObj.getString("STATUS").equalsIgnoreCase("SUCCESS")){
			//	alertDialog.show();
			   }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	 	   
		}
		
	}
	protected void logoutFunction() {
		final Context context = MenuActivity.this;
		dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.wnp_custom_alert);
		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.title_view);
		text.setText("Do you want to logout?");
		Button dialogButton = (Button) dialog.findViewById(R.id.yes_button);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
				startActivity(intent);
				String Username = "";
				String password = "";
				Utilities.setCredentials(Username, password, context);
				Utilities.selectedStore=null;
				Utilities.storeList = null;
				Utilities.pay_finish = false;

				Utilities.confirm_store = false;
				if (CartFragment.tvtax_amt != null) {
					CartFragment.tvtax_amt.setText("00.00");
				}
				if (CartFragment.tv_subtotal != null) {
					CartFragment.tv_subtotal.setText("00.00");
				}
				if (CartFragment.tvtotal_amt != null) {
					CartFragment.tvtotal_amt.setText("00.00");
				}
				if (CartFragment.cartlistadapter != null) {
					CartFragment.cartlistadapter.clear();
				}

				Utilities.flag = false;
				finish();
			}
		});
		Button negativebutton = (Button) dialog.findViewById(R.id.no_button);
		// if button is clicked, close the custom dialog
		negativebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {

		case R.id.home:

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString("userName", Utilities.userName);
		savedInstanceState.putString("userPass", Utilities.userPassword);

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		String userName = savedInstanceState.getString("userName");
		String userpass = savedInstanceState.getString("userPass");
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		if (Utilities.currentTab != null && Utilities.currentTab.equals("CHAT") && ChatFragment.userModel != null && ChatFragment.userModel.getOfficerId() > -1) {
			new SupportBG().execute();
			Utilities.currentTab = "";
			// ChatFragment.userModel = null;
		}
		switch (position) {
			case 0:
				//fragment = new CartFragment3();
				fragment = new HomeFragment();
				break;
			case 1:
				if (Utilities.confirm_store == false) {
					Utilities.scanHome = true;
					fragment = new CartFragment();
					new StoreBG(getFragmentManager(),fragment,userLatitude,userLongitude).execute();
					fragment = null;
				} else {
					Utilities.scanHome = true;
					fragment = new CartFragment();
				}
				break;
			case 2:
				Utilities.scanHome = false;
				fragment = new CartFragment();
			
				break;
			case 3:
				fragment = new WalletFragment(0,0,0,0);
				break;
			case 4:
				if (CartFragment.productslist != null && CartFragment.productslist.size() > 0) {
					fragment = new PaymentOptionScreen();
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
				} else {
					Toast.makeText(context, "No items added yet", Toast.LENGTH_SHORT).show();
				}
				break;
			case 5:
				new StoreBG(null,null,userLatitude,userLongitude).execute();
				fragment = null;
				break;
			case 6:
				try {
					if (Utilities.confirm_store == false) {
						Utilities.scanHome = true;
						fragment = new ChatFragment(Utilities.loggedInUser.getUserName(), Utilities.decode(Utilities.loggedInUser.getPassword()));
						new StoreBG(getFragmentManager(),fragment,userLatitude,userLongitude).execute();
						fragment = null;
					} else {
						Utilities.scanHome = true;
						fragment = new ChatFragment(Utilities.loggedInUser.getUserName(), Utilities.decode(Utilities.loggedInUser.getPassword()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 7:
				fragment = new AboutFragment();
				break;
			case 8:
				logoutFunction();
				break;
			default:
				break;
		}

		if (fragment != null) {
			fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle("WalkNPay");
			mDrawerLayout.closeDrawer(drawerLinear);
			} else {
				mDrawerLayout.closeDrawer(drawerLinear);
			}
	}

	/*private void changeStore() {
		StoreBO storeBO = new StoreBO(context);
		Utilities.storeList = storeBO.getAllStore(Utilities.STATUS_FILTER_ACTIVE);
		if (Utilities.storeList != null && Utilities.storeList.size() > 1) {
			Utilities.selectStorePopup(context, null, null);
		}
		if (Utilities.storeList != null && Utilities.storeList.size() == 1) {
			Toast.makeText(context, "Only one store is available now.", Toast.LENGTH_SHORT).show();
		}
	}*/

	
	

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Fragment fragment = null;
		if (Utilities.SCAN_FOR.equals(Utilities.SCAN_FOR_PURCHASE)) {
			fragment = new CartFragment();
			fragment.onActivityResult(requestCode, resultCode, data);
		}
		if (Utilities.SCAN_FOR.equals(Utilities.SCAN_FOR_PAYPAL)) {
			PaypalPaymentGateway paypal = new PaypalPaymentGateway();
			paypal.onActivityResult(requestCode, resultCode, data);
		}
		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
	}

	@Override
	public void onBackPressed() {
		FragmentManager fragmentManager = this.getFragmentManager();
		fragmentManager.popBackStack();
	}

	/*
	 * public boolean onKeyDown(int keyCode, KeyEvent event) { if (keyCode ==
	 * KeyEvent.KEYCODE_BACK) {
	 * 
	 * //MenuActivity.this.finish();
	 * 
	 * Fragment fragment = null; fragment = new HomeFragment(); if (fragment !=
	 * null) { FragmentManager fragmentManager = getFragmentManager();
	 * fragmentManager.beginTransaction() .replace(R.id.frame_container,
	 * fragment).addToBackStack(null).commit();
	 * 
	 * } } return super.onKeyDown(keyCode, event); }
	 */

	
	
	
	class UserDataBG extends AsyncTask<Void, Void, Void> {
		boolean update;
		UserBO uesrBo;
		JSONObject resp ;
		Dialog dialog;
		String userName;
		String pwd;
		public UserDataBG(boolean update,Dialog dialog,String userName,String pwd) {
			this.update = update;
			this.dialog = dialog;
			this.userName = userName;
			this.pwd = pwd;
		}
		@Override
		protected void onPreExecute() {	
			uesrBo = new UserBO(context);
			ProgressClass.startProgress(context);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			if(update){
				Utilities.loggedInUser = uesrBo.updateUser(Utilities.loggedInUser);
			}else{
				if (!(userName.length() > 0 && pwd.length() > 0)) {
					Utilities.loggedInUser = uesrBo.authenticateUser(userName, pwd);
				} else {
					Intent n1 = new Intent(MenuActivity.this, LoginScreen.class);
					startActivity(n1);
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if(update){
				if(Utilities.loggedInUser!=null){
					dialog.dismiss();
				}else{
					errortemp.setVisibility(View.VISIBLE);
					errorText.setText("Sorry , try again");
				}
			}
			super.onPostExecute(result);
			ProgressClass.finishProgress();
		}
	}

	class StoreBG extends AsyncTask<Void, Void, Void> {
		FragmentManager frgManager;
		Fragment frg;
		double lat,lon;
		public StoreBG(FragmentManager frgManager,Fragment frg,double latitude,double longitude) {
			this.frgManager=frgManager;
			this.frg=frg;
			this.lat = latitude;
			this.lon = longitude;
			
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
				Utilities.storeList = storeBO.getAllStore(Utilities.STATUS_FILTER_ACTIVE,lat,lon);
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			
			if (Utilities.storeList != null && Utilities.storeList.size() == 1) {
				Utilities.selectedStore = Utilities.storeList.get(0);
				currencySymbol = Utilities.selectedStore.getCurrencySymbol();
				if(MenuActivity.currencySymbol == null || MenuActivity.currencySymbol.trim().isEmpty()){
					MenuActivity.currencySymbol="$";
				}
				currencySymbol = currencySymbol+" ";
				Utilities.confirm_store =true;
				if (frg != null) {
					frgManager.beginTransaction().replace(R.id.frame_container, frg).addToBackStack(null).commit();
				}else{
					Toast.makeText(context, "Only one store is available now.", Toast.LENGTH_SHORT).show();
				}
			} else if (Utilities.storeList != null && Utilities.storeList.size() > 0) {
				Utilities.selectStorePopup(context,frgManager, frg);
			}
			super.onPostExecute(result);
			ProgressClass.finishProgress();
		}
	}
	
	class SupportBG extends AsyncTask<Void, Void, Void> {

		
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(context);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			CustSupportBO bo = new CustSupportBO(context);
			bo.updateOfficerAvailable(ChatFragment.userModel.getOfficerId());
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			ProgressClass.finishProgress();
		}
	}
	
}
