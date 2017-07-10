package com.app.extraslice.dao;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.app.extraslice.connection.WSConnnection;
import com.app.extraslice.model.PlanOfferModel;
import com.app.extraslice.model.ResourceTypeModel;
import com.app.extraslice.model.UserModel;
import com.app.extraslice.model.UserRequestModel;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.Utilities;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSubscriptionCollection;

public class UserDAO {
	Context mContext;
	public UserDAO(Context mContext){
		this.mContext = mContext;
	}
	
	public JSONObject checkUserName(String emailId) throws CustomException {
		String urlString = Utilities.mainUrl + "/user/checkUserName";
		JSONObject rootObject = null;
		JSONObject request = new JSONObject();
		try {
			request.put("email",emailId);
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	public JSONObject getForumLoginUrl(int userId) throws CustomException {
		String urlString = Utilities.mainUrl + "/user/getForumLoginUrl";
		JSONObject rootObject = null;
		JSONObject request = new JSONObject();
		try {
			request.put("userId",userId);
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	public JSONObject updateUser(UserModel model) throws CustomException {
		String urlString = Utilities.mainUrl + "/user/updateUser";
		JSONObject rootObject = null;
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	public JSONObject addDedicatedMembershipRequest(UserRequestModel model,List<ResourceTypeModel> resTypeList) throws CustomException{
		String urlString = Utilities.mainUrl + "/user/addDedicatedMembershipRequest";
		JSONObject request = new JSONObject();
		JSONObject rootObject = null;
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			request.put("User", model.toJSonObject());
			if(resTypeList != null && resTypeList.size() > 0){
				for(ResourceTypeModel resType : resTypeList){
					jsonList.add(resType.toJSonObject());
				}
				request.put("ResourceTypeList", new JSONArray(jsonList));
			}
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
		
	}
	/**
	 * @throws CustomException 
	 * 
	 */
	public JSONObject authenticateUser(String userName, String password,String osVersion,String appVersion,String deviceType) throws CustomException{
		String urlString = Utilities.mainUrl + "/user/getUserByUserNameAndAppDetl/";
		JSONObject requestObj = new JSONObject();
		UserModel model = new UserModel();
		model.setUserName(userName);
		model.setEmail(userName);
		try {
			model.setPassword(Utilities.encode(password));
		} catch (Exception e1) {
			throw new CustomException(e1.getLocalizedMessage());
		}
		try{
			requestObj.put("UserModel", model.toJSonObject());
			requestObj.put("DEVICETYPE", deviceType);
			requestObj.put("OSVERSION", osVersion);
			requestObj.put("APPVERSION", appVersion);
			
		}catch(Exception e){
			throw new CustomException(e.getLocalizedMessage());
		}
		JSONObject rootObject = null;
		try {
			rootObject = WSConnnection.getResult(urlString,requestObj.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
		
	}
	
	
	
	public JSONObject resendVerificationCode(UserModel model)  throws CustomException{
		String urlString = Utilities.mainUrl + "/user/resendVerificationEmail";
		JSONObject rootObject = null;

		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
	public JSONObject resetPassword(Context mContext, String userName, String newPassword)  throws CustomException{
		String urlString = Utilities.mainUrl + "/user/resetPassword";
		JSONObject rootObject = null;

		UserModel model = new UserModel();
		model.setUserName(userName);
		model.setEmail(userName);
		try {
			model.setTempPassword(Utilities.encode(newPassword));
		} catch (Exception e1) {
			throw new CustomException(e1.getLocalizedMessage());
		}
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}

	
	public JSONObject addGuestUser( UserModel model,String deviceType,String versionCode) throws CustomException{
		String urlString = Utilities.mainUrl + "/user/addGuestUser";
		JSONObject request = new JSONObject();
		JSONObject rootObject = null;
		
		try {
			request.put("User", model.toJSonObject());
			
			request.put("deviceType", deviceType);
			request.put("versionCode", versionCode);
			
			//Log.e("userrequest", request.toString());
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
	public JSONObject addUser( UserModel model,List<ResourceTypeModel> resTypeList,String userRegCode,String cardToken,
			List<Integer> planIdList ,long trialEndsAt, int trialDays,PlanOfferModel offerModel,String gateWay) throws CustomException{
		String urlString = Utilities.mainUrl + "/user/addUser";
		JSONObject request = new JSONObject();
		JSONObject rootObject = null;
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			request.put("User", model.toJSonObject());
			if(userRegCode !=null){
				request.put("UserRegCode", userRegCode);
			}
			request.put("cardToken", cardToken);
			request.put("planIdList", planIdList);
			request.put("trialEndsAt", trialEndsAt);
			request.put("trialDays", trialDays);
			request.put("gateWay", gateWay);
			if(offerModel != null){
				request.put("OfferModel", offerModel.toJSonObject());
			}
			
			
			if(resTypeList != null && resTypeList.size() > 0){
				for(ResourceTypeModel resType : resTypeList){
					jsonList.add(resType.toJSonObject());
				}
				request.put("ResourceTypeList", new JSONArray(jsonList));
			}
			//Log.e("userrequest", request.toString());
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	public JSONObject deleteUser(Context mContext, int userId,int orgId)  throws CustomException{
		String urlString = Utilities.mainUrl + "/user/deleteUser";
		JSONObject rootObject = null;
		JSONObject model = new JSONObject();
		try {
			model.put("userId", userId);
			model.put("orgId", orgId);
			rootObject = WSConnnection.getResult(urlString, model.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
	public JSONObject deleteUserByName(String userName,String orgName)  throws CustomException{
		String urlString = Utilities.mainUrl + "/user/deleteUserByName";
		JSONObject rootObject = null;
		JSONObject model = new JSONObject();
		try {
			model.put("userName", userName);
			model.put("orgName", orgName);
			rootObject = WSConnnection.getResult(urlString, model.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	public JSONObject addUserSubscription(Context mContext, int userId,int orgId,int planId,Customer customer,Charge ch,String gateWay,String acctId)  throws CustomException{ 
		String urlString = Utilities.mainUrl + "/user/addUserSubscription";
		JSONObject rootObject = null;
		JSONObject model = new JSONObject();
		try {
			model.put("userId", userId);
			model.put("orgId", orgId);
			model.put("planId", planId);
			if(customer != null){
				model.put("customerId", customer.getId());
				CustomerSubscriptionCollection coll = customer.getSubscriptions();
				model.put("subscriptionId", coll.getData().get(0).getId());
			}
			if(ch != null){
				model.put("chargeId", ch.getId());
				model.put("amount", ch.getAmount());
			}
			model.put("gateWay", gateWay);
			model.put("acctId", acctId);
			rootObject = WSConnnection.getResult(urlString, model.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
}
