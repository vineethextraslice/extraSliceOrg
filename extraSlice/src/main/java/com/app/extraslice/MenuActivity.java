package com.app.extraslice;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.bo.SmartspaceBO;
import com.app.extraslice.fragments.AboutFragment;
import com.app.extraslice.fragments.HomeFragment;
import com.app.extraslice.fragments.MyReservations;
import com.app.extraslice.fragments.ProfileFragment;
import com.app.extraslice.fragments.ReserveAConfRoom;
import com.app.extraslice.fragments.TicketDashboardFragment;
import com.app.extraslice.model.Organization;
import com.app.extraslice.utils.Utilities;
import com.extraslice.walknpay.bl.StoreBO;
import com.extraslice.walknpay.bl.StrpePaymentSetup;
import com.extraslice.walknpay.bl.UserBO;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.UserModel;
import com.extraslice.walknpay.ui.CartFragment;
import com.extraslice.walknpay.ui.WalletFragment;
import com.extraslice.walknpay.ui.transaction.PaypalPaymentGateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuActivity extends Activity {
	
	ActionBar mactionBar;
	
	FrameLayout layout;
	FragmentManager fragmentManager;
	static Context context;
	View homeMain;
	LinearLayout menuLyt ;
	public static String currencySymbol ="";
	public static  ImageView menuButton;
	
	Dialog dialog;
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private static String LOAD_RECEIPTS="LOAD_RECEIPTS";
	private static String LOAD_WALKNPAY="LOAD_WALKNPAY";
	public static boolean menuShowing = false;
	static GridView menuList;

	//static GridLayout  horzScrView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		menuList = (GridView)findViewById(R.id.menuList);

		
		context = this;
		resetWalkNPay();
		getActionBar().hide();
		
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		/*ActionBar ab = getActionBar();
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayUseLogoEnabled(true);
		ab.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		//ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme_main_color)));
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.menu_bar_title, null);
		v.setPadding(0, 0, 0, 0);*/
		menuButton = (ImageView)findViewById(R.id.menu);
		menuButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showHideMenu();
				
			}
		});
		//getActionBar().hide();
		//ab.setCustomView(v);

		String[] navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		float scale =getResources().getDisplayMetrics().density;
		//horzScrView = (GridLayout )findViewById(R.id.horzScrView);
		/*LayoutParams rlParams = new LayoutParams((int)(scale*navMenuTitles.length*220), (int)(scale*50));
		rlParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);*/
		Map<String, Integer> menuMap = new HashMap<String, Integer>(1);
		List<Map<String, Integer>> menuMapList = new ArrayList<Map<String,Integer>>(1);
		if(Utilities.loggedInUser.getUserType().equalsIgnoreCase("member")){
			menuMap.put(navMenuTitles[9],  navMenuIcons.getResourceId(9, -1));
			menuMapList.add(menuMap);
			menuMap = new HashMap<String, Integer>(1);
			menuMap.put(navMenuTitles[0],  navMenuIcons.getResourceId(0, -1));
			menuMapList.add(menuMap);
			menuMap = new HashMap<String, Integer>(1);
			menuMap.put(navMenuTitles[1],  navMenuIcons.getResourceId(1, -1));
			menuMapList.add(menuMap);
			menuMap = new HashMap<String, Integer>(1);
			menuMap.put(navMenuTitles[2],  navMenuIcons.getResourceId(2, -1));
			menuMapList.add(menuMap);
			menuMap = new HashMap<String, Integer>(1);
			menuMap.put(navMenuTitles[8],  navMenuIcons.getResourceId(8, -1));
			menuMapList.add(menuMap);
			menuMap = new HashMap<String, Integer>(1);
			menuMap.put(navMenuTitles[3],  navMenuIcons.getResourceId(3, -1));
			menuMapList.add(menuMap);
			menuMap = new HashMap<String, Integer>(1);
			menuMap.put(navMenuTitles[4],  navMenuIcons.getResourceId(4, -1));
			menuMapList.add(menuMap);
			menuMap = new HashMap<String, Integer>(1);
			menuMap.put(navMenuTitles[5],  navMenuIcons.getResourceId(5, -1));
			menuMapList.add(menuMap);
			menuMap = new HashMap<String, Integer>(1);
			
			menuMap.put(navMenuTitles[6],  navMenuIcons.getResourceId(6, -1));
			menuMapList.add(menuMap);
			menuMap = new HashMap<String, Integer>(1);
			/*int index = 0;
			
			test(inflator, index, navMenuTitles[9], navMenuIcons.getResourceId(9, -1));
			index ++;
			test(inflator, index, navMenuTitles[0], navMenuIcons.getResourceId(0, -1));
			index ++;
			test(inflator, index, navMenuTitles[1], navMenuIcons.getResourceId(1, -1));
			index ++;
			test(inflator, index, navMenuTitles[2], navMenuIcons.getResourceId(2, -1));
			index ++;
			test(inflator, index, navMenuTitles[8], navMenuIcons.getResourceId(8, -1));
			index ++;
			test(inflator, index, navMenuTitles[3], navMenuIcons.getResourceId(3, -1));
			index ++;
			test(inflator, index, navMenuTitles[4], navMenuIcons.getResourceId(4, -1));
			index ++;
			test(inflator, index, navMenuTitles[5], navMenuIcons.getResourceId(5, -1));
			index ++;
			test(inflator, index, navMenuTitles[6], navMenuIcons.getResourceId(6, -1));*/
			
		}else{
			/*int index = 0;
			test(inflator, index, navMenuTitles[4], navMenuIcons.getResourceId(4, -1));
			index ++;
			test(inflator, index, navMenuTitles[7], navMenuIcons.getResourceId(7, -1));
			index ++;
			test(inflator, index, navMenuTitles[3], navMenuIcons.getResourceId(3, -1));
			index ++;
			test(inflator, index, navMenuTitles[5], navMenuIcons.getResourceId(5, -1));
			index ++;
			test(inflator, index, navMenuTitles[6], navMenuIcons.getResourceId(6, -1));*/
			
			menuMap.put(navMenuTitles[0],  navMenuIcons.getResourceId(0, -1));
			menuMapList.add(menuMap);
			menuMap = new HashMap<String, Integer>(1);
			menuMap.put(navMenuTitles[7],  navMenuIcons.getResourceId(7, -1));
			menuMapList.add(menuMap);
			menuMap = new HashMap<String, Integer>(1);
			menuMap.put(navMenuTitles[8],  navMenuIcons.getResourceId(8, -1));
			menuMapList.add(menuMap);
			menuMap = new HashMap<String, Integer>(1);
			menuMap.put(navMenuTitles[3],  navMenuIcons.getResourceId(3, -1));
			menuMapList.add(menuMap);
			menuMap = new HashMap<String, Integer>(1);
			menuMap.put(navMenuTitles[5],  navMenuIcons.getResourceId(5, -1));
			menuMapList.add(menuMap);
		
			menuMap = new HashMap<String, Integer>(1);
			menuMap.put(navMenuTitles[6],  navMenuIcons.getResourceId(6, -1));
			menuMapList.add(menuMap);
		
		}
		
		CustomAdapter adp = new CustomAdapter(context, menuMapList);
		menuList.setAdapter(adp);
		
		/*for(int index=0;index < navMenuTitles.length;index++){
			
		}*/
		navMenuIcons.recycle(); 		
		if(Utilities.loggedInUser.getUserType().equalsIgnoreCase("member")){
			loadSelectedFragment(1);
		}else{
			loadSelectedFragment(0);
		}
			
		//}
	}
	private void test(LayoutInflater inflator,int index,String text,int imageId){
		View menv = inflator.inflate(R.layout.menu_item, null);
		
		TextView tv = (TextView)menv.findViewById(R.id.menuText);
		ImageView ico = (ImageView)menv.findViewById(R.id.menuIcon);
		tv.setText(text);
		ico.setImageResource(imageId);
		menuList.addView(menv);
		menv.setTag(index+"");
		menv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int tag = Integer.parseInt(v.getTag().toString());
				loadSelectedFragment(tag);
				// TODO Auto-generated method stub
				
			}
		});
	}
	public static void showHideMenu(){
		Log.e("menuShowing", menuShowing+"");
		//ViewGroup.LayoutParams prm = menuList.getLayoutParams();
		if(menuShowing){
			menuList.setVisibility(View.GONE);
			//prm.height=0;
			//menuList.setLayoutParams(prm);

			menuShowing=false;
		}else{
			//prm.height=ViewGroup.LayoutParams.WRAP_CONTENT;
			//menuList.setLayoutParams(prm);

			menuList.setVisibility(View.VISIBLE);
			menuShowing=true;
		}
	}
	
	
	
	private void loadSelectedFragment(int position){
		menuShowing=true;
		showHideMenu();
		Fragment fragment= null;
		if(Utilities.loggedInUser.getUserType().equalsIgnoreCase("member")){
			switch (position) {
			case 0:
				fragment = new ProfileFragment();
				break;
			case 1:
				fragment = new HomeFragment();
				break;
			case 2:
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
					fragment = new ReserveAConfRoom();//SelectRoomType();
				}
				
				break;
			case 3:
				fragment = new MyReservations();
				break;
			case 4:
				new LoadData(LOAD_RECEIPTS).execute();
				break;
				
			case 5:
				fragment = new TicketDashboardFragment();
				break;
			case 6:
				new LoadData(LOAD_WALKNPAY).execute();
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
		}else{
			switch (position) {
			
			case 0:
				new LoadData(LOAD_WALKNPAY).execute();
				break;
			case 1:
				Intent intent = new Intent(MenuActivity.this,SignupActivity.class);
				startActivity(intent);
				finish();
				break;	
			case 2:
				fragment = new WalletFragment(0);
				break;
			case 3:
				fragment = new TicketDashboardFragment();
				break;
			case 4:
				fragment = new AboutFragment();
				break;
					
			case 5:
				logoutFunction();
				break;
			default:
				break;
			}
		}
		
		if(fragment != null){
			fragmentManager = getFragmentManager();
			Utilities.loadFragment(fragmentManager, fragment, R.id.frame_container, false);
			
		}
		
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		
	}
	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
	//	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		return super.onPrepareOptionsMenu(menu);
	}
	
	protected void logoutFunction() {
		final Context context = MenuActivity.this;
		dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_alert);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.title_view);
		text.setText("Do you want to logout?");
		Button okButton = (Button) dialog.findViewById(R.id.yes_button);
		// if button is clicked, close the custom dialog
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SmartspaceBO.accountModel=null;
				SmartspaceBO.individualOrg=null;
				Utilities.loggedInUser =null;
				resetWalkNPay();
				com.extraslice.walknpay.bl.Utilities.flag = false;
				dialog.dismiss();
				Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
				startActivity(intent);
				HomeFragment.rootView=null;
				finish();
			}
		});
		Button cancelButton = (Button) dialog.findViewById(R.id.no_button);
		// if button is clicked, close the custom dialog
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void resetWalkNPay(){
		
		com.extraslice.walknpay.bl.Utilities.selectedStore=null;
		com.extraslice.walknpay.bl.Utilities.storeList = null;
		com.extraslice.walknpay.bl.Utilities.pay_finish = false;
		com.extraslice.walknpay.bl.Utilities.loggedInUser=null;
		com.extraslice.walknpay.bl.Utilities.loginUserID=1;
		com.extraslice.walknpay.bl.Utilities.confirm_store = false;
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
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(savedInstanceState);
		

	}

	

	class LoadData extends AsyncTask<Void, Void, Void> {
		String purpose;
		UserModel userModel ;
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
				String appVersion = getResources().getString(R.string.version);
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

	

	@Override
	public void onBackPressed() {
		FragmentManager fragmentManager = this.getFragmentManager();
		fragmentManager.popBackStack();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Fragment fragment = null;
		if (com.extraslice.walknpay.bl.Utilities.SCAN_FOR.equals(com.extraslice.walknpay.bl.Utilities.SCAN_FOR_PURCHASE)) {
			fragment = new CartFragment();
			fragment.onActivityResult(requestCode, resultCode, data);
		}
		if (com.extraslice.walknpay.bl.Utilities.SCAN_FOR.equals(com.extraslice.walknpay.bl.Utilities.SCAN_FOR_PAYPAL)) {
			PaypalPaymentGateway paypal = new PaypalPaymentGateway();
			paypal.onActivityResult(requestCode, resultCode, data);
		}
		if (com.extraslice.walknpay.bl.Utilities.SCAN_FOR.equals(com.extraslice.walknpay.bl.Utilities.SCAN_FOR_WLT_CARD)) {
			com.app.extraslice.utils.CustomPaymentGateway.onActivityResult(requestCode, resultCode, data);
		}
		if (com.extraslice.walknpay.bl.Utilities.SCAN_FOR.equals(com.extraslice.walknpay.bl.Utilities.SCAN_FOR_WLT_PRE_RECH)) {
			StrpePaymentSetup.onActivityResult(requestCode, resultCode, data);
		}
		if (com.extraslice.walknpay.bl.Utilities.SCAN_FOR.equals(com.extraslice.walknpay.bl.Utilities.SCAN_FOR_WNP_PAY)) {
			StrpePaymentSetup.onActivityResult(requestCode, resultCode, data);
		}

	}
	
	public class CustomAdapter extends ArrayAdapter<Map<String, Integer>> {

		private Context context;
		List<Map<String, Integer>> objectMpList;
		
		public CustomAdapter(Context context,  List<Map<String, Integer>> objectMpList) {
			super(context, R.layout.menu_item, objectMpList);
			this.context = context;
			this.objectMpList =objectMpList;
		}
		

		public View getView(int position, View convertView, ViewGroup parent) {
			//View row = super.getView(position, convertView, parent);
			
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.menu_item, parent, false);
			}
			
			Map<String, Integer> current = objectMpList.get(position);
			String key = current.keySet().iterator().next();
			int imageId = current.get(key);
			
			row.setTag(position+"");
			TextView name = (TextView) row.findViewById(R.id.menuText);
			name.setText(key);
			row.setTag(position+"");
			
			ImageView menuIcon = (ImageView) row.findViewById(R.id.menuIcon);
			menuIcon.setImageResource(imageId);
			menuIcon.setTag(position+"");
			row.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int tag = Integer.parseInt(v.getTag().toString());
					loadSelectedFragment(tag);
					// TODO Auto-generated method stub
					
				}
			});
			return row;
		}

	}	
	
}
