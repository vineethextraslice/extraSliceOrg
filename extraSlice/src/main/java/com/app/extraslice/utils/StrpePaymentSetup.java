package com.app.extraslice.utils;

import io.card.payment.CardIOActivity;
import io.card.payment.CardType;
import io.card.payment.CreditCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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

import com.app.extraslice.MenuActivity;
import com.app.extraslice.R;
import com.app.extraslice.adapter.CustomAdapter;
import com.app.extraslice.bo.CustAcctBO;
import com.stripe.model.Card;
import com.stripe.model.Customer;
//import com.app.extraslice.ui.ConnectionDetector;

public class StrpePaymentSetup {
	static final int REQUEST_SCAN = 100;
    static final int REQUEST_AUTOTEST = 200;
	ActionBar mactionBar;
	Button pay, cancel, ok, dismiss;
	TextView tvAmount, actionbartitle, alertTitle;
	public TextView errortxt;
	Intent intent;
	Dialog dialog;
	LayoutInflater inflater;
	View forgot_view;
	RadioGroup rdGroup;
	RadioButton rdbtn1, rdbtn2, rdbtn3;
	EditText pswrdcnfm;
	String Password;
	String text;
	static Context context;
	boolean status = false;
	public static String errorMessage = "";
	JSONObject objRecharge = null;
	Button saveButton, cancelButton, saveButtonSel, cancelButtonSel;
	static ImageView cardTpImgView;
	static TextView carText1,carText2,carText3,addNewCardBtn;
	String emailSendTo = null;
	String totalAmount;
	public List<Card> cardList;
	
	boolean saveUserCard = false;
	boolean emailSendStatus = false;
	CheckBox saveCard;
	Spinner cardListSpinner;
	public LinearLayout errorLyt;
	LinearLayout decriptionLyt;
	TextView decription;
	Boolean isInternetPresent = false;
	CustomAdapter dataAdapter;
	boolean isExisting = false;
	String selectedCardId;
	Card selCard=null;
	String name;
	static String cardNum;
	static int expMonth;
	static int expYear;
	static String cvv;
	Customer customer;
	LinearLayout  linear4new, selected_action,scanCard1Lyt;
	RelativeLayout login_reg;
	boolean prepaidCardReacharge = false;
	CustAcctBO cardBo;
	int currentCardLength = 0;
	String userName;
	int userId;

	public StrpePaymentSetup(String userName,int userId,String totalAmount) {
		try {
			this.totalAmount =totalAmount;
			this.userName = userName;
			this.userId = userId;
		} catch (Exception e) {

			e.printStackTrace();
		}

	}


	public void setUpUi(Dialog dialog, Context context,String descriptionText) {
		this.context = context;
		cardBo = new CustAcctBO(context);
		
		this.linear4new = (LinearLayout) dialog.findViewById(R.id.linear4new);
		this.login_reg = (RelativeLayout) dialog.findViewById(R.id.login_reg);
		this.selected_action = (LinearLayout) dialog.findViewById(R.id.selected_action);
		this.saveCard = (CheckBox) dialog.findViewById(R.id.saveCard);
		this.tvAmount = (TextView) dialog.findViewById(R.id.amount);
		this.scanCard1Lyt = (LinearLayout) dialog.findViewById(R.id.scanCard1Lyt);
		this.addNewCardBtn = (TextView) dialog.findViewById(R.id.addNewCardBtn);
		this.carText1 = (TextView) dialog.findViewById(R.id.carText1);
		this.carText2 = (TextView) dialog.findViewById(R.id.carText2);
		this.carText3 = (TextView) dialog.findViewById(R.id.carText3);
		this.cardTpImgView = (ImageView) dialog.findViewById(R.id.result_card_type_image);
		this.errorLyt = (LinearLayout) dialog.findViewById(R.id.errorLyt);
		this.errortxt = (TextView) dialog.findViewById(R.id.errortxt);
		this.saveButton = (Button) dialog.findViewById(R.id.submit);
		this.cancelButton = (Button) dialog.findViewById(R.id.cancel);
		this.saveButtonSel = (Button) dialog.findViewById(R.id.submit_selected);
		this.cancelButtonSel = (Button) dialog.findViewById(R.id.cancel_selected);
		this.cardListSpinner = (Spinner) dialog.findViewById(R.id.cardList);
		this.decriptionLyt = (LinearLayout) dialog.findViewById(R.id.decriptionLyt);
		this.decription = (TextView) dialog.findViewById(R.id.decription);
		if(descriptionText != null){
			this.decription.setText(descriptionText);
			this.decriptionLyt.setVisibility(View.VISIBLE);
		}else{
			this.decriptionLyt.setVisibility(View.GONE);
		}
		errorLyt.setVisibility(View.GONE);
		
		saveCard.setEnabled(true);
		

		if (cardList != null && cardList.size() > 0) {

			cardListSpinner.setVisibility(View.VISIBLE);
			final List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("Select Card", "Select Card");
			list.add(map);
			for (int i = 0; i < cardList.size(); i++) {
				String cardNum = cardList.get(i).getLast4();
				map = new HashMap<String, Object>();
				map.put("XXXXXXXXXXXX" + cardNum, "XXXXXXXXXXXX" + cardNum);
				list.add(map);
			}
			dataAdapter = new CustomAdapter(context,  list);
			if (dataAdapter != null) {
				cardListSpinner.setAdapter(dataAdapter);
				cardListSpinner.setSelection(1);
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
					selCard = null;
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
					errorLyt.setVisibility(View.GONE);
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
				selCard = model;
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
					selCard = null;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		carText1.setText("");
        carText2.setText("");
        carText3.setText("");
        cardTpImgView.setImageBitmap(null);
        carText1.setVisibility(View.GONE);
        carText2.setVisibility(View.GONE);
        carText3.setVisibility(View.GONE);
        cardTpImgView.setVisibility(View.GONE);
        errortxt.setText("");
		errorLyt.setVisibility(View.GONE);
		cvv =null;
		expMonth=0;
		expYear=0;
		cardNum=null;
	}

	



	
	
	public boolean checkandSetCardDetails(){
		boolean verifiedOk = true;
		if (saveCard.isChecked() == true) {
			saveUserCard = true;
		} else {
			saveUserCard = false;
		}
		//if (!saveUserCard) {
			

			
		//}
		if(verifiedOk){
			
			if(cvv == null || cvv.trim().equals("") || expMonth<=0 || expYear <=0 
					|| cardNum == null || cardNum.trim().equals("")){
				errorMessage = "Enter card details!";
				verifiedOk = false;
			}
		}
		return verifiedOk;
		
	}
	 
}
