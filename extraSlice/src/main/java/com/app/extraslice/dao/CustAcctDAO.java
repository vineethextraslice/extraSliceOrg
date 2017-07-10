package com.app.extraslice.dao;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.app.extraslice.connection.WSConnnection;
import com.app.extraslice.model.CustAcctModel;
import com.app.extraslice.model.PlanModel;
import com.app.extraslice.model.ResourceTypeModel;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.Utilities;

public class CustAcctDAO {
	
	public JSONObject getUserAccount(Context mContext, int userId,String strpAcct) {
		String urlString = Utilities.mainUrl + "/custacct/getCutomerAccount";
		CustAcctModel model = new CustAcctModel();
		model.setUserId(userId);
		model.setStrpAcct(strpAcct);
		JSONObject rootObject = null;
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}

	public JSONObject addCutomerAccount(Context mContext, int userId,String strpAcct,String custId) {
		String urlString = Utilities.mainUrl + "/custacct/addCutomerAccount";
		CustAcctModel model = new CustAcctModel();
		try {
			model.setUserId(userId);
			model.setCustomerId(custId);
			model.setStrpAcct(strpAcct);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		JSONObject rootObject = null;
		try {
			rootObject =WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}
	
	public JSONObject getProfileData(Context mContext,int userId,int orgId) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/getProfileData";
		JSONObject request = new JSONObject();
		JSONObject rootObject = null;
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			request.put("userId",userId);
			request.put("orgId",orgId);
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		
		return rootObject;
	}

	public JSONObject updateStripeCustomerDetailsForUser(Context mContext,int userId,int orgId) throws CustomException {
		String urlString = Utilities.mainUrl + "/custacct/updateStripeCustomerDetailsForUser";
		JSONObject request = new JSONObject();
		JSONObject rootObject = null;
		try {
			
			request.put("userId",userId);
			request.put("orgId",orgId);
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		
		return rootObject;
	}
	
	public JSONObject getStripeCardDetails(Context mContext,int userId) throws CustomException {
		String urlString = Utilities.mainUrl + "/custacct/getStripeCustomerDetailsForUser";
		JSONObject request = new JSONObject();
		JSONObject rootObject = null;
		try {
			
			request.put("userId",userId);
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		
		return rootObject;
	}
	
	public JSONObject requestCancelSubscription(Context mContext,int userId,int orgId,boolean cancelMeetingsToo,List<Integer> planIdList,List<Integer> addonIds) throws CustomException {
		String urlString = Utilities.mainUrl + "/custacct/requestCancelSubscription";
		JSONObject request = new JSONObject();
		JSONObject rootObject = null;
		try {
			request.put("cancelMeetingsToo",cancelMeetingsToo);
			request.put("userId",userId);
			request.put("orgId",orgId);
			request.put("planIdList",planIdList);
			request.put("addonIds",addonIds);
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		
		return rootObject;
	}

}
