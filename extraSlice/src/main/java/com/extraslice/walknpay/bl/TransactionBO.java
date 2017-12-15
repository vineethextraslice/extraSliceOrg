package com.extraslice.walknpay.bl;

import android.content.Context;

import com.extraslice.walknpay.dao.TransactionDAO;
import com.extraslice.walknpay.model.TransactionModel;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class TransactionBO {
	Context mContext;
	TransactionDAO dao;
	public TransactionBO(Context mContext){
		dao = new TransactionDAO();
		this.mContext=mContext;
	}
	public JSONObject  getAllTransactionForUser( int userId, int storeId,int noOfReceipts,int offset) {
		return (dao.getAllTransactionForUser(mContext, userId, storeId,noOfReceipts,offset));
	}
	public JSONObject  getAllESliceTransactionForUser( int userId, String startDate,String endDate) {
		return (dao.getAllESliceTransactionForUser(mContext, userId, startDate,endDate));
	}
	public JSONObject  getAllWnPTransactionForUser( int userId, String startDate,String endDate) {
		return (dao.getAllWnPTransactionForUser(mContext, userId, startDate,endDate));
	}
	public void updateTransactionToPurchase() {
		dao.updateTransactionToPurchase(mContext);
	}
	public void deleteTransaction() {
		dao.deleteTransaction(mContext);
	}
	public TransactionModel addTrasaction(String payMethod) throws CustomException {
		return dao.addTrasaction(mContext,payMethod);
	}
	 
	
	public String emailEsliceReceipt(int userId,long orderId) throws JSONException {
		String status ="";
		JSONObject rootObject = dao.emailEsliceReceipt(mContext, userId, orderId);
		if (rootObject == null) {
			status="Failed to send email";
		} else if (rootObject.get(Utilities.STATUS_STRING) != null && rootObject.get(Utilities.STATUS_STRING).equals(Utilities.STATUS_SUCCESS)) {
			status = "Success";
		}else{
			status = "Failed";
		}
		return status;
	}
	
	public String emailWnPReceipt(int userId,long orderId) throws JSONException {
		String status ="";
		JSONObject rootObject = dao.emailWnPReceipt(mContext, userId, orderId);
		if (rootObject == null) {
			status="Failed to send email";
		} else if (rootObject.get(Utilities.STATUS_STRING) != null && rootObject.get(Utilities.STATUS_STRING).equals(Utilities.STATUS_SUCCESS)) {
			status = "Success";
		}else{
			status = "Failed";
		}
		return status;
	}
	public String sendMail(int userId,long orderId) throws JSONException {
		String status ="";
		JSONObject rootObject = dao.sendMail(mContext, userId, orderId);
		if (rootObject == null) {
			status="Failed to send email";
		} else if (rootObject.get(Utilities.STATUS_STRING) != null && rootObject.get(Utilities.STATUS_STRING).equals(Utilities.STATUS_SUCCESS)) {
			status = "Success";
		}else{
			status = "Failed";
		}
		return status;
	}
}
