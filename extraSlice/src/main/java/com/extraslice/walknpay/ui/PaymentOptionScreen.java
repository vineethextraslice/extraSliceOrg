package com.extraslice.walknpay.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.codehaus.jettison.json.JSONObject;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.adapter.CouponAdapter;
import com.extraslice.walknpay.adapter.RechargeCouponAdapter;
import com.extraslice.walknpay.bl.CouponBO;
import com.extraslice.walknpay.bl.CustomException;
import com.extraslice.walknpay.bl.StoreBO;
import com.extraslice.walknpay.bl.StrpePaymentSetup;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.CouponModel;
import com.extraslice.walknpay.model.PurchasedProductModel;
import com.extraslice.walknpay.model.StoreModel;
import com.extraslice.walknpay.model.TransactionModel;
import com.extraslice.walknpay.ui.transaction.AddPrepaidBalance;
import com.extraslice.walknpay.ui.transaction.AddTransaction;
import com.extraslice.walknpay.ui.transaction.PaypalPaymentGateway;
import com.extraslice.walknpay.ui.transaction.StrpePaymentGateway;
import com.extraslice.walknpay.ui.transaction.UpdateTransaction;

public class PaymentOptionScreen extends Fragment {
	View rootView;
	RadioGroup radioPayGrp;
	Fragment fragment;
	StoreModel storeModel = null;
	public static ListView couponListView;
	static Context mContext = null;
	public static CheckBox prepaidChk;
	LinearLayout prepaidLyt, offerLyt;
	public static LinearLayout lyt2,lyt1,payableAmountLyt,offerAmountLyt,footNote,prepaidAppldLyt;
	TextView totalAmountTv,viewOffer,rechargeLink;
	public static View seperator;
	public static TextView payableAmountTv, offerAmount,title_view,prepaidTxt,prepaidAppld;
	public static double prepaidAvailableAmount = 0;
	double totalAmount = 0;
	static boolean viewClicked =false;
	boolean hasPrepaid = false;
	public static double payableAmount = 0;
	public static double prepaidAmount = 0;
	public static List<CouponModel> defCouponList = new ArrayList<CouponModel>();
	public static List<CouponModel> offCouponList = new ArrayList<CouponModel>();
	List<CouponModel> couponList = new ArrayList<CouponModel>();
	public static List<CouponModel> appliedCouponList = new ArrayList<CouponModel>();
	public static CouponAdapter couponAdapter;
	Button btn_go, btn_cancel,prep_btn_cancel,prep_btn_go;
	public static double totalAmountForOffer=0;
	public static CouponModel defCoupon = null;
	View couponDetails;
	boolean isRecharged = false;
	Dialog strpPopup;
	StrpePaymentSetup strpSetup;
	/*static String dealerStripeSecKey = null;
	static String dealerStripePubKey = null;
	static String dealerPaypalClientId = null;
	static String dealerPaypalConfigEnv = null;*/
	public static double usableAmount,rechargePayableAmount;
	public static Set<String> couponId = new HashSet<String>();
	
	LinearLayout closePopup;
	ListView prepaidList;
	boolean isCustomeRecharge = true;
	public static Dialog rechargePopup;
	
