package com.extraslice.walknpay.ui.transaction;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.StrpePaymentSetup;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.TransactionModel;
import com.extraslice.walknpay.ui.MenuActivity;
import com.extraslice.walknpay.ui.MyCards;
import com.extraslice.walknpay.ui.PaymentOptionScreen;
import com.extraslice.walknpay.ui.PaymentStatusFragment;
import com.stripe.model.Customer;

import org.codehaus.jettison.json.JSONObject;

import java.util.HashSet;
import java.util.concurrent.ExecutionException;

public class StrpePaymentGateway {

	TextView errorText;
	RelativeLayout layoutAddCard;
	Context mContext;
	Button saveButton, cancelButton, saveButtonSel, cancelButtonSel;
	EditText cardNumber, cardHolder;
	EditText cvc;

	Spinner monthSpinner, yearSpinner;

	CheckBox saveCard;
	LinearLayout errorLyt;
	
	String name;
	String cardNum;
	int expMonth;
	int expYear;
	String cvv;
	Customer customer;

	String PUBLISHABLE_KEY = "";
	String SECRETKEY_KEY = "";

	StrpePaymentSetup strpSetup;
	Dialog popup;
	public static boolean dataloded = false;
	String requestedFrom;
	JSONObject preCpn;
	TransactionModel trxnModel;
	FragmentManager fragmentManager;

	public StrpePaymentGateway(StrpePaymentSetup strpSetup,Dialog popup,Context context,  FragmentManager fragmentManager,String pKey,String sKey,String requestedFrom) {
		this.mContext = context;
		this.popup=popup;
		this.strpSetup = strpSetup;
		this.requestedFrom = requestedFrom;
		this.fragmentManager=fragmentManager;
		try {
			PUBLISHABLE_KEY = Utilities.decode(pKey);
			SECRETKEY_KEY = Utilities.decode(sKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showGateWay(final String totalAmount,TransactionModel trxnModel,JSONObject preCpn) {
		this.errorText = (TextView)popup.findViewById(R.id.errortxt);
		errorLyt = (LinearLayout)popup.findViewById(R.id.errorLyt);
		TextView totalAmtTxt = (TextView) popup.findViewById(R.id.amount);
		totalAmtTxt.setText(totalAmount);
		popup.show();
		this.preCpn = preCpn;
		this.trxnModel = trxnModel;
		this.saveButton = (Button) popup.findViewById(R.id.submit);
		this.cancelButton = (Button) popup.findViewById(R.id.cancel);
		this.saveButtonSel = (Button) popup.findViewById(R.id.submit_selected);
		this.cancelButtonSel = (Button) popup.findViewById(R.id.cancel_selected);
		
		this.layoutAddCard = (RelativeLayout) popup.findViewById(R.id.rlTouch);
		layoutAddCard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideVirtualKeyboard();
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//ProgressClass.startProgress(mContext);
				try {
					new RunInProgress(false,null,null,false,true).execute().get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		cancelButtonSel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//ProgressClass.startProgress(mContext);
				try {
					new RunInProgress(false,null,null,false,true).execute().get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		saveButtonSel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//ProgressClass.startProgress(mContext);
				makePayment(totalAmount);
				
			}

		});
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//ProgressClass.startProgress(mContext);
				if(strpSetup.checkandSetCardDetails()){
					makePayment(totalAmount);
				}
				
				hideVirtualKeyboard();
			}

		});
		

	}

	private void hideVirtualKeyboard() {
		// TODO Auto-generated method stub
		try {
			final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			Activity act = ((Activity) mContext);
			// imm.hideSoftInputFromWindow(act.getView().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Integer getExpMonth() {
		return getInteger(this.monthSpinner);
	}

	private Integer getInteger(Spinner spinner) {
		try {
			return Integer.parseInt(spinner.getSelectedItem().toString());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public Integer getExpYear() {
		return getInteger(this.yearSpinner);
	}

	private void makePayment(String amount) {
		String desc ="";
		if(requestedFrom.equals("payment-purchase")){
			desc = "(Android) Payment for bill # "+Utilities.billNo;
		}else{
			desc = "(Android) Prepaid recharge for user "+Utilities.loggedInUser.getEmail();
		}
	
		try {
			new RunInProgress(true,amount,desc,false,false).execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void updateTransaction(boolean paymentMaid,boolean forced){
		try {
			if(requestedFrom.equals("payment-purchase")){
				
				if(paymentMaid){
					UpdateTransaction updTrxn = new UpdateTransaction(mContext,paymentMaid);
					updTrxn.execute().get();
					PaymentOptionScreen.defCouponList.clear();
					PaymentOptionScreen.offCouponList.clear();
					PaymentOptionScreen.prepaidAvailableAmount = 0;
					PaymentOptionScreen.payableAmount = 0;
					PaymentOptionScreen.prepaidAmount = 0;
					//appliedCouponList = new ArrayList<CouponModel>();
					Utilities.offerAmount=0;
					Utilities.selectedCoupons= new HashSet<String>();
					if(popup != null){
						popup.dismiss();
					}
					Fragment fragment = new PaymentStatusFragment(true, Utilities.billNo+"", trxnModel, null, true);
					if (fragment != null) {
						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
					}
				}else if(forced){
					UpdateTransaction updTrxn = new UpdateTransaction(mContext,paymentMaid);
					updTrxn.execute().get();
					if(popup != null){
						popup.dismiss();
					}
				}else{
					errorText.setText(StrpePaymentSetup.errorMessage);
					errorLyt.setVisibility(View.VISIBLE);
					ProgressClass.finishProgress();
				}
				
				
			}else{
				if(paymentMaid || forced){
					UpdatePrepaidBalance updPre = new UpdatePrepaidBalance(mContext, preCpn, paymentMaid);
					Double currBal = updPre.execute().get();
					if(requestedFrom.equals("payment-recharge")){
						PaymentOptionScreen.prepaidAvailableAmount =currBal.doubleValue() ;
						PaymentOptionScreen.updatePrepaid();
						PaymentOptionScreen.rechargeAmount.setText("");
						if(popup != null)
							popup.dismiss();
						if(PaymentOptionScreen.rechargePopup != null)
							PaymentOptionScreen.rechargePopup.dismiss();
					}else if(requestedFrom.equals("wallet-recharge")){
						MyCards.availableBalance.setText("Balance("+MenuActivity.currencySymbol+") is :"+ Utilities.roundto2Decimaldouble(currBal.doubleValue()));
						if(popup != null)
							popup.dismiss();
						if(MyCards.rechargePopup != null)
							MyCards.rechargePopup.dismiss();
					}
				}else{
					errorText.setText(StrpePaymentSetup.errorMessage);
					errorLyt.setVisibility(View.VISIBLE);
					ProgressClass.finishProgress();
				}
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	public class RunInProgress extends AsyncTask<Void, Void, Void> {
		boolean paymentMaid;
		boolean forced;
		boolean makePayment;
		String amount,desc;
		public RunInProgress(boolean makePayment,String amount,String desc,boolean paymentMaid,boolean forced) {
			this.paymentMaid = paymentMaid;
			this.forced = forced;
			this.makePayment = makePayment;
			this.desc=desc;
			this.amount = amount;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			ProgressClass.startProgress(mContext);
			popup.hide();

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
			popup.show();
			super.onPostExecute(result);
			if(makePayment){
				
				boolean pymntMaid = strpSetup.doPayment(Double.parseDouble(amount), desc);
				try {
					new RunInProgress(false, amount, desc, pymntMaid, false).execute().get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				updateTransaction(paymentMaid,forced);
			}
		}

	}
}
