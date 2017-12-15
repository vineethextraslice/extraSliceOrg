package com.extraslice.walknpay.dao;

import android.content.Context;

import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.model.SupportOfficerModel;

import org.codehaus.jettison.json.JSONObject;

public class CustSupportDAO {
	public JSONObject assignAvailableOfficer(Context mContext, int userId) {
		String urlString = Utilities.mainUrl + "/custsupport/assignAvailableOfficer";
		JSONObject rootObject = null;
		SupportOfficerModel model = new SupportOfficerModel();
		model.setUserId(Utilities.loggedInUser.getUserId());
		if(Utilities.selectedStore != null ){
			model.setStoreId(Utilities.selectedStore.getStoreId());
		}
		/*try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}

	public JSONObject updateOfficerAvailable(Context mContext, int officerId) {
		String urlString = Utilities.mainUrl + "/custsupport/updateOfficerAvailable";
		JSONObject rootObject = null;
		SupportOfficerModel model = new SupportOfficerModel();
		model.setOfficerId(officerId);
		/*try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}

	public JSONObject updateLastSupportTime(Context mContext, int officerId, int userId, String chatId) {
		String urlString = Utilities.mainUrl + "/custsupport/updateLastSupportTime";
		JSONObject rootObject = null;
		SupportOfficerModel model = new SupportOfficerModel();
		model.setOfficerId(officerId);
		model.setUserId(userId);
		model.setChatId(chatId);
		/*try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}

}
