package com.extraslice.walknpay.bl;

import android.content.Context;

import com.extraslice.walknpay.dao.UserDAO;
import com.extraslice.walknpay.model.UserModel;
import com.extraslice.walknpay.ui.Forgot;

import org.codehaus.jettison.json.JSONObject;

import java.util.Random;

public class UserBO {

	
	Context mContext;

	public UserBO(Context context) {
		mContext = context;
	}
	
	public JSONObject updateLocationLoggedInUser(int userId, double lat, double lon){
		JSONObject rootObject = null;
		UserDAO dao = new UserDAO();
		try{
			rootObject=	dao.updateUserLocationInfo(mContext,userId,lat,lon);
			
		}catch(Exception e)
		{
			Utilities.errorMessage = e.getLocalizedMessage();
			return null;
		}
		return rootObject;
	}
	
	public JSONObject resendVerificationEmail(String userName , String password,int userId)
	{
		UserModel userModel = null;
		UserDAO dao = new UserDAO();
		JSONObject rootObject = null;
		try {
			rootObject = dao.resendEmail(mContext, userName, password,userId);
		} catch (Exception e1) {
			Utilities.errorMessage = e1.getLocalizedMessage();
			return null;
		}
		if (rootObject == null) {
			Utilities.errorMessage = "";
			return null;
		}
		try {
			String status = (String) rootObject.get(Utilities.STATUS_STRING);
			if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
				userModel = new UserModel().jSonToObject(rootObject.get("User").toString());
				Utilities.loginUserID = userModel.getUserId();
			} else {
				String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
				Utilities.errorMessage = errorMessage;
				userModel = null;
			}
		} catch (Exception e) {
			Utilities.errorMessage = "";
			return null;
		}
		return rootObject;
		
	}

	public UserModel authenticateUserWithMoreDetails(String userName, String password,String osVersion,String appVersion,String deviceType) {
		UserModel userModel = null;
		UserDAO dao = new UserDAO();
		JSONObject rootObject = null;
		try {
			rootObject = dao.getUser(mContext, userName, password,osVersion,appVersion,deviceType);
		} catch (Exception e1) {
			Utilities.errorMessage = e1.getLocalizedMessage();
			return null;
		}
		if (rootObject == null) {
			Utilities.errorMessage = "";
			return null;
		}
		try {
			String status = (String) rootObject.get(Utilities.STATUS_STRING);
			if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
				userModel = new UserModel().jSonToObject(rootObject.get("User").toString());
				Utilities.loginUserID = userModel.getUserId();
				/*StoreBO storeBo = new StoreBO(mContext);
				Utilities.storeList = storeBo.getAllStore(Utilities.STATUS_FILTER_ACTIVE);*/
				try {
					if(rootObject.get(Utilities.WARNING_MESAGE) != null){
						Utilities.WARNING_FROM_SERVER = (String) rootObject.get(Utilities.WARNING_MESAGE);
					}else{
						Utilities.WARNING_FROM_SERVER = null;
					}
				} catch (Exception e) {
					Utilities.WARNING_FROM_SERVER = null;
				}
				//Utilities.WARNING_FROM_SERVER="03-16-2016 12:00:000";
				
			} else {
				String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
				Utilities.errorMessage = errorMessage;
				userModel = null;
			}
		} catch (Exception e) {
			Utilities.errorMessage = "";
			return null;
		}

		return userModel;
	}

	public String updateESliceUser(com.app.extraslice.model.UserModel uModel) throws CustomException {
		UserDAO dao = new UserDAO();
		JSONObject rootObject = null;
		try {
			rootObject = dao.updateESliceUser(mContext, uModel);
		} catch (Exception e1) {
			throw new CustomException(e1.getMessage());
		}
		if (rootObject == null) {
			Utilities.errorMessage = "";
			return null;
		}
		try {
			String status = (String) rootObject.get(Utilities.STATUS_STRING);
			if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
				return status;
			} else {
				String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
				throw new CustomException(errorMessage);
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}


	}
	
	public UserModel authenticateESliceUser(com.app.extraslice.model.UserModel uModel,String deviceType,String osVersion,String appVersion) {
		UserModel userModel = null;
		UserDAO dao = new UserDAO();
		JSONObject rootObject = null;
		try {
			rootObject = dao.authenticateESliceUser(mContext, uModel,deviceType,osVersion,appVersion);
		} catch (Exception e1) {
			Utilities.errorMessage = e1.getLocalizedMessage();
			return null;
		}
		if (rootObject == null) {
			Utilities.errorMessage = "";
			return null;
		}
		try {
			String status = (String) rootObject.get(Utilities.STATUS_STRING);
			if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
				userModel = new UserModel().jSonToObject(rootObject.get("User").toString());
				Utilities.loginUserID = userModel.getUserId();
				/*StoreBO storeBo = new StoreBO(mContext);
				Utilities.storeList = storeBo.getAllStore(Utilities.STATUS_FILTER_ACTIVE);*/
			} else {
				String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
				Utilities.errorMessage = errorMessage;
				userModel = null;
			}
		} catch (Exception e) {
			Utilities.errorMessage = "";
			return null;
		}

		return userModel;
	}
	public UserModel authenticateUser(String userName, String password) {
		UserModel userModel = null;
		UserDAO dao = new UserDAO();
		JSONObject rootObject = null;
		try {
			rootObject = dao.getUser(mContext, userName, password);
		} catch (Exception e1) {
			Utilities.errorMessage = e1.getLocalizedMessage();
			return null;
		}
		if (rootObject == null) {
			Utilities.errorMessage = "";
			return null;
		}
		try {
			String status = (String) rootObject.get(Utilities.STATUS_STRING);
			if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
				userModel = new UserModel().jSonToObject(rootObject.get("User").toString());
				Utilities.loginUserID = userModel.getUserId();
				/*StoreBO storeBo = new StoreBO(mContext);
				Utilities.storeList = storeBo.getAllStore(Utilities.STATUS_FILTER_ACTIVE);*/
			} else {
				String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
				Utilities.errorMessage = errorMessage;
				userModel = null;
			}
		} catch (Exception e) {
			Utilities.errorMessage = "";
			return null;
		}

		return userModel;
	}

