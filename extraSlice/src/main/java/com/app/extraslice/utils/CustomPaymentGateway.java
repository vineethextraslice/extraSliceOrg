package com.app.extraslice.utils;

import io.card.payment.CardIOActivity;
import io.card.payment.CardType;
import io.card.payment.CreditCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.extraslice.R;
import com.app.extraslice.dao.CustAcctDAO;
import com.app.extraslice.model.CustAcctModel;
import com.app.extraslice.model.PlanOfferModel;
import com.app.extraslice.model.ReservationModel;
import com.app.extraslice.model.ResourceTypeModel;
import com.app.extraslice.model.UserModel;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;

public abstract class CustomPaymentGateway {

	public static String DISPLAY_PYMNT_DESCRIPTION     = "";
	
	public static String PLAN_NAMES        = "";
	public static double PAYABLE_AMOUNT      	   = 0;
	public static String STRIPE_PAYMENT_DESCRIPTION = "";
	public static 			int USER_ID             = -1;
	public static 		 String USER_NAME           = "";
	
	public static  String PURPOSE="";
	
	public static final String PURPOSE_ADD_USER="ADD_USER";
	public static final String PURPOSE_MAKE_REC_PAYMENT="MAKE_REC_PAYMENT";
	public static final String PURPOSE_CANCEL_PAYMENT_BYERROR="CANCEL_PAYMENT_BYERROR";
	public static final String PURPOSE_CANCEL_PAYMENT_BYUSER="CANCEL_PAYMENT_BYUSER";
	public static final String PURPOSE_UPDATE_INI_PAYMENT="PURPOSE_UPDATE_INI_PAYMENT";
	public static final String PURPOSE_MAKE_INI_PAYMENT="PURPOSE_MAKE_INI_PAYMENT";
	public static final String PURPOSE_NEW_RESERVATION="PURPOSE_NEW_RESERVATION";
	public static final String PURPOSE_UPDATE_RESERVATION="PURPOSE_UPDATE_RESERVATION";
	public UserModel userModel;
	public String custId =null;
	public PlanOfferModel offerModel;
	boolean addUser ;
	public List<ResourceTypeModel> addOnsList;
	public static boolean completeRecPayment = true;
	public static boolean completeIniPayment = true;
	public static String STRP_API_KEY="";
	static Context mContext;
	static StrpePaymentSetup strpSetup;
	static Dialog strpPopup;

	static CustomPaymentGateway thisInstance;

