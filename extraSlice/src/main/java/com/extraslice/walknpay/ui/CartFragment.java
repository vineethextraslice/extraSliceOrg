package com.extraslice.walknpay.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.R;
import com.extraslice.walknpay.adapter.CartListAdapter;
import com.extraslice.walknpay.bl.ProductBO;
import com.extraslice.walknpay.bl.StoreBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.ProductModel;
import com.extraslice.walknpay.model.PurchasedProductModel;
import com.extraslice.walknpay.model.StoreModel;

public class CartFragment extends Fragment {
	ActionBar mactionBar;
	public static ArrayList<PurchasedProductModel> productslist = new ArrayList<PurchasedProductModel>();
	Fragment fragment = null;
	ProductModel products;
	Dialog payDialog;
	static FragmentManager fragmentManager;
	public static ImageView checkout_button, imgScanner;
	StoreModel storeModel = null;

	static TextView tvPDescription, tvPCount, tvPPrice;
	public static TextView scanner_buttontext, checkoutTextView;
	static ImageView imgplus, imgminus, imgclear;
	String Password;
	int flag = 0;
	static int storeId = 1;
	Double price_tmp;
	EditText pswrdcnfm;
	static EditText barcode;
	public static Context context;
	// public static String contents="073287611054";
	String scanContent, scanFormat;
	public static Button ok, dismiss;
	Intent intent;
	View forgot_view;
	Dialog dialog;
	Button add, cancel;
	LayoutInflater inflater;
	public static int count = 0;
	public static ListView cartlist;
	public static CartListAdapter cartlistadapter;
	public static LinearLayout linearcartlabels, linear_noproducts, linearcartlabels1, linear_bottomtab;
	public static TextView tv_subtotal, tvtax_amt, tvtotal_amt, tax_label, taxamt_label, textblink, confirm_text;
	String tax_amt = "00.00", subtotal_amt = "00.00", total_amt = "00.00";
	static MediaPlayer mp;
	int visibility_value, visibility_value1;
	public static View toastView;
	public static ImageView imageView, imgError;
	public static TextView textView;
	/* public static RelativeLayout rlstore; */
	public static Activity mActivity;
	public static Animation hyperspaceJump;
	public static boolean flagpause = false;
	

	//View selectedStoreMain, homeMain;
	View  homeMain;
	Spinner selectStoreList;
	Button btnGo, btnCancel;
	static View manualBarcodeEntry;

	public CartFragment() {
		Utilities.lastAction = "CART";
		Utilities.SCAN_FOR = Utilities.SCAN_FOR_PURCHASE;
	}

	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Utilities.lastAction = "CART";
		rootView = inflater.inflate(R.layout.cart_tab, container, false);

		manualBarcodeEntry = (View) rootView.findViewById(R.id.manualBarcodeEntry);
		selectStoreList = (Spinner) rootView.findViewById(R.id.selectStoreList);

		/* This is the Components in Manual barcode entry layout */
		add = (Button) manualBarcodeEntry.findViewById(R.id.bAdd);
		cancel = (Button) manualBarcodeEntry.findViewById(R.id.bCancel);
		barcode = (EditText) manualBarcodeEntry.findViewById(R.id.etBarcode);
		imgError = (ImageView) manualBarcodeEntry.findViewById(R.id.ivError);
		
		if(storeModel==null||storeModel.getCurrencySymbol()==null){
			tax_amt = "00.00";
			subtotal_amt = "00.00";
			total_amt = "00.00";
		}else{
			tax_amt = MenuActivity.currencySymbol+"00.00";
			subtotal_amt = MenuActivity.currencySymbol+"00.00";
			total_amt = MenuActivity.currencySymbol+"00.00";
		}
	
