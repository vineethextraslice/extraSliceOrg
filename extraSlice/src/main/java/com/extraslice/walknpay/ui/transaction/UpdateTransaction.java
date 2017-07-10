package com.extraslice.walknpay.ui.transaction;

import org.codehaus.jettison.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.extraslice.walknpay.bl.TransactionBO;
import com.extraslice.walknpay.dao.ProgressClass;

public class UpdateTransaction   extends AsyncTask<Void, Void,JSONObject> {
	boolean status;
	Context mContext;
	String errorMessage=null;
	
	public UpdateTransaction(Context mContext,boolean status){
		this.mContext=mContext;
		this.status=status;
	}
	@Override
	protected void onPreExecute() {	
		ProgressClass.startProgress(mContext);
		super.onPreExecute();
	}
	@Override
	protected JSONObject doInBackground(Void... arg0) {
		TransactionBO trxnDAO = new TransactionBO(mContext);
		JSONObject jsonReslt = null;
		try{
			if (status) {
				 trxnDAO.updateTransactionToPurchase();
			}else {
				 trxnDAO.deleteTransaction();
			}
		}catch(Exception e){
			
		}
		return jsonReslt;
	}
	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);	
		ProgressClass.finishProgress();
	}
	
}
