package com.extraslice.walknpay.ui;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.CouponBO;
import com.extraslice.walknpay.bl.CustomException;
import com.extraslice.walknpay.bl.TransactionBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.CouponModel;
import com.extraslice.walknpay.model.TransactionModel;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

public class PayPalPaymentFragment extends Fragment {
	TransactionModel transaction = null;
	boolean status = false;
	String errorMessage = "";
	String totalAmount = null;
	Context mContext;
	JSONObject objRecharge=null;
	// testaccount
	/*
	 * private static final String CONFIG_ENVIRONMENT =
	 * PayPalConfiguration.ENVIRONMENT_SANDBOX; private static final String
	 * CONFIG_CLIENT_ID =
	 * "AfPj87i7KUq4TYpFIJ7nSfvt9u0-LqFKnUZYp0CQhqvwadUB9c2NJqsgTgDJCqyxM4jetvYTgaQO-cAS"
	 * AfPj87i7KUq4TYpFIJ7nSfvt9u0-LqFKnUZYp0CQhqvwadUB9c ;
	 */

	/* live account */
	/*
	 * String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
	 * String CONFIG_CLIENT_ID =
	 * "AZcnP8ArsOs7PIRaFpD4p4gP1XN22uzakRDe0OqjLflDHWQD1xACczT7s57bYlDdqOXZkH32IEA5CKx3"
	 * ;
	 */
	// String CONFIG_ENVIRONMENT = "";
	// String CONFIG_CLIENT_ID = "";

	private static PayPalConfiguration config = null;

	private static final int REQUEST_CODE_PAYMENT = 1;
	private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
	private static final int REQUEST_CODE_PROFILE_SHARING = 3;
	List<CouponModel> selectedCouponList;;

	public PayPalPaymentFragment() {

	}

	public PayPalPaymentFragment(String totalAmount, String env, String clientId, List<CouponModel> selectedCouponList) {
		this.totalAmount = totalAmount.substring(1, totalAmount.length());
		this.selectedCouponList = selectedCouponList;
		try {
			String a = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
			a = PayPalConfiguration.ENVIRONMENT_SANDBOX;
			config = new PayPalConfiguration().environment(env).clientId(clientId).merchantName("Example Merchant").merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
					.merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PayPalPaymentFragment(String totalAmount, String env, String clientId, boolean status) {
		this.totalAmount = totalAmount;
		try {
			String a = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
			a = PayPalConfiguration.ENVIRONMENT_SANDBOX;
			config = new PayPalConfiguration().environment(env).clientId(clientId).merchantName("Example Merchant").merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
					.merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = this.getActivity();
		new DoTransaction(selectedCouponList).execute();
		return null;
	}

	public void onBuyPressed(View pressed) {

		/*
		 * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
		 * Change PAYMENT_INTENT_SALE to - PAYMENT_INTENT_AUTHORIZE to only
		 * authorize payment and capture funds later. - PAYMENT_INTENT_ORDER to
		 * create a payment for authorization and capture later via calls from
		 * your server.
		 * 
		 * Also, to include additional payment details and an item list, see
		 * getStuffToBuy() below.
		 */

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				updateTransaction(true);
				errorMessage = (Utilities.billNo) + "";
				status = true;
			} else if (resultCode == Activity.RESULT_CANCELED) {
				updateTransaction(false);
				status = false;
				errorMessage = "User canceled operation.";

			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				updateTransaction(false);
				status = false;
				errorMessage = "Please check your internet connection and try again. If this problem persists, please contact " + Utilities.selectedStore.getEmail();
			}
		} else {
			updateTransaction(false);
		}
	
		Fragment fragment = new PaymentStatusFragment(status, errorMessage, transaction, null, false);
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
		}
	
	}

	private void updateTransaction(boolean isSuccess) {
		new UpdateTransaction(isSuccess).execute();

	}
	
	class DoTransaction extends AsyncTask<Void, Void, Void> {
		List<CouponModel> selectedCouponList = new ArrayList<CouponModel>();
		public DoTransaction(List<CouponModel> selectedCouponList){
			this.selectedCouponList = selectedCouponList;
		}
		
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			if (selectedCouponList != null && selectedCouponList.size() > 0) {
				CouponBO couponBo = new CouponBO(mContext);
				try {
					PaymentOptionScreen.appliedCouponList = couponBo.applyAllCoupons(selectedCouponList);
				} catch (CustomException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			TransactionBO trxnDAO = new TransactionBO(mContext);
			try {
				transaction = trxnDAO.addTrasaction("Paypal");
			} catch (CustomException e) {
				errorMessage = e.getLocalizedMessage();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			ProgressClass.finishProgress();
			if(transaction != null){
				PayPalPayment thingToBuy = null;
				thingToBuy = new PayPalPayment(new BigDecimal(totalAmount), "USD", "Charge for items for bill # : " + Utilities.billNo, PayPalPayment.PAYMENT_INTENT_SALE);
				Intent intent = new Intent(getActivity(), PaymentActivity.class);
				// send the same configuration for restart resiliency
				intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
				intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
				startActivityForResult(intent, REQUEST_CODE_PAYMENT);
			}else{
				updateTransaction(false);
				status = false;
				
				Fragment fragment = new PaymentStatusFragment(status, errorMessage, transaction, null, false);
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
				}
			}
			
		}
	}
	
	class UpdateTransaction extends AsyncTask<Void, Void, Void> {
		boolean isSuccess;
		public UpdateTransaction(boolean isSuccess){
			this.isSuccess = isSuccess;
		}
		
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			TransactionBO trxnDAO = new TransactionBO(mContext);
			if (isSuccess) {
				trxnDAO.updateTransactionToPurchase();
			}else {
				trxnDAO.deleteTransaction();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			ProgressClass.finishProgress();
			if (isSuccess) {
				Toast.makeText(mContext, "Transaction Successful.", Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(mContext, "Transaction failed.Please try again.", Toast.LENGTH_LONG).show();
			}
		}
	}
}
