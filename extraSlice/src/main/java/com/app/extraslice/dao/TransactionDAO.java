package com.app.extraslice.dao;

import android.content.Context;

import com.app.extraslice.connection.WSConnnection;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.Utilities;

import org.codehaus.jettison.json.JSONObject;

public class TransactionDAO {
	Context mContext;
	public TransactionDAO(Context mContext){
		this.mContext = mContext;
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
	
}
