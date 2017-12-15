package com.extraslice.walknpay.dao;

import android.content.Context;

import com.extraslice.walknpay.bl.CustomException;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.model.UserModel;

import org.codehaus.jettison.json.JSONObject;

public class UserDAO {
	
	public JSONObject updateESliceUser(Context mContext, com.app.extraslice.model.UserModel userModel) throws CustomException {
		String urlString = Utilities.mainUrl + "/user/updateESliceUser";
		UserModel model = new UserModel();
		model.setUserName(userModel.getUserName());
		model.setEmail(userModel.getUserName());
		
		try {
			
			model.setAuthCode(Utilities.encode(userModel.getUserId()+""));
			
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
	public JSONObject authenticateESliceUser(Context mContext, com.app.extraslice.model.UserModel userModel,String deviceType,String osVersion,String appVersion) throws CustomException {
		String urlString = Utilities.mainUrl + "/user/authenticateESliceUser";
		UserModel model = new UserModel();
		model.setUserName(userModel.getUserName());
		model.setEmail(userModel.getUserName());
		JSONObject requestObj = new JSONObject();
		try {
			model.setPassword(userModel.getPassword());
			model.setAuthCode(Utilities.encode(userModel.getUserId()+""));
			requestObj.put("UserModel", model.toJSonObject());
			requestObj.put("DEVICETYPE", deviceType);
			requestObj.put("OSVERSION", osVersion);
			requestObj.put("APPVERSION", appVersion);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, requestObj.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	public JSONObject getUser(Context mContext, String userName, String password) throws CustomException {
		String urlString = Utilities.mainUrl + "/user/getUserByUserName";
		UserModel model = new UserModel();
		model.setUserName(userName);
		model.setEmail(userName);
		try {
			model.setPassword(Utilities.encode(password));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
	public JSONObject getUser(Context mContext, String userName, String password,String osVersion,String appVersion,String deviceType) throws CustomException {
		String urlString = Utilities.mainUrl + "/user/getUserByUserNameWithDetails";
		JSONObject requestObj = new JSONObject();
		UserModel model = new UserModel();
		model.setUserName(userName);
		model.setEmail(userName);
		
		try {
			model.setPassword(Utilities.encode(password));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try{
			requestObj.put("UserModel", model.toJSonObject());
			requestObj.put("DEVICETYPE", deviceType);
			requestObj.put("OSVERSION", osVersion);
			requestObj.put("APPVERSION", appVersion);
			
		}catch(Exception e){
			return null;
		}
		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString,requestObj.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
	public JSONObject updateUserLocationInfo(Context mContext,int userId,double latitude,double longitude) throws CustomException{
		String urlString = Utilities.mainUrl + "/user/updateUserLocation";
		JSONObject rootObject = null;
		UserModel model = new UserModel();
		model.setUserId(userId);
		model.setUserLatitude(latitude);
		model.setUserLongitude(longitude);
		
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
		
		
		
	}
	
	public JSONObject resendEmail(Context context,String userName,String password,int userId) throws CustomException
	{
		String urlString = Utilities.mainUrl + "/user/resendVerificationEmail";
		UserModel model = new UserModel();
		model.setUserName(userName);
		model.setEmail(userName);
		model.setPassword(password);
		model.setUserId(userId);
		
		try {
			/*model.setPassword(Utilities.encode(password));*/
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(context, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			try {
				throw new CustomException(e.getLocalizedMessage());
			} catch (CustomException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}*/
		/*try {
			rootObject = new RunJSon(context, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			try {
				throw new CustomException(e.getLocalizedMessage());
			} catch (CustomException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),context);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
		
	}
	
	/*public JSONObject setVerificationCode(Context mContext,UserModel model) throws CustomException {
		String urlString = Utilities.mainUrl + "/user/updateUser";
		model.setVerificationCode(null);
		try {
			model.setPassword(Utilities.encode(password));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		JSONObject rootObject = null;
		try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}*/
//update 
	public JSONObject updateUser(Context mContext,UserModel model) throws CustomException {
		String urlString = Utilities.mainUrl + "/user/updateUser";
		
		
		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
	public JSONObject addUser(Context mContext, String userName, String password) {
		UserModel model = new UserModel();
		model.setUserName(userName);
		model.setEmail(userName);
		try {
			model.setPassword(Utilities.encode(password));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String urlString = Utilities.mainUrl + "/user/addUser";
		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {

		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {

		}
		return rootObject;
	}

	public JSONObject resetPassword(Context mContext, String userName, String newPassword) {
		String urlString = Utilities.mainUrl + "/user/resetPassword";
		JSONObject rootObject = null;

		UserModel model = new UserModel();
		model.setUserName(userName);
		model.setEmail(userName);
		try {
			model.setTempPassword(Utilities.encode(newPassword));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		/*try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			return null;
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			return null;
		}
		return rootObject;
	}
}
