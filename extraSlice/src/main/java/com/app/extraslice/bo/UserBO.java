package com.app.extraslice.bo;

import java.util.List;
import java.util.Random;

import org.codehaus.jettison.json.JSONObject;

import android.content.Context;

import com.app.extraslice.connection.WSConnnection;
import com.app.extraslice.dao.UserDAO;
import com.app.extraslice.model.PlanOfferModel;
import com.app.extraslice.model.ResourceTypeModel;
import com.app.extraslice.model.UserModel;
import com.app.extraslice.model.UserRequestModel;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.Utilities;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

public class UserBO {
	Context mContext;
	UserDAO dao;
	public String custId=null;
	public UserBO(Context mContext){
		this.mContext = mContext;
		dao = new UserDAO(mContext);
	}
	public JSONObject checkUserName(String emailId) throws CustomException {
		return dao.checkUserName(emailId);
	}
	public String getForumLoginUrl(int userId) throws CustomException {
		String url =null;
		JSONObject rootObject = null;
		try {
			rootObject = dao.getForumLoginUrl(userId);
		} catch (Exception e1) {
			throw new CustomException(e1.getLocalizedMessage());
		}
		if (rootObject != null) {
			try {
				String status = (String) rootObject.get(Utilities.STATUS_STRING);
				if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
					url = rootObject.getString("FORUM_URL");
				} else {
					String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
					throw new CustomException(errorMessage);
				}
			} catch (Exception e) {
				throw new CustomException(e.getLocalizedMessage());
			}
		}
		return url;

	}
	public UserModel updateUser(UserModel model) throws CustomException {
		UserModel userModel = null;
		JSONObject rootObject = null;
		try {
			rootObject = dao.updateUser( model);
		} catch (Exception e1) {
			throw new CustomException(e1.getLocalizedMessage());
		}
		if (rootObject != null) {
			try {
				String status = (String) rootObject.get(Utilities.STATUS_STRING);
				if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
					userModel = new UserModel().jSonToObject(rootObject.get("User").toString());
					Utilities.loggedInUser = userModel;
				} else {
					String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
					userModel = null;
					throw new CustomException(errorMessage);
				}
			} catch (Exception e) {
				throw new CustomException(e.getLocalizedMessage());
			}
		}
		return userModel;

	}
	public boolean addDedicatedMembershipRequest(UserRequestModel model,List<ResourceTypeModel> resTypeList) throws CustomException{
		boolean isStatus = false;
		JSONObject rootObject;
		try {
			rootObject = dao.addDedicatedMembershipRequest( model,resTypeList);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		String status = "";
		try {
			if (rootObject == null || rootObject.get(Utilities.STATUS_STRING) == null) {
				throw new CustomException("Failed to process request");
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
	public UserModel authenticateUser(String userName, String password,String osVersion,String appVersion,String deviceType) throws CustomException{
		UserModel userModel = null;
		JSONObject rootObject = null;
		try {
			rootObject = dao.authenticateUser(userName, password,osVersion,appVersion,deviceType);
		} catch (Exception e1) {
			throw new CustomException(e1.getLocalizedMessage());
		}
		if (rootObject != null) {
			try {
				String status = (String) rootObject.get(Utilities.STATUS_STRING);
				if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
					userModel = new UserModel().jSonToObject(rootObject.get("User").toString());
					try {
						if(rootObject.get(Utilities.WARNING_MESAGE) != null){
							Utilities.WARNING_FROM_SERVER = (String) rootObject.get(Utilities.WARNING_MESAGE);
						}else{
							Utilities.WARNING_FROM_SERVER = null;
						}
					} catch (Exception e) {
						Utilities.WARNING_FROM_SERVER = null;
					}
					
					
				} else {
					String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
					throw new CustomException(errorMessage);
				}
			} catch (Exception e) {
				throw new CustomException(e.getLocalizedMessage());
			}
		}
		return userModel;
		
	}
	
	public boolean resetPassword(String userName) throws CustomException {
		Random random = new Random();
		int Low = 1000;
		int High = 9999;
		int randomNumber = random.nextInt(High - Low) + Low;
		String newPassword = "Password-" + randomNumber;
		JSONObject rootObject;
		try {
			rootObject = dao.resetPassword(mContext, userName, newPassword);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		String status = "";
		try {
			if (rootObject == null || rootObject.get(Utilities.STATUS_STRING) == null) {
				throw new CustomException("Failed to reset password");
			} else {
				status = (String) rootObject.get(Utilities.STATUS_STRING);
				if (status.equals(Utilities.STATUS_SUCCESS)) {
					return true;
				}else{
					throw new CustomException(rootObject.getString(Utilities.ERROR_MESSAGE));
				}
				
			}
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
	}
	
	public UserModel resendVerificationCode(UserModel model) throws CustomException {
		
		JSONObject rootObject = null;
		try {
			rootObject = dao.resendVerificationCode(model);
		} catch (Exception e1) {
			throw new CustomException(e1.getLocalizedMessage());
		}
		if (rootObject != null) {
			
			try {
				String status = (String) rootObject.get(Utilities.STATUS_STRING);
				if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
					model = new UserModel().jSonToObject(rootObject.get("User").toString());
				} else {
					model =null;
					String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
					throw new CustomException(errorMessage);
				}
			} catch (Exception e) {
				model =null;
				throw new CustomException(e.getLocalizedMessage());
			}
		}
		return model;

	}
	
	public UserModel addGuestUser(UserModel model,String deviceType,String versionCode) throws CustomException {
		
		JSONObject rootObject = null;
		try {
			rootObject = dao.addGuestUser(model, deviceType, versionCode);
		} catch (Exception e1) {
			throw new CustomException(e1.getLocalizedMessage());
		}
		if (rootObject != null) {
			
			try {
				String status = (String) rootObject.get(Utilities.STATUS_STRING);
				if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
					model = new UserModel().jSonToObject(rootObject.get("User").toString());
				} else {
					model =null;
					String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
					throw new CustomException(errorMessage);
				}
			} catch (Exception e) {
				model =null;
				throw new CustomException(e.getLocalizedMessage());
			}
		}
		return model;

	}
	
	
	public UserModel registerUser(UserModel userModel,List<ResourceTypeModel> resTypeList,String userRegCode,String cardToken,
			List<Integer> planIdList ,long trialEndsAt, int trialDays,PlanOfferModel offerModel,String gateWay) throws CustomException {
		
		JSONObject rootObject = null;
		
		try {
			rootObject = dao.addUser(userModel,resTypeList,userRegCode,cardToken, planIdList ,trialEndsAt, trialDays, offerModel,gateWay);
		} catch (Exception e1) {
			throw new CustomException(e1.getLocalizedMessage());
		}
		if (rootObject != null) {
			
			try {
				String status = (String) rootObject.get(Utilities.STATUS_STRING);
				if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
					userModel = new UserModel().jSonToObject(rootObject.get("User").toString());
					try {
						custId=rootObject.getString("CUST_ID");
					} catch (Exception e) {
						
					}
				} else {
					String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
					throw new CustomException(errorMessage);
				}
			} catch (Exception e) {
				throw new CustomException(e.getLocalizedMessage());
			}
		}
		return userModel;

	}
	
	public boolean deleteUser( int userId,int orgId) throws CustomException {
		boolean isStatus = false;
		JSONObject rootObject;
		try {
			rootObject = dao.deleteUser(mContext, userId,orgId);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		String status = "";
		try {
			if (rootObject == null || rootObject.get(Utilities.STATUS_STRING) == null) {
				throw new CustomException("Failed to delete user");
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
	
	public boolean deleteUserByName(String username,String orgName) throws CustomException {
		boolean isStatus = false;
		JSONObject rootObject;
		try {
			rootObject = dao.deleteUserByName(username,orgName);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		String status = "";
		try {
			if (rootObject == null || rootObject.get(Utilities.STATUS_STRING) == null) {
				throw new CustomException("Failed to reset password");
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
	
	public UserModel addUserSubscription(int userId,int orgId,int planId,Customer customer,Charge ch,String gateWay,String acctId) throws CustomException {
		UserModel userModel = null;
		JSONObject rootObject;
		try {
			rootObject = dao.addUserSubscription(mContext, userId, orgId, planId, customer,ch, gateWay, acctId);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		if (rootObject != null) {
			try {
				String status = (String) rootObject.get(Utilities.STATUS_STRING);
				if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
					
				} else {
					String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
					
					throw new CustomException(errorMessage);
				}
			} catch (Exception e) {
				throw new CustomException(e.getLocalizedMessage());
			}
		}
		return userModel;
	}
}
