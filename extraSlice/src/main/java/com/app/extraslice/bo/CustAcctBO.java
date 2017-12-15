package com.app.extraslice.bo;

import android.content.Context;

import com.app.extraslice.dao.CustAcctDAO;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.Utilities;

import org.codehaus.jettison.json.JSONObject;

import java.util.List;

public class CustAcctBO {

	static Context mContext;
	CustAcctDAO cardDAO = null;


	public CustAcctBO(){
		
	}
	public CustAcctBO(Context context) {
		mContext = context;
		cardDAO = new CustAcctDAO();
	}
	
	public boolean  addCutomerAccount(Context mContext, int userId,String strpAcct,String custId) throws CustomException{
		boolean isUpdated = false;
		CustAcctDAO dao = new CustAcctDAO();
		try {
			JSONObject rootObject = dao.addCutomerAccount(mContext, userId, strpAcct, custId);
			if (rootObject == null ) {
				throw new CustomException("Error while adding stripe customer details");
			} else{
				if (((String) rootObject.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
					isUpdated =true;
				}else{
					throw new CustomException(rootObject.getString(Utilities.ERROR_MESSAGE));
				}
			}
		} catch (Exception e) {
			throw new CustomException("Error while adding stripe customer details");
		}
		return isUpdated;
	}
	
	public boolean updateStripeCustomerDetailsForUser(Context mContext,int userId,int orgId)throws CustomException{
		boolean isUpdated = false;
		CustAcctDAO dao = new CustAcctDAO();
		try {
			JSONObject rootObject = dao.updateStripeCustomerDetailsForUser(mContext, userId, orgId);
			if (rootObject == null ) {
				throw new CustomException("Error while updating Subscription");
			} else{
				if (((String) rootObject.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
					isUpdated =true;
				}else{
					throw new CustomException(rootObject.getString(Utilities.ERROR_MESSAGE));
				}
			}
		} catch (Exception e) {
			throw new CustomException("Error while cancelling Subscription");
		}
		return isUpdated;
		
		
	}
	public JSONObject getProfileData(Context mContext,int userId,int orgId) throws CustomException {
		CustAcctDAO dao = new CustAcctDAO();
		return dao.getProfileData(mContext, userId, orgId);
	}
	
	public JSONObject getStripeCardDetails(Context mContext,int userId) throws CustomException {
		CustAcctDAO dao = new CustAcctDAO();
		JSONObject rootObject = null;
		try {
			rootObject = dao.getStripeCardDetails(mContext,userId);
			
		} catch (Exception e) {
			throw new CustomException("Error while cancelling Subscription");
		}
		return rootObject;
	}
	
	
	public String requestCancelSubscription(Context mContext,int userId,int orgId,boolean cancelMeetingsToo,List<Integer> planIdList,List<Integer> addonIds) throws CustomException {
		CustAcctDAO dao = new CustAcctDAO();
		try {
			JSONObject rootObject = dao.requestCancelSubscription(mContext,userId,orgId, cancelMeetingsToo,planIdList,addonIds);
			if (rootObject == null ) {
				throw new CustomException("Error while cancelling Subscription");
			} else{
				if (((String) rootObject.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
					return rootObject.getString(Utilities.STATUS_STRING);
				}else{
					return rootObject.getString(Utilities.ERROR_MESSAGE);
				}
			}
		} catch (Exception e) {
			throw new CustomException("Error while cancelling Subscription");
		}
	}
}