	static boolean isSubmitted =false;
	public static EditText rechargeAmount;
    TextView errortxt,minAmtTxt;
    LinearLayout errorLyt,prepaidCouponLyt,customeCouponLyt;
    public static LinearLayout deatilsLyt;
    static double minRechargeAmt = 0;
    RadioGroup optionRadio,paymentRadio;
    RadioButton coupon,paypalRadioButton,strpRadioButton,paytmRadioBtn,pymntPaypalRButton,pymntStrpRButton,pymntPaytmRButton;
    String paymentGateWay = "Paypal";
    public static TextView usableAmtTV,payableAmtTV;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		isSubmitted =false;
		rootView = inflater.inflate(R.layout.payment_gateway_big, container, false);
		mContext = this.getActivity();	
		strpPopup = new Dialog(mContext);
		strpPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		strpPopup.setContentView(R.layout.wnp_paymentscreen);
		strpPopup.setCancelable(false);
		couponDetails= rootView.findViewById(R.id.cardDetails);
		closePopup = (LinearLayout) couponDetails.findViewById(R.id.closepopup);
		couponListView = (ListView) rootView.findViewById(R.id.couponListView);
		prepaidChk = (CheckBox) rootView.findViewById(R.id.prepaidChk);
		prepaidLyt = (LinearLayout) rootView.findViewById(R.id.prepaidLyt);
		offerLyt = (LinearLayout) rootView.findViewById(R.id.offerLyt);
		footNote = (LinearLayout) rootView.findViewById(R.id.footNote);
		lyt1 = (LinearLayout) rootView.findViewById(R.id.lyt1);
		lyt2 = (LinearLayout) rootView.findViewById(R.id.lyt2);
		payableAmountLyt = (LinearLayout) rootView.findViewById(R.id.payableAmountLyt);
		offerAmountLyt = (LinearLayout) rootView.findViewById(R.id.offerAmountLyt);
		prepaidTxt = (TextView) rootView.findViewById(R.id.prepaidTxt);
		prepaidAppldLyt = (LinearLayout) rootView.findViewById(R.id.prepaidAppldLyt);
		prepaidAppld = (TextView) rootView.findViewById(R.id.prepaidAppld);
		rechargeLink = (TextView) rootView.findViewById(R.id.rechargeLink);
		title_view = (TextView) rootView.findViewById(R.id.title_view);
		seperator= (View)rootView.findViewById(R.id.seperator);
		payableAmountTv = (TextView) rootView.findViewById(R.id.payableAmount);
		offerAmount = (TextView) rootView.findViewById(R.id.offerAmount);
		totalAmountTv = (TextView) rootView.findViewById(R.id.totalAmount);
		viewClicked =false;
		viewOffer = (TextView) rootView.findViewById(R.id.viewOffer);
		SpannableString viewContent = new SpannableString("View/Apply Coupons");
		viewContent.setSpan(new UnderlineSpan(), 0, viewContent.length(), 0);
		
		SpannableString rechargeText = new SpannableString("Recharge");
		rechargeText.setSpan(new UnderlineSpan(), 0, rechargeText.length(), 0);
		rechargeLink.setText(rechargeText);
		
		rechargePopup = new Dialog(getActivity());
		rechargePopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		rechargePopup.setContentView(R.layout.prepaid_recharge_popup);
		rechargePopup.setCancelable(false);
		errortxt = (TextView)rechargePopup.findViewById(R.id.errortxt);
		errorLyt = (LinearLayout)rechargePopup.findViewById(R.id.errorLyt);
		prepaidList = (ListView) rechargePopup.findViewById(R.id.prepaidList);
		rechargeAmount = (EditText) rechargePopup.findViewById(R.id.rechargeAmount);
		optionRadio = (RadioGroup)rechargePopup.findViewById(R.id.optionRadio);
		paymentRadio = (RadioGroup)rechargePopup.findViewById(R.id.paymentRadio);
		paypalRadioButton = (RadioButton)rechargePopup.findViewById(R.id.paypal);
		strpRadioButton = (RadioButton)rechargePopup.findViewById(R.id.stripe);
		paytmRadioBtn = (RadioButton)rechargePopup.findViewById(R.id.paytm);
		pymntPaypalRButton = (RadioButton)rootView.findViewById(R.id.radioPaypal);
		pymntStrpRButton = (RadioButton)rootView.findViewById(R.id.radioStrpe);
		pymntPaytmRButton = (RadioButton)rootView.findViewById(R.id.paytm);
		coupon =(RadioButton)rechargePopup.findViewById(R.id.coupon);
		prepaidCouponLyt = (LinearLayout)rechargePopup.findViewById(R.id.prepaidCouponLyt);
		customeCouponLyt = (LinearLayout)rechargePopup.findViewById(R.id.customeCouponLyt);
		deatilsLyt = (LinearLayout)rechargePopup.findViewById(R.id.deatilsLyt);
		payableAmtTV = (TextView)rechargePopup.findViewById(R.id.payableAmtTV);
		usableAmtTV = (TextView)rechargePopup.findViewById(R.id.usableAmtTV);
		minAmtTxt = (TextView)rechargePopup.findViewById(R.id.minAmtTxt);
		
		
		
		optionRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				selectOption();
				
			}
		});
		paymentRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				selectPaymentOption();
				
			}
		});
		
		prep_btn_go = (Button)rechargePopup.findViewById(R.id.btn_go);
		prep_btn_go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (!isSubmitted) {
					selectPaymentOption();
					isSubmitted = true;
					if (isCustomeRecharge) {
						if (rechargeAmount.getText() != null && rechargeAmount.getText().toString() != null 
								&& !rechargeAmount.getText().toString().trim().equals("")) {
							try {
								rechargePayableAmount = Double.parseDouble(rechargeAmount.getText().toString().trim());
								
							} catch (Exception e) {
								rechargePayableAmount = 0;
								errortxt.setText("Please enter valid amount");
								errorLyt.setVisibility(View.VISIBLE);
							}
							if (rechargePayableAmount < minRechargeAmt) {
								errortxt.setText("Minimum should be "+ minRechargeAmt);
								errorLyt.setVisibility(View.VISIBLE);
							} else {
								couponId.clear();
								couponId .add("-1");
								usableAmount = rechargePayableAmount;
								new RunInBackground(false, paymentGateWay.trim()).execute();
							}
						} else {
							rechargePayableAmount = 0;
							errortxt.setText("Please enter valid amount");
							errorLyt.setVisibility(View.VISIBLE);
						}
						
					} else {
						if (rechargePayableAmount <= 0) {
							errortxt.setText("Please select a coupon");
							errorLyt.setVisibility(View.VISIBLE);
						} else {
							new RunInBackground(false, paymentGateWay.trim()).execute();
						}
					}
				}
				isSubmitted =false;
			}
		});
		
		prep_btn_cancel = (Button)rechargePopup.findViewById(R.id.btn_cancel);
		prep_btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				rechargeAmount.setText("");
				rechargePayableAmount = 0;
				usableAmount = 0;
				PaymentOptionScreen.deatilsLyt.setVisibility(View.GONE);
				rechargePopup.dismiss();
			}
		});
		
		rechargeLink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new OnLoadPrepaidBG().execute();
				
				
			}
		});
		viewOffer.setText(viewContent);
		PaymentOptionScreen.totalAmountForOffer=Double.parseDouble(Utilities.total1.replace(MenuActivity.currencySymbol+"", ""));
		
		closePopup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				couponDetails.setVisibility(View.GONE);
			}
		});

		
	
		Utilities.offerAmount = prepaidAmount;
		
		prepaidAmount=0;
		couponAdapter = null;
		prepaidAvailableAmount = 0;
		prepaidAmount = 0;
		
		totalAmount = Double.parseDouble(Utilities.total1.replace(MenuActivity.currencySymbol+"", ""));
		payableAmount = totalAmount;
		//appliedCouponList = new ArrayList<CouponModel>();
		Utilities.offerAmount=0;
		Utilities.selectedCoupons= new HashSet<String>();
		new OnLoadBG().execute();
		
		prepaidChk.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton btn, boolean checked) {
				double amount = 0.0;
				if (prepaidAvailableAmount > (totalAmount - Utilities.offerAmount)) {
					amount = (totalAmount - (Utilities.offerAmount-prepaidAmount));
				} else {
					amount = prepaidAvailableAmount;
				}

				if (checked) {
					checkPrepaid(amount);
				} else {
					defCouponList.remove(defCoupon);
					Utilities.selectedCoupons.remove(prepaidChk.getTag().toString());
					prepaidTxt.setText(" Prepaid (Remaining bal "+MenuActivity.currencySymbol + Utilities.roundto2Decimaldouble(prepaidAvailableAmount) + ")");
					prepaidAppldLyt.setVisibility(View.GONE);
					prepaidTxt.setTextColor(Color.BLACK);
					Utilities.offerAmount = Utilities.offerAmount - prepaidAmount;
					prepaidAmount = 0;
					payableAmount = totalAmount - Utilities.offerAmount;
					if(payableAmount < 0){
						payableAmount = 0;
					}
					payableAmountTv.setText(MenuActivity.currencySymbol+"" + Utilities.roundto2Decimaldouble(payableAmount));
				}
				
				updatePaymentOptionVew();
				
			}
		});
		radioPayGrp = (RadioGroup) rootView.findViewById(R.id.radioPay);
		/*
		 * Button cancelbutton = (Button)
		 * rootView.findViewById(R.id.btn_canprepaidChk);
		 */
		btn_go = (Button) rootView.findViewById(R.id.btn_go);
		btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
		//btn_cancel.bringToFront();
		//btn_go.bringToFront();
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = new CartFragment();
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
				}
			}
		});
		btn_go.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new RunInBackground(true,null).execute();
			}
		});
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {

					Fragment fragment = null;
					fragment = new CartFragment();
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
	

	
	
	private void hideVirtualKeyboard() {
		// TODO Auto-generated method stub
		try {
			final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void selectOption(){
		rechargePayableAmount = 0;
		usableAmount = 0;
		payableAmtTV.setText("Payable amount : "+MenuActivity.currencySymbol+0);
		usableAmtTV.setText("Usable amount : "+MenuActivity.currencySymbol+0);
		deatilsLyt.setVisibility(View.GONE);
		int selected = optionRadio.getCheckedRadioButtonId();
		couponId.clear(); 
		RadioButton selectedButton = (RadioButton) rechargePopup.findViewById(selected);
		if (selectedButton != null){
			if(selectedButton.getText().toString().trim().equalsIgnoreCase("Custom")) {
				couponId.clear();
				isCustomeRecharge = true;
				prepaidCouponLyt.setVisibility(View.GONE);
				customeCouponLyt.setVisibility(View.VISIBLE);
			}else{
				couponId.remove("-1");
				isCustomeRecharge = false;
				prepaidCouponLyt.setVisibility(View.VISIBLE);
				customeCouponLyt.setVisibility(View.GONE);
			}
		}
	}
	
	private void selectPaymentOption(){
		int selected = paymentRadio.getCheckedRadioButtonId();
		RadioButton selectedButton = (RadioButton) rechargePopup.findViewById(selected);
		if (selectedButton != null){
			if(selectedButton.getText().toString().trim().equalsIgnoreCase("Paypal")) {
				paymentGateWay = "Paypal";
			}else if(selectedButton.getText().toString().trim().equalsIgnoreCase("Stripe")) {
				paymentGateWay = "Stripe";
			}else if(selectedButton.getText().toString().trim().equalsIgnoreCase("paytm")) {
				paymentGateWay = "Paytm";
			}
		}
	}
	public static void updatePrepaid(){
		double totalAmount=0.00;
		
		
		 totalAmount = Double.parseDouble(Utilities.total1);
		
		double amount = 0.0;
		if (prepaidAvailableAmount > (totalAmount - (Utilities.offerAmount-prepaidAmount))) {
			amount = (totalAmount - (Utilities.offerAmount-prepaidAmount));
		} else {
			amount = prepaidAvailableAmount;
		}
	
		if (prepaidChk.isChecked()) {
			Utilities.offerAmount = Utilities.offerAmount - PaymentOptionScreen.prepaidAmount;
			prepaidChk.setTag(R.id.prepaidChk, amount + "");
			Utilities.offerAmount = Utilities.offerAmount + amount;
			prepaidAmount = amount;
			payableAmount = totalAmount - Utilities.offerAmount;
			if(payableAmount < 0){
				payableAmount = 0;
			}
			payableAmountTv.setText(MenuActivity.currencySymbol+"" + Utilities.roundto2Decimaldouble(payableAmount));
			prepaidTxt.setTextColor(Color.GRAY);
		}
		
		if(prepaidAvailableAmount > 0){
			prepaidChk.setClickable(true);
			PaymentOptionScreen.prepaidTxt.setTextColor(Color.BLACK);						
			prepaidTxt.setText(" Prepaid (Remaining bal "+MenuActivity.currencySymbol + Utilities.roundto2Decimaldouble(prepaidAvailableAmount-prepaidAmount) + ")");
			prepaidAppldLyt.setVisibility(View.VISIBLE);
			prepaidAppld.setText(MenuActivity.currencySymbol+""+ Utilities.roundto2Decimaldouble(prepaidAmount));
		}else{
			prepaidChk.setClickable(false);
			PaymentOptionScreen.prepaidTxt.setTextColor(Color.GRAY);						
			prepaidTxt.setText(" Prepaid (Remaining bal 0");
			prepaidAppldLyt.setVisibility(View.GONE);			
		}
		updatePaymentOptionVew();
		
	}
	
	private void checkPrepaid(double amount){
		prepaidChk.setTag(R.id.prepaidChk, amount + "");
		defCouponList.add(defCoupon);
		Utilities.selectedCoupons.add(prepaidChk.getTag().toString());
		Utilities.offerAmount = Utilities.offerAmount + amount;
		prepaidAmount = amount;
		payableAmount = totalAmount - Utilities.offerAmount;
		if(payableAmount <= 0){
			payableAmount = 0;
		}else if(payableAmount < 0.5){
			
			Utilities.offerAmount = Utilities.offerAmount - amount;
			amount = amount - (0.5-payableAmount);
			
			payableAmount =0.5;
			prepaidAmount = amount;
			prepaidChk.setTag(R.id.prepaidChk, amount + "");
			Utilities.offerAmount = Utilities.offerAmount + amount;
			prepaidAmount = amount;
			payableAmount = totalAmount - Utilities.offerAmount;
		}
		payableAmountTv.setText(MenuActivity.currencySymbol+"" + Utilities.roundto2Decimaldouble(payableAmount));
		prepaidTxt.setText(" Prepaid (Remaining bal "+MenuActivity.currencySymbol + Utilities.roundto2Decimaldouble(prepaidAvailableAmount-prepaidAmount) + ")");
		prepaidTxt.setTextColor(Color.GRAY);
		prepaidAppldLyt.setVisibility(View.VISIBLE);
		prepaidAppld.setText(MenuActivity.currencySymbol+""+ Utilities.roundto2Decimaldouble(prepaidAmount));
		updatePaymentOptionVew();
	}
	private static void updatePaymentOptionVew(){
		if(payableAmount <= 0 ){
			lyt1.setVisibility(View.GONE);
			if(PaymentOptionScreen.prepaidAmount <= 0){
				title_view.setVisibility(View.GONE);
				seperator.setVisibility(View.GONE);
				lyt2.setVisibility(View.GONE);
			}else{
				title_view.setVisibility(View.VISIBLE);
				seperator.setVisibility(View.VISIBLE);
				lyt2.setVisibility(View.VISIBLE);
				PaymentOptionScreen.title_view.setText("");
			}
		}else{
			PaymentOptionScreen.title_view.setText("Pay balance "+MenuActivity.currencySymbol+Utilities.roundto2Decimaldouble(payableAmount)+" using");
			title_view.setVisibility(View.VISIBLE);
			seperator.setVisibility(View.VISIBLE);
			lyt2.setVisibility(View.VISIBLE);
			lyt1.setVisibility(View.VISIBLE);
		}
		
	}
	
	
	class OnLoadBG extends AsyncTask<Void, Void, Void> {
		CouponBO couponBO;
		List<CouponModel> couponListToSort;
		List<CouponModel> deflist = new ArrayList<CouponModel>();
		List<CouponModel> offrList = new ArrayList<CouponModel>();
		public OnLoadBG() {

		}
		
		@Override
		protected void onPreExecute() {	
			couponBO = new CouponBO(mContext);
			ProgressClass.startProgress(getActivity());
			defCouponList = new ArrayList<CouponModel>();
			offCouponList = new ArrayList<CouponModel>();
			defCoupon = null;
			if (couponList == null) {
				couponList = new ArrayList<CouponModel>();
			}
			for(PurchasedProductModel pm : CartFragment.productslist){
				pm.setOfferAppliedAmt(0);
				pm.setOfferAppliedQty(0);
			}
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				StoreBO storeBO = new StoreBO(getActivity());
				storeModel = storeBO.getSelectedStore(Utilities.selectedStore.getStoreId());
				if(storeModel != null){
					minRechargeAmt = storeModel.getMinRechargeAmt();
				}
				
				minAmtTxt.setText("Minimum "+MenuActivity.currencySymbol+minRechargeAmt);
				couponListToSort = couponBO.getAllCouponsForPurchase();
			} catch (CustomException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			boolean hasAcct = false;
			if(storeModel.getDlrPaypalClientId() == null || storeModel.getDlrPaypalClientId().trim().equals("")){
				paypalRadioButton.setVisibility(View.GONE);  
			}else{
				paypalRadioButton.setVisibility(View.VISIBLE); 
				hasAcct =true;
				paymentGateWay="paypal";
				paypalRadioButton.setChecked(true);
			}
			
			if(storeModel.getDlrStripeSecretKey() == null || storeModel.getDlrStripeSecretKey().trim().equals("")){
				strpRadioButton.setVisibility(View.GONE); 
			}else{
				strpRadioButton.setVisibility(View.VISIBLE); 
				if(!hasAcct){
					paymentGateWay="Stripe";
					strpRadioButton.setChecked(true);
				}
				hasAcct =true;
			}
			if(storeModel.getDlrPayTmAcctMap() == null || storeModel.getDlrPayTmAcctMap().size() <= 0){
				paytmRadioBtn.setVisibility(View.GONE); 
			}else{
				paytmRadioBtn.setVisibility(View.VISIBLE); 
				if(!hasAcct){
					paymentGateWay="Paytm";
					paytmRadioBtn.setChecked(true);
				}
				hasAcct =true;
			}
			
			if(hasAcct){
				prep_btn_go.setEnabled(true);
				prep_btn_go.setClickable(true);
			}else{
				prep_btn_go.setEnabled(false);
				prep_btn_go.setClickable(false);
			}
			
			
			boolean hasStrAcct = false;
			if(storeModel.getPaypalClientId() == null || storeModel.getPaypalClientId().trim().equals("")){
				pymntPaypalRButton.setVisibility(View.GONE);  
			}else{
				pymntPaypalRButton.setVisibility(View.VISIBLE); 
				hasStrAcct =true;
				pymntPaypalRButton.setChecked(true);
				paymentGateWay="paypal";
			}
			
			if(storeModel.getStripeSecretKey() == null || storeModel.getStripeSecretKey().trim().equals("")){
				pymntStrpRButton.setVisibility(View.GONE); 
			}else{
				pymntStrpRButton.setVisibility(View.VISIBLE); 
				if(!hasStrAcct){
					pymntStrpRButton.setChecked(true);
					paymentGateWay="Stripe";
				}
				hasStrAcct =true;
			}
			if(storeModel.getStrPayTmAcctMap() == null || storeModel.getStrPayTmAcctMap().size() <=0 ){
				pymntPaytmRButton.setVisibility(View.GONE); 
			}else{
				pymntPaytmRButton.setVisibility(View.VISIBLE); 
				if(!hasStrAcct){
					pymntPaytmRButton.setChecked(true);
					paymentGateWay="Paytm";
				}
				hasStrAcct =true;
			}
			if(hasStrAcct){
				btn_go.setEnabled(true);
				btn_go.setClickable(true);
			}else{
				btn_go.setEnabled(false);
				btn_go.setClickable(false);
			}
			for (CouponModel cpn : couponListToSort) {
				if (cpn.getCouponType().equalsIgnoreCase("PREPAID")) {
					defCoupon = cpn;
				} else if (cpn.getApplyBy().equalsIgnoreCase("DEFAULT")) {
					deflist.add(cpn);
				} else {
					offrList.add(cpn);
				}
			}
			if (deflist != null && deflist.size() > 0) {
				Collections.sort(deflist);
				couponList.addAll(deflist);
			}
			if (offrList != null && offrList.size() > 0) {
				Collections.sort(offrList);
				couponList.addAll(offrList);
			}
			if (couponList != null && couponList.size() > 0) {
				
				for (CouponModel cpn : couponList) {
					if (cpn.getCouponType().equalsIgnoreCase("PREPAID")) {
					} else {
						if(Utilities.selectedCoupons != null && Utilities.selectedCoupons.contains(cpn.getCouponCode())){
							Utilities.offerAmount = Utilities.offerAmount+cpn.getRecalculatedOfferAmount();
							
						}else{
							if(cpn.getApplyBy().equalsIgnoreCase("DEFAULT")){
								cpn.calcualteOfferAmount(true,false);
								if(cpn.getRecalculatedOfferAmount() > 0){
									Utilities.offerAmount = Utilities.offerAmount+cpn.getRecalculatedOfferAmount();
									PaymentOptionScreen.totalAmountForOffer  = PaymentOptionScreen.totalAmountForOffer  - cpn.getRecalculatedOfferAmount();
									defCouponList.add(cpn);
									Utilities.selectedCoupons.add(cpn.getCouponCode());
								}
							}else{
								cpn.calcualteOfferAmount(false,false);
								if(cpn.getRecalculatedOfferAmount() > 0){
									offCouponList.add(cpn);
								}
							}
							
						}				
					}				
				}
				couponList.clear();
				couponList.addAll(defCouponList);
				couponList.addAll(offCouponList);
				
				
				if (couponList != null && couponList.size()> 0) {
					 viewOffer.setVisibility(View.VISIBLE);
					 offerAmountLyt.setVisibility(View.VISIBLE);
					 PaymentOptionScreen.payableAmountLyt.setVisibility(View.VISIBLE);
					} else {
						 viewOffer.setVisibility(View.GONE);
						 //moreOffer.setVisibility(View.GONE);
						 offerAmountLyt.setVisibility(View.GONE);
						 PaymentOptionScreen.payableAmountLyt.setVisibility(View.GONE);
					}
			
				viewOffer.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(viewClicked){ 
							//moreClicked=false;
							viewClicked =false;
							SpannableString viewContent = new SpannableString("View/Apply Coupons");
							viewContent.setSpan(new UnderlineSpan(), 0, viewContent.length(), 0);
							viewOffer.setText(viewContent);
							
							offerLyt.setVisibility(View.GONE);
						}else{
							viewClicked=true;
							SpannableString viewContent = new SpannableString("Hide");
							viewContent.setSpan(new UnderlineSpan(), 0, viewContent.length(), 0);
							viewOffer.setText(viewContent);
							couponAdapter = new CouponAdapter(getFragmentManager(), mContext, R.layout.coupon_item_list,couponList);
							couponListView.setAdapter(couponAdapter);						
							offerLyt.setVisibility(View.VISIBLE);
						}
						
					}
				});
			}
			if (defCoupon != null){
				hasPrepaid = true;
				prepaidChk.setTag(defCoupon.getCouponCode());
				prepaidTxt.setText(" Prepaid (Remaining bal "+MenuActivity.currencySymbol + Utilities.roundto2Decimaldouble(defCoupon.getOfferedAmount()) + ")");
				prepaidAvailableAmount = Utilities.roundto2Decimaldouble(defCoupon.getOfferedAmount());
				if(prepaidAvailableAmount == 0){
					prepaidChk.setClickable(false);
					PaymentOptionScreen.prepaidTxt.setTextColor(Color.GRAY);						
				}else{
					prepaidChk.setChecked(true);
					prepaidChk.setClickable(true);
					PaymentOptionScreen.prepaidTxt.setTextColor(Color.BLACK);
				}
			}
			totalAmountTv.setText(MenuActivity.currencySymbol+"" + Utilities.roundto2Decimaldouble(totalAmount));
			ProgressClass.finishProgress();
		}
	}
	class OnLoadPrepaidBG extends AsyncTask<Void, Void, Void> {
		CouponBO couponBO;

		List<CouponModel> couponList = new ArrayList<CouponModel>();
	
		@Override
		protected void onPreExecute() {	
			couponBO = new CouponBO(mContext);
			ProgressClass.startProgress(getActivity());
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			try{
				couponList = couponBO.getAllPrepaidCoupons();
			} catch (CustomException e1) {
				e1.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			selectOption();
			RechargeCouponAdapter rechargeAdapter = new RechargeCouponAdapter( mContext,R.layout.prepaid_recharge_item, couponList,true);
			prepaidList.setAdapter(rechargeAdapter);
			if(couponList == null || couponList.size() == 0){
				coupon.setVisibility(View.GONE);
			}
			rechargePayableAmount = 0;
			usableAmount = 0;
			
			PaymentOptionScreen.deatilsLyt.setVisibility(View.GONE);
			errortxt.setText("");
			errorLyt.setVisibility(View.GONE);
			rechargePopup.show();
			
			ProgressClass.finishProgress();
			
		}
	}	
	
	private boolean rechargePrepaidCard(String gateWay){
		CouponModel modelCoupon = new CouponModel();
		modelCoupon.setCouponType("PREPAID");
		modelCoupon.setCouponPrice(rechargePayableAmount);
		modelCoupon.setUserId(Utilities.loggedInUser.getUserId());
		modelCoupon.setStoreId(Utilities.selectedStore.getStoreId());
		modelCoupon.setOfferedAmount(usableAmount);

		JSONObject prepaidJson = new JSONObject();
		List<String> sList = new ArrayList<String>();
		int Id = Utilities.loggedInUser.getUserId();
		String userIdString = String.valueOf(Id);
		sList.add(userIdString);

		try {
			prepaidJson.put("userList", sList);
			prepaidJson.put("couponIds", couponId);
			prepaidJson.put("CouponModel", modelCoupon.toJSonObject());
			prepaidJson.put("userId", Utilities.loggedInUser.getUserId());
			prepaidJson.put("storeId", Utilities.selectedStore.getStoreId());
			prepaidJson.put("payWith", gateWay);
			AddPrepaidBalance prepBal = new AddPrepaidBalance(mContext, prepaidJson);
			boolean rechargeMaid = prepBal.execute().get();
			if(rechargeMaid){
				if(gateWay.equalsIgnoreCase("Stripe")){
					StrpePaymentGateway pymntGateway = new StrpePaymentGateway(strpSetup,strpPopup,mContext, getFragmentManager(), storeModel.getDlrStripePublushKey(), 
							storeModel.getStripeSecretKey(), "payment-recharge");
					pymntGateway.showGateWay(rechargePayableAmount+"", null, prepaidJson);
				}else if(gateWay.equalsIgnoreCase("Paypal")){
					PaypalPaymentGateway pymntGateway = new PaypalPaymentGateway(mContext, getFragmentManager(), 
							storeModel.getDlrPaypalEnv(), storeModel.getDlrPaypalClientId(), "payment-recharge");
					pymntGateway.makePayment(rechargePayableAmount+"", null, prepaidJson);
				}/*else if(gateWay.equalsIgnoreCase("Paytm")){
					PaytmPaymentGateway payTmGtway = new PaytmPaymentGateway(getFragmentManager(),mContext,storeModel.getStrPayTmAcctMap(),"payment-recharge",errorLyt,errortxt);
					payTmGtway.makePayment(null,prepaidJson,Utilities.billNo+"", rechargePayableAmount+"");
				}*/
			}else{
				errortxt.setText("Error : "+prepBal.errorMessage);
				errorLyt.setVisibility(View.VISIBLE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		return isRecharged;
	}
	
	public class RunInBackground extends AsyncTask<Void, Void, Void> {
		boolean isPurchase;
		String paywith;
		public RunInBackground(boolean isPurchase,String paywith) {
			this.isPurchase = isPurchase;
			this.paywith    = paywith;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			ProgressClass.startProgress(mContext);

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(isPurchase){
				int selectedId = radioPayGrp.getCheckedRadioButtonId();
				RadioButton radioSelectedPay = (RadioButton) rootView.findViewById(selectedId);
				String payWith= null;
				if(payableAmount <=0){
					payWith="Prepaid";
				}else{
					payWith = radioSelectedPay.getText().toString().trim();
				}
				if(payWith !=null && payWith.equalsIgnoreCase("Stripe")){
					strpSetup = new StrpePaymentSetup(storeModel.getStripeSecretKey(), Utilities.total1);
					strpSetup.setUpUi(strpPopup, null, mContext);
					if(strpSetup.cardList == null || strpSetup.cardList.size() ==0){
						strpSetup.scanForCard();
					}
				}
				addTransaction();
			}else{
				if(paywith !=null && paywith.equalsIgnoreCase("Stripe")){
					strpSetup = new StrpePaymentSetup(storeModel.getDlrStripeSecretKey(), Utilities.total1);
					strpSetup.setUpUi(strpPopup, null, mContext);
				}
				rechargePrepaidCard(paywith);
			}
			
		}	
	}
	private void addTransaction(){
		Utilities.total1 =""+Utilities.roundto2Decimaldouble(payableAmount) ;
		
		List<CouponModel> selectedCouponList = new ArrayList<CouponModel>();
		for (CouponModel mdl : defCouponList) {
			if (Utilities.selectedCoupons != null && Utilities.selectedCoupons.contains(mdl.getCouponCode())) {
				if (mdl.getCouponType().equalsIgnoreCase("PREPAID")) {
					mdl.setApplicableAmount(prepaidAmount);
					mdl.setStoreId(Utilities.selectedStore.getStoreId());
				}else{
					mdl.setApplicableAmount(mdl.getRecalculatedOfferAmount());
				}
				selectedCouponList.add(mdl);
			}
		}
		String payWith="";
		
		int selectedId = radioPayGrp.getCheckedRadioButtonId();
		RadioButton radioSelectedPay = (RadioButton) rootView.findViewById(selectedId);
		if(payableAmount <=0){
			payWith="Prepaid";
		}else{
			payWith = radioSelectedPay.getText().toString().trim();
		}
		AddTransaction addTrxn = new AddTransaction(mContext, selectedCouponList,payWith);
		try {
			TransactionModel trxnModel = addTrxn.execute().get();
			if(trxnModel != null){
				boolean success =true;
				if (payableAmount > 0) {
					
					
					if(payWith.equalsIgnoreCase("Paypal")){
						PaypalPaymentGateway pymntGateway = new PaypalPaymentGateway(mContext, getFragmentManager(), 
								storeModel.getPaypalEnv(), storeModel.getPaypalClientId(), "payment-purchase");
						pymntGateway.makePayment(Utilities.total1, trxnModel, null);
					}else if(payWith.equalsIgnoreCase("Stripe")){
						StrpePaymentGateway pymntGateway = new StrpePaymentGateway(strpSetup,strpPopup,mContext,  getFragmentManager(), 
								storeModel.getStripePublushKey(), storeModel.getStripeSecretKey(), "payment-purchase");
						pymntGateway.showGateWay(Utilities.total1, trxnModel, null);
					}else if(payWith.equalsIgnoreCase("paytm")){
						/*PaytmPaymentGateway payTmGtway = new PaytmPaymentGateway(getFragmentManager(),mContext,storeModel.getStrPayTmAcctMap(),"payment-purchase",errorLyt,errortxt);
						payTmGtway.makePayment(trxnModel,null,Utilities.billNo+"", Utilities.total1);
						Log.e("ssssss","Paytm completed.......................................................................................");*/
					}
				}else{
					UpdateTransaction updTrxn = new UpdateTransaction(mContext, success);
					updTrxn.execute().get();
					defCouponList.clear();
					offCouponList.clear();
					prepaidAvailableAmount = 0;
					payableAmount = 0;
					prepaidAmount = 0;
					//appliedCouponList = new ArrayList<CouponModel>();
					Utilities.offerAmount=0;
					Utilities.selectedCoupons= new HashSet<String>();
					fragment = new PaymentStatusFragment(true, Utilities.billNo+"", trxnModel, null, true);
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
					}
				}
				
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void text(FragmentManager fragmentManager, TransactionModel trxnModel){
		Fragment fragment = new PaymentStatusFragment(true, Utilities.billNo+"", trxnModel, null, true);
		if (fragment != null) {
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			
		}
	}
}