public UserModel updateUser(UserModel model)
{
	UserModel userModel = null;
	UserDAO dao = new UserDAO();
	JSONObject rootObject = null;
	try {
		rootObject = dao.updateUser(mContext,  model);
	} catch (Exception e1) {
		Utilities.errorMessage = e1.getLocalizedMessage();
		return null;
	}
	if (rootObject == null) {
		Utilities.errorMessage = "";
		return null;
	}
	try {
		String status = (String) rootObject.get(Utilities.STATUS_STRING);
		if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
			userModel = new UserModel().jSonToObject(rootObject.get("User").toString());
			Utilities.loginUserID = userModel.getUserId();
		} else {
			String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
			Utilities.errorMessage = errorMessage;
			userModel = null;
		}
	} catch (Exception e) {
		Utilities.errorMessage = "";
		return null;
	}

	return userModel;
	
}
	public UserModel registerUser(String userName, String password) {
		UserModel userModel = null;
		UserDAO dao = new UserDAO();
		JSONObject rootObject = null;
		try {
			rootObject = dao.addUser(mContext, userName, password);
		} catch (Exception e1) {
			Utilities.errorMessage = e1.getLocalizedMessage();
			return null;
		}
		if (rootObject == null) {
			Utilities.errorMessage = "";
			return null;
		}
		try {
			String status = (String) rootObject.get(Utilities.STATUS_STRING);
			if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
				userModel = new UserModel().jSonToObject(rootObject.get("User").toString());
			} else {
				String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
				Utilities.errorMessage = errorMessage;
			}
		} catch (Exception e) {
			Utilities.errorMessage = "";
			return null;
		}
		return userModel;

	}

	public boolean resetPassword(String userName) {
		Random random = new Random();
		int Low = 1000;
		int High = 9999;
		int randomNumber = random.nextInt(High - Low) + Low;
		String newPassword = "Password-" + randomNumber;
		UserDAO dao = new UserDAO();
		JSONObject rootObject;
		try {
			rootObject = dao.resetPassword(mContext, userName, newPassword);
		} catch (Exception e) {
			Utilities.errorMessage = e.getLocalizedMessage();
			return false;
		}
		String status = "";
		try {
			if (rootObject == null || rootObject.get(Utilities.STATUS_STRING) == null) {
				Utilities.errorMessage = "Failed to reset password";
				return false;
			} else {
					status = (String) rootObject.get(Utilities.STATUS_STRING);
					if (status.equals(Utilities.STATUS_SUCCESS)) {
						Forgot.newPassword=newPassword;
						return true;
					}else{
						Utilities.errorMessage = rootObject.getString(Utilities.ERROR_MESSAGE);
						return false;
					}
				
			}
		} catch (Exception e) {
			Utilities.errorMessage = e.getLocalizedMessage();
			return false;
		}
	}

}
