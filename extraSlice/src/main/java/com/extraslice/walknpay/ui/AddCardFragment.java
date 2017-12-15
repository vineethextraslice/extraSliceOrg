package com.extraslice.walknpay.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.extraslice.R;
import com.app.extraslice.adapter.CustomAdapter;
import com.app.extraslice.bo.CustAcctBO;
import com.app.extraslice.utils.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.StripeCardModel;
import com.stripe.Stripe;
import com.stripe.model.Card;
import com.stripe.model.Customer;
import com.stripe.model.Token;

import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.card.payment.CardIOActivity;
import io.card.payment.CardType;
import io.card.payment.CreditCard;


public class AddCardFragment extends Fragment {
	String custId=null;
	String apiKey = null;
	Button cancel, ok, dismiss;
	TextView errortxtView;
	Context mContext;
	String errorMessage = "";
	LinearLayout layoutAddCard;
	Button saveButton, cancelButton;
	ImageView cardTpImgView;
	//EditText cardNumber, cardHolder;
	//EditText cvc;
	TextView cardText,subscribeText,carText1,carText2,carText3,addNewCardBtn;
	FragmentManager fragmentManager;
	//Spinner monthSpinner, yearSpinner,extCardList;
	Spinner  extCardList;
	LinearLayout errorLyt,linear5;
	Boolean isInternetPresent = false;
	StripeCardModel cardModel = null;
	LinearLayout cardDetlLyt,subscriptionLyt;
	LinearLayout extCardLyt,scanCard1Lyt;
	String cardHolderNameVal;
	String cardNoVal;
	String cvvVal;
	int expMonth ;
	int expYear ;
	boolean isACH;
	String selectedCardId=null;
	List<StripeCardModel> extCards=null;
	public static final String PURPOSE_ADD_CARD="PURPOSE_ADD_CARD";
	public static final String PURPOSE_EDIT_CARD="PURPOSE_EDIT_CARD";
	public static final String PURPOSE_CHANGE_DEF_CARD="PURPOSE_CHANGE_DEF_CARD";
	public static final String PURPOSE_ADD_NEW_SUBSC_STRING="PURPOSE_ADD_NEW_SUBSC_STRING";
	boolean havePlanSubscription = false;
	String purpose;
	CheckBox selSubscr;
    private static final int REQUEST_SCAN = 100;
    private static final int REQUEST_AUTOTEST = 200;
	public AddCardFragment(String purpose,List<StripeCardModel> extCards,StripeCardModel model,String custId,String apiKey,boolean havePlanSubscription,boolean isACH) {
		this.cardModel = model;
		this.custId = custId;
		this.apiKey=apiKey;
		this.extCards =extCards;
		this.purpose = purpose;
		this.isACH=isACH;
		this.havePlanSubscription = havePlanSubscription;
	}
	public AddCardFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.addcard, container, false);

		mContext = this.getActivity();
		fragmentManager = getFragmentManager();
		this.saveButton = (Button) rootView.findViewById(R.id.submit);
		this.cancelButton = (Button) rootView.findViewById(R.id.cancel);
		this.addNewCardBtn = (TextView) rootView.findViewById(R.id.addNewCardBtn);
		this.cardTpImgView = (ImageView) rootView.findViewById(R.id.result_card_type_image);
		this.cardDetlLyt = (LinearLayout) rootView.findViewById(R.id.cardDetlLyt);
		this.extCardLyt = (LinearLayout) rootView.findViewById(R.id.extCardLyt);
		this.scanCard1Lyt = (LinearLayout) rootView.findViewById(R.id.scanCard1Lyt);
		/*this.cardNumber = (EditText) rootView.findViewById(R.id.cardnumber);
		this.cardHolder = (EditText) rootView.findViewById(R.id.cardholder);
		this.cvc = (EditText) rootView.findViewById(R.id.cvc);
		this.yearSpinner = (Spinner) rootView.findViewById(R.id.expYear);
		this.monthSpinner = (Spinner) rootView.findViewById(R.id.expMonth);*/
		this.extCardList = (Spinner)rootView.findViewById(R.id.extCardList);
		this.errorLyt = (LinearLayout) rootView.findViewById(R.id.textLinear);
		this.errortxtView = (TextView) rootView.findViewById(R.id.errortxt);
		this.carText1 = (TextView) rootView.findViewById(R.id.carText1);
		this.carText2 = (TextView) rootView.findViewById(R.id.carText2);
		this.carText3 = (TextView) rootView.findViewById(R.id.carText3);
		this.layoutAddCard = (LinearLayout) rootView.findViewById(R.id.rlTouch);
		this.cardText = (TextView) rootView.findViewById(R.id.cardText);
		this.selSubscr =(CheckBox)rootView.findViewById(R.id.selSub);
		this.subscriptionLyt =(LinearLayout)rootView.findViewById(R.id.subscriptionLyt);
		this.subscribeText =(TextView)rootView.findViewById(R.id.subscribeText);
		this.linear5 = (LinearLayout)rootView.findViewById(R.id.linear5);
	   
		/* To hide virtual keyboard when clicked on layout */
		layoutAddCard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideVirtualKeyboard();
			}
		});
		scanCard1Lyt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				scanForCard();
			}
		});
		/* cancel button onClickListener to go back to the previous fragment */
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				goBackToWallet();
			}
		});
		if(Utilities.loggedInUser.getUserType().equalsIgnoreCase("guest")){
			subscriptionLyt.setVisibility(View.GONE);
		}else if(this.purpose.equalsIgnoreCase(PURPOSE_ADD_NEW_SUBSC_STRING)){
			subscriptionLyt.setVisibility(View.GONE);
		}else{
			if(!isACH){
				subscriptionLyt.setVisibility(View.VISIBLE);
				if(havePlanSubscription){
					if(this.purpose.equalsIgnoreCase(PURPOSE_CHANGE_DEF_CARD)){
						subscriptionLyt.setVisibility(View.GONE);
					}else{
						String redString = getResources().getString(R.string.changeSubs);
					    subscribeText.setText(Html.fromHtml(redString));
					}
				}else{
					String redString = getResources().getString(R.string.addCardToNewSubs);
				    subscribeText.setText(Html.fromHtml(redString));
				}	
			}else{
				subscriptionLyt.setVisibility(View.GONE);
			}
			
		}
		 
		if(this.purpose.equalsIgnoreCase(PURPOSE_CHANGE_DEF_CARD) || this.purpose.equalsIgnoreCase(PURPOSE_ADD_NEW_SUBSC_STRING)){
			if(this.purpose.equalsIgnoreCase(PURPOSE_CHANGE_DEF_CARD)){
				//saveButton.setText("Change card");
				cardText.setText("Change card");
			}else{
				//saveButton.setText("Add Subscription");
				cardText.setText("Add Subscription");
			}
			
			if(extCards != null && extCards.size()>0){
				this.extCardLyt.setVisibility(View.VISIBLE);
				List<Map<String, Object>> cardMapList = new ArrayList<Map<String,Object>>();
				Map<String, Object> obj = new HashMap<String, Object>();
				obj.put("Select card", "Select card");
				cardMapList.add(obj);
				for(StripeCardModel crdMdl : extCards){
					obj = new HashMap<String, Object>();
					obj.put("XXXX-XXXX-XXXX-"+crdMdl.getLast4(), crdMdl);
					cardMapList.add(obj);
				}
				CustomAdapter cst = new CustomAdapter(mContext, cardMapList);
				this.extCardList.setAdapter(cst);
				this.extCardList.setOnItemSelectedListener(new OnItemSelectedListener() {
	
					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int position, long id) {
						if(position==0){
							cardDetlLyt.setVisibility(View.VISIBLE);
							linear5.setVisibility(View.VISIBLE);
							selectedCardId = null;
						}else{
							cardDetlLyt.setVisibility(View.GONE);
							linear5.setVisibility(View.GONE);
							StripeCardModel cardModel = (StripeCardModel)view.getTag();
							selectedCardId = cardModel.getCardId();
						}
						
					}
	
					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						cardDetlLyt.setVisibility(View.VISIBLE);
						selectedCardId = null;
						
					}
				});
			}else{
				this.extCardLyt.setVisibility(View.GONE);
				scanForCard();
			}
		}else{
			this.extCardLyt.setVisibility(View.GONE);
			if(this.purpose.equalsIgnoreCase(PURPOSE_ADD_CARD)){
				//saveButton.setText("Add card");
				cardText.setText("Add card");
			}else{
				selectedCardId = cardModel.getCardId();
				//saveButton.setText("Update card");
				cardText.setText("Update card");
			}
			scanForCard();
			
		}

		/* month spinner value */
		/*List<String> listMonth = new ArrayList<String>();
		fillMonths(listMonth);
		ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(getActivity(), R.layout.wnp_spinner_item, listMonth);
		monthSpinner.setAdapter(adapterMonth);*/
		
		/* year spinner value */
		/*List<String> listYear = new ArrayList<String>();
		fillYear(listYear);
		ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(getActivity(), R.layout.wnp_spinner_item, listYear);
		yearSpinner.setAdapter(adapterYear);*/

		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if((purpose.equalsIgnoreCase(PURPOSE_ADD_NEW_SUBSC_STRING) || purpose.equalsIgnoreCase(PURPOSE_CHANGE_DEF_CARD)) && selectedCardId != null){
					try {
						new ProcessCard().execute();
					} catch (Exception e) {
						showErrorMessage("Failed to add card. "+e.getLocalizedMessage());
					}
				}else{
					if(cvvVal == null || cvvVal.trim().equals("") || expMonth<=0 || expYear <=0 
							|| cardNoVal == null || cardNoVal.trim().equals("")){
						showErrorMessage("Enter card details!");
					}else{
						try {
							new ProcessCard().execute();
						} catch (Exception e) {
							showErrorMessage("Failed to add card. "+e.getLocalizedMessage());
						} 
					}
				}
			}
		});

		if (this.cardModel != null) {/*
			cardHolder.setText(cardModel.getName());
			cardNumber.setText("XXXXXXXXXXXX"+cardModel.getLast4());
			this.cardNumber.setEnabled(false);
			this.cvc.setEnabled(false);
			int posYear = listYear.indexOf(cardModel.getExpYear()+"");
			String cardYear = cardModel.getExpYear()+"";
			for (int j = 0; j < listYear.size(); j++) {
				if (cardYear.equals(listYear.get(j))) {
					posYear = j;
				}
			}
			monthSpinner.setSelection( cardModel.getExpMonth());
			yearSpinner.setSelection(posYear);
		*/} 
		return rootView;

	}

	protected void hideVirtualKeyboard() {
		try {
			final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private void fillMonths(List<String> listMonth){
		listMonth.add("Month");
		listMonth.add("January");
		listMonth.add("February");
		listMonth.add("March");
		listMonth.add("April");
		listMonth.add("May");
		listMonth.add("June");
		listMonth.add("July");
		listMonth.add("August");
		listMonth.add("September");
		listMonth.add("October");
		listMonth.add("November");
		listMonth.add("December");
	}
	private void fillYear(List<String> listYear){
		listYear.add("Year");
		Calendar calendar = Calendar.getInstance();
		for (int i = calendar.get(Calendar.YEAR); i < (calendar.get(Calendar.YEAR) + 35); i++) {
			String year = String.valueOf(i);
			listYear.add(year);
		}
	}
	private void goBackToWallet(){
		hideVirtualKeyboard();
		Fragment fragment = null;
		fragment = new WalletFragment(1);
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

		}
	}
	
	private void showErrorMessage(String message){
		errorLyt.setVisibility(View.VISIBLE);
		errortxtView.setText(message);
	}
	
	class ProcessCard extends AsyncTask<Void, Void, Void> {
		
		String errorMessage =null;
		JSONObject responseJson = null;
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			try{
				Stripe.apiKey=apiKey;
				Customer cu = null;
				CustAcctBO custBO = new CustAcctBO(mContext);
				if(custId ==null){
					Map<String, Object> custParams = new HashMap<String, Object>();
					custParams.put("email", Utilities.loggedInUser.getUserName());
					cu = Customer.create(custParams);
					custBO.addCutomerAccount(mContext, Utilities.loggedInUser.getUserId(),apiKey,cu.getId());
				}else{
					cu = Customer.retrieve(custId);
				}
				if(selectedCardId == null || selectedCardId.trim().equals("")){
					Map<String, Object> inputParams = new HashMap<String, Object>();
					Map<String, Object> cardParams = new HashMap<String, Object>();
					cardParams.put("number", cardNoVal);
					cardParams.put("exp_month", expMonth);
					cardParams.put("exp_year", expYear);
					cardParams.put("cvc", cvvVal);
					if (cardHolderNameVal != null && !cardHolderNameVal.trim().equals("")) {
						cardParams.put("name", cardHolderNameVal);
					}
					inputParams.put("card", cardParams);
					Token token = Token.create(inputParams);
					inputParams = new HashMap<String, Object>();
					inputParams.put("source", token.getId());
					Card card = cu.createCard(inputParams);
					selectedCardId = card.getId();
				}
				
				
				if(purpose.equalsIgnoreCase(PURPOSE_EDIT_CARD)){
					Card card = (Card) cu.getSources().retrieve(selectedCardId);
					Map<String, Object> cardParams = new HashMap<String, Object>();
					
					if (cardHolderNameVal != null && !cardHolderNameVal.trim().equals("")) {
						cardParams.put("name", cardHolderNameVal);
					}
					cardParams.put("exp_month", expMonth);
					cardParams.put("exp_year", expYear);
					
					card = card.update(cardParams);
					selectedCardId = card.getId();
				}
				if(selSubscr.isChecked() || purpose.equalsIgnoreCase(PURPOSE_CHANGE_DEF_CARD)){
					Map<String, Object> updateDefParams = new HashMap<String, Object>();
					updateDefParams.put("default_source", selectedCardId);
					cu.update(updateDefParams);
					if(!havePlanSubscription){
						custBO.updateStripeCustomerDetailsForUser(mContext, Utilities.loggedInUser.getUserId(), Utilities.loggedInUser.getOrgList().get(0).getOrgId());
					}
				}
				
				if(purpose.equalsIgnoreCase(PURPOSE_ADD_NEW_SUBSC_STRING)){
					custBO.updateStripeCustomerDetailsForUser(mContext, Utilities.loggedInUser.getUserId(), Utilities.loggedInUser.getOrgList().get(0).getOrgId());
				}
			}catch(Exception e){
				Log.e("error", e.toString());
				errorMessage =e.getMessage();
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			ProgressClass.finishProgress();
			if(errorMessage == null){
				goBackToWallet();
			}else{
				errortxtView.setText(errorMessage);
				errorLyt.setVisibility(View.VISIBLE);
			}
			
		}
	}
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       

        String text1 = new String();
        String text2 = new String();
        String text3 = new String();
        Bitmap cardTypeImage = null;

        if ((requestCode == REQUEST_SCAN || requestCode == REQUEST_AUTOTEST) && data != null
                && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard result = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
            if (result != null) {
            	CardType cardType = result.getCardType();
            	cardTypeImage = cardType.imageBitmap(mContext);
            	
            	text1 += result.getRedactedCardNumber() + " ";
            	text2 += result.expiryMonth + "/" + result.expiryYear+ " ";
            	text3 += result.cvv ;
                //outStr += result.cardNumber ;
                this.cvvVal=result.cvv ;
                this.expMonth=result.expiryMonth;
                this.expYear=result.expiryYear;
                this.cardNoVal=result.cardNumber;
            }
            this.carText1.setText(text1);
            this.carText2.setText(text2);
            this.carText3.setText(text3);
            this.carText1.setVisibility(View.VISIBLE);
            this.carText2.setVisibility(View.VISIBLE);
            this.carText3.setVisibility(View.VISIBLE);
            this.cardTpImgView.setVisibility(View.VISIBLE);
            cardTpImgView.setImageBitmap(cardTypeImage);
        } else if (resultCode == Activity.RESULT_CANCELED) {
           
        }
        
    }
    private void scanForCard(){
    	com.extraslice.walknpay.bl.Utilities.SCAN_FOR=com.extraslice.walknpay.bl.Utilities.SCAN_FOR_WLT_CARD;
    	carText1.setText("");
        carText2.setText("");
        carText3.setText("");
        cardTpImgView.setImageBitmap(null);
        carText1.setVisibility(View.GONE);
        carText2.setVisibility(View.GONE);
        carText3.setVisibility(View.GONE);
        cardTpImgView.setVisibility(View.GONE);
        errortxtView.setText("");
		errorLyt.setVisibility(View.GONE);
		cvvVal =null;
		expMonth=0;
		expYear=0;
		cardNoVal=null;
		Intent intent = new Intent(mContext, CardIOActivity.class)
        	.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
        	.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true)
        	.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV,true)
        	.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true)
        	.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false)
        	.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true)
        	.putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, Color.parseColor("#268CAB"));
		
		startActivityForResult(intent, REQUEST_SCAN);
    }
}
