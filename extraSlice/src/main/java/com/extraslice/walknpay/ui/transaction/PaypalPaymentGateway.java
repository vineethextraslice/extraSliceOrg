package com.extraslice.walknpay.ui.transaction;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.model.TransactionModel;
import com.extraslice.walknpay.ui.MenuActivity;
import com.extraslice.walknpay.ui.MyCards;
import com.extraslice.walknpay.ui.PaymentOptionScreen;
import com.extraslice.walknpay.ui.PaymentStatusFragment;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.codehaus.jettison.json.JSONObject;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
public class PaypalPaymentGateway  {

	boolean status = false;
	static Context mContext;
	static JSONObject prepaidJson=null;
	private static PayPalConfiguration config = null;
	private static final int REQUEST_CODE_PAYMENT = 1;
	static String requestedFrom;
	static TransactionModel trxnModel;
	static FragmentManager fragmentManager;

	public PaypalPaymentGateway() {

	}

	
	public PaypalPaymentGateway(Context context, FragmentManager fragmentManager, String env,String clientId,String requestedFrom) {
		PaypalPaymentGateway.mContext			= context;
		PaypalPaymentGateway.requestedFrom		= requestedFrom;
		PaypalPaymentGateway.fragmentManager	= fragmentManager;
		try {
			config = new PayPalConfiguration().environment(env).clientId(clientId).merchantName("Extraslice ")
					.merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
					.merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void makePayment(String totalAmount,TransactionModel trxnModel,JSONObject prepaidJson) {
		PaypalPaymentGateway.prepaidJson 	= prepaidJson;
		PaypalPaymentGateway.trxnModel		= trxnModel;
		Utilities.SCAN_FOR					= Utilities.SCAN_FOR_PAYPAL;
		String desc 						= "";
		if(requestedFrom.equals("payment-purchase")){
			desc = "(Android) Payment for bill # "+Utilities.billNo;
		}else{
			desc = "(Android) Prepaid recharge for "+Utilities.loggedInUser.getEmail();
		}
		PayPalPayment thingToBuy 			= new PayPalPayment(new BigDecimal(totalAmount), "USD",desc , PayPalPayment.PAYMENT_INTENT_SALE);
		Intent intent 						= new Intent(mContext, PaymentActivity.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
		((Activity)mContext).startActivityForResult(intent, REQUEST_CODE_PAYMENT);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				updateTransaction(true);
				status = true;
			} else if (resultCode == Activity.RESULT_CANCELED) {
				updateTransaction(false);
				status = false;

			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				updateTransaction(false);
				status = false;
			}
		} else {
			updateTransaction(false);
		}
	}

	private void updateTransaction(boolean paymentMaid){
		try {
			if(requestedFrom.equals("payment-purchase")){
				UpdateTransaction updTrxn = new UpdateTransaction(mContext,paymentMaid);
				updTrxn.execute().get();
				if(paymentMaid){
					Fragment fragment = new PaymentStatusFragment(true, Utilities.billNo+"", trxnModel, null, true);
					if (fragment != null) {
						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
					}
				}
				
			}else{
				UpdatePrepaidBalance updPre = new UpdatePrepaidBalance(mContext, prepaidJson, paymentMaid);
				Double currBal = updPre.execute().get();
				if(paymentMaid){
					if(requestedFrom.equals("payment-recharge")){
						PaymentOptionScreen.prepaidAvailableAmount =currBal.doubleValue() ;
						PaymentOptionScreen.updatePrepaid();
						PaymentOptionScreen.rechargeAmount.setText("");
						if(PaymentOptionScreen.rechargePopup != null)
							PaymentOptionScreen.rechargePopup.dismiss();
					}else if(requestedFrom.equals("wallet-recharge")){
						MyCards.availableBalance.setText("Balance("+MenuActivity.currencySymbol+") is :"+ Utilities.roundto2Decimaldouble(currBal.doubleValue()));
						if(MyCards.rechargePopup != null)
							MyCards.rechargePopup.dismiss();
					}
				}
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