	String paymentStatus = null;
	public ReservationModel resModel;
	List<Integer> planNameList = new ArrayList<Integer>();
	String errorMessage = null;
	String selectedPlanNames=null;
	JSONObject resUdateStstus;
	public static CustomPaymentGateway getInstance(){
		return thisInstance;
	}
	public CustomPaymentGateway(Context context,UserModel userModel,List<ResourceTypeModel> selectedAddOnsList,PlanOfferModel offerModel,ReservationModel resModel) {
		Log.e("STRP_API_KEY", STRP_API_KEY);
		this.mContext = context;
		this.userModel=userModel;
		this.addOnsList=selectedAddOnsList;
		this.offerModel=offerModel;
		this.resModel=resModel;
		setStripeParameter(context);
		thisInstance = this;	
		
	}
	private void setStripeParameter(Context context){
		this.mContext = context;
		strpPopup = new Dialog(mContext);
		strpPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		strpPopup.setContentView(R.layout.paymentscreen);
		strpPopup.setCancelable(false);
		
		this.strpSetup = new StrpePaymentSetup(USER_NAME,USER_ID, PAYABLE_AMOUNT + "");
		
		try {
			Stripe.apiKey = STRP_API_KEY; 
			Stripe.getApiBase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.strpSetup.setUpUi(strpPopup, mContext,DISPLAY_PYMNT_DESCRIPTION);
		scanForCard();
	}
	public void makeStripePayment() {
		if(PURPOSE.equals(PURPOSE_ADD_USER)){
			addUser = true;
		}
		
		if(strpPopup != null && !strpPopup.isShowing()){
			strpPopup.show();
		}
		
		Button saveButton = (Button) strpPopup.findViewById(R.id.submit);
		Button cancelButton = (Button) strpPopup.findViewById(R.id.cancel);
		Button saveButtonSel = (Button) strpPopup.findViewById(R.id.submit_selected);
		Button cancelButtonSel = (Button) strpPopup.findViewById(R.id.cancel_selected);
		LinearLayout scanCard1Lyt = (LinearLayout) strpPopup.findViewById(R.id.scanCard1Lyt);
		scanCard1Lyt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				scanForCard();
			}
		});
		RelativeLayout layoutAddCard = (RelativeLayout) strpPopup.findViewById(R.id.rlTouch);
		layoutAddCard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//hideVirtualKeyboard();
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateTransaction(null,true,strpPopup);
				
			}
		});
		cancelButtonSel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateTransaction(null,true,strpPopup);
				
			}
		});

		saveButtonSel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				submitClicked();
			}
			
		});
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				strpPopup.hide();
				if(strpSetup.checkandSetCardDetails()){
					try {
						submitClicked();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		

	}
	public void submitClicked(){
		if(strpSetup.checkandSetCardDetails()){
			try {
				if(PURPOSE.equalsIgnoreCase(PURPOSE_ADD_USER) || PURPOSE.equalsIgnoreCase(PURPOSE_NEW_RESERVATION)
						||  PURPOSE.equalsIgnoreCase(PURPOSE_UPDATE_RESERVATION)){
					Map<String, Object> inputParams = new HashMap<String, Object>();
					Map<String, Object> cardParams = new HashMap<String, Object>();
					cardParams.put("number", strpSetup.cardNum);
					cardParams.put("exp_month", strpSetup.expMonth);
					cardParams.put("exp_year", strpSetup.expYear);
					cardParams.put("cvc", strpSetup.cvv);
					if (strpSetup.name != null && !strpSetup.name.trim().equals("")) {
						cardParams.put("name", strpSetup.name);
					}
					inputParams.put("card", cardParams);
					new CreateCardToken(inputParams).execute();
				}else if(PURPOSE.equalsIgnoreCase(PURPOSE_MAKE_INI_PAYMENT)){
					if(custId !=null && !custId.trim().isEmpty()){
						new ChargeTheCard(custId).execute();
					}else{
						new GetESliceCustomer(USER_ID).execute();
					}
					
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			updateTransaction(null, false, strpPopup);
		}
	}

	public abstract void updateTransaction(Object responseObj,boolean forced,Dialog strpPopup);
	
	class RetrieveStrpCustomer extends AsyncTask<Void, Void, Void> {
		Token token;
		Charge charge;
		String custId;
		Customer customer;
		double amount;
		int userId;
		public RetrieveStrpCustomer(String custId,double amount,int userId) {
			this.custId = custId;
			this.amount = amount;
			this.userId = userId;
		}
		
		@Override
		protected void onPreExecute() {	
			//ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				customer = Customer.retrieve(custId);
			} catch (Exception e) {
				errorMessage = e.getMessage();
			} 

			return null;
		}
		@Override
		protected void onPostExecute(Void cst) {
			super.onPostExecute(cst);
			try{
				new ChargeTheCard(customer.getId()).execute();
			}catch(Exception e){
				strpSetup.errorMessage = e.getMessage();
				updateTransaction(null, false, strpPopup);
			}
		}
	}
	
	public class CreateCardToken extends AsyncTask<Void, Void, Void> {
		Map<String, Object> inputParams;
		Token token;
		public CreateCardToken(Map<String, Object> inputParams) {
			this.inputParams = inputParams;
		}
		
		@Override
		protected void onPreExecute() {
			errorMessage = null;
			super.onPreExecute();
			ProgressClass.startProgress(mContext);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				token = Token.create(inputParams); 
			} catch (Exception e) {
				errorMessage = e.getMessage();
			} 

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(errorMessage == null){
				if(token !=null){
					updateTransaction(token,false,strpPopup);
				}else{
					strpSetup.errorMessage="Failed to create card token";
					updateTransaction(null,false,strpPopup);
				}
				
			}else{
				strpSetup.errorMessage=errorMessage;
				updateTransaction(null,false,strpPopup);
			}
			
		}
	}
	
	public class ChargeTheCard extends AsyncTask<Void, Void, Void> {
		Map<String, Object> inputParams;
		String customerId;
		Charge charge;
		public ChargeTheCard(String customerId) {
			this.inputParams = new HashMap<String, Object>();
			inputParams.put("amount", (int)(PAYABLE_AMOUNT*100));
			inputParams.put("currency", "usd");
			inputParams.put("customer", customerId);
			inputParams.put("description", "Charge for " + STRIPE_PAYMENT_DESCRIPTION);
			Map<String, String> initialMetadata = new HashMap<String, String>();
			initialMetadata.put("order_id", STRIPE_PAYMENT_DESCRIPTION);
			if(userModel != null && userModel.getUserName() != null){
				initialMetadata.put("user", userModel.getUserName());
			}else if(USER_NAME != null && USER_NAME != null){
				initialMetadata.put("user",USER_NAME);
			}
			if(selectedPlanNames != null){
				initialMetadata.put("Plans",selectedPlanNames);
			}
			if(addOnsList != null && addOnsList.size() > 0){
				String addonNames = "";
		    	boolean isFirst = true;
		    	for(ResourceTypeModel resT: addOnsList){
		    		if(isFirst){
		    			addonNames= resT.getResourceTypeName();
		    			isFirst=false;
		    		}else{
		    			addonNames= addonNames+","+resT.getResourceTypeName();
		    		}
				}
				initialMetadata.put("Addons",addonNames);
			}
			inputParams.put("metadata", initialMetadata);
		}
		
		@Override
		protected void onPreExecute() {
			errorMessage = null;
			super.onPreExecute();
			ProgressClass.startProgress(mContext);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// test account
			try {
				charge = Charge.create(inputParams);
			} catch (Exception e) {
				errorMessage = e.getMessage();
			} 

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(errorMessage == null){
				if(charge !=null){
					updateTransaction(charge,false,strpPopup);
				}else{
					strpSetup.errorMessage="Failed to charge the card";
					updateTransaction(null,false,strpPopup);
				}
			}else{
				strpSetup.errorMessage=errorMessage;
				updateTransaction(null,false,strpPopup);
			}
			
		}
	}
	
	
	
	
	class GetESliceCustomer extends AsyncTask<Void, Void, Void> {
		JSONObject acctObj;		
		int userId=-1;
		CustAcctDAO custDAo = new CustAcctDAO();
		public GetESliceCustomer(int userId){
			this.userId = userId;
		}
		
		@Override
		protected void onPreExecute() {	
			//ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			if( userId > 0 ){
				acctObj = custDAo.getUserAccount(mContext, userId, STRP_API_KEY);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void cst) {
			super.onPostExecute(cst);
			try{
				if (acctObj == null ) {
				} else if (((String) acctObj.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
					CustAcctModel model = new CustAcctModel().jSonToObject(acctObj.get("Account").toString());
					//new RetrieveStrpCustomer(model.getCustomerId(),amount,userId).execute();
					new ChargeTheCard( model.getCustomerId()).execute();
				} 
			}catch(Exception e){
				strpSetup.errorMessage = e.getMessage();
				updateTransaction(null, false, strpPopup);
			}
		}
	}

	
	
	public void showError(){
		strpSetup.errortxt.setText(StrpePaymentSetup.errorMessage);
		strpSetup.errorLyt.setVisibility(View.VISIBLE);
		if(strpPopup != null){
			strpPopup.show();
		}
	}
	
	
	public void showError(String errorMsg){
		strpSetup.errortxt.setText(errorMsg);
		strpSetup.errorLyt.setVisibility(View.VISIBLE);
		if(strpPopup != null){
			strpPopup.show();
		}
		
	}
	private void scanForCard(){
		 com.extraslice.walknpay.bl.Utilities.SCAN_FOR=com.extraslice.walknpay.bl.Utilities.SCAN_FOR_WLT_CARD;
		 	strpSetup.carText1.setText("");
		 	strpSetup.carText2.setText("");
		 	strpSetup.carText3.setText("");
		 	strpSetup.cardTpImgView.setImageBitmap(null);
		 	strpSetup.carText1.setVisibility(View.GONE);
		 	strpSetup.carText2.setVisibility(View.GONE);
		 	strpSetup.carText3.setVisibility(View.GONE);
		 	strpSetup.cardTpImgView.setVisibility(View.GONE);
		 	strpSetup.errortxt.setText("");
		 	strpSetup.errorLyt.setVisibility(View.GONE);
		 	strpSetup.cvv =null;
		 	strpSetup.expMonth=0;
		 	strpSetup.expYear=0;
		 	strpSetup.cardNum=null;
			Intent intent = new Intent(mContext, CardIOActivity.class)
	        	.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
	        	.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true)
	        	.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV,true)
	        	.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true)
	        	.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true)
	        	.putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, Color.parseColor("#268CAB"));
			((Activity)mContext).startActivityForResult(intent, strpSetup.REQUEST_SCAN);
	    }
	    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	//((Activity)context).onActivityResult(requestCode, resultCode, data);
	       
	    	Log.e("ffffffffffffffffffff", "onActivityResult");
	        String text1 = new String();
	        String text2 = new String();
	        String text3 = new String();
	        Bitmap cardTypeImage = null;

	        if ((requestCode == strpSetup.REQUEST_SCAN || requestCode == strpSetup.REQUEST_AUTOTEST) && data != null
	                && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
	            CreditCard result = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
	            if (result != null) {
	            	CardType cardType = result.getCardType();
	            	cardTypeImage = cardType.imageBitmap(mContext);
	            	
	            	text1 += result.getRedactedCardNumber() + " ";
	            	text2 += result.expiryMonth + "/" + result.expiryYear+ " ";
	            	text3 += result.cvv ;
	                //outStr += result.cardNumber ;
	            	strpSetup.cvv=result.cvv ;
	            	strpSetup.expMonth=result.expiryMonth;
	            	strpSetup.expYear=result.expiryYear;
	            	strpSetup.cardNum=result.cardNumber;
	            }
	            strpSetup.carText1.setText(text1);
	            strpSetup.carText2.setText(text2);
	            strpSetup.carText3.setText(text3);
	            strpSetup.carText1.setVisibility(View.VISIBLE);
	            strpSetup.carText2.setVisibility(View.VISIBLE);
	            strpSetup.carText3.setVisibility(View.VISIBLE);
	            strpSetup.cardTpImgView.setVisibility(View.VISIBLE);
	            strpSetup.cardTpImgView.setImageBitmap(cardTypeImage);
	            thisInstance.submitClicked();
	        } else if (resultCode == Activity.RESULT_CANCELED) {
	           
	        }
	        
	    }
}
