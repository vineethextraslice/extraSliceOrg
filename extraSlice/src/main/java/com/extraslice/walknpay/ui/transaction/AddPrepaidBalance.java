package com.extraslice.walknpay.ui.transaction;

import org.codehaus.jettison.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.extraslice.walknpay.bl.CouponBO;
import com.extraslice.walknpay.bl.CustomException;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.TransactionModel;

public class AddPrepaidBalance   extends AsyncTask<Void, Void,Boolean> {
	JSONObject inputJson;
	Context mContext;
	public String errorMessage=null;
	
	public AddPrepaidBalance(Context mContext,JSONObject inputJson){
		this.mContext=mContext;
		this.inputJson=inputJson;
	}
	@Override
	protected void onPreExecute() {	
		//ProgressClass.startProgress(mContext);
		super.onPreExecute();
	}
	@Override
	protected Boolean doInBackground(Void... arg0) {
		
		CouponBO couponBO = new CouponBO(mContext);
		JSONObject resultJson=null;
		boolean status = true;
		try {
			resultJson = couponBO.assignCoupon(mContext, inputJson);
			if(resultJson == null ){
				errorMessage = "Failed to update recharge";
				status=false;
			}else if(resultJson.getString(Utilities.STATUS_STRING) == null){
				errorMessage = "Failed to update recharge";
				status=false;
			}else if(resultJson.getString(Utilities.STATUS_STRING).equalsIgnoreCase(Utilities.STATUS_FAILED)){
				errorMessage = resultJson.getString(Utilities.ERROR_MESSAGE);
				status=false;
			}else if(resultJson.getString(Utilities.STATUS_STRING).equalsIgnoreCase(Utilities.STATUS_SUCCESS)){
				JSONObject transactionObj = (JSONObject) resultJson.get("Transaction");
				TransactionModel mdl = new TransactionModel().jSonToObject(transactionObj.toString());
				
				if(mdl !=null){
					Utilities.billNo = mdl.getOrderId();
				}
				
				status=true;
			}
		} catch (Exception e) {
			errorMessage = e.getLocalizedMessage();
		}
		
		
		return status;
	}
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);	
		if(errorMessage != null){
			ProgressClass.finishProgress();
		}
	}	
}
