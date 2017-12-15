package com.app.extraslice.bo;

import android.content.Context;

import com.app.extraslice.dao.SmartspaceDAO;
import com.app.extraslice.model.AdminAccountModel;
import com.app.extraslice.model.Organization;
import com.app.extraslice.model.PlanModel;
import com.app.extraslice.model.PlanOfferModel;
import com.app.extraslice.model.ReservationModel;
import com.app.extraslice.model.ResourceTypeModel;
import com.app.extraslice.model.SmartSpaceModel;
import com.app.extraslice.model.UserOrgModel;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.Utilities;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SmartspaceBO {
	Context mContext;
	SmartspaceDAO dao;
	public SmartspaceBO(Context mContext){
		this.mContext = mContext;
		dao = new SmartspaceDAO(mContext);
	}
	public static List<PlanModel> planList = new ArrayList<PlanModel>(1);
	public static List<ResourceTypeModel> addonList= new ArrayList<ResourceTypeModel>(1);
	public static List<PlanOfferModel> offerList= new ArrayList<PlanOfferModel>(1);
	public static AdminAccountModel accountModel;
	public static Organization individualOrg= null;
	public int noOfdaystoSubsDate = 0;
	public long trialEndsAt = 0;
	public long firstsubDate = 0;
	public int noOFDaysInMoth = 30;
	public String message="";
	public int noOfdaystoNextMonth = 0;
	
	public String updatePlanForOrg(int userId,int orgId,List<Integer> planIdList,String customerId,String subscriptionId,
			long planStartDate,long planEndDate,String pymntGateway,String eventId,double amount) throws CustomException {
		JSONObject rootObject = dao.updatePlanForOrg(userId, orgId, planIdList, customerId, subscriptionId, planStartDate, 
				planEndDate,pymntGateway,eventId,amount);
		try {
			if (rootObject != null &&  rootObject.get(Utilities.STATUS_STRING) != null &&  rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
				return Utilities.STATUS_SUCCESS;
			}else{
				try {
					throw new CustomException(rootObject.getString(Utilities.ERROR_MESSAGE)); 
				} catch (JSONException e) {
					throw new CustomException("Failed to update onetime payment"); 
				}
			}
		} catch (JSONException e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		
	}
	

	public JSONObject getExistingUser(String email) throws CustomException {
		return dao.getExistingUser(email);
	}

	public void getSignupData() throws CustomException {
		try {
			JSONObject rootObject = dao.getSignupData();
			if (rootObject != null &&  rootObject.get(Utilities.STATUS_STRING) != null 
					&&  rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
				JSONArray planArray = (JSONArray)rootObject.get("PlanList");
				JSONArray addonArray = (JSONArray)rootObject.get("AddonList");
				JSONArray offerArray = (JSONArray)rootObject.get("OfferList");
				if (planArray != null) {
					for (int index = 0; index < planArray.length(); index++) {
						JSONObject obj = (JSONObject) planArray.get(index);
						PlanModel plan = new PlanModel().jSonToObject(obj.toString());
						planList.add(plan);
					}
				}
				if (addonArray != null) {
					for (int index = 0; index < addonArray.length(); index++) {
						JSONObject obj = (JSONObject) addonArray.get(index);
						ResourceTypeModel resType = new ResourceTypeModel().jSonToObject(obj.toString());
						addonList.add(resType);
					}
				}
				if (offerArray != null) {
					for (int index = 0; index < offerArray.length(); index++) {
						JSONObject obj = (JSONObject) offerArray.get(index);
						PlanOfferModel resType = new PlanOfferModel().jSonToObject(obj.toString());
						offerList.add(resType);
					}
				}
				accountModel = new AdminAccountModel().jSonToObject(rootObject.get("AdminAccountModel").toString());
				noOfdaystoSubsDate = rootObject.getInt("noOfdaystoSubsDate");
				trialEndsAt = rootObject.getLong("trialEndsAt");
				firstsubDate = rootObject.getLong("firstsubDate");
				noOFDaysInMoth = rootObject.getInt("noOFDaysInMoth");
				message=rootObject.getString("message");
				noOfdaystoNextMonth = rootObject.getInt("noOfdaystoNextMonth");
			} else {
				String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
				throw new CustomException(errorMessage);
			}
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
	}
	
	public boolean deleteOrgPlan( int userId,int orgId) throws CustomException {
		boolean isStatus = false;
		
		JSONObject rootObject;
		try {
			rootObject = dao.deleteOrgPlan(mContext, userId,orgId);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		String status = "";
		try {
			if (rootObject == null || rootObject.get(Utilities.STATUS_STRING) == null) {
				throw new CustomException("Failed to delete plan");
			} else {
				status = (String) rootObject.get(Utilities.STATUS_STRING);
				if (status.equals(Utilities.STATUS_SUCCESS)) {
					isStatus = true;
				}else{
					isStatus = false;
				}
			}
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return isStatus;
	}
	public boolean deleteUserFromOrg( int userId,int orgId) throws CustomException {
		boolean isStatus = false;
		
		JSONObject rootObject;
		try {
			rootObject = dao.deleteUserFromOrg(mContext, userId,orgId);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		String status = "";
		try {
			if (rootObject == null || rootObject.get(Utilities.STATUS_STRING) == null) {
				throw new CustomException("Failed to delete plan");
			} else {
					status = (String) rootObject.get(Utilities.STATUS_STRING);
					if (status.equals(Utilities.STATUS_SUCCESS)) {
						isStatus = true;
					}else{
						isStatus = false;
					}
				
			}
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return isStatus;
	}
	
	
	
	public List<SmartSpaceModel> getAllSmartSpace(double latitude,double longitude) throws CustomException {
		
		if((Utilities.smartSpaceList == null || Utilities.smartSpaceList.size() == 0)|| accountModel ==null){
			Utilities.smartSpaceList = new ArrayList<SmartSpaceModel>(1);
			try {
				JSONObject rootObject = dao.getAllSmartSpace(mContext, latitude, longitude);
				if (rootObject != null &&  rootObject.get(Utilities.STATUS_STRING) != null &&  rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
					JSONArray itemsArray = (JSONArray) rootObject.get("SmartSpaceList");
					if (itemsArray != null) {
						for (int index = 0; index < itemsArray.length(); index++) {
							JSONObject obj = (JSONObject) itemsArray.get(index);
							SmartSpaceModel sSpace = new SmartSpaceModel().jSonToObject(obj.toString());
							Utilities.smartSpaceList.add(sSpace);
						}
					}
					accountModel = new AdminAccountModel().jSonToObject(rootObject.get("AdminAccountModel").toString());
				}
			} catch (Exception e) {
				throw new CustomException(e.getLocalizedMessage());
			}
		}
		return Utilities.smartSpaceList;
	}
	
	public List<ReservationModel> getCurrentSchedulesForPeriod(String startTime,String endTime) throws CustomException {
		List<ReservationModel> resourceList = new ArrayList<ReservationModel>();
		try {
			JSONObject rootObject = dao.getCurrentSchedulesForPeriod(mContext, startTime,endTime);
			if (rootObject != null &&  rootObject.get(Utilities.STATUS_STRING) != null &&  rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
				JSONArray itemsArray = (JSONArray) rootObject.get("ReservationList");
				if (itemsArray != null) {
					for (int index = 0; index < itemsArray.length(); index++) {
						JSONObject obj = (JSONObject) itemsArray.get(index);
						ReservationModel item = new ReservationModel().jSonToObject(obj.toString());
						resourceList.add(item);
					}
				}
				
			}
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return resourceList;
	}
	
	public AdminAccountModel getAdminAccount() throws CustomException {
		if(accountModel == null){
			try {
				JSONObject rootObject = dao.getAdminAccount(mContext);
				if (rootObject != null &&  rootObject.get(Utilities.STATUS_STRING) != null &&  rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
					JSONObject itemObj = (JSONObject) rootObject.get("AdminAccountModel");
					accountModel = new AdminAccountModel().jSonToObject(itemObj.toString());
				}
			} catch (Exception e) {
				throw new CustomException(e.getLocalizedMessage());
			}
		}
		return accountModel;
	}
	
	public Organization getIndividualOrg() throws CustomException {
		if(individualOrg == null){
			try {
				JSONObject rootObject = dao.getIndividualOrg(mContext);
				if (rootObject != null &&  rootObject.get(Utilities.STATUS_STRING) != null &&  rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
					JSONObject itemObj = (JSONObject) rootObject.get("OrganizationModel");
					individualOrg = new Organization().jSonToObject(itemObj.toString());
				}
			} catch (Exception e) {
				throw new CustomException(e.getLocalizedMessage());
			}
		}
		return individualOrg;
	}
	public ReservationModel makeAReservation(ReservationModel resModel) throws CustomException {
		ReservationModel resultModel = null;
		try {
			JSONObject rootObject = dao.makeAReservation(mContext, resModel);
			if (rootObject != null &&  rootObject.get(Utilities.STATUS_STRING) != null &&  rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
				JSONObject itemObj = (JSONObject) rootObject.get("ReservationModel");
				resultModel = new ReservationModel().jSonToObject(itemObj.toString());
			}
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return resultModel;
	}
	
	public List<String> getAllOrganizationNames(double latitude,double longitude) throws CustomException {
		
		List<String> orgList = new ArrayList<String>();
		try {
			JSONObject rootObject = dao.getAllOrganizationNames(mContext, latitude, longitude);
			if(rootObject != null){
				if (rootObject.get(Utilities.STATUS_STRING) != null) {
					if(rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)){
						JSONArray itemsArray = (JSONArray) rootObject.get("OrganizationNameList");
						if (itemsArray != null) {
							for (int index = 0; index < itemsArray.length(); index++) {
								orgList.add(itemsArray.get(index).toString().toUpperCase());
							}
						}
					}else if(rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_FAILED)){
						if(rootObject.get(Utilities.ERROR_MESSAGE) != null){
							throw new CustomException(rootObject.getString(Utilities.ERROR_MESSAGE));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return orgList;
	}
	public List<ResourceTypeModel> getResourcseTypesForPlan( int planId) throws CustomException {
		
		List<ResourceTypeModel> resTypeList = new ArrayList<ResourceTypeModel>();
		try {
			JSONObject rootObject = dao.getResourcseTypesForPlan(mContext,planId);
			if (rootObject != null &&  rootObject.get(Utilities.STATUS_STRING) != null &&  rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
				JSONArray itemsArray = (JSONArray) rootObject.get("ResourceTypeList");
				if (itemsArray != null) {
					for (int index = 0; index < itemsArray.length(); index++) {
						JSONObject obj = (JSONObject) itemsArray.get(index);
						ResourceTypeModel plan = new ResourceTypeModel().jSonToObject(obj.toString());
						resTypeList.add(plan);
					}
				}
				
			}
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return resTypeList;
	}
	
	public JSONObject addReservation(ReservationModel model,String pymntRefKey,double amountPaid) throws CustomException {
		return dao.addReservation(mContext, model,pymntRefKey,amountPaid);
			
	}
	public String updateReservationStatus(ReservationModel model,String cardToken,int trialPeriods,double amountPaid,String pymntGateway) throws CustomException {
		JSONObject rootObject = dao.updateReservationStatus(model, cardToken, trialPeriods, amountPaid, pymntGateway);
		try {
			if (rootObject != null &&  rootObject.get(Utilities.STATUS_STRING) != null &&  rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
				return Utilities.STATUS_SUCCESS;
			}else{
				try {
					return rootObject.getString(Utilities.ERROR_MESSAGE);
				} catch (JSONException e) {
					return "Failed to update payment";
				}
				
			}
		} catch (JSONException e) {
			throw new CustomException(e.getLocalizedMessage());
		}
			
	}
	
	public boolean deleteReservation(ReservationModel model) throws CustomException {
		try {
			JSONObject rootObject = dao.deleteReservation(mContext, model);
			if (rootObject != null &&  rootObject.get(Utilities.STATUS_STRING) != null &&  rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
				return true;
			}else if (rootObject != null &&  rootObject.get(Utilities.STATUS_STRING) != null &&  rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_FAILED)) {
				if(rootObject.get(Utilities.ERROR_MESSAGE) != null ){
					throw new CustomException(rootObject.get(Utilities.ERROR_MESSAGE).toString());
				}
			}
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return false;
	}
	public JSONObject updateReservation(ReservationModel model,String cardToken,int trialPeriods,String gateWay,double paidAmount,boolean agreeToPay) throws CustomException {
		return  dao.updateReservation(model, cardToken, trialPeriods, gateWay, paidAmount, agreeToPay);
		
	}
	
	public UserOrgModel updateUser(Context mContext,UserOrgModel userModel) throws CustomException {
		UserOrgModel resUserModel = null;
		try {
			JSONObject rootObject = dao.updateUser(mContext, userModel);
			if (rootObject != null &&  rootObject.get(Utilities.STATUS_STRING) != null &&  rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
				JSONObject itemObj = (JSONObject) rootObject.get("UserOrgModel");
				resUserModel = new UserOrgModel().jSonToObject(itemObj.toString());
				
			}
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return resUserModel;
	}
	
	
}
