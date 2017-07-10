package com.extraslice.walknpay.ui.transaction;

import org.codehaus.jettison.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.extraslice.walknpay.bl.CouponBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;

public class UpdatePrepaidBalance   extends AsyncTask<Void, Void,Double> {
	JSONObject inputJson;
	Context mContext;
	String errorMessage=null;
	boolean status;
	
	public UpdatePrepaidBalance(Context mContext,JSONObject inputJson,boolean status){
		this.mContext=mContext;
		this.inputJson=inputJson;
		this.status = status;
	}
	@Override
	protected void onPreExecute() {	
		ProgressClass.startProgress(mContext);
		super.onPreExecute();
	}
	@Override
	protected Double doInBackground(Void... arg0) {
		CouponBO couponBO = new CouponBO(mContext);
		Double prepaidValue=Double.valueOf(0);
		try {
			if(status){
				//jsonOutput = couponBO.deleteCardRecharge(mContext, inputJson);
				Object prepValueObj = couponBO.updatePrepaidToComplete(Utilities.billNo);
				prepaidValue = Double.valueOf(prepValueObj.toString());
			}
			else{
				Object prepValueObj  = couponBO.reallocatePrepaid(mContext, Utilities.billNo);
				prepaidValue = Double.valueOf(prepValueObj.toString());
			}
		} catch (Exception e) {
			errorMessage = e.getLocalizedMessage();
		}
		
		return prepaidValue;
	}
	@Override
	protected void onPostExecute(Double result) {
		super.onPostExecute(result);	
		ProgressClass.finishProgress();
	}
	
}
