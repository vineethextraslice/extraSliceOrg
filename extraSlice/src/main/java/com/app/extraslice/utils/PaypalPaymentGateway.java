package com.app.extraslice.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
public abstract class PaypalPaymentGateway  {
	static Context mContext;
	private static PayPalConfiguration config = null;
	private static final int REQUEST_CODE_PAYMENT = 1;
	double usableAmount;
	public PaypalPaymentGateway() {

	}

	
	public PaypalPaymentGateway(Context context,  String env,String clientId,double usableAmount) {
		PaypalPaymentGateway.mContext			= context;
		this.usableAmount = usableAmount;
		try {
			config = new PayPalConfiguration().environment(env).clientId(clientId).merchantName("Extraslice ")
					.merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
					.merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void makePaypalPayment() {
		Utilities.SCAN_FOR					= Utilities.SCAN_FOR_PAYPAL;
		String desc 						= "";
		PayPalPayment thingToBuy 			= new PayPalPayment(new BigDecimal(usableAmount+""), "USD",desc , PayPalPayment.PAYMENT_INTENT_SALE);
		Intent intent 						= new Intent(mContext, PaymentActivity.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
		((Activity)mContext).startActivityForResult(intent, REQUEST_CODE_PAYMENT);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				updateTransaction(true);
			} else if (resultCode == Activity.RESULT_CANCELED) {
				updateTransaction(false);
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				updateTransaction(false);
			}
		} else {
			updateTransaction(false);
		}
	}

	public abstract void updateTransaction(boolean paymentMaid);
}
