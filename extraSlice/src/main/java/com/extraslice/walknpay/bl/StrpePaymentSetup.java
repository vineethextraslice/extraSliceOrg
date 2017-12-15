package com.extraslice.walknpay.bl;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.ui.ConnectionDetector;
import com.extraslice.walknpay.ui.MenuActivity;
import com.extraslice.walknpay.ui.transaction.StrpePaymentGateway;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;

import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.card.payment.CardIOActivity;
import io.card.payment.CardType;
import io.card.payment.CreditCard;

public class StrpePaymentSetup {
	private static final int REQUEST_SCAN = 100;
    private static final int REQUEST_AUTOTEST = 200;
	ActionBar mactionBar;
	Button pay, cancel, ok, dismiss;
	TextView tvAmount, actionbartitle, alertTitle, errortxt;
	Intent intent;
	Dialog dialog;
	LayoutInflater inflater;
	View forgot_view;
	RadioGroup rdGroup;
	RadioButton rdbtn1, rdbtn2, rdbtn3;
	EditText pswrdcnfm;
	String Password;
	String text;
	static ImageView cardTpImgView;
	static TextView carText1,carText2,carText3,addNewCardBtn;
	static Context context;
	boolean status = false;
	public static String errorMessage = "";
	JSONObject objRecharge = null;
	Button saveButton, cancelButton, saveButtonSel, cancelButtonSel;
	
	String emailSendTo = null;
	String totalAmount;
	public List<Card> cardList;
	
	boolean saveUserCard = false;
	boolean emailSendStatus = false;
	CheckBox saveCard;
	Spinner cardListSpinner;
	LinearLayout errormsg;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	ArrayAdapter<String> dataAdapter;
	boolean isExisting = false;
	String selectedCardId;
	String name;
	static String cardNum;
	static int expMonth;
	static int expYear;
	static String cvv;
	Customer customer;
	LinearLayout linear4new, selected_action,scanCard1Lyt;
	RelativeLayout login_reg;
	boolean prepaidCardReacharge = false;
	String SECRETKEY_KEY = "";
	CustAcctBO cardBo;
	int currentCardLength = 0;
	

