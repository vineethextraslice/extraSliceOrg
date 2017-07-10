package com.extraslice.walknpay.ui.transaction;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.extraslice.walknpay.bl.CouponBO;
import com.extraslice.walknpay.bl.CustomException;
import com.extraslice.walknpay.bl.TransactionBO;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.CouponModel;
import com.extraslice.walknpay.model.TransactionModel;
import com.extraslice.walknpay.ui.PaymentOptionScreen;

public class AddTransaction   extends AsyncTask<Void, Void,TransactionModel> {
	List<CouponModel> selectedCouponList = new ArrayList<CouponModel>();
	Context mContext;
	String errorMessage=null;
	String payWith;
	
	public AddTransaction(Context mContext,List<CouponModel> selectedCouponList,String payWith){
		this.mContext=mContext;
		this.selectedCouponList=selectedCouponList;
		this.payWith=payWith;
	}
	@Override
	protected void onPreExecute() {	
		//ProgressClass.startProgress(mContext);
		super.onPreExecute();
	}
	@Override
	protected TransactionModel doInBackground(Void... arg0) {
		if (selectedCouponList != null && selectedCouponList.size() > 0) {
			try {
				CouponBO couponBo = new CouponBO(mContext);
				PaymentOptionScreen.appliedCouponList = couponBo.applyAllCoupons(selectedCouponList);
			} catch (CustomException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		TransactionBO trxnDAO = new TransactionBO(mContext);
		TransactionModel transaction=null;
		try {
			transaction = trxnDAO.addTrasaction(payWith);
		} catch (CustomException e) {
			errorMessage = e.getLocalizedMessage();
		}
		return transaction;
	}
	@Override
	protected void onPostExecute(TransactionModel result) {
		super.onPostExecute(result);	
		//ProgressClass.finishProgress();
	}
	
}
