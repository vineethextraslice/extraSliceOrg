package com.extraslice.walknpay.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.adapter.RechargeCouponAdapter;
import com.extraslice.walknpay.adapter.WalletCardAdapter;
import com.extraslice.walknpay.bl.CouponBO;
import com.extraslice.walknpay.bl.CustomException;
import com.extraslice.walknpay.bl.StrpePaymentSetup;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.CouponModel;
import com.extraslice.walknpay.model.StoreModel;
import com.extraslice.walknpay.model.StripeCardModel;
import com.extraslice.walknpay.ui.transaction.AddPrepaidBalance;
import com.extraslice.walknpay.ui.transaction.PaypalPaymentGateway;
import com.extraslice.walknpay.ui.transaction.StrpePaymentGateway;

public class MyCards extends Fragment {
	boolean isSuccess;
	ListView strpeCards,prepaidList;
	FragmentManager fragmentManager;
	ImageView addCard;
	Dialog strpPopup;
	WalletCardAdapter adapterCard = null;
	TextView noCard, rechargeLink;
	public static TextView availableBalance;
	List<StripeCardModel> strpCardList =null;
	Context mContext;
	boolean isCustomeRecharge = true;
	public static Dialog rechargePopup;
	String currSymbol="";
	Fragment fragment;
	Button btn_cancel,btn_go;
	static boolean isSubmitted =false;
	static EditText rechargeAmount;
	public static double payableAmount =0;
	public static double usableAmount =0;
	StrpePaymentSetup strpSetup;
    String strpApiKey = null;
    StoreModel storeModel;
    TextView errortxt,minAmtTxt,subscribeText;
    LinearLayout  errorLyt,prepaidCouponLyt,customeCouponLyt;
    public static LinearLayout deatilsLyt;
    double minRechargeAmt = 0;
    RadioGroup optionRadio,paymentRadio;
    RadioButton coupon,paypalRadioButton,strpRadioButton,payTmRadioBtn;
    String paymentGateWay = "Paypal";
    public static Set<String> couponId = new HashSet<String>();
    public static TextView usableAmtTV,payableAmtTV;
    String custId;
    boolean isACH ;
    boolean haveSubscription,haveMemberSubscription,havePermission;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		isSubmitted =false;
		final View rootView = inflater.inflate(R.layout.mycards_wallet,container, false);
		try {
			
			mContext = getActivity();
			strpPopup = new Dialog(mContext);
			strpPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
			strpPopup.setContentView(R.layout.wnp_paymentscreen);
			strpPopup.setCancelable(false);
			
			subscribeText = (TextView) rootView.findViewById(R.id.subscribeText);
			
			rechargeLink = (TextView) rootView.findViewById(R.id.rechargeLink);
			availableBalance = (TextView) rootView.findViewById(R.id.availableBalance);
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
			payTmRadioBtn = (RadioButton)rechargePopup.findViewById(R.id.paytm);
			minAmtTxt = (TextView)rechargePopup.findViewById(R.id.minAmtTxt);

			coupon =(RadioButton)rechargePopup.findViewById(R.id.coupon);
			prepaidCouponLyt = (LinearLayout)rechargePopup.findViewById(R.id.prepaidCouponLyt);
			customeCouponLyt = (LinearLayout)rechargePopup.findViewById(R.id.customeCouponLyt);
			deatilsLyt = (LinearLayout)rechargePopup.findViewById(R.id.deatilsLyt);
			payableAmtTV = (TextView)rechargePopup.findViewById(R.id.payableAmtTV);
			usableAmtTV = (TextView)rechargePopup.findViewById(R.id.usableAmtTV);
		    String redString = getResources().getString(R.string.setupNewSubs);
		    subscribeText.setText(Html.fromHtml(redString));
		    strpeCards = (ListView) rootView.findViewById(R.id.strpeCards);
			noCard = (TextView) rootView.findViewById(R.id.noCard);
			addCard = (ImageView) rootView.findViewById(R.id.addCard);

			btn_cancel = (Button)rechargePopup.findViewById(R.id.btn_cancel);
			btn_go = (Button)rechargePopup.findViewById(R.id.btn_go);
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
			
			btn_go.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if (!isSubmitted) {
						isSubmitted = true;
						selectPaymentOption();
						if (isCustomeRecharge) {
							if (rechargeAmount.getText() != null && rechargeAmount.getText().toString() != null 
									&& !rechargeAmount.getText().toString().trim().equals("")) {
								try {
									payableAmount = Double.parseDouble(rechargeAmount.getText().toString().trim());
									
								} catch (Exception e) {
									payableAmount = 0;
									errortxt.setText("Please enter valid amount");
									errorLyt.setVisibility(View.VISIBLE);
								}
								if (payableAmount < minRechargeAmt) {
									errortxt.setText("Minimum should be "+ minRechargeAmt);
									errorLyt.setVisibility(View.VISIBLE);
								} else if (payableAmount > 0){
									couponId.clear();
									couponId .add("-1");
									usableAmount = payableAmount;
									new RunInBackground(false,paymentGateWay.trim()).execute();
								}
							} else {
								payableAmount = 0;
								errortxt.setText("Please enter valid amount");
								errorLyt.setVisibility(View.VISIBLE);
							}
							
						} else {
							if (payableAmount <= 0) {
								errortxt.setText("Please select a coupon");
								errorLyt.setVisibility(View.VISIBLE);
							} else {
								new RunInBackground(false,paymentGateWay.trim()).execute();
							}
						}
					}
					isSubmitted = false;
				}
			});
			
			btn_cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					btn_go.setClickable(false);
					btn_cancel.setClickable(false);
					rechargeAmount.setText("");
					payableAmount = 0;
					usableAmount = 0;
					MyCards.deatilsLyt.setVisibility(View.GONE);
					rechargePopup.dismiss();
					btn_go.setClickable(true);
					btn_cancel.setClickable(true);
				}
			});
			subscribeText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					fragment = new AddCardFragment(AddCardFragment.PURPOSE_ADD_NEW_SUBSC_STRING,strpCardList ,null,custId,strpApiKey,haveMemberSubscription,isACH);
					if (fragment != null) {
						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
					}
				}
			});
			callCardTransactionAPI();
			fragmentManager = getFragmentManager();
			
			addCard.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Fragment fragment = null;
					fragment = new AddCardFragment(AddCardFragment.PURPOSE_ADD_CARD, null,null,custId,strpApiKey,haveMemberSubscription,isACH);
					if (fragment != null) {
						fragmentManager.beginTransaction()
								.replace(R.id.frame_container, fragment)
								.addToBackStack(null).commit();
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		rechargeLink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				rechargeAmount.setText("");
				payableAmount = 0;
				usableAmount = 0;
				payableAmtTV.setText("Payable amount : "+currSymbol+0);
				usableAmtTV.setText("Usable amount : "+currSymbol+0);
				rechargeAmount.setText("");
				deatilsLyt.setVisibility(View.GONE);
				
				new RunInBackground(true,null).execute();
				errortxt.setText("");
				errorLyt.setVisibility(View.GONE);
				
			}
		});
		return rootView;
	}



	public void callCardTransactionAPI() {
		new LoadSubscriptionDetails().execute();
		if(Utilities.selectedStore == null){
			rechargeLink.setTextColor(Color.GRAY);
			rechargeLink.setClickable(false);
			rechargeLink.setEnabled(false);
			noCard.setText("No store selected");
			noCard.setVisibility(View.VISIBLE);
			paypalRadioButton.setVisibility(View.GONE); 
			strpRadioButton.setVisibility(View.GONE); 
			payTmRadioBtn.setVisibility(View.GONE); 
			btn_go.setEnabled(false);
			btn_go.setClickable(false);
			currSymbol ="";
		}else{
			rechargeLink.setTextColor(getResources().getColor(R.color.blueBaground));
			rechargeLink.setClickable(true);
			rechargeLink.setEnabled(true);
			storeModel = Utilities.selectedStore;
			minRechargeAmt = storeModel.getMinRechargeAmt();
			currSymbol = MenuActivity.currencySymbol;
			minAmtTxt.setText("Minimum "+currSymbol+minRechargeAmt);
		
			boolean hasAcct = false;
			if(Utilities.selectedStore.getDlrPaypalClientId() == null || Utilities.selectedStore.getDlrPaypalClientId().trim().equals("")){
				paypalRadioButton.setVisibility(View.GONE);  
			}else{
				paypalRadioButton.setVisibility(View.VISIBLE); 
				strpRadioButton.setChecked(true);
				hasAcct =true;
				paymentGateWay="paypal";
			}
			
			if(Utilities.selectedStore.getDlrStripeSecretKey() == null || Utilities.selectedStore.getDlrStripeSecretKey().trim().equals("")){
				strpRadioButton.setVisibility(View.GONE); 
			}else{
				strpRadioButton.setVisibility(View.VISIBLE); 
				if(!hasAcct){
					paymentGateWay="Stripe";
					strpRadioButton.setChecked(true);
				}
				hasAcct =true;
			}
			if(Utilities.selectedStore.getDlrPayTmAcctMap() == null || Utilities.selectedStore.getDlrPayTmAcctMap().size() <= 0){
				payTmRadioBtn.setVisibility(View.GONE); 
			}else{
				payTmRadioBtn.setVisibility(View.VISIBLE); 
				if(!hasAcct){
					paymentGateWay="Paytm";
					payTmRadioBtn.setChecked(true);
				}
				hasAcct =true;
			}
			if(hasAcct){
				btn_go.setEnabled(true);
				btn_go.setClickable(true);
			}else{
				btn_go.setEnabled(false);
				btn_go.setClickable(false);
			}
			
			new LoadPrepaidBalance().execute();
			
		}
		
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
		payableAmount = 0;
		usableAmount = 0;
		payableAmtTV.setText("Payable amount : "+currSymbol+0);
		usableAmtTV.setText("Usable amount : "+currSymbol+0);
		rechargeAmount.setText("");
		deatilsLyt.setVisibility(View.GONE);
		int selected = optionRadio.getCheckedRadioButtonId();
		couponId.clear(); 
		RadioButton selectedButton = (RadioButton) rechargePopup.findViewById(selected);
		if (selectedButton != null){
			if(selectedButton.getText().toString().trim().equalsIgnoreCase("Custom")) {
				isCustomeRecharge = true;
				prepaidCouponLyt.setVisibility(View.GONE);
				customeCouponLyt.setVisibility(View.VISIBLE);
			}else{
				isCustomeRecharge = false;
				prepaidCouponLyt.setVisibility(View.VISIBLE);
				customeCouponLyt.setVisibility(View.GONE);;
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
	
	private boolean rechargePrepaidCard(String gateWay){
		CouponModel modelCoupon = new CouponModel();
		modelCoupon.setCouponType("PREPAID");
		modelCoupon.setCouponPrice(payableAmount);
		modelCoupon.setUserId(Utilities.loggedInUser.getUserId());
		modelCoupon.setStoreId(storeModel.getStoreId());
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
					StrpePaymentGateway pymntGateway = new StrpePaymentGateway(strpSetup,strpPopup,mContext,getFragmentManager(), storeModel.getDlrStripePublushKey(), 
							storeModel.getDlrStripeSecretKey(), "wallet-recharge");
					pymntGateway.showGateWay(payableAmount+"", null, prepaidJson);
					
					
				}else if(gateWay.equalsIgnoreCase("Paypal")){
					PaypalPaymentGateway pymntGateway = new PaypalPaymentGateway(mContext, getFragmentManager(), 
							storeModel.getDlrPaypalEnv(), storeModel.getDlrPaypalClientId(), "wallet-recharge");
					pymntGateway.makePayment(payableAmount+"", null, prepaidJson);
				}else if(gateWay.equalsIgnoreCase("paytm")){
					

					/*PaytmPaymentGateway payTmGtway = new PaytmPaymentGateway(getFragmentManager(),mContext,storeModel.getDlrPayTmAcctMap(),"wallet-recharge",errorLyt,errortxt);
					payTmGtway.makePayment(null,prepaidJson,Utilities.billNo+"", payableAmount+"");*/
				}
			}else{
				errortxt.setText("Error : "+prepBal.errorMessage);
				errorLyt.setVisibility(View.VISIBLE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		return true;
	}
	public class RunInBackground extends AsyncTask<Void, Void, Void> {
		String paywith;
		boolean loadInitialdata;
		public RunInBackground(boolean loadInitialdata,String paywith) {
			this.paywith         = paywith;
			this.loadInitialdata = loadInitialdata;
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
			if(loadInitialdata){
				try {
					new LoadRechargeData().execute().get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				if(paywith.equalsIgnoreCase("Stripe")){
					strpSetup = new StrpePaymentSetup(storeModel.getDlrStripeSecretKey(), usableAmount + "");
					strpSetup.setUpUi(strpPopup, null, mContext);
					if(strpSetup.cardList == null || strpSetup.cardList.size() ==0){
						strpSetup.scanForCard();
					}
				}
				
				rechargePrepaidCard(paywith);
			}
		}	
	}
	
	class LoadRechargeData extends AsyncTask<Void, Void, Void> {
		
		List<CouponModel> couponList=null;
		@Override
		protected void onPreExecute() {	
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			CouponBO couponBO = new CouponBO(mContext);
			
			try {
				couponList = couponBO.getAllPrepaidCoupons();
			} catch (CustomException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			RechargeCouponAdapter rechargeAdapter = new RechargeCouponAdapter( mContext,R.layout.prepaid_recharge_item, couponList,false);
			prepaidList.setAdapter(rechargeAdapter);
			if(couponList == null || couponList.size() == 0){
				coupon.setVisibility(View.GONE);
			}
			super.onPostExecute(result);
			rechargePopup.show();
			selectOption();
			ProgressClass.finishProgress();
		}
	}
	class LoadPrepaidBalance extends AsyncTask<Void, Void, Void> {
	
		Object responseJson = null;
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			CouponBO couponBO = new CouponBO(mContext);
			
			try {
				responseJson = couponBO.getPrepaidBalance();
			} catch (CustomException e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			
			if (responseJson != null) {
				double amount = Double.parseDouble(responseJson.toString());
				availableBalance.setText("Balance("+currSymbol+") is :"+ Utilities.roundto2Decimaldouble(amount) + "");
			}
			super.onPostExecute(result);
			ProgressClass.finishProgress();
		}
	}
	class LoadSubscriptionDetails extends AsyncTask<Void, Void, Void> {
		
		JSONObject responseJson = null;
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			com.app.extraslice.bo.CustAcctBO custBO = new com.app.extraslice.bo.CustAcctBO(mContext);
			
			try {
				responseJson = custBO.getStripeCardDetails(mContext, com.app.extraslice.utils.Utilities.loggedInUser.getUserId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try{
				if (responseJson == null ) {
					noCard.setVisibility(View.VISIBLE);
					noCard.setText("Error while retriving card details");
				} else{
					if (responseJson.getString(Utilities.STATUS_STRING).equals(Utilities.STATUS_SUCCESS)) {
						if(com.app.extraslice.utils.Utilities.loggedInUser.getUserType().equalsIgnoreCase("member")){
							haveSubscription = responseJson.getBoolean("haveSubscription");
							haveMemberSubscription = responseJson.getBoolean("haveMemberSubscription");
							havePermission = responseJson.getBoolean("havePermission");
							isACH =  responseJson.getBoolean("isACH");
							try{
								strpApiKey=com.app.extraslice.utils.Utilities.decode(responseJson.getString("strpApiKey"));
							}catch(Exception e){
								
							}
							try{
								custId=responseJson.getString("custId");
							}catch(Exception e){
								
							}
							
							boolean subscriptionBySameUser = false;
							if(haveSubscription){
								subscriptionBySameUser =  responseJson.getBoolean("subscriptionBySameUser");
							}
							if(haveMemberSubscription){
								subscribeText.setVisibility(View.GONE);
							}else{
								if(!isACH){
									subscribeText.setVisibility(View.VISIBLE);
								}else{
									subscribeText.setVisibility(View.GONE);
								}
								
							}
							JSONArray cardArray = responseJson.getJSONArray("cardList");
							strpCardList = new ArrayList<StripeCardModel>();
							for(int index=0;index < cardArray.length();index++){
								JSONObject obj = (JSONObject)cardArray.get(index);
								StripeCardModel cardModel = new StripeCardModel().jSonToObject(obj.toString());
								strpCardList.add(cardModel);
							}
							if (strpCardList != null && strpCardList.size() > 0) {
								noCard.setVisibility(View.INVISIBLE);
								adapterCard = new WalletCardAdapter(fragmentManager, mContext,R.layout.card_item_list, 
										strpCardList,haveSubscription,haveMemberSubscription,havePermission, subscriptionBySameUser,custId,strpApiKey,isACH);
								if (adapterCard != null) {
									strpeCards.setAdapter(adapterCard);
								}
							} else {
								noCard.setVisibility(View.VISIBLE);
								noCard.setText("You have no cards on file!");
		
							}
						}else{
							
							try{
								strpApiKey=com.app.extraslice.utils.Utilities.decode(responseJson.getString("strpApiKey"));
							}catch(Exception e){
								
							}
							try{
								custId=responseJson.getString("custId");
							}catch(Exception e){
								
							}
							
							
								subscribeText.setVisibility(View.GONE);
							
							JSONArray cardArray = responseJson.getJSONArray("cardList");
							strpCardList = new ArrayList<StripeCardModel>();
							for(int index=0;index < cardArray.length();index++){
								JSONObject obj = (JSONObject)cardArray.get(index);
								StripeCardModel cardModel = new StripeCardModel().jSonToObject(obj.toString());
								strpCardList.add(cardModel);
							}
							if (strpCardList != null && strpCardList.size() > 0) {
								noCard.setVisibility(View.INVISIBLE);
								adapterCard = new WalletCardAdapter(fragmentManager, mContext,R.layout.card_item_list, 
										strpCardList, custId,strpApiKey);
								if (adapterCard != null) {
									strpeCards.setAdapter(adapterCard);
								}
							} else {
								noCard.setVisibility(View.VISIBLE);
								noCard.setText("You have no cards on file!");
		
							}
						}
						
					}else{
						noCard.setVisibility(View.VISIBLE);
						noCard.setText(responseJson.getString(Utilities.ERROR_MESSAGE));
					}
				}
			}catch(Exception e){
				noCard.setVisibility(View.VISIBLE);
				noCard.setText(e.getMessage());
			}
			ProgressClass.finishProgress();
		}
	}
	
	
}