	public StrpePaymentSetup(String sKey,String totalAmount) {
		try {
			this.totalAmount =totalAmount;
			SECRETKEY_KEY = Utilities.decode(sKey);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}


	public View setUpUi(Dialog dialog, View rootView,Context context) {
		this.context = context;
		cardBo = new CustAcctBO(context, SECRETKEY_KEY);
		try {
			cardList = cardBo.getOrCreateCustomerAndCard(null, Utilities.loggedInUser.getEmail(), null, 1, 1, null, false);
			customer = cardBo.getUserAccount();
		} catch (CustomException e2) {
			e2.printStackTrace();
		}

		if(rootView !=null){
			
			this.linear4new = (LinearLayout) rootView.findViewById(R.id.linear4new);
			this.login_reg = (RelativeLayout) rootView.findViewById(R.id.login_reg);
			this.selected_action = (LinearLayout) rootView.findViewById(R.id.selected_action);
			this.saveCard = (CheckBox) rootView.findViewById(R.id.saveCard);
			this.tvAmount = (TextView) rootView.findViewById(R.id.amount);
			this.errormsg = (LinearLayout) rootView.findViewById(R.id.errorLyt);
			this.errortxt = (TextView) rootView.findViewById(R.id.errortxt);
			this.saveButton = (Button) rootView.findViewById(R.id.submit);
			this.cancelButton = (Button) rootView.findViewById(R.id.cancel);
			this.saveButtonSel = (Button) rootView.findViewById(R.id.submit_selected);
			this.cancelButtonSel = (Button) rootView.findViewById(R.id.cancel_selected);
			this.cardListSpinner = (Spinner) rootView.findViewById(R.id.cardList);
			this.scanCard1Lyt = (LinearLayout) rootView.findViewById(R.id.scanCard1Lyt);
			this.addNewCardBtn = (TextView) rootView.findViewById(R.id.addNewCardBtn);
			this.carText1 = (TextView) rootView.findViewById(R.id.carText1);
			this.carText2 = (TextView) rootView.findViewById(R.id.carText2);
			this.carText3 = (TextView) rootView.findViewById(R.id.carText3);
			this.cardTpImgView = (ImageView) rootView.findViewById(R.id.result_card_type_image);
		}else{
			this.linear4new = (LinearLayout) dialog.findViewById(R.id.linear4new);
			this.login_reg = (RelativeLayout) dialog.findViewById(R.id.login_reg);
			this.selected_action = (LinearLayout) dialog.findViewById(R.id.selected_action);
			this.saveCard = (CheckBox) dialog.findViewById(R.id.saveCard);
			this.tvAmount = (TextView) dialog.findViewById(R.id.amount);
			this.errormsg = (LinearLayout) dialog.findViewById(R.id.errorLyt);
			this.errortxt = (TextView) dialog.findViewById(R.id.errortxt);
			this.saveButton = (Button) dialog.findViewById(R.id.submit);
			this.cancelButton = (Button) dialog.findViewById(R.id.cancel);
			this.saveButtonSel = (Button) dialog.findViewById(R.id.submit_selected);
			this.cancelButtonSel = (Button) dialog.findViewById(R.id.cancel_selected);
			this.cardListSpinner = (Spinner) dialog.findViewById(R.id.cardList);
			this.scanCard1Lyt = (LinearLayout) dialog.findViewById(R.id.scanCard1Lyt);
			this.addNewCardBtn = (TextView) dialog.findViewById(R.id.addNewCardBtn);
			this.carText1 = (TextView) dialog.findViewById(R.id.carText1);
			this.carText2 = (TextView) dialog.findViewById(R.id.carText2);
			this.carText3 = (TextView) dialog.findViewById(R.id.carText3);
			this.cardTpImgView = (ImageView) dialog.findViewById(R.id.result_card_type_image);
		}
		carText1.setText("");
        carText2.setText("");
        carText3.setText("");
        cardTpImgView.setImageBitmap(null);
        carText1.setVisibility(View.GONE);
        carText2.setVisibility(View.GONE);
        carText3.setVisibility(View.GONE);
        cardTpImgView.setVisibility(View.GONE);
        errortxt.setText("");
		errormsg.setVisibility(View.GONE);
		cvv =null;
		expMonth=0;
		expYear=0;
		cardNum=null;
		scanCard1Lyt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				scanForCard();
			}
		});
		errormsg.setVisibility(View.GONE);
		
		saveCard.setEnabled(true);
		

		if (cardList != null && cardList.size() > 0) {

			cardListSpinner.setVisibility(View.VISIBLE);
			final List<String> list = new ArrayList<String>();
			list.add("Select Card");

			for (int i = 0; i < cardList.size(); i++) {
				String cardNum = cardList.get(i).getLast4();
				list.add("XXXXXXXXXXXX" + cardNum);
			}
			dataAdapter = new ArrayAdapter<String>(context, R.layout.wnp_spinner_item, list);
			if (dataAdapter != null) {
				cardListSpinner.setAdapter(dataAdapter);
			}
		}
		

		
		saveButton.setClickable(true);
		saveButtonSel.setClickable(true);
		cancelButton.setClickable(true);
		cancelButtonSel.setClickable(true);
		
		
		this.tvAmount.setText(MenuActivity.currencySymbol+""+totalAmount);
		
		cardListSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
				int item = cardListSpinner.getSelectedItemPosition();
				if (item == 0) {
					saveCard.setEnabled(true);
					isExisting = false;
					linear4new.setVisibility(View.VISIBLE);
					login_reg.setVisibility(View.VISIBLE);
					selected_action.setVisibility(View.GONE);
					selectedCardId = null;
					scanCard1Lyt.setVisibility(View.VISIBLE);
					return;
				}else{
					carText1.setText("");
			        carText2.setText("");
			        carText3.setText("");
			        cardTpImgView.setImageBitmap(null);
			        carText1.setVisibility(View.GONE);
			        carText2.setVisibility(View.GONE);
			        carText3.setVisibility(View.GONE);
			        cardTpImgView.setVisibility(View.GONE);
			        errortxt.setText("");
					errormsg.setVisibility(View.GONE);
					cvv =null;
					expMonth=0;
					expYear=0;
					cardNum=null;
					scanCard1Lyt.setVisibility(View.GONE);
				}
				Card model = cardList.get(item - 1);
				String cardNum = model.getLast4();
				String cardNam = model.getName();
				/*
				 * int cardMonth = model.getExpMonth(); int cardYear =
				 * model.getExpYear();
				 */
				selectedCardId = model.getId();
				linear4new.setVisibility(View.GONE);
				login_reg.setVisibility(View.GONE);
				selected_action.setVisibility(View.VISIBLE);
				saveButtonSel.setText("Pay using X-" + model.getLast4());
				isExisting = true;
				saveCard.setEnabled(false);
				String itemString = cardListSpinner.getSelectedItem().toString();
				if (itemString.equals("Select Card")) {
					isExisting = false;
					linear4new.setVisibility(View.VISIBLE);
					login_reg.setVisibility(View.VISIBLE);
					selected_action.setVisibility(View.GONE);
					selectedCardId = null;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		//new StripeJSON().execute();
		StrpePaymentGateway.dataloded =true;
		//ProgressClass.finishProgress();
		
		if(rootView !=null){
			rootView.setFocusableInTouchMode(true);
			rootView.requestFocus();
			rootView.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
	
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						return true;
					} else {
						return false;
					}
				}
			});
		}
		return rootView;
	}

	

	public boolean doPayment(double totalAmount,String desc) {
		try {
			

			int amoun = (int) Math.round(totalAmount * 100);
			

			String sourceId = null;
			Card card = null;
			Token token = null;
			if (isExisting) {
				sourceId = null;
				saveUserCard = false;
			} else {
				if (saveUserCard) {
					if(customer == null){
						cardList = cardBo.getOrCreateCustomerAndCard(name, Utilities.loggedInUser.getEmail(),  cardNum, expMonth, expYear, cvv, true);
						customer = cardBo.getUserAccount();
					}
					card = cardBo.addCardToCustomer(customer, name, cardNum, expMonth, expYear, cvv);

					selectedCardId = card.getId();
					isExisting = true;
				} else {
					token = cardBo.createCardToken(name, cardNum, expMonth, expYear, cvv);
					sourceId = token.getId();
				}
			}
			Charge charge = cardBo.doPayment(customer, selectedCardId, sourceId, amoun, desc, isExisting);

			if (charge == null) {
				status = false;
				errorMessage = "Error while charging the card";
			} else {
				status = true;
				errorMessage = Utilities.billNo + "";
			}

		} catch (CustomException e) {
			status = false;
			errorMessage = e.getLocalizedMessage();
		} 
		return status;
	}
	public boolean checkandSetCardDetails(){
		boolean verifiedOk = true;
		if (saveCard.isChecked() == true) {
			saveUserCard = true;
		} else {
			saveUserCard = false;
		}

		if(verifiedOk){
			if(cvv == null || cvv.trim().equals("") || expMonth<=0 || expYear <=0 
					|| cardNum == null || cardNum.trim().equals("")){
				errorMessage = "Enter card details!";
				verifiedOk = false;
			}
		}
		return verifiedOk;
		
	}
	
	 public void scanForCard(){
		 Utilities.SCAN_FOR=Utilities.SCAN_FOR_WNP_PAY;
	    	carText1.setText("");
	        carText2.setText("");
	        carText3.setText("");
	        cardTpImgView.setImageBitmap(null);
	        carText1.setVisibility(View.GONE);
	        carText2.setVisibility(View.GONE);
	        carText3.setVisibility(View.GONE);
	        cardTpImgView.setVisibility(View.GONE);
	        errortxt.setText("");
			errormsg.setVisibility(View.GONE);
			cvv =null;
			expMonth=0;
			expYear=0;
			cardNum=null;
			Intent intent = new Intent(context, CardIOActivity.class)
	        	.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
	        	.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true)
	        	.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV,true)
	        	.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true)
	            .putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true)
	           .putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, Color.parseColor("#268CAB"));
			((Activity)context).startActivityForResult(intent, REQUEST_SCAN);
	    }
	    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	//((Activity)context).onActivityResult(requestCode, resultCode, data);
	       
	    	Log.e("ffffffffffffffffffff", "onActivityResult");
	        String text1 = new String();
	        String text2 = new String();
	        String text3 = new String();
	        Bitmap cardTypeImage = null;

	        if ((requestCode == REQUEST_SCAN || requestCode == REQUEST_AUTOTEST) && data != null
	                && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
	            CreditCard result = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
	            if (result != null) {
	            	CardType cardType = result.getCardType();
	            	cardTypeImage = cardType.imageBitmap(context);
	            	
	            	text1 += result.getRedactedCardNumber() + " ";
	            	text2 += result.expiryMonth + "/" + result.expiryYear+ " ";
	            	text3 += result.cvv ;
	                //outStr += result.cardNumber ;
	                cvv=result.cvv ;
	                expMonth=result.expiryMonth;
	                expYear=result.expiryYear;
	                cardNum=result.cardNumber;
	            }
	            carText1.setText(text1);
	            carText2.setText(text2);
	            carText3.setText(text3);
	            carText1.setVisibility(View.VISIBLE);
	            carText2.setVisibility(View.VISIBLE);
	            carText3.setVisibility(View.VISIBLE);
	            cardTpImgView.setVisibility(View.VISIBLE);
	            cardTpImgView.setImageBitmap(cardTypeImage);
	        } else if (resultCode == Activity.RESULT_CANCELED) {
	           
	        }
	        
	    }
}
