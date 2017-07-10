package com.extraslice.walknpay.dao;

import com.app.extraslice.R;

import android.R.style;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class ProgressClass {
	private static ProgressDialog progressDialog;

	public static void startProgress(Context mContext) {
		try {
			
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(mContext,R.style.MyTheme);
				progressDialog.setCancelable(false);
				progressDialog.setMessage("Please wait...");
				progressDialog.setTitle(null);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setProgress(0);
				progressDialog.setMax(100);				
			}
			if(!progressDialog.isShowing()){
				progressDialog.show();
			}
		} catch (Exception e) {
			Log.e("error progress",e.getLocalizedMessage());
		}
	}

	public static void finishProgress() {
		try {
			if (progressDialog != null) {
				if(progressDialog.isShowing()){
					progressDialog.dismiss();
					progressDialog = null;
				}
			}
		} catch (Exception e) {
			Log.e("error progress",e.getLocalizedMessage());
		}
		
	}

}
