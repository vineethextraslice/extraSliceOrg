package com.app.extraslice.dao;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.content.Context;

import com.app.extraslice.connection.WSConnnection;
import com.app.extraslice.model.ReservationModel;
import com.app.extraslice.model.UserOrgModel;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.Utilities;

public class SmartspaceDAO {
	Context mContext;
	public SmartspaceDAO(Context mContext){
		this.mContext = mContext;
	}
	

	
	public JSONObject updatePlanForOrg(int userId,int orgId,List<Integer> planIdList,String customerId,String subscriptionId,
			long planStartDate,long planEndDate,String pymntGateway,String eventId,double amount) throws CustomException {
		JSONObject rootObject = null;
		String urlString = Utilities.mainUrl + "/smSpace/updatePlanForOrg";
		JSONObject model = new JSONObject();
		try {
			model.put("userId", userId);
			model.put("orgId", orgId);
			model.put("planIdList", planIdList);
			model.put("customerId", customerId);
			model.put("subscriptionId", subscriptionId);
			model.put("planStartDate", planStartDate);
			model.put("planEndDate", planEndDate);
			model.put("pymntGateway", pymntGateway);
			model.put("eventType", "ontimepayment");
			model.put("eventId", eventId);
			model.put("amount", amount);
			rootObject = WSConnnection.getResult(urlString, model.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
	public JSONObject getExistingUser(String email) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/getExistingPlansForUser";
		JSONObject rootObject = null;

		JSONObject model = new JSONObject();
		try {
			model.put("email", email);
			rootObject = WSConnnection.getResult(urlString, model.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	public JSONObject getSignupData() throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/getSignupData";
		JSONObject rootObject = null;
		try {
			rootObject = WSConnnection.getResult(urlString, "",mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	public JSONObject deleteOrgPlan(Context mContext, int userId,int orgId) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/deleteOrgPlan";
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
	public JSONObject updateUser(Context mContext,UserOrgModel userModel) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/updateUser";
		JSONObject rootObject = null;
		try {
			rootObject = WSConnnection.getResult(urlString, userModel.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
	public JSONObject getAdminAccount(Context mContext) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/getAdminAccount";
		JSONObject rootObject = null;
		
		try {
			rootObject = WSConnnection.getResult(urlString, "",mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
	public JSONObject getIndividualOrg(Context mContext) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/getIndividualOrg";
		JSONObject rootObject = null;
		
		try {
			rootObject = WSConnnection.getResult(urlString, "",mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	public JSONObject deleteUserFromOrg(Context mContext, int userId,int orgId) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/deleteUserFromOrg";
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
	
	public JSONObject getAllSmartSpace(Context mContext,double latitude,double longitude) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/getAllSmartSpace";
		JSONObject rootObject = null;
		JSONObject trxnModel = new JSONObject();
		try {
			rootObject = WSConnnection.getResult(urlString, "",mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
	public JSONObject getCurrentSchedulesForPeriod(Context mContext,String startTime,String endTime) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/getCurrentSchedulesForPeriod";
		JSONObject rootObject = null;
		JSONObject input = new JSONObject();
		try {
			input.put("startTime", startTime);
			input.put("endTime", endTime);
		} catch (JSONException e1) {
			throw new CustomException(e1.getLocalizedMessage());
		}
		
		try {
			rootObject = WSConnnection.getResult(urlString, input.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	public JSONObject makeAReservation(Context mContext,ReservationModel resModel) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/makeAReservation";
		JSONObject rootObject = null;
		try {
			rootObject = WSConnnection.getResult(urlString, resModel.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	public JSONObject getAllOrganizationNames(Context mContext,double latitude,double longitude) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/getAllOrganizationNames";
		JSONObject rootObject = null;
		try {
			rootObject = WSConnnection.getResult(urlString, "",mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
	public JSONObject addReservation(Context mContext, ReservationModel model,String pymntRefKey,double amountPaid) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/addReservation";
		JSONObject rootObject = null;
		JSONObject jsonInput = new JSONObject();
		
		try {
			jsonInput.put("pymntRefKey", pymntRefKey == null ? "":pymntRefKey);
			jsonInput.put("ReservationModel", model.toJSonObject());
			jsonInput.put("amountPaid", amountPaid);
			
			rootObject = WSConnnection.getResult(urlString, jsonInput.toString(), mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
	public JSONObject deleteReservation(Context mContext, ReservationModel model) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/deleteReservation";
		JSONObject rootObject = null;
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(), mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	public JSONObject updateReservationStatus(ReservationModel model,String cardToken,int trialPeriods,double amountPaid,String pymntGateway) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/updateReservationStatus";
		JSONObject rootObject = null;
		JSONObject jsonInput = new JSONObject();
		try {
			jsonInput.put("cardToken", cardToken);
			jsonInput.put("trialPeriods", trialPeriods);
			jsonInput.put("amountPaid", amountPaid);
			jsonInput.put("ReservationModel", model.toJSonObject());
			jsonInput.put("pymntGateway", pymntGateway);
			rootObject = WSConnnection.getResult(urlString, jsonInput.toString(), mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	public JSONObject updateReservation(ReservationModel model,String cardToken,int trialPeriods,String gateWay,double paidAmount,boolean agreeToPay) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/updateReservation";
		JSONObject rootObject = null;
		JSONObject jsonInput = new JSONObject();
		
		try {
			jsonInput.put("agreeToPay", agreeToPay);
			jsonInput.put("cardToken", cardToken);
			jsonInput.put("trialPeriods", trialPeriods);
			jsonInput.put("paidAmount", paidAmount);
			jsonInput.put("gateWay", gateWay);
			jsonInput.put("ReservationModel", model.toJSonObject());
			rootObject = WSConnnection.getResult(urlString, jsonInput.toString(), mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}

	public JSONObject getResourcseTypesForPlan(Context mContext, int planId) throws CustomException {
		String urlString = Utilities.mainUrl + "/smSpace/getResourcseTypesForPlan";
		JSONObject rootObject = null;
		JSONObject input = new JSONObject();
		try {
			input.put("planId", planId);
		} catch (JSONException e1) {
			throw new CustomException(e1.getLocalizedMessage());
		}
		try {
			rootObject = WSConnnection.getResult(urlString, input.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
}