		manualBarcodeEntry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utilities.hideVirtualKeyBoard(getActivity(), barcode);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utilities.hideVirtualKeyBoard(context, barcode);
				barcode.setText("");
				imgError.setVisibility(View.INVISIBLE);
				manualBarcodeEntry.setVisibility(View.GONE);

			}
		});

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String barcodeStr = barcode.getText().toString();
				Utilities.hideVirtualKeyBoard(context, barcode);
				if (barcodeStr.equalsIgnoreCase("")) {
					imgError.setVisibility(View.VISIBLE);

				} else {

					checkScanItem(barcodeStr);

					barcode.setText("");
					imgError.setVisibility(View.INVISIBLE);
					manualBarcodeEntry.setVisibility(View.GONE);
				}

			}
		});

		hyperspaceJump = AnimationUtils.loadAnimation(getActivity(), R.anim.cycle);
		imgScanner = (ImageView) rootView.findViewById(R.id.scanner_button);

		checkout_button = (ImageView) rootView.findViewById(R.id.checkoutbutton);


		checkout_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				checkOut();

			}
		});


			
		
		context = this.getActivity();
		scanner_buttontext = (TextView) rootView.findViewById(R.id.scanner_buttontext);
		checkoutTextView = (TextView) rootView.findViewById(R.id.checkoutbuttontext);
		dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		cartlist = (ListView) rootView.findViewById(R.id.lbllist);
		linearcartlabels = (LinearLayout) rootView.findViewById(R.id.cartlabels);
		linearcartlabels1 = (LinearLayout) rootView.findViewById(R.id.cartlabels1);
		linear_bottomtab = (LinearLayout) rootView.findViewById(R.id.linear56);
		tv_subtotal = (TextView) rootView.findViewById(R.id.subtotal_amt);
		tvtax_amt = (TextView) rootView.findViewById(R.id.tax_amt);

		tvtotal_amt = (TextView) rootView.findViewById(R.id.total_amt);
		tax_label = (TextView) rootView.findViewById(R.id.taxlabel);

		taxamt_label = (TextView) rootView.findViewById(R.id.textlab2);
		textblink = (TextView) rootView.findViewById(R.id.textView1);
		/* confirm_tick = (Button) rootView.findViewById(R.id.imageView2); */
		// confirm_text = (TextView) findViewById(R.id.textViewconfirm);

		//

		selectStoreList = (Spinner) rootView.findViewById(R.id.selectStoreList);
		//selectedStoreMain = (View) rootView.findViewById(R.id.selectedStoreMain);
		btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
		btnGo = (Button) rootView.findViewById(R.id.btn_go);

		/*if (Utilities.confirm_store == true) {

			selectedStoreMain.setVisibility(View.GONE);

		}
*/
		tvtax_amt.setText(tax_amt);
		tv_subtotal.setText(subtotal_amt);
		tvtotal_amt.setText(total_amt);

		imgScanner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Utilities.confirm_store == false) {
					new StoreBG(true).execute();
				} else {
					scanItem();
				}

			}
		});
		
		checkout_button.setClickable(true);
		imgScanner.setClickable(true);
		fragmentManager = getFragmentManager();

		mActivity = (Activity) getActivity();

		

		LayoutInflater inflater2 = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		/*
		 * toastView = inflater2.inflate(R.layout.toast, (ViewGroup)
		 * getActivity() .findViewById(R.id.toastLayout)); textView = (TextView)
		 * toastView.findViewById(R.id.text);
		 */

		cartlistadapter = new CartListAdapter(mActivity, R.layout.row_cart, productslist, count) {
			@Override
			public void refresh() {

				productslist = localList;

				cartlistadapter.notifyDataSetChanged();

				calculate();
			}
		};

		cartlist.setAdapter(cartlistadapter);

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
		/*
		 * if(productslist.size()>0) { productslist.clear(); }
		 */
		if (Utilities.scanHome == true) {
			Utilities.scanHome = false;
			scanItem();
		}
		return rootView;
	}

	

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void scanItem() {
		CustomScanner.initiateScan(mActivity, R.layout.zxinglib_capture, R.id.zxinglib_viewfinder_view, R.id.zxinglib_preview_view, true,R.id.manual_button1,"CART",Utilities.CAMERA_ID+"");
	}

	private void checkOut() {
		// TODO Auto-generated method stub

		if (productslist != null && productslist.size() > 0) {
			checkout_button.setClickable(false);
			imgScanner.setClickable(false);
			fragment = new PaymentOptionScreen();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
		} else {
			Toast toast = Toast.makeText(context, "No items added to cart!", Toast.LENGTH_SHORT);
			toast.show();
		}

		// }

	}

	private void stop() {
		// TODO Auto-generated method stub

		Animation anim = new AlphaAnimation(0.0f, 0.0f);
		anim.setDuration(0); // You can manage the time of the blink with this
								// parameter
		anim.setStartOffset(0);
		anim.setRepeatMode(Animation.REVERSE);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == -1) {
			// new code
			IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
			String contents = intent.getStringExtra("SCAN_RESULT");

			if (scanningResult != null) {
				scanContent = scanningResult.getContents();
				scanFormat = scanningResult.getFormatName();
			} else {
				Toast toast = Toast.makeText(context, "No scan data received! : "+scanContent, Toast.LENGTH_SHORT);
				toast.show();
			}

			if (Utilities.checkInternetConnection(context)) {
				int storeId = 0;
				try {
					storeId = Utilities.selectedStore.getStoreId();
				} catch (NullPointerException e) {
					e.printStackTrace();
					storeId = 0;
				}
				if (storeId != 0) {
					checkScanItem(contents);
				} else {
					Toast.makeText(context, "Sorry no stores added yet!", Toast.LENGTH_LONG).show();
				}
			} else {
				Utilities.showInternetStatus(context, false);
			}
		}else if (resultCode == 0) {
			/*Toast toast = Toast.makeText(context, "Please try again!", Toast.LENGTH_SHORT);
			toast.show();*/
		}
	}

	private void checkScanItem(String contents) {
		if (Utilities.checkInternetConnection(context)) {
			int storeId = 0;
			try {
				storeId = Utilities.selectedStore.getStoreId();
			} catch (NullPointerException e) {
				e.printStackTrace();
				storeId = 0;
			}
			if (storeId != 0) {
				new ProductBG(contents).execute();
			} else {
				Toast.makeText(context, "No active stores available!", Toast.LENGTH_LONG).show();
			}
		} else {
			Utilities.showInternetStatus(context, false);
		}

	}


	public static void playSoundbeep(Context context) {
		// TODO Auto-generated method stub

		mp = MediaPlayer.create(mActivity, R.raw.beeep);
		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.release();
			}

		});
		mp.start();

	}

	public static void playSound(Context context) {

		mp = MediaPlayer.create(mActivity, R.raw.beep);
		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub

				mp.release();
			}

		});

		mp.start();

	}

	private void scrollMyListViewToBottom() {
		cartlist.post(new Runnable() {
			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				cartlist.setSelection(cartlistadapter.getCount() - 1);
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (Utilities.confirm_store == true) {

			stop();

		}

		/*if (Utilities.confirm_store == true) {

			selectedStoreMain.setVisibility(View.GONE);

		}*/
		if (productslist.size() > 0) {

			tvtotal_amt.setText(Utilities.total1);
			tvtax_amt.setText(Utilities.Tax1);
			tv_subtotal.setText(Utilities.subtotal_amt);
		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		String amt = tvtotal_amt.getText().toString();
		if(amt != null && amt.startsWith(MenuActivity.currencySymbol)){
			amt = (amt.substring(MenuActivity.currencySymbol.length(), amt.length())).trim();
		}
		Utilities.total1=amt;
		Utilities.subtotal_amt = tv_subtotal.getText().toString();
		Utilities.Tax1 = tvtax_amt.getText().toString();

		flagpause = true;

		Utilities.classname = this.getClass().getName();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

		}
		return true;
	}

	public static void calculate() {
		double sum = 0.00, rewards = 0.00, tax = 0.00;
		if (productslist == null || productslist.size() == 0) {

			Utilities.total = 0;
			Utilities.rewards = 0;
			tv_subtotal.setText(MenuActivity.currencySymbol+"00.00");
			tvtax_amt.setText(MenuActivity.currencySymbol+"00.00");
			tvtotal_amt.setText(MenuActivity.currencySymbol+"00.00");
			Utilities.total = 00.00;
			((LinearLayout) linearcartlabels1).setVisibility(View.VISIBLE);

		} else {

			((LinearLayout) linearcartlabels1).setVisibility(View.GONE);
			double total = 0.0;
			double totalTax = 0.0;
			double grossTotal = 0.0;
			for (int i = 0; i < productslist.size(); i++) {

				PurchasedProductModel product = productslist.get(i);
				Double itemPrice = product.getPrice();
				int itemCount = (int) product.getPurchasedQuantity();
				double taxforItem = itemPrice * (product.getTaxPercentage() / 100);
				double totalForItem = (itemCount * itemPrice) + (itemCount * taxforItem);
				totalTax = totalTax + Double.parseDouble(new DecimalFormat("00.00").format(itemCount * taxforItem));
				grossTotal = grossTotal + Double.parseDouble(new DecimalFormat("00.00").format(totalForItem));
				total = total + Double.parseDouble(new DecimalFormat("00.00").format(itemCount * itemPrice));
			}

			rewards = (0.00f);
			tv_subtotal.setText(MenuActivity.currencySymbol+"" + String.valueOf(new DecimalFormat("00.00").format(total)));

			total = ((sum + tax) - rewards);

			Utilities.total = grossTotal;
			Utilities.rewards = rewards;

			String v = String.valueOf(new DecimalFormat("##.##").format(Utilities.total));

			tvtotal_amt.setText(MenuActivity.currencySymbol+"" + v);
			tvtax_amt.setText(MenuActivity.currencySymbol+"" + String.valueOf(new DecimalFormat("00.00").format(totalTax)));
			((LinearLayout) linearcartlabels).setVisibility(View.VISIBLE);
		}
	}
	public static void manual(Context context) {

		manualBarcodeEntry.setVisibility(View.VISIBLE);
		manualBarcodeEntry.setFocusable(true);
		// manualBarcodeEntry.setFocusableInTouchMode(true);//double
		// keyboard
		barcode.requestFocus();
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(barcode, InputMethodManager.SHOW_IMPLICIT);

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
				Fragment fragment = new CartFragment();
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
	
	class ProductBG extends AsyncTask<Void, Void, Void> {
		String contents;
		public ProductBG(String contents) {
			this.contents=contents;
		}
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(context);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {

			ProductBO bo = new ProductBO(context);
			products = bo.getProductForStoreByCode(contents, Utilities.selectedStore.getStoreId(), Utilities.STATUS_FILTER_ACTIVE);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (products != null) {
				flag = 0;
				if (productslist.size() > 0) {
					for (PurchasedProductModel modelIn : productslist) {
						if (modelIn.getCode().equals(products.getCode())) {
							if (modelIn.getPurchasedQuantity() + 1 <= products.getAvailableQty()) {
								modelIn.setPurchasedQuantity(modelIn.getPurchasedQuantity() + 1);
								count = 1;
								flag = 1;
								try {
									playSound(getActivity());
								} catch (Exception e) {
								}
								if (mActivity == null) {
									return;
								} else
									cartlistadapter = new CartListAdapter(mActivity, R.layout.row_cart, productslist, count) {
										@Override
										public void refresh() {
											productslist = localList;
											cartlistadapter.notifyDataSetChanged();
											calculate();
										}
									};
								cartlist.setAdapter(cartlistadapter);
								scrollMyListViewToBottom();
								count = 0;
								Toast toast = Toast.makeText(context, "Item quantity updated.", Toast.LENGTH_LONG);
								toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
								toast.show();
							} else {
								count = 1;
								flag = 1;
								modelIn.setPurchasedQuantity(modelIn.getPurchasedQuantity());
								Double qty = products.getAvailableQty();
								int qtyint = qty.intValue();
								Toast toast = Toast.makeText(context, "We're sorry! We are able to accommodate only " + qtyint + " units", Toast.LENGTH_LONG);
								toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
								toast.show();
							}
							break;
						}
					}
					if (flag != 1) {
						PurchasedProductModel model = new PurchasedProductModel(products);
						if (products.isOnDemandItem() || products.getAvailableQty() > 0) {
							model.setPurchasedQuantity(1);
							productslist.add(model);
							try {
								playSound(getActivity());
							} catch (Exception e) {
							}
							if (mActivity == null) {
								return;
							} else
								cartlistadapter = new CartListAdapter(mActivity, R.layout.row_cart, productslist, count) {
									@Override
									public void refresh() {
										productslist = localList;
										cartlistadapter.notifyDataSetChanged();
										calculate();
									}
								};
							cartlist.setAdapter(cartlistadapter);
							scrollMyListViewToBottom();
							count = 0;
							Toast toast = Toast.makeText(context, " " + products.getName() + " is added to the list.", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
							toast.show();
						} else {
							Toast toast = Toast.makeText(context, "We're sorry! This item is out of stock", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
							toast.show();
						}
					}
				} else {
					PurchasedProductModel model = new PurchasedProductModel(products);
					if (products.isOnDemandItem() || products.getAvailableQty() > 0) {
						model.setPurchasedQuantity(1);
						productslist.add(model);
						try {
							playSound(getActivity());
						} catch (Exception e) {
						}
						if (mActivity == null) {
							return;
						} else
							cartlistadapter = new CartListAdapter(mActivity, R.layout.row_cart, productslist, count) {
								@Override
								public void refresh() {
									productslist = localList;
									cartlistadapter.notifyDataSetChanged();
									calculate();
								}
							};
							cartlist.setAdapter(cartlistadapter);
							scrollMyListViewToBottom();
							count = 0;
							Toast toast = Toast.makeText(context, " " + products.getName() + " is added to the list.", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
							toast.show();
						} else {
							Toast toast = Toast.makeText(context, "We're sorry! This item is out of stock", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
							toast.show();
						}
					}

				} else {
					playSoundbeep(getActivity());

					// Product not found
					try {

						playSoundbeep(context);

						final Dialog dialog1;
						dialog1 = new Dialog(context);
						dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog1.setContentView(R.layout.alert);

						TextView textTitle = (TextView) dialog1.findViewById(R.id.alertTitle);
						textTitle.setText("Sorry");

						// set the custom dialog components - text, image
						// and button
						TextView text = (TextView) dialog1.findViewById(R.id.content);
						text.setText("The scanned item is not in the inventory!");

						// image.setImageResource(R.drawable.ic_launcher);

						Button dialogButton = (Button) dialog1.findViewById(R.id.alertpositivebutton);
						// if button is clicked, close the custom dialog
						dialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								dialog1.dismiss();
							}
						});
						dialog1.show();

					} catch (Exception e) {

					}
				}
			Utilities.setTotalCount();
			ProgressClass.finishProgress();
		}
	}
}